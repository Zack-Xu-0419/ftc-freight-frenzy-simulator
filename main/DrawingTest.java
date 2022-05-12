import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DrawingTest extends JPanel implements MouseListener, KeyListener {

    static RobotCopy robot = new RobotCopy();

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Drawer.drawRobot(g, robot);
    }

    public DrawingTest(){
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
        addMouseListener(this);

        getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), "UP");
        getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
        getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");

        getActionMap().put("UP", new MoveAction("UP"));
        getActionMap().put("DOWN", new MoveAction("DOWN"));
        getActionMap().put("LEFT", new MoveAction("LEFT"));
        getActionMap().put("RIGHT", new MoveAction("RIGHT"));
    }


    public static void main(String[] args) {
        robot.setSize(20, 20);
        JFrame window = new JFrame("FTC Freight Frenzy");
        window.setBounds(100, 100, 1000 + 20, 1000 + 20);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DrawingTest dt = new DrawingTest();
        dt.setBackground(Color.WHITE);
        Container c = window.getContentPane();
        window.add(dt);
        window.setVisible(true);
        refreshScreen(dt);
        System.out.println("App is running");
    }

    public void mouseClicked(MouseEvent e) {}
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
        robot.setPosition(p.x, p.y);
    }

    public void keyTyped(KeyEvent e) {
        System.out.println("Type!");
    }
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent event) {

        int k = event.getKeyCode();

        if (k == 38) {
            // Up
            robot.setPosition(robot.getPosition()[0], robot.getPosition()[1]  - 1);
        }
        if (k == KeyEvent.VK_DOWN) {
            // Down
            robot.setPosition(robot.getPosition()[0], robot.getPosition()[1]  + 1);
        }
        if (k == 37) {
            // Left
        }
        if (k == 39) {
            // Right
        }

        if (event.getKeyCode() == KeyEvent.VK_HOME) {

            System.out.println("Key codes: " + event.getKeyCode());

        }
    }

    public class MoveAction extends AbstractAction {

        String direction;

        MoveAction(String direction) {
            this.direction = direction;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Typed!");
            switch (direction) {
                case "UP":
                    robot.setPosition(robot.getPosition()[0], robot.getPosition()[1] - 5);
                    break;
                case "DOWN":
                    robot.setPosition(robot.getPosition()[0], robot.getPosition()[1] + 5);
                    break;
                case "LEFT":
                    robot.setPosition(robot.getPosition()[0] - 5, robot.getPosition()[1]);
                    break;
                case "RIGHT":
                    robot.setPosition(robot.getPosition()[0] + 5, robot.getPosition()[1]);
                    break;
            }

        }
    }

    public static void refreshScreen(DrawingTest param) {
        Thread thread = new Thread() {
            public void run(){
                while(true) {
                    try {
                        // 60 FPS
                        sleep((int) (1000.0/60));
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
