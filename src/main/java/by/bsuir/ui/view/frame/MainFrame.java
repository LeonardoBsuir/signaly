package by.bsuir.ui.view.frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import by.bsuir.digitalsignal.model.LinearFrequency;
import by.bsuir.ui.file.FilesOpener;
import by.bsuir.ui.view.dialog.FrequencyListEditDialog;
import by.bsuir.ui.view.graphic.view.EmblemView;
import by.bsuir.ui.view.panel.DataViewPanel;
import by.bsuir.ui.view.panel.ViewPanel;

public class MainFrame extends JFrame implements ActionListener {

    private final FilesOpener filesOpener = new FilesOpener(this);

    private final JMenuItem openMenuItem = new JMenuItem("Открыть");
    private final JMenuItem quitMenuItem = new JMenuItem("Выйти");
    private final JRadioButtonMenuItem timeSignalMenuItem = new JRadioButtonMenuItem("Временная Реализация", true);
    private final JRadioButtonMenuItem spectreMenuItem = new JRadioButtonMenuItem("Амплитудный Спектр", false);
    private final JRadioButtonMenuItem histogramMenuItem = new JRadioButtonMenuItem("Гистограмма Частот", false);
    private final JRadioButtonMenuItem linearSpectreMenuItem = new JRadioButtonMenuItem("Полосовой Спектр", false);
    private final JMenuItem linearFreqItem = new JMenuItem("Частотный диапазон");

    public MainFrame(String title) throws HeadlessException {
        super(title);
        this.setBounds(new Rectangle(0, 0, 1200, 600));
        this.setMinimumSize(new Dimension(1000, 600));


        JMenu menuFile = new JMenu("Файл");
        this.openMenuItem.addActionListener(this);
        menuFile.add(this.openMenuItem);
        menuFile.addSeparator();
        this.quitMenuItem.addActionListener(this);
        menuFile.add(this.quitMenuItem);

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

        menuView.addSeparator();
        this.linearSpectreMenuItem.addActionListener(this);
        areaGroup.add(this.linearSpectreMenuItem);
        menuView.add(this.linearSpectreMenuItem);
        this.linearFreqItem.addActionListener(this);
        menuView.add(this.linearFreqItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuView);
        this.setJMenuBar(menuBar);

        JPanel pane = this.getDefaultContentPane();
        this.setContentPane(pane);
        this.disableMenu();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                quit();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JMenuItem sender = (JMenuItem) event.getSource();
        Container c;
        if (this.openMenuItem == sender) {
            if (filesOpener.openFile()) {
                File[] files = filesOpener.getFiles();
                Container container = this.getContentPane();
                if (container != null && container instanceof DataViewPanel) {
                    ((DataViewPanel) container).close();
                }

                try {
                    this.setContentPane(new DataViewPanel(this, files, true));
                    this.enableMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                    this.setContentPane(this.getDefaultContentPane());
                    this.disableMenu();
                }

                this.setVisible(true);
            }
        } else if (this.quitMenuItem == sender) {
            this.quit();
        } else if (this.timeSignalMenuItem == sender) {
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
        } else if (this.linearFreqItem == sender) {
            FrequencyListEditDialog frequencyListDialog = new FrequencyListEditDialog(this, this.linearFreqItem.getText(), LinearFrequency.getArrayFreq());
            if (frequencyListDialog.showDialog(this) == 1) {
                LinearFrequency.setArrayFreq(frequencyListDialog.getFrequencyArray());
            }
        }
    }

    private void quit() {
        Container c = this.getContentPane();
        if (c != null && c instanceof DataViewPanel) {
            ((DataViewPanel) c).close();
        }

        System.exit(0);
    }

    private JPanel getDefaultContentPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        panel.add(new EmblemView(), BorderLayout.CENTER);
        return panel;
    }

    private void enableMenu() {
        switchEnabling(true);
    }

    private void disableMenu() {
        switchEnabling(false);
    }

    private void switchEnabling(boolean enabled) {
        this.timeSignalMenuItem.setEnabled(enabled);
        this.spectreMenuItem.setEnabled(enabled);
        this.linearSpectreMenuItem.setEnabled(enabled);
        this.histogramMenuItem.setEnabled(enabled);
    }
}
