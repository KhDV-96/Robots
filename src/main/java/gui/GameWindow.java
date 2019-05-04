package gui;

import game.Game;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame implements Disposable {

    public GameWindow(Game game) {
        super("Игровое поле", true, true, true, true);
        getContentPane().add(new GameVisualizer(game), BorderLayout.CENTER);
        pack();
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
