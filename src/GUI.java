import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JPanel implements ChangeListener {
	private TrafficLight[] trafficLightObject;
	private JButton restartButton, switchLightButton;
	private JSlider trafficSlider;
	private Map map;
	private MovingCars cars;
	private TrafficLightsOnMap trafficLights;
	
	public GUI(Map map, MovingCars cars, TrafficLightsOnMap trafficLights) {
		this.map = map;
		this.cars = cars;
		this.trafficLights = trafficLights;

		map.setGUI(this);
		trafficLights.setGui(this);
		cars.setGui(this);

		restartButton = new JButton("restart");
		restartButton.setBounds(100,map.getBottomCrossLine()+100,150,30);
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
		trafficSlider.addChangeListener(this);
		add(trafficSlider);

		switchLightButton = new JButton("switch lights");
		switchLightButton.setBounds(100, map.getBottomCrossLine()+10,150,30);
		switchLightButton.addActionListener(trafficLights);
		switchLightButton.setLayout(null);
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
			//reset();
			restartButton.getAction();
		}
		
	}
	
}


