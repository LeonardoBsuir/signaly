package by.bsuir.ui.view.graphic.cursor;

import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public class HistogramCursorPainter implements CursorPainter {

    @Override
    public CursorInfo paint(GraphicView graphicView, Graphics graphDrawer) {
        GraphSignalData graphSignalData = graphicView.getGraphSignalData();

        int halfKolDiapason = graphSignalData.getCommonData().getHistogramNumberOfRanges() / 2;
        int step = graphSignalData.getCommonData().getHorizontalGridSpacing();
        int halfWidth = graphicView.getWidth() >> 1;

        int cursorX = graphicView.getMouseX() - halfWidth;
        if (cursorX < -halfKolDiapason * step || cursorX > halfKolDiapason * step) {
            return CursorInfo.notPrintable();
        }

        int height = graphicView.getHeight() - GraphicView.DOWN_PARAGRAPH - GraphicView.UP_PARAGRAPH;
        graphDrawer.drawLine(cursorX, -height, cursorX, 0);
        String cursorLegend = String.format(" (%.4f; %.5f) ", graphSignalData.getHorizontalPinchRatio() * cursorX, graphSignalData.getRealData(cursorX + halfKolDiapason * step + 1));

        return CursorInfo.of(cursorLegend, cursorX, -height + 15);
    }
}
