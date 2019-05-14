package gui;

import localization.LanguageManager;

import game.Game;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame implements Disposable {

    public GameWindow(LanguageManager languageManager, Game game) {
        super(null, true, true, true, true);
        languageManager.bindField("gameWindow.title", this::setTitle);

        getContentPane().add(new GameVisualizer(game), BorderLayout.CENTER);
        pack();
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
