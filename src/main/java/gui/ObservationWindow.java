package gui;

import game.Game;

import javax.swing.*;
import java.awt.*;


public class ObservationWindow extends JInternalFrame implements Disposable {

    private Game game;
    private double X;
    private double Y;

    ObservationWindow(Game game) {
        super("Координаты", true, true, true, true);
        var panel = createPanel(game);
        getContentPane().add(panel);
        pack();
        setVisible(true);
    }

    private JPanel createPanel(Game game) {
        var robot = game.getRobot();
        var panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));

        var xLabel = new CoordsJLabel();
        xLabel.setText("    X: ");
        var yLabel = new CoordsJLabel();
        yLabel.setText("    Y: ");

        var x_value = new CoordsJLabel();
        var y_value = new CoordsJLabel();
        x_value.setText(String.valueOf(robot.getX()));
        y_value.setText(String.valueOf(robot.getY()));

        panel.add(xLabel);
        panel.add(x_value);
        panel.add(yLabel);
        panel.add(y_value);
        return panel;
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private class CoordsJLabel extends JLabel {
        CoordsJLabel() {
            super();
            setOpaque(true);
        }

        protected void paintComponent(Graphics gr) {
            super.paintComponent(gr);
        }
    }
}
