package game;

public class Game {

    private static final int MAP_WIDTH = 500;
    private static final int MAP_HEIGHT = 500;
    private static final int ROBOT_X = 100;
    private static final int ROBOT_Y = 100;
    private static final int ROBOT_DIRECTION = 0;
    private static final int TARGET_X = 150;
    private static final int TARGET_Y = 100;

    private final Map map;
    private final Robot robot;
    private final Target target;

    public Game() {
        map = new Map(MAP_WIDTH, MAP_HEIGHT);
        robot = new Robot(ROBOT_X, ROBOT_Y, ROBOT_DIRECTION);
        target = new Target(TARGET_X, TARGET_Y);
    }

    public void update() {
        robot.update(map, target);
    }

    public Map getMap() {
        return map;
    }

    public Robot getRobot() {
        return robot;
    }

    public Target getTarget() {
        return target;
    }
}
