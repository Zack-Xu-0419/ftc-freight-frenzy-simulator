import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class keyboardController {

  public static void main(String[] argv) throws Exception {

    JTextField textField = new JTextField();

    textField.addKeyListener(new MKeyListener());

    JFrame jframe = new JFrame();

    jframe.add(textField);

    jframe.setSize(400, 350);

    jframe.setVisible(true);

  }
}

class MKeyListener extends KeyAdapter {

  @Override
  public void keyPressed(KeyEvent event) {

    int k = event.getKeyCode();

    if (k == 38) {
      // UP

      // Check if robot is partially colliding,

    }
    if (k == 40) {
      // Down
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
}