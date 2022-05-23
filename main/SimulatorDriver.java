import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class SimulatorDriver extends JPanel implements MouseListener {

    static Field field = new Field();
    static Robot robot = new Robot();
    static Thread refreshThread;

    // Flags for handling multiple key presses at the same time
    public static boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false,
            rotateLeft = false, rotateRight = false, extendingSlide = false, retractingSlide = false, slideReturned = true,
            realisticMode = false;

    // Robots current orientation
    public static int currentOrientation;

    // Game time initializer
    public static int gameTime = 120;
    public static int caroselTime = 0;

    // End of Game boolean
    public static boolean end = false;

    // Counters for key press toggles
    public static int rIntakeCounter = 0;
    public static int bIntakeCounter = 0;
    public static int slideModeCounter = 0;

    // Flag for manual slide control
    public static boolean slideManualMode = false;

    // Timer and Timer Runner for carosel
    public static Runnable caroselTimer;
    static ScheduledExecutorService caroselTimeScheduler = Executors.newScheduledThreadPool(1);

    // Flag for if the robot is near the carosel
    public static boolean onceNear = false;

    // This variable is required for window focusing for keyboard detection
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        boolean isParked = false;
        // Math for determining whether robot is in warehouse
        if(gameTime == 0){
            double rightMostX = robot.getPosition()[0]
                    + Math.abs((Math.sqrt(2) * robot.getSizeX()) * Math.cos(Math.PI / 4 - robot.getOrientation() % 90 * Math.PI / 180));
            double downMostY = robot.getPosition()[1]
                    + Math.abs((Math.sqrt(2) * robot.getSizeY()) * Math.cos(Math.PI / 4 - robot.getOrientation() % 90 * Math.PI / 180));
            if(downMostY <= 300 && rightMostX <= 300){
                isParked = true;
            }
        }
        Drawer.drawField(g, field);
        Drawer.drawRobot(g, robot, field);
        Drawer.drawScore(g, field, robot, isParked);
        Drawer.drawTimer(g, gameTime);
        Drawer.drawCarosel(g, field);
        Drawer.drawControls(g);
    }

    public SimulatorDriver() {
        // Sets focus onto the window
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        addMouseListener(this);

        // Keybinds for robot movement. These listen for key presses.

        // You first get the input in this step, which gets the key, and then you set that
        // key press to an action key, which is the last string.

        // Keybind for when a keyboard key corresponding to movement is pressed.
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "UP_PRESSED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DOWN_PRESSED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LEFT_PRESSED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RIGHT_PRESSED");

        // Then, you set the action key to an action initialized in this file
        getActionMap().put("UP_PRESSED", new PressMoveAction("UP"));
        getActionMap().put("DOWN_PRESSED", new PressMoveAction("DOWN"));
        getActionMap().put("LEFT_PRESSED", new PressMoveAction("LEFT"));
        getActionMap().put("RIGHT_PRESSED", new PressMoveAction("RIGHT"));

        // Keybind for when a keyboard key corresponding to movement is released.
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "UP_RELEASED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "DOWN_RELEASED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "LEFT_RELEASED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "RIGHT_RELEASED");

        getActionMap().put("UP_RELEASED", new ReleaseMoveAction("UP"));
        getActionMap().put("DOWN_RELEASED", new ReleaseMoveAction("DOWN"));
        getActionMap().put("LEFT_RELEASED", new ReleaseMoveAction("LEFT"));
        getActionMap().put("RIGHT_RELEASED", new ReleaseMoveAction("RIGHT"));

        // Keybind for when a keyboard key corresponding to rotation is pressed.
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "RLEFT_PRESSED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "RRIGHT_PRESSED");

        getActionMap().put("RLEFT_PRESSED", new PressRotateAction("LEFT"));
        getActionMap().put("RRIGHT_PRESSED", new PressRotateAction("RIGHT"));

        // Keybind for when a keyboard key corresponding to rotation is released.
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "RLEFT_RELEASED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "RRIGHT_RELEASED");

        getActionMap().put("RLEFT_RELEASED", new ReleaseRotateAction("LEFT"));
        getActionMap().put("RRIGHT_RELEASED", new ReleaseRotateAction("RIGHT"));

        // Keybinds for when a keyboard key corresponding to slide extension is pressed
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "SLIDE_EXTENDED_PRESS");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "SLIDE_RETRACTED_PRESS");

        getActionMap().put("SLIDE_EXTENDED_PRESS", new SlidePressAction(true));
        getActionMap().put("SLIDE_RETRACTED_PRESS", new SlidePressAction(false));

        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "SLIDE_EXTENDED_RELEASE");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "SLIDE_RETRACTED_RELEASE");

        getActionMap().put("SLIDE_EXTENDED_RELEASE", new SlideReleaseAction(true));
        getActionMap().put("SLIDE_RETRACTED_RELEASE", new SlideReleaseAction(false));

        // Keybinds for when a keyboard key corresponding to intakes is pressed
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), "R_INTAKE");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "B_INTAKE");

        getActionMap().put("R_INTAKE", new RedIntakeAction());
        getActionMap().put("B_INTAKE", new BlueIntakeAction());

        // Keybinds for manual slide extension mode
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, true), "SLIDE_MODE");

        getActionMap().put("SLIDE_MODE", new SlideModeAction());

        // Keybinds for deposit
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "DEPOSIT");

        getActionMap().put("DEPOSIT", new DepositAction());

        // Keybinds for "realistic mode"
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE , 0, true), "REALISM");

        getActionMap().put("REALISM", new RealismAction());
    }

    public static void main(String[] args) {
        // Code for game timer
        final ScheduledExecutorService gameTimeScheduler = Executors.newScheduledThreadPool(1);

        final Runnable runnable = new Runnable() {
            public void run() {
                gameTime--;
                if (gameTime <= 0) {
                    gameTimeScheduler.shutdown();
                }
            }
        };
        gameTimeScheduler.scheduleAtFixedRate(runnable, 1, 1, SECONDS);

        // Robot size initializer
        robot.setSize(50, 50);

        // Robot initial position
        robot.setPosition(robot.getSizeX(), 500);

        // Window setting
        JFrame window = new JFrame("FTC Freight Frenzy");
        window.setBounds(0, 0, 1300 + 20, 900 + 57);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SimulatorDriver sd = new SimulatorDriver();
        sd.setBackground(Color.WHITE);
        Container c = window.getContentPane();
        window.add(sd);
        window.setVisible(true);
        refreshScreen(sd);
        System.out.println("App is running");
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Click!");
        Point p = e.getPoint();
        System.out.println("" + p.x + ", " + p.y);
    }

    // Tracks whether the movement keys are pressed
    public class PressMoveAction extends AbstractAction {

        String direction;

        PressMoveAction(String direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (direction) {
                case "UP":
                    upPressed = true;
                    break;
                case "DOWN":
                    downPressed = true;
                    break;
                case "LEFT":
                    leftPressed = true;
                    break;
                case "RIGHT":
                    rightPressed = true;
                    break;
            }

        }
    }

    // Tracks when movement keys are not pressed
    public class ReleaseMoveAction extends AbstractAction {

        String direction;

        ReleaseMoveAction(String direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (direction) {
                case "UP":
                    upPressed = false;
                    break;
                case "DOWN":
                    downPressed = false;
                    break;
                case "LEFT":
                    leftPressed = false;
                    break;
                case "RIGHT":
                    rightPressed = false;
                    break;
            }

        }
    }

    // Tracks if rotate keys are pressed
    public class PressRotateAction extends AbstractAction {

        String direction;

        PressRotateAction(String direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (direction) {
                case "LEFT":
                    rotateLeft = true;
                    break;
                case "RIGHT":
                    rotateRight = true;
                    break;
            }
        }
    }

    // Tracks if rotate keys are not pressed
    public class ReleaseRotateAction extends AbstractAction {

        String direction;

        ReleaseRotateAction(String direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (direction) {
                case "LEFT":
                    rotateLeft = false;
                    break;
                case "RIGHT":
                    rotateRight = false;
                    break;
            }
        }
    }

    // Tracks if slide keys were pressed. The boolean flags are changed regardless of slide mode
    public class SlidePressAction extends AbstractAction {

        // True - extend; False - retract
        boolean extendOrRetract;

        SlidePressAction(boolean extendOrRetract) {
            this.extendOrRetract = extendOrRetract;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (extendOrRetract) {
                extendingSlide = true;
                if (retractingSlide) {
                    retractingSlide = false;
                }
            } else {
                retractingSlide = true;
                if (extendingSlide) {
                    extendingSlide = false;
                }
            }
        }
    }

    // Tracks if slide keys are not pressed. Only fires when manual mode is on.
    // Sets boolean flags to false when keys are not pressed
    public class SlideReleaseAction extends AbstractAction {

        // True - extend; False - retract
        boolean extendOrRetract;

        SlideReleaseAction(boolean extendOrRetract) {
            this.extendOrRetract = extendOrRetract;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (slideManualMode) {
                if (extendOrRetract) {
                    extendingSlide = false;
                } else {
                    retractingSlide = false;
                }
            }
        }
    }

    // Tracks whether the slide mode key was pressed
    // Acts as a toggle action
    public class SlideModeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (slideModeCounter == 0) {
                slideManualMode = true;
                slideModeCounter++;
            } else {
                slideManualMode = false;
                slideModeCounter = 0;
            }
            extendingSlide = false;
            retractingSlide = false;
        }
    }

    // Tracks if the red intake key was pressed and sets the flag
    // Will automatically make the slide retract
    public class RedIntakeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (rIntakeCounter == 0 && !robot.hasGameElement) {
                retractingSlide = true;
                extendingSlide = false;
                robot.intakeDownLeft = true;
                rIntakeCounter++;
            } else {
                robot.intakeDownLeft = false;
                rIntakeCounter = 0;
            }
        }
    }

    // Tracks if the blue intake key was pressed and sets the flag
    // Will automatically make the slide retract
    public class BlueIntakeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (bIntakeCounter == 0 && !robot.hasGameElement) {
                robot.intakeDownRight = true;
                retractingSlide = true;
                extendingSlide = false;
                bIntakeCounter++;
            } else {
                robot.intakeDownRight = false;
                bIntakeCounter = 0;
            }
        }
    }

    // Tracks if the deposit key was pressed
    public class DepositAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            // Only deposits if the robot has a game element
            if (robot.hasGameElement) {
                robot.deposit(field);
            }
        }
    }

    // Checks if "realistic mode" key was pressed
    // Allows the robot to have a chance to fry each frame
    public class RealismAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!realisticMode) {
                realisticMode = true;
            }
            else {
                realisticMode = false;
            }
        }
    }

    // Drawing loop thread
    public static void refreshScreen(SimulatorDriver param) {
        refreshThread = new Thread() {
            public void run() {
                while (!end) {
                    try {
                        // The thread does nothing when the game timer runs down
                        if (gameTime <= 0) {
                            end = true;
                            continue;
                        }
                        // Creates random numbers each frame for realistic mode frying
                        if (realisticMode) {
                            Random random = new Random();
                            int chance = random.nextInt(7200);
                            if (chance < 2) {
                                robot.isFried = true;
                            }
                        }
                        // Checks if the robot is fried. If it is, no movement is possible.
                        if (!robot.isFried) {
                            currentOrientation = robot.getOrientation();
                            if (upPressed) {
                                robot.move(3);
                            }
                            if (downPressed) {
                                robot.move(4);
                            }
                            if (leftPressed) {
                                robot.move(1);
                            }
                            if (rightPressed) {
                                robot.move(2);
                            }
                            if (rotateLeft) {
                                currentOrientation = (currentOrientation + 3) % 360;
                                robot.setOrientation(currentOrientation);
                            }
                            if (rotateRight) {
                                currentOrientation = (currentOrientation) - 3 % 360;
                                if (currentOrientation < 0) {
                                    currentOrientation = 360 + currentOrientation;
                                }
                                robot.setOrientation(currentOrientation);
                            }
                            if (extendingSlide) {
                                robot.slideExtended = true;
                                if (robot.getCurrentSlideLength() + 5 <= robot.getMaxSlideLength()) {
                                    robot.setCurrentSlideLength(robot.getCurrentSlideLength() + 5);
                                } else {
                                    robot.setCurrentSlideLength(robot.getMaxSlideLength());
                                    extendingSlide = false;
                                }
                            }
                            if (retractingSlide) {
                                if (robot.getCurrentSlideLength() - 5 >= 0) {
                                    robot.setCurrentSlideLength(robot.getCurrentSlideLength() - 5);
                                } else {
                                    robot.setCurrentSlideLength(0);
                                    retractingSlide = false;
                                    robot.slideExtended = false;
                                }
                            }
                            // Checks if robot is near carosel and is in endgame
                            if (robot.nearCarosel() && gameTime <= 30) {
                                // If the near flag has not been marked as true before.
                                if (!onceNear) {
                                    // Create timer scheduler
                                    caroselTimeScheduler = Executors.newScheduledThreadPool(1);
                                    // Set flag as true so that a timer does not get created every frame
                                    onceNear = true;
                                    caroselTimer = new Runnable() {
                                        public void run() {
                                            caroselTime++;
                                            // Carosel timer will run for one second before duck drops
                                            if (caroselTime >= 1000) {
                                                field.carosel.removeDuck();
                                                // "Drops" the duck into the field
                                                if (field.carosel.getDucks() != 0) {
                                                    field.ducks[10 - field.carosel.getDucks() - 1].move(0, 900 - 75 - 10);
                                                }
                                                // Reset the timer scheduler and timer
                                                caroselTimeScheduler = Executors.newScheduledThreadPool(1);
                                                caroselTime = 0;
                                            }
                                        }
                                    };
                                    // Start timer
                                    caroselTimeScheduler.scheduleAtFixedRate(caroselTimer, 0, 1, MILLISECONDS);
                                }
                            } else {
                                // Carosel timer is paused, and near carosel flag is marked as false
                                onceNear = false;
                                caroselTimeScheduler.shutdown();
                            }
                            // Checks if the slide is returned for robot depositing
                            if (!slideReturned) {
                                if (robot.getCurrentSlideLength() == -1) {
                                    slideReturned = true;
                                }
                            }
                        }
                        // 60 FPS
                        sleep((int) (1000.0 / 60));
                        param.repaint();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        refreshThread.start();
    }

}
