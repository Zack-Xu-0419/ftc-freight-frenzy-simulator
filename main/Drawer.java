import java.awt.*;

import GameElements.*;

public class Drawer {
    public static void drawRobot(Graphics g, Robot robot, Field field) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.BLACK);
        // get postion of four corners
        int leftX = robot.getPosition()[0] - robot.getSizeX();
        int rightX = robot.getPosition()[0] + robot.getSizeX();
        int upperY = robot.getPosition()[1] - robot.getSizeY();
        int lowerY = robot.getPosition()[1] + robot.getSizeY();
        Stroke originalStroke = g2D.getStroke();
        // rotate the corner and draw the robot
        g2D.rotate(Math.toRadians(robot.getOrientation()), robot.getPosition()[0], robot.getPosition()[1]);
        g2D.fillPolygon(new int[] { leftX, leftX, rightX, rightX }, new int[] { upperY, lowerY, lowerY, upperY }, 4);
        // draw the slide
        g2D.setStroke(new BasicStroke(3));
        g2D.setColor(Color.LIGHT_GRAY);
        g2D.drawLine(robot.getPosition()[0], robot.getPosition()[1],
                robot.getPosition()[0],
                robot.getPosition()[1] + robot.getSizeX() + robot.getCurrentSlideLength());
        g2D.setStroke(originalStroke);
        g2D.setColor(Color.BLACK);

        // Draw intake left
        g2D.setColor(Color.RED);
        if (robot.intakeDownLeft) {
            g2D.fillRect(robot.getPosition()[0] - robot.getSizeX() - 20, robot.getPosition()[1] - robot.getSizeY() / 2,
                    robot.getSizeX() - 20,
                    robot.getSizeY());
            robot.intake(field);
        }
        g2D.setColor(Color.BLUE);
        if (robot.intakeDownRight) {
            g2D.fillRect(robot.getPosition()[0] + robot.getSizeX() + 20 - (robot.getSizeX() - 20),
                    robot.getPosition()[1] - robot.getSizeY() / 2,
                    robot.getSizeX() - 20,
                    robot.getSizeY());
            robot.intake(field);
        }

        // If robot has game element, draw indication
        if (robot.hasGameElement) {
            if (DrawingTest.slideReturned) {
                g2D.setColor(Color.GREEN);
                g2D.fillOval(robot.getPosition()[0] - 15, robot.getPosition()[1] - 15, 30, 30);
            }
            else {
                g2D.setColor(Color.ORANGE);
                g2D.fillOval(robot.getPosition()[0] - 15, robot.getPosition()[1] - 15, 30, 30);
            }
        }

        g2D.setColor(Color.BLACK);
        // rotate back the graphic so that next time it be stadard direction, this will
        // not affect previous drawing
        g2D.rotate(-1 * Math.toRadians(robot.getOrientation()), robot.getPosition()[0], robot.getPosition()[1]);
        g2D.fillOval(robot.getPositionAtSlideEnd()[0], robot.getPositionAtSlideEnd()[1], 5, 5);
    }

    public static void drawField(Graphics g, Field field) {
        g.setColor(new Color(241, 241, 241));
        g.fillRect(0, 0, 900, 900);
        g.setColor(Color.BLACK);
        // draw edge of square and grid
        g.drawRect(0, 0, field.size, field.size);
        for (int i = 1; i <= 5; i++) {
            g.drawLine(0, i * field.robotAreaSize, field.size, i * field.robotAreaSize);
            g.drawLine(i * field.robotAreaSize, 0, i * field.robotAreaSize, field.size);
        }
        // draw game elements
        for (Ball ball : field.balls) {
            // if not picked up
            if (!ball.isPickedUp()) {
                g.setColor(Color.WHITE);
                g.fillRect(ball.getX(), ball.getY(), 10, 10);
                g.setColor(Color.BLACK);
                g.drawOval(ball.getX(), ball.getY(), 10, 10);
            }
        }
        for (Cube cube : field.cubes) {
            if (!cube.isPickedUp()) {
                g.setColor(Color.YELLOW);
                g.fillRect(cube.getX(), cube.getY(), 10, 10);
                g.setColor(Color.BLACK);
                g.drawRect(cube.getX(), cube.getY(), 10, 10);
            }
        }
        for (Duck duck : field.ducks) {
            if (!duck.isPickedUp()) {
                g.setColor(Color.YELLOW);
                g.fillOval(duck.getX(), duck.getY(), 10, 10);
                g.setColor(Color.BLACK);
                g.drawOval(duck.getX(), duck.getY(), 10, 10);
            }
        }

        // draw barer
        g.drawRect(100, 275, 700, 50);
        g.drawRect(275, 100, 50, 175);
        g.drawRect(575, 100, 50, 175);

        // draw hubs
        g.setColor(Color.gray);
        g.fillOval(400, 100, 100, 100);
        g.fillOval(250, 475, 100, 100);
    }

    // draw the scoring board
    public static void drawScore(Graphics g, Field field) {
        g.setColor(Color.BLACK);
        int allianceScore = Scoring.score(field.allianceHub);
        int sharedScore = Scoring.score((field.sharedHub));
        int duckScore = Scoring.score(field.carosel);
        Font oldFont = g.getFont();
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Alliance Hub: " + allianceScore, 975, 50);
        g.drawString("Shared Hub: " + sharedScore, 975, 100);
        g.drawString("Duck Spinner: " + duckScore, 975, 150);
        g.drawString("Total Score: " + (allianceScore + sharedScore + duckScore), 975, 200);
        g.setFont(oldFont);
    }

    public static void drawTimer(Graphics g, int time) {
        g.setColor(Color.BLACK);
        Font oldFont = g.getFont();
        g.setFont(new Font("Arial", Font.PLAIN, 36));
        String timerString = "" + time / 60 + ":";
        if (time % 60 < 10) {
            timerString += ("0" + time % 60);
        }
        else {
            timerString += time % 60;
        }
        if (time <= 30 && time % 2 == 0) {
            g.setColor(Color.RED);
        }
        g.drawString(timerString, 975, 300);
        g.setColor(Color.BLACK);
    }

    public static void drawCarosel(Graphics g, Field field) {
        // draw the carosel
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.BLACK);
        g2D.fillOval(-field.robotAreaSize / 2, field.size - field.robotAreaSize / 2, field.robotAreaSize,
                field.robotAreaSize);
        g2D.rotate(-Math.toRadians(DrawingTest.caroselTime * 9 / 100.), 0, 900);
        if (field.carosel.getDucks() != 0) {
            g2D.drawOval(50, 890, 10, 10);
            g2D.setColor(Color.YELLOW);
            g2D.fillOval(50, 890, 10, 10);
            g2D.setColor(Color.BLACK);
        }
        g2D.rotate(Math.toRadians(DrawingTest.caroselTime * 9 / 100.), 0, 900);
    }
}
