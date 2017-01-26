import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class TrafficLightsOnMap extends JFrame implements ActionListener {
    private TrafficLight[] trafficLightObject;
    private JButton button;
    private long backupTime;
    private Timer timer;
    private boolean futureGreenY, futureGreenX;

    public TrafficLightsOnMap() {
        trafficLightObject = new TrafficLight[4];
        for (int i = 0; i < 4; i++) {
            trafficLightObject[i] = new TrafficLight();
            trafficLightObject[i].setOpaque(false);
            trafficLightObject[i].setColor(Color.GREEN);
        }

        trafficLightObject[0].setColor(Color.RED);										// left
        trafficLightObject[0].setBounds(Map.getLeftCrossLine()-20, Map.getBottomCrossLine()-10, 20, 20);
        trafficLightObject[0].setAngle(90);

        trafficLightObject[1].setColor(Color.RED);										// right
        trafficLightObject[1].setBounds(Map.getRightCrossLine(), Map.getTopCrossLine()-10, 20, 20);
        trafficLightObject[1].setAngle(270);

        trafficLightObject[2].setBounds(Map.getLeftCrossLine()-10, Map.getTopCrossLine()-20, 20, 20);		// top
        trafficLightObject[2].setAngle(180);

        trafficLightObject[3].setBounds(Map.getRightCrossLine()-10, Map.getBottomCrossLine(), 20, 20);	// bottom

        button = new JButton("switch lights");
        button.setBounds(100,Map.getBottomCrossLine(),150,30);
        button.addActionListener(this);

        timer = new Timer(500, this);

    }

    public void setYellowX() {
        trafficLightObject[0].setColor(Color.YELLOW);
        trafficLightObject[1].setColor(Color.YELLOW);
    }

    public boolean isYellowX() {
        if (trafficLightObject[0].getColor() == Color.YELLOW)
            return true;
        else
            return false;
    }

    public void setYellowY() {
        trafficLightObject[2].setColor(Color.YELLOW);
        trafficLightObject[3].setColor(Color.YELLOW);
    }

    public boolean isYellowY() {
        if (trafficLightObject[2].getColor() == Color.YELLOW)
            return true;
        else
            return false;
    }

    public void setRedX() {
        trafficLightObject[0].setColor(Color.RED);
        trafficLightObject[1].setColor(Color.RED);
    }

    public void setRedY() {
        trafficLightObject[2].setColor(Color.RED);
        trafficLightObject[3].setColor(Color.RED);
    }

    public void setGreenX() {
        trafficLightObject[0].setColor(Color.GREEN);
        trafficLightObject[1].setColor(Color.GREEN);
    }

    public boolean isGreenX() {
        if (trafficLightObject[0].getColor() == Color.GREEN)
            return true;
        else
            return false;
    }

    public void setGreenY() {
        trafficLightObject[2].setColor(Color.GREEN);
        trafficLightObject[3].setColor(Color.GREEN);
    }

    public boolean isGreenY() {
        if (trafficLightObject[2].getColor() == Color.GREEN)
            return true;
        else
            return false;
    }

    public void restart() {
        button.setEnabled(true);
        timer.restart();
        timer.stop();
        trafficLightObject[0].setColor(Color.RED);										// left
        trafficLightObject[1].setColor(Color.RED);										// right
        trafficLightObject[2].setColor(Color.GREEN);     // top
        trafficLightObject[3].setColor(Color.GREEN);     // bottom
        futureGreenX = false;
        futureGreenY = false;
    }

    public void setVisible(boolean arg) {
        for (int i = 0; i < 4; i++)
            trafficLightObject[i].setVisible(arg);
    }

    public void addOnTopOfMap(Map map) {
        for (int i = 0; i < 4; i++) {
            map.addOnTop(trafficLightObject[i]);
        }
        map.addOnTop(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object z = e.getSource();
        if (z == button) {
            button.setEnabled(false);
            backupTime = System.currentTimeMillis();
            timer.restart();
            timer.start();
        }

        if (z == timer) {
            if (isGreenX()) {
                setYellowX();
                futureGreenY = true;
            }
            if (isGreenY()) {
                setYellowY();
                futureGreenX = true;
            }
            if (futureGreenY == true && System.currentTimeMillis() - backupTime > 2000) {
                setRedX();
            }
            if (futureGreenY == true && System.currentTimeMillis() - backupTime > 4000) {
                setYellowY();
            }
            if (futureGreenY == true && System.currentTimeMillis() - backupTime > 6000) {
                setGreenY();
                futureGreenY = false;
                button.setEnabled(true);
                timer.stop();
            }
            if (futureGreenX == true && System.currentTimeMillis() - backupTime > 2000) {
                setRedY();
            }
            if (futureGreenX == true && System.currentTimeMillis() - backupTime > 4000) {
                setYellowX();
            }
            if (futureGreenX == true && System.currentTimeMillis() - backupTime > 6000) {
                setGreenX();
                futureGreenX = false;
                button.setEnabled(true);
                timer.stop();
            }
        }
    }
}
