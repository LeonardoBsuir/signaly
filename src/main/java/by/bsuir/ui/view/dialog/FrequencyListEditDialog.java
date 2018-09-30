package by.bsuir.ui.view.dialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

public class FrequencyListEditDialog extends JDialog implements ActionListener {

    private static final int RETURN_CODE_CANCEL = 0;
    private static final int RETURN_CODE_OK = 1;

    private final JTextField txtFreq = new JTextField();
    private final List<Float> frequencyArray = new ArrayList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JButton buttonAdd = new JButton("Добавить");
    private final JButton buttonRemove = new JButton("Удалить");
    private final JButton buttonOk = new JButton("Oк");
    private final JButton buttonCancel = new JButton("Отмена");

    private ListSelectionModel listSelectionModel;
    private int returnCode = 0;

    public FrequencyListEditDialog(Frame frame, String title, float[] frequencies) {
        super(frame, title, true);
        this.initComponent(this.getContentPane());
        this.setLocationRelativeTo(frame);
        this.setFrequencyArray(frequencies);
    }

    private void initComponent(Container c) {
        JList<String> listFreq = new JList<>(this.listModel);
        listFreq.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listFreq.setVisibleRowCount(60);
        this.listSelectionModel = listFreq.getSelectionModel();
        JScrollPane listScrollPane = new JScrollPane(listFreq);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(1), "Частоты"));
        inputPanel.add(this.txtFreq, BorderLayout.BEFORE_LINE_BEGINS);
        inputPanel.add(listScrollPane, BorderLayout.CENTER);

        JPanel centralPanel = new JPanel(new GridLayout(1, 1));
        centralPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centralPanel.add(inputPanel);
        this.buttonAdd.addActionListener(this);
        this.buttonRemove.addActionListener(this);
        this.buttonOk.addActionListener(this);
        this.buttonCancel.addActionListener(this);
        this.getRootPane().setDefaultButton(this.buttonAdd);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 30));

        JPanel buttonPanel1 = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel1.add(this.buttonAdd);
        buttonPanel1.add(this.buttonRemove);
        buttonPanel.add(buttonPanel1);

        JPanel buttonPane2 = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPane2.add(this.buttonOk);
        buttonPane2.add(this.buttonCancel);

        buttonPanel.add(buttonPane2);

        JPanel rightBox = new JPanel(new BorderLayout());
        rightBox.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));
        rightBox.add(buttonPanel, BorderLayout.NORTH);
        c.add(centralPanel, BorderLayout.CENTER);
        c.add(rightBox, BorderLayout.EAST);
        this.pack();
        this.setMinimumSize(new Dimension(355, 345));
        this.setSize(new Dimension(356, 346));
        this.setMaximumSize(new Dimension(400, 500));
    }

    public int showDialog(Component c) {
        this.setLocationRelativeTo(c);
        this.setVisible(true);
        return this.returnCode;
    }

    public float[] getFrequencyArray() {
        float[] arrFreq = new float[this.frequencyArray.size()];

        for (int i = 0; i < arrFreq.length; ++i) {
            arrFreq[i] = this.frequencyArray.get(i);
        }

        return arrFreq;
    }

    private void setFrequencyArray(float[] frequencies) {
        this.frequencyArray.clear();

        for (float frequency : frequencies) {
            this.frequencyArray.add(frequency);
        }

        Collections.sort(this.frequencyArray);
        this.listModel.clear();

        for (Float f : this.frequencyArray) {
            this.listModel.addElement(String.format("%.3f Hz", f));
        }
    }

    public void actionPerformed(ActionEvent event) {
        Object sender = event.getSource();
        if (this.buttonCancel == sender) {
            this.returnCode = RETURN_CODE_CANCEL;
            this.setVisible(false);
        } else if (this.buttonOk == sender) {
            this.returnCode = RETURN_CODE_OK;
            this.setVisible(false);
        }
        if (this.buttonAdd == sender) {
            try {
                float freq = Float.parseFloat(this.txtFreq.getText());
                Float oFreq = freq;
                if (this.frequencyArray.indexOf(oFreq) < 0) {
                    this.frequencyArray.add(oFreq);
                    Collections.sort(this.frequencyArray);
                    int i = this.frequencyArray.indexOf(oFreq);
                    this.listModel.insertElementAt(String.format("%.3f Hz", freq), i);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (this.buttonRemove == sender) {
            if (!this.listSelectionModel.isSelectionEmpty()) {
                int minIndex = this.listSelectionModel.getMinSelectionIndex();
                int maxIndex = this.listSelectionModel.getMaxSelectionIndex();

                for (int i = maxIndex; i >= minIndex; --i) {
                    if (this.listSelectionModel.isSelectedIndex(i)) {
                        this.listModel.removeElementAt(i);
                        this.frequencyArray.remove(i);
                    }
                }
            }
        }
    }
}
