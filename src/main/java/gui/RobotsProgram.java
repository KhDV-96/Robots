package gui;

import serialization.WindowStorage;

import javax.swing.*;

public class RobotsProgram {

    private static final String WINDOW_PATH = "window.ser";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//          UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        var storage = new WindowStorage(WINDOW_PATH);
        SwingUtilities.invokeLater(() -> new MainApplicationFrame(storage).setVisible(true));
    }
}
