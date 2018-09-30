package by.bsuir.ui.view.panel;

import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.io.File;

import javax.swing.*;

import by.bsuir.digitalsignal.model.AreaType;
import by.bsuir.digitalsignal.model.FileSignalData;
import by.bsuir.ui.view.graphic.view.CtrlGraphicView;
import com.alee.laf.scroll.WebScrollPane;

public class DataViewPanel extends ViewPanel {

    public DataViewPanel(JFrame parent, File[] files, boolean isCommonOption) {
        super(parent, isCommonOption);
        this.setCountColumnsAndRows(files.length);
        int openedFilesCount = 0;

        this.centerPanel = new JPanel(new GridLayout(this.rowCount, this.colCount));
        for (File file : files) {
            try {
                FileSignalData data = new FileSignalData(file, this.commonData);
                this.commonData.setTimeFieldSize(data.getTimeFieldSize());
                this.centerPanel.add(new CtrlGraphicView(this.parent, data));
                ++openedFilesCount;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (openedFilesCount > 0) {
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
        } else {
            throw new RuntimeException("Could not open any file");
        }
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent event) {
        this.commonData.setDisplacement(event.getValue());
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
