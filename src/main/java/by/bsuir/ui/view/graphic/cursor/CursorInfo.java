package by.bsuir.ui.view.graphic.cursor;

public class CursorInfo {

    private final String cursorLegend;
    private final int x;
    private final int y;
    private final boolean shouldPrint;

    private CursorInfo(String cursorLegend, int x, int y, boolean shouldPrint) {
        this.cursorLegend = cursorLegend;
        this.x = x;
        this.y = y;
        this.shouldPrint = shouldPrint;
    }

    public String getCursorLegend() {
        return cursorLegend;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean shouldPrint() {
        return shouldPrint;
    }

    public static CursorInfo of(String cursorLegend, int x, int y) {
        return new CursorInfo(cursorLegend, x, y, true);
    }

    public static CursorInfo notPrintable() {
        return new CursorInfo(null, 0, 0, false);
    }
}
