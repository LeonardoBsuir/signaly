package by.bsuir.ui.view.panel;

import by.bsuir.digitalsignal.model.CommonData;
import by.bsuir.ui.view.graphic.view.GraphicView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;

public abstract class ViewPanel extends JPanel implements AdjustmentListener, ActionListener {

    public static final String[] NUMBER_OF_DOTS = new String[]{"64", "128", "256", "512", "1024", "2048", "4096", "8192", "16384", "32768"};
    static final int UNIT = 16;
    private static final int MIN_GRAPH_HEIGHT = 100;
    private static final int DEFAULT_GRAPH_HEIGHT = 300;
    private static final int MAX_GRAPH_HEIGHT = 1000;

    protected final JFrame parent;
    protected JPanel centerPanel;
    protected final CommonData commonData = new CommonData();
    protected final int colCount = 1;
    protected int rowCount;
    private int scrollPage;
    protected int graphHeight = DEFAULT_GRAPH_HEIGHT;
    private final boolean isCommonOption;

    private final JToolBar toolBar;
    private final JComboBox comboFieldSize;
    private JTextField yLimitTextField;
    private final JTextField graphHeightLimitTextField;
    protected final JScrollBar scrollBar;

    public ViewPanel(JFrame frame, boolean isCommonOption) {
        super(new BorderLayout());

        this.parent = frame;
        this.scrollPage = 1024;
        this.isCommonOption = isCommonOption;

        this.scrollBar = new JScrollBar(Adjustable.HORIZONTAL);
        this.scrollBar.setUnitIncrement(30);
        this.scrollBar.setBlockIncrement(this.scrollPage);
        this.scrollBar.addAdjustmentListener(this);

        this.comboFieldSize = new JComboBox<>(NUMBER_OF_DOTS);
        this.comboFieldSize.setMaximumSize(new Dimension(99, 25));
        this.comboFieldSize.setMinimumSize(new Dimension(98, 24));

        this.toolBar = new JToolBar();
        this.toolBar.add(new JLabel(" Число дискретных точек: "));
        this.toolBar.add(this.comboFieldSize);
        this.toolBar.setFloatable(true);
        this.toolBar.setRollover(true);

        this.graphHeightLimitTextField = new JTextField();
        this.graphHeightLimitTextField.setMaximumSize(new Dimension(99, 25));
        this.graphHeightLimitTextField.setMinimumSize(new Dimension(98, 24));
        this.graphHeightLimitTextField.setText(Integer.toString(this.graphHeight));
        this.graphHeightLimitTextField.addActionListener(this);

        this.toolBar.add(new JLabel(" Минимальная высота графика: "));
        this.toolBar.add(this.graphHeightLimitTextField);

        this.add(this.toolBar, BorderLayout.BEFORE_FIRST_LINE);
        this.add(this.scrollBar, BorderLayout.SOUTH);
    }

    protected void setScrollBlockIncrement(boolean boCommonOption, int increment) {
        if (this.scrollPage > this.commonData.maxDataLength) {
            this.scrollPage = this.commonData.maxDataLength;
        }

        if (boCommonOption) {
            this.scrollBar.setBlockIncrement(increment);
        } else {
            this.scrollBar.setBlockIncrement(this.scrollPage);
        }
    }

    protected void setScrollMaximum(int max) {
        if (max < 0) {
            this.scrollBar.setMaximum(0);
        } else {
            this.scrollBar.setMaximum(max);
        }
    }

    protected void setCountColumnsAndRows(int displayedPanels) {
        this.rowCount = displayedPanels;
    }

    public void setTimeArea(boolean isTime) {
        this.refresh();
        this.updateOptionDialog();
    }

    public void setLinearSpectreArea() {
        this.refresh();
        this.updateOptionDialog();
    }

    public void setHistogram() {
        this.refresh();
        this.updateOptionDialog();
    }

    public void refresh() {
        Component[] centerPanelComponents = this.centerPanel.getComponents();

        for (Component component : centerPanelComponents) {
            if (component instanceof GraphicView) {
                ((GraphicView) component).refresh();
            } else {
                component.repaint();
            }
        }
    }

    public void close() {
        if (this.centerPanel != null) {
            Component[] centerPanelComponents = this.centerPanel.getComponents();

            for (Component comp : centerPanelComponents) {
                if (comp instanceof GraphicView) {
                    ((GraphicView) comp).close();
                }
            }
            
            this.toolBar.setVisible(false);
        }
    }

    protected void createOptionDialog() {
        this.comboFieldSize.addActionListener(this);
        this.setTimeFieldSizeComboBox(this.commonData.getTimeFieldSize());
        if (this.isCommonOption) {
            this.yLimitTextField = new JTextField();
            this.yLimitTextField.setMaximumSize(new Dimension(99, 25));
            this.yLimitTextField.setMinimumSize(new Dimension(98, 24));
            this.yLimitTextField.setText(Float.toString(this.commonData.getLimit()));
            this.yLimitTextField.addActionListener(this);

            this.toolBar.add(new JLabel(" Ограничение оси ординат: "));
            this.toolBar.add(this.yLimitTextField);
        }
    }

    private void setTimeFieldSizeComboBox(int iSize) {
        for (int i = 0; i < NUMBER_OF_DOTS.length; ++i) {
            if (NUMBER_OF_DOTS[i].equals(Integer.toString(iSize))) {
                this.comboFieldSize.setSelectedIndex(i);
                return;
            }
        }
    }

    public void setTimeFieldSize(int iSize) {
        this.commonData.setTimeFieldSize(iSize);
    }

    private void updateOptionDialog() {
        if (this.isCommonOption) {
            this.yLimitTextField.setText(Float.toString(this.commonData.getLimit()));
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object sender = event.getSource();
        if (sender == this.comboFieldSize) {
            int iTimeFieldSize = Integer.parseInt(this.comboFieldSize.getSelectedItem().toString());
            this.setTimeFieldSize(iTimeFieldSize);
            this.scrollBar.setBlockIncrement(iTimeFieldSize);
            this.setScrollMaximum(this.commonData.maxDataLength - iTimeFieldSize);
            this.repaint();
        } else if (this.isCommonOption && sender == this.yLimitTextField) {
            float dataLimit = 0;

            try {
                dataLimit = Float.parseFloat(this.yLimitTextField.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.commonData.setLimit(dataLimit);
            this.repaint();
        } else if (sender == this.graphHeightLimitTextField) {
            int height = DEFAULT_GRAPH_HEIGHT;

            try {
                height = Integer.parseInt(this.graphHeightLimitTextField.getText());
                if (height < MIN_GRAPH_HEIGHT || height > MAX_GRAPH_HEIGHT) {
                    height = DEFAULT_GRAPH_HEIGHT;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.graphHeightLimitTextField.setText(Integer.toString(height));
            this.graphHeight = height;
            this.repaint();
        }
    }

    @Override
    public void repaint() {
        if (this.centerPanel != null) {
            this.centerPanel.setPreferredSize(new Dimension(0, this.rowCount * this.graphHeight));
            this.centerPanel.getParent().revalidate();
        }
        super.repaint();
    }
}
