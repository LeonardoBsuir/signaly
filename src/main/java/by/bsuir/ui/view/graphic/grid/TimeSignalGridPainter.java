package by.bsuir.ui.view.graphic.grid;

import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public class TimeSignalGridPainter implements GridPainter {

    @Override
    public void paint(GraphicView graphicView, Graphics gridDrawer) {
        int halfHeight = (graphicView.getHeight() >> 1) - GraphicView.UP_PARAGRAPH;
        int step = graphicView.getGraphSignalData().getCommonData().getHorizontalGridSpacing();
        boolean isEven = true;

        for (int i = GraphicView.TIME_SIGNAL_PADDING; i <= graphicView.getWidth(); i += step) {
            gridDrawer.drawLine(i, -halfHeight, i, halfHeight);
            isEven = !isEven;
            if (isEven) {
                String textToDraw = String.format("%.4f", 
                        graphicView.getGraphSignalData().getHorizontalPinchRatio() 
                        * (i + graphicView.getGraphSignalData().getDisplacement() - GraphicView.TIME_SIGNAL_PADDING));
                int textWidth = gridDrawer.getFontMetrics().stringWidth(textToDraw);
                gridDrawer.drawString(textToDraw, i - (textWidth >> 1), halfHeight + GraphicView.GRID_FONT.getSize());
            }
        }

        step = graphicView.getGraphSignalData().getCommonData().getVerticalGridSpacing();

        for (int i = 0; i > -halfHeight; i -= step) {
            gridDrawer.drawLine(0, i, graphicView.getWidth(), i);
            String textToDraw = String.format("%.3f", -i * graphicView.getGraphSignalData().getVerticalPinchRatio());
            int textWidth = gridDrawer.getFontMetrics().stringWidth(textToDraw);
            gridDrawer.drawString(textToDraw, GraphicView.TIME_SIGNAL_PADDING - textWidth - 2, i + GraphicView.GRID_FONT.getSize() + 2);
        }

        for (int i = step; i < halfHeight; i += step) {
            gridDrawer.drawLine(0, i, graphicView.getWidth(), i);
            if (i < halfHeight - GraphicView.GRID_FONT.getSize()) {
                String textToDraw = String.format("-%.3f", i * graphicView.getGraphSignalData().getVerticalPinchRatio());
                int textWidth = gridDrawer.getFontMetrics().stringWidth(textToDraw);
                gridDrawer.drawString(textToDraw, GraphicView.TIME_SIGNAL_PADDING - textWidth - 2, i + GraphicView.GRID_FONT.getSize() + 2);
            }
        }
    }
}
