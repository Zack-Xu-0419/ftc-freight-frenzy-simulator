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

        // Draw red intake
        g2D.setColor(Color.RED);
        if (robot.intakeDownLeft) {
            g2D.fillRect(robot.getPosition()[0] - robot.getSizeX() - 20, robot.getPosition()[1] - robot.getSizeY() / 2,
                    robot.getSizeX() - 20,
                    robot.getSizeY());
            robot.intake(field);
        }

        // Draw blue intake
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
            // If the slide has returned, draw green
            if (SimulatorDriver.slideReturned) {
                g2D.setColor(Color.GREEN);
                g2D.fillOval(robot.getPosition()[0] - 15, robot.getPosition()[1] - 15, 30, 30);
            }
            // If the slide hasn't returned, draw orange
            else {
                g2D.setColor(Color.ORANGE);
                g2D.fillOval(robot.getPosition()[0] - 15, robot.getPosition()[1] - 15, 30, 30);
            }
        }
        // If the robot is fried, draw red
        if (robot.isFried) {
            g2D.setColor(Color.RED);
            g2D.fillOval(robot.getPosition()[0] - 15, robot.getPosition()[1] - 15, 30, 30);
        }

        g2D.setColor(Color.BLACK);
        // rotate back the graphic so that next time it be standard direction, this will
        // not affect previous drawing
        g2D.rotate(-1 * Math.toRadians(robot.getOrientation()), robot.getPosition()[0], robot.getPosition()[1]);

        // Indicator for slide end
        g2D.fillOval(robot.getPositionAtSlideEnd()[0], robot.getPositionAtSlideEnd()[1], 5, 5);
    }

    public static void drawField(Graphics g, Field field) {
        Graphics2D g2D = (Graphics2D) g;
        g.setColor(new Color(241, 241, 241));
        g.fillRect(0, 0, 900, 900);
        g.setColor(Color.BLACK);
        // draw edge of square and grid
        g.drawRect(0, 0, field.size, field.size);
        for (int i = 1; i <= 5; i++) {
            g.drawLine(0, i * field.robotAreaSize, field.size, i * field.robotAreaSize);
            g.drawLine(i * field.robotAreaSize, 0, i * field.robotAreaSize, field.size);
        }

        Stroke originalStroke = g2D.getStroke();
        g2D.setStroke(new BasicStroke(10));
        g2D.setColor(Color.WHITE);
        g2D.drawLine(0, 2 * field.robotAreaSize, 2 * field.robotAreaSize, 2 * field.robotAreaSize);
        g2D.drawLine(2 * field.robotAreaSize, 0, 2 * field.robotAreaSize, 2 * field.robotAreaSize);
        g2D.setColor(Color.BLACK);
        g2D.setStroke(originalStroke);

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

        // draw barrier
        g2D.setStroke(new BasicStroke(5));
        g.drawRect(100, 275, 700, 50);
        g.drawRect(275, 100, 50, 175);
        g.drawRect(575, 100, 50, 175);
        g2D.setStroke(originalStroke);

        // draw hubs
        g.setColor(Color.gray);
        // Shared shipping hub
        g.fillOval(400, 100, 100, 100);
        // Alliance Hub
        g.fillOval(250, 475, 100, 100);
    }

    // draw the scoring board
    public static void drawScore(Graphics g, Field field, Robot robot, boolean isParked) {
        g.setColor(Color.BLACK);
        // Calculate total score
        int allianceScore = Scoring.score(field.allianceHub);
        int sharedScore = Scoring.score((field.sharedHub));
        int duckScore = Scoring.score(field.carosel);
        int totalScore = allianceScore + sharedScore + duckScore - 10 * robot.getPenalties();
        // Display the score
        Font oldFont = g.getFont();
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Alliance Hub: " + allianceScore, 1000, 50);
        g.drawString("Shared Hub: " + sharedScore, 1000, 100);
        g.drawString("Duck Spinner: " + duckScore, 1000, 150);
        g.drawString("Penalties: " + (-10 * robot.getPenalties()), 1000, 200);
        if (SimulatorDriver.realisticMode) {
            g.setColor(Color.RED);
            g.drawString("Realistic Mode On!", 990, 350);
            g.setColor(Color.BLACK);
        }
        //if parked correctly after the game ended, add 6 points
        if (isParked) {
            // If the total score is less than zero, display 0
            if (totalScore + 6 < 0) {
                g.drawString("Total Score: " + 0 , 1000, 250);
            }
            else {
                g.drawString("Total Score: " + (totalScore + 6), 1000, 250);
            }
        } else {
            // If the total score is less than zero, display 0
            if (totalScore < 0) {
                g.drawString("Total Score: " + 0, 1000, 250);
            }
            else {
                g.drawString("Total Score: " + (totalScore), 1000, 250);
            }
        }
        g.setFont(oldFont);
        
    }

    public static void drawControls(Graphics g) {
        Font oldFont = g.getFont();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Arrow Keys - Movement", 925, 400);
        g.drawString("A & D - Rotation", 925, 450);
        g.drawString("Q & E - Intakes", 925, 500);
        g.drawString("Shift Key - Toggle Manual Slide", 925, 550);
        g.drawString("W & S - Extend and Retract Slide", 925, 600);
        g.drawString("If in manual mode, hold to adjust.", 925, 650);
        g.drawString("Spacebar - Deposit", 925, 700);
        g.drawString("Backspace - Realistic Mode", 925, 750);
        if (SimulatorDriver.slideManualMode) {
            g.setColor(Color.GREEN);
            g.drawString("Manual Mode ON!", 990, 825);
        }
        else {
            g.setColor(Color.RED);
            g.drawString("Manual Mode OFF!", 990, 825);
        }
        g.setColor(Color.BLACK);
        g.setFont(oldFont);
    }

    public static void drawTimer(Graphics g, int time) {
        g.setColor(Color.BLACK);
        Font oldFont = g.getFont();
        g.setFont(new Font("Arial", Font.PLAIN, 36));
        // Draws the timer string
        String timerString = "" + time / 60 + ":";
        if (time % 60 < 10) {
            // Handles if the seconds place is single digit
            timerString += ("0" + time % 60);
        }
        else {
            timerString += time % 60;
        }
        // Time flashes red at endgame
        if (time <= 30 && time % 2 == 0) {
            g.setColor(Color.RED);
        }
        g.drawString(timerString, 1040, 305);
        g.setColor(Color.BLACK);
    }

    public static void drawCarosel(Graphics g, Field field) {
        // draw the carosel
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.BLACK);
        g2D.fillOval(-field.robotAreaSize / 2, field.size - field.robotAreaSize / 2, field.robotAreaSize,
                field.robotAreaSize);
        // "Spins" duck based on how long the robot has been near the carosel
        g2D.rotate(-Math.toRadians(SimulatorDriver.caroselTime * 9 / 100.), 0, 900);
        // Displays a duck on the carosel
        if (field.carosel.getDucks() != 0) {
            g2D.drawOval(50, 890, 10, 10);
            g2D.setColor(Color.YELLOW);
            g2D.fillOval(50, 890, 10, 10);
            g2D.setColor(Color.BLACK);
        }
        g2D.rotate(Math.toRadians(SimulatorDriver.caroselTime * 9 / 100.), 0, 900);
    }
}
