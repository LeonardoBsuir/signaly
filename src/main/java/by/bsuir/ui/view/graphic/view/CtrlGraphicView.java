package by.bsuir.ui.view.graphic.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import by.bsuir.digitalsignal.model.AreaType;
import by.bsuir.digitalsignal.model.CommonData;
import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.digitalsignal.model.filter.FilteredSignalData;
import by.bsuir.digitalsignal.model.integrating.SpeedPartSignalData;
import by.bsuir.digitalsignal.model.integrating.SpeedSignalData;
import by.bsuir.digitalsignal.model.integrating.TransferPartSignalData;
import by.bsuir.digitalsignal.model.integrating.TransferSignalData;
import by.bsuir.digitalsignal.model.drift.DriftFirstDegreeSignalData;
import by.bsuir.digitalsignal.model.drift.DriftSecondDegreeSignalData;
import by.bsuir.digitalsignal.model.wavlet.Gaus3pSignalData;
import by.bsuir.digitalsignal.model.wavlet.Gaus4pSignalData;
import by.bsuir.digitalsignal.model.wavlet.MexicanHatSignalData;
import by.bsuir.digitalsignal.model.wavlet.MorletSignalData;
import by.bsuir.digitalsignal.model.wavlet.WaveSignalData;
import by.bsuir.ui.processing.ThreadWaveCreating;
import by.bsuir.ui.view.dialog.FilterDialog;
import by.bsuir.ui.view.dialog.OptionViewDialog;
import by.bsuir.ui.view.frame.ViewFrame;
import by.bsuir.ui.view.panel.ListViewPanel;
import com.alee.laf.optionpane.WebOptionPane;

public class CtrlGraphicView extends GraphicView implements ActionListener, ComponentListener {

    private static final int OPTION_DIALOG_PADDING = 33;

    private final JPopupMenu popupMenu;

    private final JMenuItem trendFirstDeletionItem;
    private final JMenuItem trendSecondDeletionItem;

    private final JMenuItem filtrationItem;

    private final JMenuItem integrationItem;
    private final JMenuItem partialIntegrationItem;

    private final JMenuItem waveItem;
    private final JMenuItem mexicanHatItem;
    private final JMenuItem gaus3pItem;
    private final JMenuItem gaus4pItem;
    private final JMenuItem morletItem;
    
    private OptionViewDialog optionDialog;

    public CtrlGraphicView(JFrame frame, GraphSignalData graphSignalData) {
        super(frame, graphSignalData);
        this.popupMenu = new JPopupMenu("Контекстное меню");

        this.trendFirstDeletionItem = new JMenuItem("Удаление НЧ Дрейфа. Полином 1 степени");
        this.trendSecondDeletionItem = new JMenuItem("Удаление НЧ Дрейфа. Полином 2 степени");

        this.filtrationItem = new JMenuItem("Фильтрация");

        this.partialIntegrationItem = new JMenuItem("Частичное Интегрирование");
        this.integrationItem = new JMenuItem("Полное Интегрирование");

        this.waveItem = new JMenuItem("Вейвлет \"Антисимметричная Волна\"");
        this.mexicanHatItem = new JMenuItem("Вейвлет \"Мексиканская Шляпа\"");
        this.gaus3pItem = new JMenuItem("Гауссов Вейвлет 3 порядка");
        this.gaus4pItem = new JMenuItem("Гауссов Вейвлет 4 порядка");
        this.morletItem = new JMenuItem("Вейвлет Морле");
        this.createOptionDialog();

        if (this.graphSignalData.isTransformedCapacity()) {
            this.trendFirstDeletionItem.addActionListener(this);
            this.popupMenu.add(this.trendFirstDeletionItem);
            this.trendSecondDeletionItem.addActionListener(this);
            this.popupMenu.add(this.trendSecondDeletionItem);
            this.popupMenu.addSeparator();

            this.filtrationItem.addActionListener(this);
            this.popupMenu.add(this.filtrationItem);
            this.popupMenu.addSeparator();

            this.partialIntegrationItem.addActionListener(this);
            this.popupMenu.add(this.partialIntegrationItem);
            this.integrationItem.addActionListener(this);
            this.popupMenu.add(this.integrationItem);
            this.popupMenu.addSeparator();

            this.waveItem.addActionListener(this);
            this.popupMenu.add(this.waveItem);
            this.mexicanHatItem.addActionListener(this);
            this.popupMenu.add(this.mexicanHatItem);
            this.gaus3pItem.addActionListener(this);
            this.popupMenu.add(this.gaus3pItem);
            this.gaus4pItem.addActionListener(this);
            this.popupMenu.add(this.gaus4pItem);
            this.morletItem.addActionListener(this);
            this.popupMenu.add(this.morletItem);
        }
    }

    @Override
    public void refresh() {
        super.refresh();
        this.updateOptionDialog();
    }

    @Override
    public void repaint() {
        super.repaint();
        this.moveOptionDialog();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object sender = event.getSource();
        if (this.optionDialog != null && this.optionDialog.txtYLimit == sender) {
            float limit = 0;
            try {
                limit = Float.parseFloat(this.optionDialog.txtYLimit.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.graphSignalData.setPreferableLimit(limit);
            this.repaint();
            return;
        }
        if (this.waveItem == sender) {
            float fw = this.requestFrequency();
            if (-1.0F != fw) {
                this.viewWave(fw);
            }
        } else if (this.mexicanHatItem == sender) {
            float fw = this.requestFrequency();
            if (-1.0F != fw) {
                this.viewMexicanHat(fw);
            }
        } else if (this.gaus3pItem == sender) {
            float fw = this.requestFrequency();
            if (-1.0F != fw) {
                this.viewGaus3p(fw);
            }
        } else if (this.gaus4pItem == sender) {
            float fw = this.requestFrequency();
            if (-1.0F != fw) {
                this.viewGaus4p(fw);
            }
        } else if (this.morletItem == sender) {
            float fw = this.requestFrequency();
            if (-1.0F != fw) {
                this.viewMorlet(fw);
            }
        } else if (this.filtrationItem == sender) {
            this.createFilterFrame();
        } else if (this.integrationItem == sender) {
            this.createIntegratedSignal();
        } else if (this.partialIntegrationItem == sender) {
            this.createPartialIntegratedSignal();
        } else if (this.trendFirstDeletionItem == sender) {
            this.deleteFirstTrend();
        } else if (this.trendSecondDeletionItem == sender) {
            this.deleteSecondTrend();
        } else {
            System.out.println("'" + sender.toString() + "' ActionEvent was performed.");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.showPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.showPopup(e);
    }

    private void showPopup(MouseEvent mouseEvent) {
        if (mouseEvent.isPopupTrigger()) {
            this.popupMenu.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
        }
    }

    private void showOptionDialog(boolean shouldShow) {
        if (this.optionDialog != null) {
            this.optionDialog.setVisible(shouldShow);
        }
    }

    private void createOptionDialog() {
        this.optionDialog = new OptionViewDialog(this.parentFrame, this);
        this.optionDialog.setDataLimit(this.graphSignalData.getLimit());
        this.optionDialog.txtYLimit.addActionListener(this);
        this.parentFrame.addComponentListener(this);
        this.showOptionDialog(true);
    }

    private void updateOptionDialog() {
        if (this.optionDialog != null) {
            switchOptionDialogVisibility();

            this.optionDialog.setDataLimit(this.graphSignalData.getLimit());
        }
    }

    private void moveOptionDialog() {
        if (this.optionDialog != null) {
            switchOptionDialogVisibility();

            Point p = this.getLocation();
            int x = p.x;
            int y = p.y;

            for (Container c = this.getParent(); c != null; c = c.getParent()) {
                p = c.getLocation();
                x += p.x;
                y += p.y;
            }

            if (this.getRootPane() != null) {
                int rootPaneHeight = this.getRootPane().getHeight();
                int optionDialogWithPadding = y - this.optionDialog.getHeight() - OPTION_DIALOG_PADDING;
                if (optionDialogWithPadding > 0 && rootPaneHeight - y - OPTION_DIALOG_PADDING * 0.6 - this.optionDialog.getHeight() > 0) {
                    setOptionDialogBounds(x, y);
                } else {
                    this.optionDialog.setVisible(false);
                }
            } else {
                setOptionDialogBounds(x, y);
            }
        }
    }

    private void switchOptionDialogVisibility() {
        if (this.graphSignalData.getAreaType() == AreaType.SPECTRE || this.graphSignalData.getAreaType() == AreaType.LINEAR_SPECTRE) {
            this.showOptionDialog(false);
        } else {
            this.showOptionDialog(true);
        }
    }

    private void setOptionDialogBounds(int x, int y) {
        this.optionDialog.setBounds(
                x + this.getWidth() - this.optionDialog.getWidth() - 10,
                y + OPTION_DIALOG_PADDING,
                this.optionDialog.getWidth(),
                this.optionDialog.getHeight()
        );
    }

    @Override
    public void close() {
        this.showOptionDialog(false);
        super.close();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        this.moveOptionDialog();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        this.moveOptionDialog();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        this.moveOptionDialog();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    private float requestFrequency() {
        String standardFrequency = "60.0";
        String newFrequency = WebOptionPane.showInputDialog(this, "Введите значение частоты", standardFrequency);
        if (newFrequency != null) {
            return Float.valueOf(newFrequency);
        } else {
            return -1;
        }
    }

    private void viewWave(float fw) {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            GraphSignalData wavelet = new WaveSignalData(signalData, fw, new CommonData());
            ThreadWaveCreating thread = new ThreadWaveCreating(this, wavelet, signalData, "Вейвлет \"Антисимметричная Волна\"");
            thread.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewMexicanHat(float fw) {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            GraphSignalData wavelet = new MexicanHatSignalData(signalData, fw, new CommonData());
            ThreadWaveCreating thread = new ThreadWaveCreating(this, wavelet, signalData, "Вейвлет \"Мексиканская Шляпа\"");
            thread.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewGaus3p(float fw) {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            GraphSignalData wavelet = new Gaus3pSignalData(signalData, fw, new CommonData());
            ThreadWaveCreating thread = new ThreadWaveCreating(this, wavelet, signalData, "Гауссов Вейвлет 3 порядка");
            thread.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewGaus4p(float fw) {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            GraphSignalData wavelet = new Gaus4pSignalData(signalData, fw, new CommonData());
            ThreadWaveCreating thread = new ThreadWaveCreating(this, wavelet, signalData, "Гауссов Вейвлет 4 порядка");
            thread.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewMorlet(float fw) {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            GraphSignalData wavelet = new MorletSignalData(signalData, fw, new CommonData());
            ThreadWaveCreating thread = new ThreadWaveCreating(this, wavelet, signalData, "Вейвлет Морле");
            thread.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFilterFrame() {
        FilterDialog dialog = new FilterDialog(this.parentFrame, false);
        if (dialog.showDialog(this) == 1) {
            try {
                GraphSignalData signalData = this.graphSignalData.clone();
                GraphSignalData signalTwo = new FilteredSignalData(signalData, dialog.getMinFrequency(), dialog.getMaxFrequency());

                List<GraphSignalData> signalDataList = new ArrayList<>();
                signalDataList.add(signalData);
                signalDataList.add(signalTwo);

                ViewFrame viewFrame = new ViewFrame(getFilterResultTitle(dialog.getMinFrequency(), dialog.getMaxFrequency()));
                viewFrame.setContentPane(new ListViewPanel(viewFrame, signalDataList, true));
                viewFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getFilterResultTitle(Float minFrequency, Float maxFrequency) {
        StringBuilder stringBuilder = new StringBuilder("Отфильтрованные частоты: ");
        if (minFrequency != null && maxFrequency != null) {
            stringBuilder.append("c ").append(minFrequency)
                    .append(" по ")
                    .append(maxFrequency);
        } else if (minFrequency != null) {
            stringBuilder.append("менее ")
                    .append(minFrequency);
        } else {
            stringBuilder.append("более ")
                    .append(maxFrequency);
        }
        stringBuilder.append(" Hz");
        return stringBuilder.toString();
    }

    private void createIntegratedSignal() {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            SpeedSignalData speedData = new SpeedSignalData(signalData);
            TransferSignalData transferSignalData = new TransferSignalData(speedData);

            List<GraphSignalData> signalDataList = new ArrayList<>();
            signalDataList.add(signalData);
            signalDataList.add(speedData);
            signalDataList.add(transferSignalData);

            ViewFrame viewFrame = new ViewFrame("Полное Интегрирование Сигнала");
            viewFrame.setContentPane(new ListViewPanel(viewFrame, signalDataList, false));
            viewFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPartialIntegratedSignal() {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            SpeedPartSignalData speedData = new SpeedPartSignalData(signalData);
            TransferPartSignalData trData = new TransferPartSignalData(speedData);

            List<GraphSignalData> list = new ArrayList<>();
            list.add(signalData);
            list.add(speedData);
            list.add(trData);

            ViewFrame viewFrame = new ViewFrame("Частичное Интегрирование Сигнала");
            viewFrame.setContentPane(new ListViewPanel(viewFrame, list, false));
            viewFrame.setVisible(true);
            updateOptionDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteFirstTrend() {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            GraphSignalData trendData = new DriftFirstDegreeSignalData(signalData);
            ArrayList<GraphSignalData> list = new ArrayList<>();
            list.add(signalData);
            list.add(trendData);
            ViewFrame frm = new ViewFrame("Удаление НЧ Дрейфа. Полином 1 степени");
            frm.setContentPane(new ListViewPanel(frm, list, false));
            frm.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSecondTrend() {
        try {
            GraphSignalData signalData = this.graphSignalData.clone();
            GraphSignalData trendData = new DriftSecondDegreeSignalData(signalData);
            ArrayList<GraphSignalData> list = new ArrayList<>();
            list.add(signalData);
            list.add(trendData);
            ViewFrame frm = new ViewFrame("Удаление НЧ Дрейфа. Полином 2 степени");
            frm.setContentPane(new ListViewPanel(frm, list, false));
            frm.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
