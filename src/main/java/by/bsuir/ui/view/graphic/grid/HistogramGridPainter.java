package by.bsuir.ui.view.graphic.grid;

import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public class HistogramGridPainter implements GridPainter {

    @Override
    public void paint(GraphicView graphicView, Graphics gridDrawer) {
        int halfHeight = graphicView.getHeight() - GraphicView.DOWN_PARAGRAPH - GraphicView.UP_PARAGRAPH;
        int halfKolDiapason = graphicView.getGraphSignalData().getCommonData().getHistogramNumberOfRanges() / 2;
        int step = graphicView.getGraphSignalData().getCommonData().getHorizontalGridSpacing();
        boolean isEven = false;

        for (int i = -halfKolDiapason * step; i <= halfKolDiapason * step; i += step) {
            gridDrawer.drawLine(i, 0, i, -halfHeight);
            isEven = !isEven;
            if (isEven) {
                String textToDraw = String.format("%.3f", i * graphicView.getGraphSignalData().getHorizontalPinchRatio());
                int textWidth = gridDrawer.getFontMetrics().stringWidth(textToDraw);
                gridDrawer.drawString(textToDraw, i - (textWidth >> 1), GraphicView.GRID_FONT.getSize() + 2);
            }
        }

        step = graphicView.getGraphSignalData().getCommonData().getVerticalGridSpacing();

        for (int i = 0; i > -halfHeight; i -= step) {
            gridDrawer.drawLine(-halfKolDiapason * step, i, halfKolDiapason * step, i);
            if (i != 0) {
                String textToDraw = String.format("%.3f", (-i) * graphicView.getGraphSignalData().getVerticalPinchRatio());
                gridDrawer.drawString(textToDraw, 2, i + GraphicView.GRID_FONT.getSize() + 2);
            }
        }
    }
}
