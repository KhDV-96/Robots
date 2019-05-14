package gui;

import game.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class ObservationWindow extends JInternalFrame implements Disposable {

    private GameObject gameObject;
    private JTextArea textArea;

    ObservationWindow(GameObject gameObject) {
        super("Координаты", true, true, true, true);
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
        textArea.setText(String.format("X: %.2f\nY: %.2f", gameObject.getX(), gameObject.getY()));
    }
}
