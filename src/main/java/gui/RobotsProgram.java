package gui;

import serialization.Serializer;

import javax.swing.*;
import java.awt.*;

public class RobotsProgram {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//          UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        var restoredFrame = (MainApplicationFrame) Serializer.load(MainApplicationFrame.SERIALIZATION_FILE);
        SwingUtilities.invokeLater(() -> {
            var frame = restoredFrame;
            if (frame == null) {
                frame = new MainApplicationFrame();
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                frame.pack();
            }
            frame.setVisible(true);
        });
    }
}
