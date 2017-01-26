import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JFrame implements ActionListener, ChangeListener {
	private Timer timer;
	private Car[] car;
	private TrafficLight[] trafficLightObject;
	private Random rd;
	private JButton restartButton;
	private int amountOfCars, newAmountOfCars;
	private boolean greenX, greenY, futureGreenX, futureGreenY;
	private double passRedLightSpeed;
	private Date seconds;
	private long secondsBackup;
	private JSlider trafficSlider;
	private Map map;
	private TrafficLightsOnMap trafficLights;
	
	public GUI() {
		rd = new Random();
		seconds = new Date();
		map = new Map();
		map.setVisible(true);
		map.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// The speed at which cars ignore the red light.
		passRedLightSpeed = 1.3;
		
		// The default traffic light setup for 'X' and 'Y' axis.
		greenX = false;
		greenY = true;

		restartButton = new JButton("restart");
		restartButton.setBounds(100,Map.getBottomCrossLine()+100,100,20);
		restartButton.addActionListener(this);
		map.addOnTop(restartButton);
		
		trafficLights = new TrafficLightsOnMap();
		trafficLights.addOnTopOfMap(map);
		trafficLights.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		trafficSlider = new JSlider(JSlider.HORIZONTAL, 12, 32, 24);
		trafficSlider.setBounds(10, 500, 350, 30);
		trafficSlider.setOpaque(false);
		trafficSlider.setMajorTickSpacing(4);
		trafficSlider.setSnapToTicks(true);
		trafficSlider.addChangeListener(this);
		map.addOnTop(trafficSlider);
		
		amountOfCars = 32;
		newAmountOfCars = amountOfCars;
		car = new Car[amountOfCars];		
		for (int i = 0; i < amountOfCars; i++) {
			car[i] = new Car();
			car[i].setOpaque(false);
			car[i].setRandomColor();
			map.addOnTop(car[i]);
		}	
		
		for (int i = 0; i < amountOfCars/4; i++) {									// vertical left line
			car[i].setCarPosition(400, 600 - 100 * i, 90);
			car[i].setRespawnPosition(400, -100, 90);
			car[i].setSpeed(rd.nextDouble() + 1);
		}
		for (int i = amountOfCars/4; i < amountOfCars/2; i++) {						// vertical right line
			car[i].setCarPosition(500, 100*i, 270);
			car[i].setRespawnPosition(500, 750, 270);
			car[i].setSpeed(rd.nextDouble() + 1);
		}
		for (int i = amountOfCars/2; i < amountOfCars * 3 / 4; i++) {				// horizontal top line
			car[i].setCarPosition(Map.getRightCrossLine() + 100 + (i-amountOfCars/2)*(car[i].getCarWidth()+20), 95, 180);
			car[i].setRespawnPosition(1000, 95, 180);
			car[i].setSpeed(0.4);
		}
		for (int i = amountOfCars * 3 / 4; i < amountOfCars; i++) {					// horizontal bottom line
			car[i].setCarPosition(Map.getLeftCrossLine() - 100 - (i-amountOfCars*3/4)*(car[i].getCarWidth()+20), 195, 0);
			car[i].setRespawnPosition(-150, 195, 0);
			car[i].setSpeed(0.4);
		}
		
		car[0].setTurnLine(Map.getTopCrossLine() + 27);
		car[amountOfCars/4].setTurnLine(Map.getBottomCrossLine() - 30);
		car[amountOfCars/2].setTurnLine(Map.getRightCrossLine() - 30);
		car[amountOfCars*3/4].setTurnLine(Map.getLeftCrossLine() + 30);

		timer = new Timer(10, this);
		timer.addActionListener(this);
		timer.start();		
	}

	public static void main(String[] args) {
		Main app = new Main();
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
		System.out.println();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		
		// The following code is performed every 10 milliseconds.
		if (z == timer) {
			
			for (int i = 0; i < amountOfCars; i++) {	
				car[i].moveAhead();
				
				int frontCarNumber;			
				if (i == 0)
					frontCarNumber = (amountOfCars / 4) - 1;
				else if (i == amountOfCars / 4)
					frontCarNumber = (amountOfCars / 2) - 1;
				else if (i == amountOfCars / 2)
					frontCarNumber = (amountOfCars * 3 / 4) - 1;
				else if (i == amountOfCars * 3 / 4)
					frontCarNumber = amountOfCars - 1;
				else
					frontCarNumber = i - 1;			
				
				if (car[i].hasLeftMap() && car[frontCarNumber].isOnMap() && car[frontCarNumber].getAngle() == car[frontCarNumber].getRespawnAngle()) {
					car[i].respawn();
					car[i].setRandomSpeed();
					car[i].setRandomColor();
				}				
				if (car[i].isFollowing(car[frontCarNumber], 110)) {
					car[i].moveAsSlowlyAs(car[frontCarNumber]);
				}
				if ( (!car[i].isFollowing(car[frontCarNumber], 110))
						&& ((car[i].getSpeed() > passRedLightSpeed) || (isLightGreenFor(car[i])) || (car[i].hasCrossedLine()))
						&& ((!car[i].isGoingToTurn()) || (car[i].getSpeed() < 0.7)) ) {
					car[i].accelerateBy(0.007);
				}
				
				// stops cars when lights are changing
				if ( (!isLightGreenFor(car[i])) && (!car[i].hasCrossedLine())
						&& (car[i].getSpeed() < passRedLightSpeed) ) {
					car[i].stopNearCrossing();
				}
			}
			
			for (int i = 0; i < amountOfCars; i = i + amountOfCars/4) {
				if ((car[i].isAtTurningPosition()) && (car[i].getAngle() < car[i].getRespawnAngle() + 90) && (car[i].getSpeed() < 0.8)) {
					car[i].rotateBy(car[i].getSpeed()/1.5);
				}
				if (haveAllCarsLeft(car, i, i+(amountOfCars/4 - 1))) {
					car[i].respawn();
					car[i].setRandomSpeed();
					car[i].setRandomColor();
				}
				
				if (car[i].getSpeed() < 0.8)
					car[i].setGoingToTurn(true);
				else
					car[i].setGoingToTurn(false);
			}

		}// End of timer.
		
		if (z == restartButton) {
			reset();
		}
		
	}//end of ActionListener
	
	private int howManyCarsLeftMap(Car[] car, int firstCar, int lastCar) {
		int counter = 0;
		for (int i = firstCar; i <= lastCar; i++)
			if (car[i].hasLeftMap())
				counter++;
		return counter;
	}
	
	private boolean haveAllCarsLeft(Car[] car, int firstCar, int lastCar) {
		return howManyCarsLeftMap(car, firstCar, lastCar) == lastCar + 1 - firstCar;
	}
	
	private boolean isLightGreenFor(Car car) {
		if ((car.getAngle() == 0 || car.getAngle() == 180 || car.getAngle() == 360) && (!car.isGoingToTurn() || !car.hasCrossedLine()))
			return trafficLights.isGreenX();
		else if ((car.getAngle() == 90 || car.getAngle() == 270) && (!car.isGoingToTurn() || !car.hasCrossedLine()))
			return trafficLights.isGreenY();
		else
			return true;
	}
	
	public void reset() {

		trafficLights.restart();

		for (int i = 0; i < amountOfCars; i++) {
			car[i].setVisible(false);
		}
		
		amountOfCars = newAmountOfCars;
		
		car = new Car[amountOfCars];		
		for (int i = 0; i < amountOfCars; i++) {
			car[i] = new Car();
			car[i].setOpaque(false);
			car[i].setRandomColor();
			map.addOnTop(car[i]);
		}	
				
		for (int i = 0; i < amountOfCars/4; i++) {									// vertical left line
			car[i].setCarPosition(400, 600 - 100 * i, 90);
			car[i].setRespawnPosition(400, -100, 90);
			car[i].setSpeed(rd.nextDouble() + 1);
		}
		for (int i = amountOfCars/4; i < amountOfCars/2; i++) {						// vertical right line
			car[i].setCarPosition(500, 100*i, 270);
			car[i].setRespawnPosition(500, 750, 270);
			car[i].setSpeed(rd.nextDouble() + 1);
		}
		for (int i = amountOfCars/2; i < amountOfCars * 3 / 4; i++) {				// horizontal top line
			car[i].setCarPosition(Map.getRightCrossLine() + 100 + (i-amountOfCars/2)*(car[i].getCarWidth()+20), 95, 180);
			car[i].setRespawnPosition(1000, 95, 180);
			car[i].setSpeed(0.4);
		}
		for (int i = amountOfCars * 3 / 4; i < amountOfCars; i++) {					// horizontal bottom line
			car[i].setCarPosition(Map.getLeftCrossLine() - 100 - (i-amountOfCars*3/4)*(car[i].getCarWidth()+20), 195, 0);
			car[i].setRespawnPosition(-150, 195, 0);
			car[i].setSpeed(0.4);
		}
				
		car[0].setTurnLine(Map.getTopCrossLine() + 27);
		car[amountOfCars/4].setTurnLine(Map.getBottomCrossLine() - 30);
		car[amountOfCars/2].setTurnLine(Map.getRightCrossLine() - 30);
		car[amountOfCars*3/4].setTurnLine(Map.getLeftCrossLine() + 30);
		
		timer.restart();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		Object z = e.getSource();
		
		if (z == trafficSlider) {
			newAmountOfCars = trafficSlider.getValue();
			reset();
		}
		
	}
	
}


