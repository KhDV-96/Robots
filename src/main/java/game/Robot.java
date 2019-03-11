package game;

import java.util.function.Function;

public class Robot {

    private static final double RADIUS = 0.5;
    private static final double DURATION = 10;
    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;

    private Point position, target;
    private double direction;

    public Robot(double x, double y, double direction) {
        this.position = new Point(x, y);
        this.direction = direction;
    }

    public void update() {
        if (MathUtils.distance(position, target) >= RADIUS) {
            moveToTarget();
        }
    }

    private void moveToTarget() {
        var angularVelocity = calculateAngularVelocity();
        var velocityRatio = MAX_VELOCITY / angularVelocity;
        var newDirection = direction + angularVelocity * DURATION;
        var newX = shiftCoordinate(position.x, velocityRatio, newDirection, Math::sin, Math::cos);
        var newY = shiftCoordinate(position.y, -velocityRatio, newDirection, Math::cos, Math::sin);
        position = new Point(newX, newY);
        direction = MathUtils.asNormalizedRadians(newDirection);
    }

    private double calculateAngularVelocity() {
        var angleToTarget = MathUtils.angleTo(position, target) - direction;
        var angleDifference = MathUtils.minByModulus(angleToTarget - MathUtils.TWO_PI, angleToTarget);
        return Math.signum(angleDifference) * MAX_ANGULAR_VELOCITY;
    }

    private double shiftCoordinate(double coordinate, double velocity, double angle,
                                   Function<Double, Double> f1, Function<Double, Double> f2) {
        var newCoordinate = coordinate + velocity * (f1.apply(angle) - f1.apply(direction));
        if (Double.isFinite(newCoordinate))
            return newCoordinate;
        return coordinate + MAX_VELOCITY * DURATION + f2.apply(direction);
    }

    public Point getTarget() {
        return target;
    }

    public Point getPosition() {
        return position;
    }

    public void setTarget(double x, double y) {
        target = new Point(x, y);
    }

    public double getDirection() {
        return direction;
    }
}
