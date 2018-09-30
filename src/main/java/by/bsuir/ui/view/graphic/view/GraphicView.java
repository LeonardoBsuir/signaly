package by.bsuir.ui.view.graphic.view;

import by.bsuir.digitalsignal.model.AreaType;
import by.bsuir.digitalsignal.model.GraphSignalData;
import by.bsuir.ui.view.graphic.cursor.CursorInfo;
import by.bsuir.ui.view.graphic.cursor.CursorPainter;
import by.bsuir.ui.view.graphic.cursor.HistogramCursorPainter;
import by.bsuir.ui.view.graphic.cursor.TimeSignalCursorPainter;
import by.bsuir.ui.view.graphic.cursor.SpectreCursorPainter;
import by.bsuir.ui.view.graphic.chart.GraphPainter;
import by.bsuir.ui.view.graphic.chart.HistogramGraphPainter;
import by.bsuir.ui.view.graphic.chart.TimeSignalGraphPainter;
import by.bsuir.ui.view.graphic.chart.SpectreGraphPainter;
import by.bsuir.ui.view.graphic.grid.GridPainter;
import by.bsuir.ui.view.graphic.grid.HistogramGridPainter;
import by.bsuir.ui.view.graphic.grid.TimeSignalGridPainter;
import by.bsuir.ui.view.graphic.grid.SpectreGridPainter;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class GraphicView extends JComponent implements MouseInputListener {

    public static final int UP_PARAGRAPH = 18;
    public static final int DOWN_PARAGRAPH = 18;

    public static final Font SIGNAL_FONT = new Font("TimesRoman", Font.BOLD, 14);
    public static final Font TEXT_FONT = new Font("TimesRoman", Font.BOLD, 12);
    public static final Font GRID_FONT = new Font("TimesRoman", Font.BOLD, 10);
    public static final Font LEGEND_FONT = new Font("TimesRoman", Font.BOLD, 12);
    public static final Color GRID_COLOR = Color.GRAY;
    public static final Color GRAPH_COLOR = Color.BLUE;
    public static final int TIME_SIGNAL_PADDING = 65;

    private static final Map<AreaType, GridPainter> AREA_TYPE_TO_GRID_PAINTER = new HashMap<AreaType, GridPainter>() {
        {
            put(AreaType.HISTOGRAM, new HistogramGridPainter());
            put(AreaType.TIME_SIGNAL, new TimeSignalGridPainter());
            put(AreaType.SPECTRE, new SpectreGridPainter());
            put(AreaType.LINEAR_SPECTRE, new SpectreGridPainter());
        }
    };
    private static final Map<AreaType, GraphPainter> AREA_TYPE_TO_GRAPH_PAINTER = new HashMap<AreaType, GraphPainter>() {
        {
            put(AreaType.HISTOGRAM, new HistogramGraphPainter());
            put(AreaType.TIME_SIGNAL, new TimeSignalGraphPainter());
            put(AreaType.SPECTRE, new SpectreGraphPainter());
            put(AreaType.LINEAR_SPECTRE, new SpectreGraphPainter());
        }
    };
    private static final Map<AreaType, CursorPainter> AREA_TYPE_TO_CURSOR_PAINTER = new HashMap<AreaType, CursorPainter>() {
        {
            put(AreaType.HISTOGRAM, new HistogramCursorPainter());
            put(AreaType.TIME_SIGNAL, new TimeSignalCursorPainter());
            put(AreaType.SPECTRE, new SpectreCursorPainter());
            put(AreaType.LINEAR_SPECTRE, new SpectreCursorPainter());
        }
    };

    protected final GraphSignalData graphSignalData;
    protected final JFrame parentFrame;
    private final String signalText;

    private int mouseX;
    private boolean windowIsActive = false;

    protected GraphicView(JFrame parentFrame, GraphSignalData graphSignalData) {
        this.parentFrame = parentFrame;
        this.graphSignalData = graphSignalData;
        this.signalText = graphSignalData.toString();
        this.setBackground(Color.WHITE);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public int getMouseX() {
        return mouseX;
    }

    @Override
    public void paint(Graphics graph) {
        Graphics2D g = (Graphics2D) graph;
        g.setColor(this.getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, this.getSize().width - 1, this.getSize().height - 1);
        this.doPaint(g);
    }

    private void doPaint(Graphics g) {
        this.graphSignalData.setWindowSize(this.getWidth(), this.getHeight() - UP_PARAGRAPH - DOWN_PARAGRAPH);
        AreaType areaType = this.graphSignalData.getAreaType();

        if (AreaType.HISTOGRAM == areaType) {
            g.translate(this.getWidth() >> 1, this.getHeight() - UP_PARAGRAPH);
            g.setColor(GRAPH_COLOR);
            this.paintGraph(g);
            g.setColor(GRID_COLOR);
            this.paintGrid(g);
            this.paintCursor(g);
            g.translate(-(this.getWidth() >> 1), -(this.getHeight() - UP_PARAGRAPH));
        } else if (AreaType.TIME_SIGNAL == areaType) {
            g.translate(0, this.getHeight() >> 1);
            g.setColor(GRID_COLOR);
            this.paintGrid(g);
            g.setColor(GRAPH_COLOR);
            this.paintGraph(g);
            this.paintCursor(g);
            g.translate(0, -(this.getHeight() >> 1));
        } else {
            g.translate(0, this.getHeight() - UP_PARAGRAPH);
            g.setColor(GRAPH_COLOR);
            this.paintGraph(g);
            g.setColor(GRID_COLOR);
            this.paintGrid(g);
            this.paintCursor(g);
            g.translate(0, -(this.getHeight() - UP_PARAGRAPH));
        }

        g.setColor(Color.GREEN);
        g.setFont(SIGNAL_FONT);
        g.drawString("( " + this.signalText + " )", 3, SIGNAL_FONT.getSize());
        int signalTextWidth = g.getFontMetrics().stringWidth("( " + this.signalText + " )  ");

        g.setFont(TEXT_FONT);
        String textToDraw = this.graphSignalData.getInfo();
        g.drawString(textToDraw, signalTextWidth + 5, 1 + TEXT_FONT.getSize());
    }

    private void paintGrid(Graphics gridDrawer) {
        gridDrawer.setFont(GRID_FONT);

        AreaType areaType = this.graphSignalData.getAreaType();
        GridPainter gridPainter = AREA_TYPE_TO_GRID_PAINTER.get(areaType);
        gridPainter.paint(this, gridDrawer);
    }

    private void paintGraph(Graphics graphDrawer) {
        AreaType areaType = this.graphSignalData.getAreaType();
        GraphPainter graphPainter = AREA_TYPE_TO_GRAPH_PAINTER.get(areaType);
        graphPainter.paint(this, graphDrawer);
    }

    private void paintCursor(Graphics graphics) {
        if (this.windowIsActive) {
            graphics.setColor(Color.GREEN);
            graphics.setFont(LEGEND_FONT);

            AreaType areaType = this.graphSignalData.getAreaType();
            CursorPainter cursorPainter = AREA_TYPE_TO_CURSOR_PAINTER.get(areaType);
            CursorInfo cursorInfo = cursorPainter.paint(this, graphics);

            if (cursorInfo.shouldPrint()) {
                int widthLegend = graphics.getFontMetrics().stringWidth(cursorInfo.getCursorLegend());
                if (this.mouseX < this.getWidth() - widthLegend) {
                    graphics.drawString(cursorInfo.getCursorLegend(), cursorInfo.getX(), cursorInfo.getY());
                } else {
                    graphics.drawString(cursorInfo.getCursorLegend(), cursorInfo.getX() - widthLegend, cursorInfo.getY());
                }
            }
        }
    }

    public void refresh() {
        this.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent ev) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        this.windowIsActive = true;
        this.repaint();
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        this.windowIsActive = false;
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        this.mouseX = event.getX();
        this.repaint();
    }

    public void close() {
    }

    public GraphSignalData getGraphSignalData() {
        return this.graphSignalData;
    }
}
