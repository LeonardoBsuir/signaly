package by.bsuir.ui.view.graphic.cursor;

import by.bsuir.ui.view.graphic.view.GraphicView;

import java.awt.Graphics;

public interface CursorPainter {

    CursorInfo paint(GraphicView graphicView, Graphics graphDrawer);
}
