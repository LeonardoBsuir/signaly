package by.bsuir.ui.processing;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.*;

import by.bsuir.digitalsignal.model.GraphSignalData;

public abstract class ThreadSignalCreating extends SwingWorker<GraphSignalData, Integer> {

    private final ProgressMonitor monitor;
    private final GraphSignalData signal;
    private final Object message;

    public ThreadSignalCreating(Component comp, GraphSignalData signal, Object message) {
        this.signal = signal;
        this.message = message;
        this.monitor = new ProgressMonitor(comp, message, "Start", 0, 101);
        this.monitor.setProgress(0);
    }

    @Override
    protected GraphSignalData doInBackground() throws Exception {
        int iPercent = this.signal.getPercent();

        while (!this.isCancelled()) {
            boolean shouldExit = this.signal.calculateNextPart();
            if (shouldExit) {
                break;
            }

            if (iPercent != this.signal.getPercent()) {
                iPercent = this.signal.getPercent();
                this.publish(iPercent);
            }
        }

        return this.signal;
    }

    @Override
    protected void process(List<Integer> chunks) {
        for (Integer i : chunks) {
            this.monitor.setProgress(i);
            this.monitor.setNote(String.format("Завершено %d%%.", i));
        }

        if (this.monitor.isCanceled()) {
            this.cancel(true);
        }

        super.process(chunks);
    }

    @Override
    protected void done() {
        if (!this.monitor.isCanceled()) {
            try {
                this.done(this.get(), this.message.toString());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            this.done(null, this.message.toString());
        }

        this.monitor.close();
        super.done();
    }

    protected abstract void done(GraphSignalData signal, String message);
}
