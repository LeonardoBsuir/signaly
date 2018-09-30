package by.bsuir.ui.view.panel;

import java.awt.*;
import java.awt.event.AdjustmentEvent;

import javax.swing.*;

import by.bsuir.digitalsignal.model.AreaType;
import by.bsuir.digitalsignal.model.CommonData;
import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.ui.view.graphic.view.CtrlGraphicView;
import com.alee.laf.scroll.WebScrollPane;

public class WaveletViewPanel extends ViewPanel {

    private final CommonData waveletCommonData;

    public WaveletViewPanel(JFrame frame, GraphSignalData wavelet, GraphSignalData firstSignal, GraphSignalData secondSignal) {
        super(frame, false);
        this.waveletCommonData = wavelet.getCommonData();
        this.commonData.setTimeFieldSize(firstSignal.getTimeFieldSize());
        
        firstSignal.setCommonData(this.commonData);
        secondSignal.setCommonData(this.commonData);
        
        this.centerPanel = new JPanel(new GridLayout(3, 1));
        this.centerPanel.add(new CtrlGraphicView(this.parent, wavelet));
        this.centerPanel.add(new CtrlGraphicView(this.parent, firstSignal));
        this.centerPanel.add(new CtrlGraphicView(this.parent, secondSignal));
        
        this.setScrollBlockIncrement(false, this.commonData.getTimeFieldSize());
        this.setScrollMaximum(this.commonData.maxDataLength - this.scrollBar.getBlockIncrement());

        WebScrollPane scrollPane = new WebScrollPane(this.centerPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(UNIT);
        scrollPane.getViewport().addChangeListener(e -> {
            for (Component component : centerPanel.getComponents()) {
                component.repaint();
            }
        });
        this.centerPanel.setPreferredSize(new Dimension(0, this.rowCount * this.graphHeight));
        this.add(scrollPane, BorderLayout.CENTER);
        
        this.createOptionDialog();
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent ev) {
        this.commonData.setDisplacement(ev.getValue());
        this.refresh();
    }

    @Override
    public void setTimeArea(boolean isTime) {
        if (isTime) {
            this.commonData.setAreaType(AreaType.TIME_SIGNAL);
            this.waveletCommonData.setAreaType(AreaType.TIME_SIGNAL);
        } else {
            this.commonData.setAreaType(AreaType.SPECTRE);
            this.waveletCommonData.setAreaType(AreaType.SPECTRE);
        }

        super.setTimeArea(isTime);
    }

    @Override
    public void setLinearSpectreArea() {
        this.commonData.setAreaType(AreaType.LINEAR_SPECTRE);
        this.waveletCommonData.setAreaType(AreaType.LINEAR_SPECTRE);
        super.setLinearSpectreArea();
    }

    @Override
    public void setHistogram() {
        this.commonData.setAreaType(AreaType.HISTOGRAM);
        this.waveletCommonData.setAreaType(AreaType.HISTOGRAM);
        super.setHistogram();
    }

    @Override
    public void setTimeFieldSize(int iSize) {
        this.waveletCommonData.setTimeFieldSize(iSize);
        super.setTimeFieldSize(iSize);
    }
}
