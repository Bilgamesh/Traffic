import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JPanel implements ChangeListener {
    private TrafficLight[] trafficLightObject;
    private JButton restartButton, switchLightButton;
    private JSlider trafficSlider;
    private Map map;
    private MovingCars cars;

    public GUI(Map map, MovingCars cars, TrafficLightsOnMap trafficLights) {
        this.map = map;
        this.cars = cars;

        map.setGUI(this);
        trafficLights.setGui(this);
        cars.setGui(this);

        restartButton = new JButton("restart");
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

        switchLightButton = new JButton("switch lights");
        switchLightButton.setBounds(100, map.getBottomCrossLine() + 10, 150, 30);
        switchLightButton.setLayout(null);
        switchLightButton.addActionListener(trafficLights);
        add(switchLightButton);
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

    @Override
    public void stateChanged(ChangeEvent e) {
        Object z = e.getSource();

        if (z == trafficSlider) {
            cars.setAmountOfCars(trafficSlider.getValue());
        }

    }

}


