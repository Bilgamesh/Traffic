import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrafficLightsOnMap extends JPanel implements ActionListener {
    private TrafficLight[] trafficLightObject;
    private JButton button;
    private long backupTime;
    private Timer timer;
    private boolean YTurningGreen, XTurningGreen;

    public TrafficLightsOnMap(Map map) {

        trafficLightObject = new TrafficLight[4];
        for (int i = 0; i < 4; i++) {
            trafficLightObject[i] = new TrafficLight();
            trafficLightObject[i].setOpaque(false);
            trafficLightObject[i].setColor(Color.GREEN);
            add(trafficLightObject[i]);
        }

        trafficLightObject[0].setColor(Color.RED);										// left
        trafficLightObject[0].setBounds(map.getLeftCrossLine()-20, map.getBottomCrossLine()-10, 20, 20);
        trafficLightObject[0].setAngle(90);

        trafficLightObject[1].setColor(Color.RED);										// right
        trafficLightObject[1].setBounds(map.getRightCrossLine(), map.getTopCrossLine()-10, 20, 20);
        trafficLightObject[1].setAngle(270);

        trafficLightObject[2].setBounds(map.getLeftCrossLine()-10, map.getTopCrossLine()-20, 20, 20);		// top
        trafficLightObject[2].setAngle(180);

        trafficLightObject[3].setBounds(map.getRightCrossLine()-10, map.getBottomCrossLine(), 20, 20);	// bottom

        button = new JButton("switch lights");
        button.setBounds(100, map.getBottomCrossLine(),150,30);
        button.addActionListener(this);
        add(button);

        timer = new Timer(500, this);

    }

    public void setYellowX() {
        trafficLightObject[0].setColor(Color.YELLOW);
        trafficLightObject[1].setColor(Color.YELLOW);
    }

    public void setYellowY() {
        trafficLightObject[2].setColor(Color.YELLOW);
        trafficLightObject[3].setColor(Color.YELLOW);
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
        return trafficLightObject[0].getColor() == Color.GREEN;
    }

    public void setGreenY() {
        trafficLightObject[2].setColor(Color.GREEN);
        trafficLightObject[3].setColor(Color.GREEN);
    }

    public boolean isGreenY() {
        return trafficLightObject[2].getColor() == Color.GREEN;
    }

    public void restart() {
        button.setEnabled(true);
        timer.restart();
        timer.stop();
        trafficLightObject[0].setColor(Color.RED);										// left
        trafficLightObject[1].setColor(Color.RED);										// right
        trafficLightObject[2].setColor(Color.GREEN);     // top
        trafficLightObject[3].setColor(Color.GREEN);     // bottom
        XTurningGreen = false;
        YTurningGreen = false;
    }

    public void setVisible(boolean arg) {
        for (int i = 0; i < 4; i++)
            trafficLightObject[i].setVisible(arg);
    }

//    public void addOnTopOfMap(Map map) {
//        for (int i = 0; i < 4; i++) {
//            map.addOnTop(trafficLightObject[i]);
//        }
//        map.addOnTop(button);
//    }

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
                YTurningGreen = true;
            }
            if (isGreenY()) {
                setYellowY();
                XTurningGreen = true;
            }
            if (YTurningGreen && System.currentTimeMillis() - backupTime > 2000) {
                setRedX();
            }
            if (YTurningGreen && System.currentTimeMillis() - backupTime > 4000) {
                setYellowY();
            }
            if (YTurningGreen && System.currentTimeMillis() - backupTime > 6000) {
                setGreenY();
                YTurningGreen = false;
                button.setEnabled(true);
                timer.stop();
            }
            if (XTurningGreen && System.currentTimeMillis() - backupTime > 2000) {
                setRedY();
            }
            if (XTurningGreen && System.currentTimeMillis() - backupTime > 4000) {
                setYellowX();
            }
            if (XTurningGreen && System.currentTimeMillis() - backupTime > 6000) {
                setGreenX();
                XTurningGreen = false;
                button.setEnabled(true);
                timer.stop();
            }
        }
    }
}
