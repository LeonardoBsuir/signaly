package by.bsuir.ui.view.graphic.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class EmblemView extends Component {

    private final BufferedImage emblem;
    private VolatileImage volatileImg;

    public EmblemView() {
        this.emblem = null;
        GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.volatileImg = null;

        this.setBackground(Color.lightGray);
    }

    private void createBackBuffer() {
        GraphicsConfiguration gc = this.getGraphicsConfiguration();
        this.volatileImg = gc.createCompatibleVolatileImage(this.getWidth(), this.getHeight());
    }

    public void paint(Graphics graph) {
        if (this.emblem != null) {
            this.createBackBuffer();

            do {
                GraphicsConfiguration gc = this.getGraphicsConfiguration();
                int valCode = this.volatileImg.validate(gc);
                if (valCode == 2) {
                    this.createBackBuffer();
                }

                Graphics offscreenGraphics = this.volatileImg.getGraphics();
                offscreenGraphics.setColor(this.getBackground());
                offscreenGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                this.doPaint(offscreenGraphics);
                graph.drawImage(this.volatileImg, 0, 0, this);
            } while (this.volatileImg.contentsLost());
        }
    }

    private void doPaint(Graphics g) {
        Dimension sz = this.getSize();
        if (sz.width >= this.emblem.getWidth() && sz.height >= this.emblem.getHeight()) {
            g.drawImage(this.emblem, (sz.width >> 1) - (this.emblem.getWidth() >> 1), (sz.height >> 1) - (this.emblem.getHeight() >> 1), null);
        } else {
            Point p;
            if (sz.width < sz.height) {
                p = new Point(0, (sz.height >> 1) - (sz.width >> 1));
                sz.height = sz.width;
            } else {
                p = new Point((sz.width >> 1) - (sz.height >> 1), 0);
                sz.width = sz.height;
            }

            g.drawImage(this.emblem, p.x, p.y, sz.width + p.x, sz.height + p.y, 0, 0, this.emblem.getWidth(), this.emblem.getHeight(), null);
        }
    }
}
