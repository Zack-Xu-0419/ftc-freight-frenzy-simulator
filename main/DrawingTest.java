import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class DrawingTest extends JPanel implements MouseListener {

    static Field field = new Field();
    static Robot robot = new Robot();
    static Thread refreshThread;

    // Flags for handling multiple key presses at the same time
    public static boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false,
            rotateLeft = false, rotateRight = false, extendingSlide = false, retractingSlide = false;
    public static int currentOrientation;
    public static int gameTime = 120;
    public static int caroselTime = 0;
    public static boolean end = false;

    public static int rIntakeCounter = 0;
    public static int bIntakeCounter = 0;
    public static int slideModeCounter = 0;
    public static boolean slideManualMode = false;

    public static Runnable caroselTimer;
    static ScheduledExecutorService caroselTimeScheduler = Executors.newScheduledThreadPool(1);
    public static boolean onceNear = false;

    // This variable is required for window focusing for keyboard detection
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Drawer.drawField(g, field);
        Drawer.drawRobot(g, robot, field);
        Drawer.drawScore(g, field);
        Drawer.drawTimer(g, gameTime);
        Drawer.drawCarosel(g, field);
    }

    public DrawingTest() {
        // Sets focus onto the window
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        addMouseListener(this);

        // Keybinds for robot movement. These listen for key presses.

        // You first get the input in this step, which gets the key, and then you set
        // that
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
    }

    public static void main(String[] args) {
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
        robot.setSize(50, 50);
        robot.setPosition(robot.getSizeX(), 500);
        JFrame window = new JFrame("FTC Freight Frenzy");
        window.setBounds(0, 0, 1200 + 20, 900 + 57);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingTest dt = new DrawingTest();
        dt.setBackground(Color.WHITE);
        Container c = window.getContentPane();
        window.add(dt);
        window.setVisible(true);
        refreshScreen(dt);
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

    public class DepositAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (robot.hasGameElement) {
                robot.deposit(field);
            }
        }
    }

    public static void refreshScreen(DrawingTest param) {
        refreshThread = new Thread() {
            public void run() {
                while (!end) {
                    try {
                        if (gameTime <= 0) {
                            end = true;
                            continue;
                        }
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
                        if (robot.nearCarosel() && gameTime <= 30) {
                            if (!onceNear) {
                                caroselTimeScheduler = Executors.newScheduledThreadPool(1);
                                onceNear = true;
                                caroselTimer = new Runnable() {
                                    public void run() {
                                        caroselTime++;
                                        if (caroselTime >= 1000) {
                                            field.carosel.removeDuck();
                                            field.ducks[10 - field.carosel.getDucks() - 1].move(0, 900 - 75 - 10);
                                            caroselTimeScheduler = Executors.newScheduledThreadPool(1);
                                            caroselTime = 0;
                                        }
                                    }
                                };
                                caroselTimeScheduler.scheduleAtFixedRate(caroselTimer, 0, 1, MILLISECONDS);
                            }
                        }
                        else {
                            onceNear= false;
                            caroselTimeScheduler.shutdown();
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
