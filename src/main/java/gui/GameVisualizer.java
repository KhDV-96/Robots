package gui;

import game.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.round;

public class GameVisualizer extends JPanel {

    private final Robot robot;

    GameVisualizer() {
        robot = new Robot(100, 100, 0);
        robot.setTarget(150, 100);

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
                robot.update();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                robot.setTarget(e.getX(), e.getY());
                repaint();
            }
        });

        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        var g2d = (Graphics2D) g;
        drawRobot(g2d, (int) round(robot.getPosition().x), (int) round(robot.getPosition().y), robot.getDirection());
        drawTarget(g2d, (int) round(robot.getTarget().x), (int) round(robot.getTarget().y));
    }

    private void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction) {
        g.setTransform(AffineTransform.getRotateInstance(direction, x, y));
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);
        g.setTransform(AffineTransform.getRotateInstance(0, 0, 0));
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
