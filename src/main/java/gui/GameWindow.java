package gui;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame implements Disposable {

    public GameWindow() {
        super("Игровое поле", true, true, true, true);
        var visualizer = new GameVisualizer();
        var panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
