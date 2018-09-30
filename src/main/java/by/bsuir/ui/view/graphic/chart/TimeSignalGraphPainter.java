package by.bsuir.ui.view.graphic.chart;

import by.bsuir.ui.view.graphic.view.GraphicView;
import com.alee.utils.GraphicsUtils;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class TimeSignalGraphPainter implements GraphPainter {

    @Override
    public void paint(GraphicView graphicView, Graphics graphDrawer) {
        float[] data = graphicView.getGraphSignalData().getPinchedData();
        ((Graphics2D) graphDrawer).setStroke(GraphicsUtils.getStroke(2));

        int[] x = new int[data.length];
        int[] y = new int[data.length];

        for (int i = 0; i < data.length; ++i) {
            x[i] = i + GraphicView.TIME_SIGNAL_PADDING;
            y[i] = (int) -data[i];
        }

        graphDrawer.drawPolyline(x, y, data.length);
    }
}
