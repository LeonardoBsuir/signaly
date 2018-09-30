package by.bsuir.ui;

import by.bsuir.ui.view.frame.MainFrame;
import com.alee.laf.WebLookAndFeel;

import javax.swing.*;
import java.util.Locale;

public class Starter {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("RU"));
        SwingUtilities.invokeLater(() -> {
            WebLookAndFeel.install();
            MainFrame mainWnd = new MainFrame("SignalAnalyzer");
            mainWnd.setVisible(true);
        });
    }
}