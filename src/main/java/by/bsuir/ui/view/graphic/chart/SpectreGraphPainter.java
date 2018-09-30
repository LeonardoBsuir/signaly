package by.bsuir.ui.view.graphic.chart;

import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public class SpectreGraphPainter implements GraphPainter {

    @Override
    public void paint(GraphicView graphicView, Graphics graphDrawer) {
        float[] data = graphicView.getGraphSignalData().getPinchedData();

        for (int i = 0; i < data.length; ++i) {
            graphDrawer.drawLine(i, 0, i, (int) -data[i]);
        }
    }
}
