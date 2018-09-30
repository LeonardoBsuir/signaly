package by.bsuir.ui.view.dialog;

import java.awt.*;

import javax.swing.*;

public class OptionViewDialog extends JDialog {

    public final JTextField txtYLimit = new JTextField();

    public OptionViewDialog(JFrame frame, Component component) {
        super(frame, "Options", ModalityType.MODELESS);
        this.setType(Type.POPUP);
        this.setUndecorated(true);
        this.initComponent(this);
        this.setLocationRelativeTo(component);
    }

    private void initComponent(Container container) {
        JPanel limitPanel = new JPanel(new GridLayout(1, 2));
        limitPanel.add(new JLabel("Ограничение y: "));
        limitPanel.add(this.txtYLimit);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        mainPanel.add(limitPanel);
        container.add(mainPanel, BorderLayout.CENTER);
        this.pack();
    }

    public void setDataLimit(float f) {
        this.txtYLimit.setText(Float.toString(f));
    }
}
