import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel implements ChangeListener, ActionListener {
    private final JButton restartButton, switchLightButton, backToMenuButton, startButton;
    private final JSlider trafficSlider;
    private final MovingCars cars;

    public GUI(Map map, MovingCars cars, TrafficLightsOnMap trafficLights) {
        this.cars = cars;
        map.setGUI(this);
        trafficLights.setGui(this);
        cars.setGui(this);

        startButton = new JButton("Start");
        startButton.setBounds(350, 285, 100, 30);
        startButton.addActionListener(cars);
        startButton.addActionListener(this);
        startButton.addActionListener(trafficLights);
        add(startButton);

        restartButton = new JButton("Restart");
        restartButton.setBounds(100, map.getBottomCrossLine() + 100, 150, 30);
        restartButton.addActionListener(cars);
        restartButton.addActionListener(trafficLights);
        restartButton.setLayout(null);
        add(restartButton);

        trafficSlider = new JSlider(JSlider.HORIZONTAL, 12, 32, 24);
        trafficSlider.setBounds(10, 500, 350, 30);
        trafficSlider.setOpaque(false);
        trafficSlider.setMajorTickSpacing(4);
        trafficSlider.setSnapToTicks(true);
        trafficSlider.addChangeListener(this);
        trafficSlider.setLayout(null);
        add(trafficSlider);

        switchLightButton = new JButton("Switch lights");
        switchLightButton.setBounds(100, map.getBottomCrossLine() + 10, 150, 30);
        switchLightButton.setLayout(null);
        switchLightButton.addActionListener(trafficLights);
        add(switchLightButton);

        backToMenuButton = new JButton("Pause");
        backToMenuButton.setBounds(700, 0, 100, 30);
        backToMenuButton.setLayout(null);
        backToMenuButton.addActionListener(cars);
        backToMenuButton.addActionListener(this);
        backToMenuButton.addActionListener(trafficLights);
        add(backToMenuButton);

        setButtonsVisible(false);

    }

    public boolean isSwitchLightButtonEnabled() {
        return switchLightButton.isEnabled();
    }

    public void setSwitchLightButtonEnabled(boolean arg) {
        switchLightButton.setEnabled(arg);
    }

    public JButton getSwitchLightButton() {
        return switchLightButton;
    }

    public JButton getRestartButton() {
        return restartButton;
    }

    public JButton getBackToMenuButton() {
        return backToMenuButton;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public void setButtonsVisible(boolean arg) {
        restartButton.setVisible(arg);
        trafficSlider.setVisible(arg);
        switchLightButton.setVisible(arg);
        backToMenuButton.setVisible(arg);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        Object z = e.getSource();

        if (z == trafficSlider) {
            cars.setAmountOfCars(trafficSlider.getValue());
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object z = e.getSource();
        if (z == startButton) {
            setButtonsVisible(true);
            startButton.setVisible(false);
        }
        if (z == backToMenuButton) {
            setButtonsVisible(false);
            startButton.setVisible(true);
        }
    }
}


