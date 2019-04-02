package game;

import java.util.function.Function;

public class Robot extends GameObject {

    private static final double RADIUS = 0.5;
    private static final double DURATION = 10;
    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;

    private double direction;

    public Robot(double x, double y, double direction) {
        super(x, y);
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    void update(Map map) {
        var target = map.getTarget();
        if (MathUtils.distance(x, y, target.x, target.y) >= RADIUS) {
            moveToTarget(target);
        }
        x = pushOffFromBorder(x, map.getWidth(), MathUtils.TWO_PI);
        y = pushOffFromBorder(y, map.getHeight(), Math.PI);
    }

    private void moveToTarget(GameObject target) {
        var angularVelocity = calculateAngularVelocity(target);
        var velocityRatio = MAX_VELOCITY / angularVelocity;
        var newDirection = direction + angularVelocity * DURATION;
        x = shiftCoordinate(x, velocityRatio, newDirection, Math::sin, Math::cos);
        y = shiftCoordinate(y, -velocityRatio, newDirection, Math::cos, Math::sin);
        direction = MathUtils.asNormalizedRadians(newDirection);
    }

    private double calculateAngularVelocity(GameObject target) {
        var angleToTarget = MathUtils.angleTo(x, y, target.x, target.y) - direction;
        angleToTarget = MathUtils.asNormalizedRadians(angleToTarget);
        var angleDifference = MathUtils.minByModulus(angleToTarget - MathUtils.TWO_PI, angleToTarget);
        return Math.signum(angleDifference) * MAX_ANGULAR_VELOCITY;
    }

    private double shiftCoordinate(double coordinate, double velocity, double angle,
                                   Function<Double, Double> f1, Function<Double, Double> f2) {
        var newCoordinate = coordinate + velocity * (f1.apply(angle) - f1.apply(direction));
        if (Double.isFinite(newCoordinate)) {
            return newCoordinate;
        }
        return coordinate + MAX_VELOCITY * DURATION + f2.apply(direction);
    }

    private double pushOffFromBorder(double coordinate, double border, double criticalAngle) {
        if (coordinate < 0 || coordinate > border) {
            if (direction % (criticalAngle / 2) > MAX_ANGULAR_VELOCITY) {
                direction = MathUtils.TWO_PI - criticalAngle - direction;
            }
            direction = MathUtils.asNormalizedRadians(direction + Math.PI);
            coordinate = MathUtils.applyLimits(coordinate, 0, border);
        }
        return coordinate;
    }
}
