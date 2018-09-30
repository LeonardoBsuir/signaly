package by.bsuir.ui.view.frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import by.bsuir.ui.view.panel.ViewPanel;

public class ViewFrame extends JFrame implements ActionListener {

    private static final Dimension PREFERRED_SIZE = new Dimension(1000, 600);

    private final JRadioButtonMenuItem timeSignalMenuItem = new JRadioButtonMenuItem("Временная Реализация", true);
    private final JRadioButtonMenuItem spectreMenuItem = new JRadioButtonMenuItem("Амплитудный Спектр", false);
    private final JRadioButtonMenuItem histogramMenuItem = new JRadioButtonMenuItem("Гистограмма Частот", false);
    private final JRadioButtonMenuItem linearSpectreMenuItem = new JRadioButtonMenuItem("Полосовой Спектр", false);

    public ViewFrame(String title) throws HeadlessException {
        super(title);
        this.setMinimumSize(PREFERRED_SIZE);

        JMenu menuView = new JMenu("Отображение");
        ButtonGroup areaGroup = new ButtonGroup();
        this.timeSignalMenuItem.addActionListener(this);
        areaGroup.add(this.timeSignalMenuItem);
        menuView.add(this.timeSignalMenuItem);
        this.spectreMenuItem.addActionListener(this);
        areaGroup.add(this.spectreMenuItem);
        menuView.add(this.spectreMenuItem);
        this.histogramMenuItem.addActionListener(this);
        areaGroup.add(this.histogramMenuItem);
        menuView.add(this.histogramMenuItem);

        this.linearSpectreMenuItem.addActionListener(this);
        areaGroup.add(this.linearSpectreMenuItem);
        menuView.add(this.linearSpectreMenuItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuView);
        this.setJMenuBar(menuBar);

        this.setAutoRequestFocus(true);
    }

    public void actionPerformed(ActionEvent event) {
        JMenuItem sender = (JMenuItem) event.getSource();
        Container c;
        if (this.timeSignalMenuItem == sender) {
            c = this.getContentPane();
            if (c instanceof ViewPanel) {
                ((ViewPanel) c).setTimeArea(true);
            }
        } else if (this.spectreMenuItem == sender) {
            c = this.getContentPane();
            if (c instanceof ViewPanel) {
                ((ViewPanel) c).setTimeArea(false);
            }
        } else if (this.linearSpectreMenuItem == sender) {
            c = this.getContentPane();
            if (c instanceof ViewPanel) {
                ((ViewPanel) c).setLinearSpectreArea();
            }
        } else if (this.histogramMenuItem == sender) {
            c = this.getContentPane();
            if (c instanceof ViewPanel) {
                ((ViewPanel) c).setHistogram();
            }
        }
    }
}
