import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class SteeringWheel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
    private final Image steeringWheel;
    private int angle;
    private double previousX, previousY;
    private Timer timer;

    public SteeringWheel() {
        angle = 0;
        steeringWheel = LoadImage("src//wheel.png");
        addMouseMotionListener(this);
        addMouseListener(this);
        timer = new Timer(10, this);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(Math.toRadians(angle), steeringWheel.getWidth(null) / 2, steeringWheel.getHeight(null) / 2);
        g2d.drawImage(steeringWheel, 0, 0, null);
        g2d.rotate(-Math.toRadians(angle));
    }

    private Image LoadImage(String FileName) {
        Image img = null;
        try {
            img = ImageIO.read(new File(FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public int getAngle() {
        return angle;
    }

    public void mouseDragged(MouseEvent e) {
        Object z = e.getSource();
        if (z == this) {
            double x = e.getX();
            if (x > previousX && angle < 90)
                angle += 3;
            else if (angle > -90)
                angle -= 3;
            previousX = x;
        }
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
        Object z = e.getSource();
        if (z == this) {
            previousX = e.getX();
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {
        Object z = e.getSource();
        if (z == this) {
            timer.start();
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void actionPerformed(ActionEvent e) {
        Object z = e.getSource();
        if (z == timer) {
            if (Math.abs(angle) <= 3) {
                angle = 0;
                timer.stop();
            } else if (angle > 3)
                angle--;
            else
                angle++;
        }
    }
}
