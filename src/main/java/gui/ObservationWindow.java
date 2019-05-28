package gui;

import game.GameObject;
import localization.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class ObservationWindow extends JInternalFrame implements Disposable {

    private GameObject gameObject;
    private JTextArea textArea;

    public ObservationWindow(LanguageManager languageManager, GameObject gameObject) {
        super(null, true, true, true, true);
        languageManager.bindField("observationWindow.title", this::setTitle);
        this.gameObject = gameObject;

        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 17));
        textArea.setEditable(false);

        gameObject.addObserver(this::update);

        getContentPane().add(textArea);
        pack();
    }

    @Override
    public void onDispose() {
        gameObject.deleteObserver(this::update);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void update(Observable o, Object arg) {
        SwingUtilities.invokeLater(
                () -> textArea.setText(String.format("X: %.2f\nY: %.2f", gameObject.getX(), gameObject.getY()))
        );
    }
}
