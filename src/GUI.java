import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JPanel implements ActionListener, ChangeListener {
	private TrafficLight[] trafficLightObject;
	private JButton restartButton;
	private JSlider trafficSlider;
	private MovingCars cars;
	
	public GUI(Map map) {

		restartButton = new JButton("restart");
		restartButton.setBounds(100,map.getBottomCrossLine()+100,100,20);
		restartButton.addActionListener(this);
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
	}

	public static void main(String[] args) {
		Main app = new Main();
		app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		app.setVisible(true);
		System.out.println();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		
		if (z == restartButton) {
			reset();
		}
		
	}//end of ActionListener
	
	public void reset() {

	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object z = e.getSource();
		
		if (z == trafficSlider) {
			//cars.setAmountOfCars(trafficSlider.getValue());
			//reset();
		}
		
	}
	
}


