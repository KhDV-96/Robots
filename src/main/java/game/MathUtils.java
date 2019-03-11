package game;

class MathUtils {

    static final double TWO_PI = 2 * Math.PI;

    static double distance(Point p1, Point p2) {
        var diffX = p1.x - p2.x;
        var diffY = p1.y - p2.y;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    static double angleTo(Point from, Point to) {
        return asNormalizedRadians(Math.atan2(to.y - from.y, to.x - from.x));
    }

    static double asNormalizedRadians(double angle) {
        angle = angle % TWO_PI;
        return angle >= 0 ? angle : angle + TWO_PI;
    }

    static double minByModulus(double a, double b) {
        return Math.abs(a) < Math.abs(b) ? a : b;
    }

    static double applyLimits(double value, double min, double max) {
        return value < min ? min : value > max ? max : value;
    }
}
