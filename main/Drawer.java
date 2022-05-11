import java.awt.*;

public class Drawer {
    public static void drawRobot(Graphics g, Robot robot) {
        Graphics2D g2D = (Graphics2D) g;
        int leftX = robot.getPosition()[0] - robot.getSizeX();
        int rightX = robot.getPosition()[0] + robot.getSizeX();
        int upperY = robot.getPosition()[1] - robot.getSizeY();
        int lowerY = robot.getPosition()[1] + robot.getSizeY();
        g2D.rotate(Math.toRadians(robot.getOrientation()));
        g2D.fillPolygon(new int[]{leftX, leftX, rightX, rightX}, new int[]{upperY, lowerY, upperY, lowerY}, 4);
        g2D.rotate(-1 * Math.toRadians(robot.getOrientation()));
    }
}
