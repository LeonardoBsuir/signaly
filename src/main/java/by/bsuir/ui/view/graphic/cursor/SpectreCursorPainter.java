package by.bsuir.ui.view.graphic.cursor;

import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public class SpectreCursorPainter implements CursorPainter {

    @Override
    public CursorInfo paint(GraphicView graphicView, Graphics graphDrawer) {
        int y = graphicView.getHeight() - GraphicView.DOWN_PARAGRAPH - GraphicView.UP_PARAGRAPH;
        int mouseX = graphicView.getMouseX();
        GraphSignalData graphSignalData = graphicView.getGraphSignalData();

        graphDrawer.drawLine(mouseX, -y, mouseX, 0);
        String cursorLegend = String.format(
                " (%.4f; %.5f) ",
                graphSignalData.getHorizontalPinchRatio() * mouseX,
                graphSignalData.getRealData(mouseX)
        );

        return CursorInfo.of(cursorLegend, mouseX, -y + 15);
    }
}
