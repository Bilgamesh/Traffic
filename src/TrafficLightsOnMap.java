import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrafficLightsOnMap extends JPanel implements ActionListener {
    private final Timer timer;
    private final TrafficLight[] trafficLightObject;
    private long backupTime, pauseTime;
    private boolean YTurningGreen, XTurningGreen;
    private GUI gui;

    public TrafficLightsOnMap(Map map) {
        backupTime = 0;

        trafficLightObject = new TrafficLight[4];
        for (int i = 0; i < 4; i++) {
            trafficLightObject[i] = new TrafficLight();
            trafficLightObject[i].setOpaque(false);
            trafficLightObject[i].setColor(Color.GREEN);
            add(trafficLightObject[i]);
        }

        // left traffic light
        trafficLightObject[0].setColor(Color.RED);
        trafficLightObject[0].setBounds(map.getLeftCrossLine() - 20, map.getBottomCrossLine() - 10, 20, 20);
        trafficLightObject[0].setAngle(90);

        // right traffic light
        trafficLightObject[1].setColor(Color.RED);
        trafficLightObject[1].setBounds(map.getRightCrossLine(), map.getTopCrossLine() - 10, 20, 20);
        trafficLightObject[1].setAngle(270);

        // top traffic light
        trafficLightObject[2].setBounds(map.getLeftCrossLine() - 10, map.getTopCrossLine() - 20, 20, 20);
        trafficLightObject[2].setAngle(180);

        // bottom traffic light
        trafficLightObject[3].setBounds(map.getRightCrossLine() - 10, map.getBottomCrossLine(), 20, 20);

        timer = new Timer(10, this);
        timer.setInitialDelay(0);

    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    // This method sets the color of traffic lights on the X axis as yellow
    public void setYellowX() {
        trafficLightObject[0].setColor(Color.YELLOW);
        trafficLightObject[1].setColor(Color.YELLOW);
    }

    // This method sets the color of traffic lights on the Y axis as yellow
    public void setYellowY() {
        trafficLightObject[2].setColor(Color.YELLOW);
        trafficLightObject[3].setColor(Color.YELLOW);
    }

    // This method sets the color of traffic lights on the X axis as red
    public void setRedX() {
        trafficLightObject[0].setColor(Color.RED);
        trafficLightObject[1].setColor(Color.RED);
    }

    // This method sets the color of traffic lights on the Y axis as red
    public void setRedY() {
        trafficLightObject[2].setColor(Color.RED);
        trafficLightObject[3].setColor(Color.RED);
    }

    // This method sets the color of traffic lights on the X axis as green
    public void setGreenX() {
        trafficLightObject[0].setColor(Color.GREEN);
        trafficLightObject[1].setColor(Color.GREEN);
    }

    // This method returns true if the traffic lights on the X axis are green
    public boolean isGreenX() {
        return trafficLightObject[0].getColor() == Color.GREEN;
    }

    // This method sets the color of traffic lights on the Y axis as green
    public void setGreenY() {
        trafficLightObject[2].setColor(Color.GREEN);
        trafficLightObject[3].setColor(Color.GREEN);
    }

    // This method returns true if the traffic lights on the Y axis are green
    public boolean isGreenY() {
        return trafficLightObject[2].getColor() == Color.GREEN;
    }

    // This method returns true if the traffic lights on the Y axis are red
    public boolean isRedY() {
        return trafficLightObject[2].getColor() == Color.RED;
    }

    // This method returns true if the traffic lights on the Y axis are yellow
    public boolean isYellowY() {
        return trafficLightObject[2].getColor() == Color.YELLOW;
    }

    // This method returns true if the traffic lights on the X axis are red
    public boolean isRedX() {
        return trafficLightObject[0].getColor() == Color.RED;
    }

    // This method returns true if the traffic lights on the X axis are yellow
    public boolean isYellowX() {
        return trafficLightObject[0].getColor() == Color.YELLOW;
    }

    public void restart() {
        gui.setSwitchLightButtonEnabled(true);
        timer.restart();
        timer.stop();
        trafficLightObject[0].setColor(Color.RED); // left
        trafficLightObject[1].setColor(Color.RED); // right
        trafficLightObject[2].setColor(Color.GREEN); // top
        trafficLightObject[3].setColor(Color.GREEN); // bottom
        XTurningGreen = false;
        YTurningGreen = false;
    }

    public void setVisible(boolean arg) {
        for (int i = 0; i < 4; i++)
            trafficLightObject[i].setVisible(arg);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object z = e.getSource();

        if (z == gui.getSwitchLightButton()) {
            gui.setSwitchLightButtonEnabled(false);
            timer.start();
            backupTime = System.currentTimeMillis();
        }

        if (z == gui.getRestartButton()) {
            restart();
            backupTime = 0;
        }

        if (z == gui.getBackToMenuButton() && timer.isRunning()) {
            timer.stop();
            pauseTime = System.currentTimeMillis();
        }

        if (z == gui.getStartButton() && backupTime != 0) {
            timer.start();
            backupTime = backupTime + System.currentTimeMillis() - pauseTime;
        }

        // After switchLightButton is pressed, the traffic lights are changed in 4 stages, there is 2 seconds delay between each stage.
        // 1. All green lights turn yellow.
        // 2. The lights which were red before the stage 1 now turn yellow.
        // 3. The lights which turned yellow in the stage 1 now turn red.
        // 4. The lights which turned yellow in the stage 2 now turn green.
        if (z == timer) {
            if (isGreenX()) {
                setYellowX();
                YTurningGreen = true;
            }
            if (isGreenY()) {
                setYellowY();
                XTurningGreen = true;
            }
            if (YTurningGreen && System.currentTimeMillis() - backupTime > 2000 && !isRedX())
                setRedX();
            if (YTurningGreen && System.currentTimeMillis() - backupTime > 4000 && isYellowY())
                setYellowY();
            if (YTurningGreen && System.currentTimeMillis() - backupTime > 6000) {
                setGreenY();
                YTurningGreen = false;
                gui.setSwitchLightButtonEnabled(true);
                timer.stop();
                backupTime = 0;
            }
            if (XTurningGreen && System.currentTimeMillis() - backupTime > 2000 && !isRedY())
                setRedY();
            if (XTurningGreen && System.currentTimeMillis() - backupTime > 4000 && !isYellowX())
                setYellowX();
            if (XTurningGreen && System.currentTimeMillis() - backupTime > 6000) {
                setGreenX();
                XTurningGreen = false;
                gui.setSwitchLightButtonEnabled(true);
                timer.stop();
                backupTime = 0;
            }
        } // end of timer
    }
}
