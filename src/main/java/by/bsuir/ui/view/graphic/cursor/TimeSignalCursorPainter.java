package by.bsuir.ui.view.graphic.cursor;

import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public class TimeSignalCursorPainter implements CursorPainter {

    @Override
    public CursorInfo paint(GraphicView graphicView, Graphics graphDrawer) {
        int mouseX = graphicView.getMouseX();
        if (mouseX < GraphicView.TIME_SIGNAL_PADDING) {
            return CursorInfo.notPrintable();
        } else {
            int y = (graphicView.getHeight() >> 1) - GraphicView.UP_PARAGRAPH;
            GraphSignalData graphSignalData = graphicView.getGraphSignalData();

            graphDrawer.drawLine(mouseX, -y, mouseX, y);
            String cursorLegend = String.format(
                    " (%.4f; %.5f) ",
                    graphSignalData.getHorizontalPinchRatio() * (mouseX + graphSignalData.getDisplacement() - GraphicView.TIME_SIGNAL_PADDING),
                    graphSignalData.getRealData(mouseX - GraphicView.TIME_SIGNAL_PADDING)
            );

            return CursorInfo.of(cursorLegend, mouseX, -y + 12);
        }
    }
}
