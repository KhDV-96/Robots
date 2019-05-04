package gui;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.round;

public class GameVisualizer extends JPanel {

    private final Game game;

    GameVisualizer(Game game) {
        this.game = game;

        var timer = new Timer("Events generator", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (game) {
                    game.update();
                }
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                synchronized (game) {
                    game.getTarget().move(e.getX(), e.getY());
                }
                repaint();
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                synchronized (game) {
                    game.getMap().setWidth(getWidth());
                    game.getMap().setHeight(getHeight());
                }
            }
        });

        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        double robotX, robotY, robotDirection, targetX, targetY;
        synchronized (game) {
            robotX = game.getRobot().getX();
            robotY = game.getRobot().getY();
            robotDirection = game.getRobot().getDirection();
            targetX = game.getTarget().getX();
            targetY = game.getTarget().getY();
        }
        var g2d = (Graphics2D) g;
        drawRobot(g2d, (int) round(robotX), (int) round(robotY), robotDirection);
        drawTarget(g2d, (int) round(targetX), (int) round(targetY));
    }

    private void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        g.setTransform(AffineTransform.getRotateInstance(direction, x, y));
        drawOvalWithCircuit(g, x, y, 30, 10, Color.MAGENTA);
        drawOvalWithCircuit(g, x + 10, y, 5, 5, Color.WHITE);
        g.setTransform(AffineTransform.getRotateInstance(0, 0, 0));
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        drawOvalWithCircuit(g, x, y, 5, 5, Color.GREEN);
    }

    private static void drawOvalWithCircuit(Graphics g, int x, int y, int diam1, int diam2, Color fill) {
        x -= diam1 / 2;
        y -= diam2 / 2;
        g.setColor(fill);
        g.fillOval(x, y, diam1, diam2);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, diam1, diam2);
    }
}
