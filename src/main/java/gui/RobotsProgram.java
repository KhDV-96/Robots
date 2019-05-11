package gui;

import localization.LanguageManager;
import serialization.WindowStorage;

import javax.swing.*;

public class RobotsProgram {

    private static final String WINDOW_PATH = "window.ser";
    private static final String RESOURCES_NAME = "Resources";

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
        var languageManager = new LanguageManager(RESOURCES_NAME);
        SwingUtilities.invokeLater(() -> new MainApplicationFrame(storage, languageManager).setVisible(true));
    }
}
