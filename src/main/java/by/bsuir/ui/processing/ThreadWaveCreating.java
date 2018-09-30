package by.bsuir.ui.processing;

import java.awt.*;

import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.digitalsignal.model.wavlet.WaveletTransformSignalData;
import by.bsuir.ui.view.frame.ViewFrame;
import by.bsuir.ui.view.panel.WaveletViewPanel;

public class ThreadWaveCreating extends ThreadSignalCreating {

    private final GraphSignalData signalData;
    private final GraphSignalData wavelet;

    public ThreadWaveCreating(Component component, GraphSignalData wavelet, GraphSignalData signalData, Object message) {
        super(component, new WaveletTransformSignalData(wavelet, signalData), message);
        this.signalData = signalData;
        this.wavelet = wavelet;
    }

    @Override
    public void done(GraphSignalData doneSignal, String title) {
        if (doneSignal != null) {
            ViewFrame viewFrame = new ViewFrame(title);
            WaveletViewPanel pane = new WaveletViewPanel(viewFrame, this.wavelet, this.signalData, doneSignal);
            viewFrame.setContentPane(pane);
            viewFrame.setVisible(true);
        }
    }
}
