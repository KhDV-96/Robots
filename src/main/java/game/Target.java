package game;

public class Target extends GameObject {

    public Target(double x, double y) {
        super(x, y);
    }

    public void move(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
