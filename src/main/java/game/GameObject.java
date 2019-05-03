package game;

import java.util.Observable;

public class GameObject extends Observable {

    double x, y;

    GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
