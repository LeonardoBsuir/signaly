package by.bsuir.ui.view.panel;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.util.List;

import javax.swing.*;

import by.bsuir.digitalsignal.model.AreaType;
import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.ui.view.graphic.view.CtrlGraphicView;
import com.alee.laf.scroll.WebScrollPane;

public class ListViewPanel extends ViewPanel {

    public ListViewPanel(JFrame frame, List<GraphSignalData> signalDataList, boolean isCommonOption) {
        super(frame, isCommonOption);
        this.setCountColumnsAndRows(signalDataList.size());
        this.centerPanel = new JPanel(new GridLayout(this.rowCount, this.colCount));
        int dataDisplacement = 0;

        try {
            dataDisplacement = signalDataList.get(0).getCommonData().getDisplacement();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (GraphSignalData signalData : signalDataList) {
            signalData.setCommonData(this.commonData);
            this.commonData.setTimeFieldSize(signalData.getTimeFieldSize());
            this.centerPanel.add(new CtrlGraphicView(this.parent, signalData));
        }

        this.setScrollBlockIncrement(isCommonOption, this.commonData.getTimeFieldSize());
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
        this.scrollBar.setValue(dataDisplacement);
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
        } else {
            this.commonData.setAreaType(AreaType.SPECTRE);
        }

        super.setTimeArea(isTime);
    }

    @Override
    public void setLinearSpectreArea() {
        this.commonData.setAreaType(AreaType.LINEAR_SPECTRE);
        super.setLinearSpectreArea();
    }

    @Override
    public void setHistogram() {
        this.commonData.setAreaType(AreaType.HISTOGRAM);
        super.setHistogram();
    }
}
