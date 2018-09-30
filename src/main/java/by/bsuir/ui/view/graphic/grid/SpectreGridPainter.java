package by.bsuir.ui.view.graphic.grid;

import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public class SpectreGridPainter implements GridPainter {

    @Override
    public void paint(GraphicView graphicView, Graphics gridDrawer) {
        int halfHeight = graphicView.getHeight() - GraphicView.DOWN_PARAGRAPH - GraphicView.UP_PARAGRAPH;
        int touchSize = 4;
        gridDrawer.drawLine(0, 0, graphicView.getWidth(), 0);
        int step = graphicView.getGraphSignalData().getCommonData().getHorizontalGridSpacing();
        boolean isEven = true;

        for (int i = 0; i <= graphicView.getWidth(); i += step) {
            gridDrawer.drawLine(i, -touchSize, i, touchSize);
            isEven = !isEven;
            if (isEven) {
                String textToDraw = String.format("%.3f", i * graphicView.getGraphSignalData().getHorizontalPinchRatio());
                int textWidth = gridDrawer.getFontMetrics().stringWidth(textToDraw);
                gridDrawer.drawString(textToDraw, i - (textWidth >> 1), GraphicView.GRID_FONT.getSize() + 2);
            }
        }

        step = graphicView.getGraphSignalData().getCommonData().getVerticalGridSpacing();
        gridDrawer.drawLine(0, -halfHeight, 0, 0);

        for (int i = 0; i > -halfHeight; i -= step) {
            gridDrawer.drawLine(0, i, touchSize, i);
            if (i != 0) {
                String textToDraw = String.format("%.3f", -i * graphicView.getGraphSignalData().getVerticalPinchRatio());
                gridDrawer.drawString(textToDraw, 2, i + GraphicView.GRID_FONT.getSize() + 2);
            }
        }
    }
}
