import java.awt.*;

import GameElements.*;
import StationaryElements.AllianceHub;

public class Drawer {
    public static void drawRobot(Graphics g, Robot robot) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.BLACK);
        int leftX = robot.getPosition()[0] - robot.getSizeX();
        int rightX = robot.getPosition()[0] + robot.getSizeX();
        int upperY = robot.getPosition()[1] - robot.getSizeY();
        int lowerY = robot.getPosition()[1] + robot.getSizeY();
        g2D.rotate(Math.toRadians(robot.getOrientation()), robot.getPosition()[0], robot.getPosition()[1]);
        g2D.fillPolygon(new int[] { leftX, leftX, rightX, rightX }, new int[] { upperY, lowerY, lowerY, upperY }, 4);
        
        if(robot.slideExtended){
            g2D.drawLine(robot.getPosition()[0], robot.getPosition()[1],
                    robot.getPosition()[0] + robot.getSizeX() + robot.getSlideLength(),
                    robot.getPosition()[1]);
        }


        g2D.rotate(-1 * Math.toRadians(robot.getOrientation()), robot.getPosition()[0], robot.getPosition()[1]);
    }

    public static void drawField(Graphics g, Field field) {
        g.setColor(Color.BLACK);
        // draw edge of square and grid
        g.drawRect(0, 0, field.size, field.size);
        for (int i = 1; i <= 5; i++) {
            g.drawLine(0, i * field.robotAreaSize, field.size, i * field.robotAreaSize);
            g.drawLine(i * field.robotAreaSize, 0, i * field.robotAreaSize, field.size);
        }

        // draw the carosel
        g.fillOval(-field.robotAreaSize / 2, field.size - field.robotAreaSize / 2, field.robotAreaSize,
                field.robotAreaSize);

        // draw game elements
        for (Ball ball : field.balls) {
            g.drawOval(ball.getX(), ball.getY(), 10, 10);
        }
        for (Cube cube : field.cubes) {
            g.drawOval(cube.getX(), cube.getY(), 10, 10);
        }
        /*for (Duck duck : field.ducks) {
            g.drawOval(duck.getX() - 10, duck.getY(), 20, 20);
        }*/

        // draw barer
        g.drawRect(100, 275, 700, 50);
        g.drawRect(275, 100, 50, 175);
        g.drawRect(575, 100, 50, 175);

        // draw hubs
        g.setColor(Color.gray);
        g.fillOval(400, 100, 100, 100);
        g.fillOval(250, 475, 100, 100);
    }
    public static void drawScore(Graphics g, Field field) {
        g.setColor(Color.BLACK);
        int allianceScore = Scoring.score(field.allianceHub);
        int sharedScore = Scoring.score((field.sharedHub));
        g.drawString("Alliance Hub: " + allianceScore, 975, 50);
        g.drawString("Shared Hub: " + sharedScore, 975, 100);
    }
}
