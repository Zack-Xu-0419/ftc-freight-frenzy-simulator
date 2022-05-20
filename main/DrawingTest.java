import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawingTest extends JPanel implements MouseListener {

    static Field field = new Field();
    static Robot robot = new Robot();

    // Flags for handling multiple key presses at the same time
    static boolean upPressed = false, downPressed = false, leftPressed = false, rightPressed = false,
            rotateLeft = false, rotateRight = false;
    static int currentOrientation;

    static int rIntakeCounter = 0;
    static int bIntakeCounter = 0;
    static int slideModeCounter = 0;
    static boolean slideManualMode = false;

    // This variable is required for window focusing for keyboard detection
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Drawer.drawField(g, field);
        Drawer.drawRobot(g, robot);
        Drawer.drawScore(g, field);
    }

    public DrawingTest() {
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
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "SLIDE_EXTENDED");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "SLIDE_RETRACTED");

        getActionMap().put("SLIDE_EXTENDED", new SlideExtendAction(true));
        getActionMap().put("SLIDE_RETRACTED", new SlideExtendAction(false));

        // Keybinds for when a keyboard key corresponding to intakes is pressed
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), "R_INTAKE");
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true), "B_INTAKE");

        getActionMap().put("R_INTAKE", new RedIntakeAction());
        getActionMap().put("B_INTAKE", new BlueIntakeAction());

        // Keybinds for manual slide extension mode
        getInputMap(IFW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, true), "SLIDE_MODE");

        getActionMap().put("SLIDE_MODE", new RedIntakeAction());
    }

    public static void main(String[] args) {
        robot.setSize(50, 50);
        JFrame window = new JFrame("FTC Freight Frenzy");
        window.setBounds(0, 0, 900 + 20, 900 + 57);
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

    public class SlideExtendAction extends AbstractAction {

        // True - extend; False - retract
        boolean extendOrRetract;
        SlideExtendAction(boolean extendOrRetract) {
            this.extendOrRetract = extendOrRetract;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (extendOrRetract) {
                robot.slideExtended = true;
            }
            else {
                robot.slideExtended = false;
            }
        }
    }

    public class SlideModeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (slideModeCounter == 0) {
                slideManualMode = true;
                slideModeCounter++;
            }
            else {
                slideManualMode = false;
                slideModeCounter = 0;
            }
        }
    }

    public class RedIntakeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (rIntakeCounter == 0) {
                robot.intakeDownLeft = true;
                rIntakeCounter++;
            }
            else {
                robot.intakeDownLeft = false;
                rIntakeCounter = 0;
            }
        }
    }

    public class BlueIntakeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (bIntakeCounter == 0) {
                robot.intakeDownRight = true;
                bIntakeCounter++;
            }
            else {
                robot.intakeDownRight = false;
                bIntakeCounter = 0;
            }
        }
    }

    public static void refreshScreen(DrawingTest param) {
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    try {
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
                            currentOrientation = (currentOrientation + 5) % 360;
                            robot.setOrientation(currentOrientation);
                        }
                        if (rotateRight) {
                            currentOrientation = (currentOrientation) - 5 % 360;
                            if (currentOrientation < 0) {
                                currentOrientation = 360 + currentOrientation;
                            }
                            robot.setOrientation(currentOrientation);
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
        thread.start();
    }
}
