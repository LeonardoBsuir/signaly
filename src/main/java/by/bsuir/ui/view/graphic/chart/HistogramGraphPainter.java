package by.bsuir.ui.view.graphic.chart;

import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public class HistogramGraphPainter implements GraphPainter {

    @Override
    public void paint(GraphicView graphicView, Graphics graphDrawer) {
        GraphSignalData graphSignalData = graphicView.getGraphSignalData();

        float[] data = graphSignalData.getPinchedData();
        int halfKolDiapason = graphSignalData.getCommonData().getHistogramNumberOfRanges() / 2;
        int step = graphSignalData.getCommonData().getHorizontalGridSpacing();

        for (int i = 0; i < data.length; ++i) {
            graphDrawer.drawLine(i - halfKolDiapason * step, 0, i - halfKolDiapason * step, (int) -data[i]);
        }
    }
}
