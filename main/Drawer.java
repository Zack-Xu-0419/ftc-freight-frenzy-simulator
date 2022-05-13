import java.awt.*;

import GameElements.*;

public class Drawer {
    public static void drawRobot(Graphics g, Robot robot) {
        Graphics2D g2D = (Graphics2D) g;
        int leftX = robot.getPosition()[0] - robot.getSizeX();
        int rightX = robot.getPosition()[0] + robot.getSizeX();
        int upperY = robot.getPosition()[1] - robot.getSizeY();
        int lowerY = robot.getPosition()[1] + robot.getSizeY();
        g2D.rotate(Math.toRadians(robot.getOrientation()), robot.getPosition()[0], robot.getPosition()[1]);
        g2D.fillPolygon(new int[] { leftX, leftX, rightX, rightX }, new int[] { upperY, lowerY, lowerY, upperY }, 4);
        g2D.rotate(-1 * Math.toRadians(robot.getOrientation()), robot.getPosition()[0], robot.getPosition()[1]);
    }

    public static void drawField(Graphics g, Field field) {
        Graphics2D g2D = (Graphics2D) g;
        g.setColor(Color.BLACK);
        // draw edge of square and grid
        g.drawRect(0, 0, field.size, field.size);
        for (int i = 1; i <= 5; i++) {
            g.drawLine(0, i * field.robotAreaSize, field.size, i * field.robotAreaSize);
            g.drawLine(i * field.robotAreaSize, 0, i * field.robotAreaSize, field.size);
        }

        // draw the carosel
        g.fillOval(-1 * field.robotAreaSize / 2, field.size - field.robotAreaSize / 2, field.robotAreaSize / 2,
                field.robotAreaSize / 2);

        // draw game elements
        for (Ball ball : field.balls) {
            g.drawOval(ball.getX() - 10, ball.getY(), 20, 20);
        }
        for (Cube cube : field.cubes) {
            g.drawOval(cube.getX() - 10, cube.getY(), 20, 20);
        }
        for (Duck duck : field.ducks) {
            g.drawOval(duck.getX() - 10, duck.getY(), 20, 20);
        }

        // draw barer
        g.drawRect(150, 150, 50, 900);
        g.drawRect(350, 150, 225, 50);
        g.drawRect(750, 150, 225, 50);

        // draw hubs
        g.setColor(Color.gray);
        g.fillOval(525, 125, 150, 150);
        g.fillOval(325, 725, 150, 150);
    }
}
