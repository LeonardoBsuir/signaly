package by.bsuir.ui.view.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import by.bsuir.ui.view.panel.ViewPanel;

public class FilterDialog extends JDialog implements ActionListener {

    private static final String DIALOG_TITLE = "Фильтрация";
    private static final int CANCEL_CODE = 0;
    private static final int OK_CODE = 1;

    private final JTextField minFrequencyTextField = new JTextField();
    private final JTextField maxFrequencyTextField = new JTextField();
    private final JComboBox timeSizeComboBox;
    private final JButton buttonCancel;
    private final JButton buttonOk;
    private int returnCode;

    public FilterDialog(Frame frame, boolean shouldShowTimeSizeCombo) {
        super(frame, DIALOG_TITLE, true);
        this.timeSizeComboBox = new JComboBox<>(ViewPanel.NUMBER_OF_DOTS);
        this.returnCode = 0;
        this.buttonCancel = new JButton("Отмена");
        this.buttonOk = new JButton("Ok");
        this.initComponent(this.getContentPane(), shouldShowTimeSizeCombo);
        this.setLocationRelativeTo(frame);
    }

    public int showDialog(Component component) {
        this.setLocationRelativeTo(component);
        this.setVisible(true);
        return this.returnCode;
    }

    private void initComponent(Container container, boolean shouldShowTimeSizeCombo) {
        byte rows = 2;
        if (shouldShowTimeSizeCombo) {
            ++rows;
        }

        JPanel panel = new JPanel(new GridLayout(rows, 2));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Частоты"));
        panel.add(new JLabel("Минимальное значение: "));
        panel.add(this.minFrequencyTextField);
        panel.add(new JLabel("Максимальное значение: "));
        panel.add(this.maxFrequencyTextField);
        if (shouldShowTimeSizeCombo) {
            panel.add(new JLabel("Число дискретных точек: "));
            panel.add(this.timeSizeComboBox);
        }

        JPanel centralPanel = new JPanel(new GridLayout(1, 1));
        centralPanel.setBorder(BorderFactory.createEmptyBorder(6, 10, 3, 10));
        centralPanel.add(panel);

        this.buttonCancel.addActionListener(this);
        this.buttonOk.addActionListener(this);
        this.getRootPane().setDefaultButton(this.buttonOk);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(3, 60, 6, 12));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(this.buttonOk);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(this.buttonCancel);

        container.add(centralPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

        this.pack();
    }

    public Float getMinFrequency() {
        String minFrequency = this.minFrequencyTextField.getText();
        if (minFrequency != null && !"".equals(minFrequency)) {
            return Float.parseFloat(minFrequency);
        } else {
            return null;
        }
    }

    public Float getMaxFrequency() {
        String maxFrequency = this.maxFrequencyTextField.getText();
        if (maxFrequency != null && !"".equals(maxFrequency)) {
            return Float.parseFloat(maxFrequency);
        } else {
            return null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object sender = event.getSource();
        if (this.buttonCancel == sender) {
            this.returnCode = CANCEL_CODE;
            this.setVisible(false);
        } else if (this.buttonOk == sender) {
            if (this.minFrequencyTextField.getText().equals("") && this.maxFrequencyTextField.getText().equals("")) {
                this.returnCode = CANCEL_CODE;
            } else {
                this.returnCode = OK_CODE;
            }
            this.setVisible(false);
        }
    }
}
