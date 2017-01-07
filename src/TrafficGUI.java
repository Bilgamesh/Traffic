import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TrafficGUI extends JFrame implements ActionListener, ChangeListener {
	private Timer timer;
	private Car[] car;
	private TrafficLight[] trafficLightObject;
	private Random rd;
	private ImageIcon backgroundImage;
	private JLabel backgroundLabel;
	private JButton trafficLightButton, restartButton;
	private int amountOfCars, newAmountOfCars;
	private static int topCrossLine, bottomCrossLine, leftCrossLine, rightCrossLine;
	private boolean greenX, greenY, futureGreenX, futureGreenY;
	private double passRedLightSpeed;
	private Date seconds;
	private long secondsBackup;
	private JSlider trafficSlider;
	
	public TrafficGUI() {
		setSize(800, 600);
		setLayout(null);
		setResizable(false);
		setTitle("Traffic simulator");
		
		backgroundImage = new ImageIcon(getClass().getResource("background.png"));
		backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, 800, 600);
		add(backgroundLabel);
		rd = new Random();
		seconds = new Date();
		
		// The coordinates of road intersection
		topCrossLine = 63;
		bottomCrossLine = 230;
		leftCrossLine = 368;
		rightCrossLine = 533;
		
		// The speed at which cars ignore the red light.
		passRedLightSpeed = 1.3;
		
		// The default traffic light setup for 'X' and 'Y' axis.
		greenX = false;
		greenY = true;
		
		trafficLightButton = new JButton("Switch lights");
		trafficLightButton.setBounds(100, bottomCrossLine, 200, 30);
		trafficLightButton.addActionListener(this);
		backgroundLabel.add(trafficLightButton);
		
		restartButton = new JButton("Restart");
		restartButton.setBounds(100, bottomCrossLine+60, 200, 30);
		restartButton.addActionListener(this);
		backgroundLabel.add(restartButton);
		
		trafficLightObject = new TrafficLight[4];
		for (int i = 0; i < 4; i++) {
			trafficLightObject[i] = new TrafficLight();
			trafficLightObject[i].setOpaque(false);
			trafficLightObject[i].setColor(Color.GREEN);
			backgroundLabel.add(trafficLightObject[i]);
		}
		
		trafficLightObject[0].setColor(Color.RED);										// left
		trafficLightObject[0].setBounds(leftCrossLine-20, bottomCrossLine-10, 20, 20);
		trafficLightObject[0].setAngle(90);
		
		trafficLightObject[1].setColor(Color.RED);										// right
		trafficLightObject[1].setBounds(rightCrossLine, topCrossLine-10, 20, 20);
		trafficLightObject[1].setAngle(270);	

		trafficLightObject[2].setBounds(leftCrossLine-10, topCrossLine-20, 20, 20);		// top
		trafficLightObject[2].setAngle(180);

		trafficLightObject[3].setBounds(rightCrossLine-10, bottomCrossLine, 20, 20);	// bottom
		
		trafficSlider = new JSlider(JSlider.HORIZONTAL, 12, 32, 24);
		trafficSlider.setBounds(10, 500, 350, 30);
		trafficSlider.setOpaque(false);
		trafficSlider.setMajorTickSpacing(4);
		trafficSlider.setSnapToTicks(true);
		trafficSlider.addChangeListener(this);
		backgroundLabel.add(trafficSlider);
		
		amountOfCars = 32;
		newAmountOfCars = amountOfCars;
		car = new Car[amountOfCars];		
		for (int i = 0; i < amountOfCars; i++) {
			car[i] = new Car();
			car[i].setOpaque(false);
			car[i].setRandomColor();
			backgroundLabel.add(car[i]);
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
			car[i].setCarPosition(rightCrossLine + 100 + (i-amountOfCars/2)*(car[i].getCarWidth()+20), 95, 180);
			car[i].setRespawnPosition(1000, 95, 180);
			car[i].setSpeed(0.4);
		}
		for (int i = amountOfCars * 3 / 4; i < amountOfCars; i++) {					// horizontal bottom line
			car[i].setCarPosition(leftCrossLine - 100 - (i-amountOfCars*3/4)*(car[i].getCarWidth()+20), 195, 0);
			car[i].setRespawnPosition(-150, 195, 0);
			car[i].setSpeed(0.4);
		}
		
		car[0].setTurnLine(topCrossLine + 27);
		car[amountOfCars/4].setTurnLine(bottomCrossLine - 30);
		car[amountOfCars/2].setTurnLine(rightCrossLine - 30);
		car[amountOfCars*3/4].setTurnLine(leftCrossLine + 30);
		
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
				if ( (!isLightGreenFor(car[i])) && (seconds.getTime() - secondsBackup >= 500) && (!car[i].hasCrossedLine())
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
			
			
			if (!trafficLightButton.isEnabled()) {
				seconds = new Date();
				if (greenX == true) {
					greenX = false;
					trafficLightObject[0].setColor(Color.YELLOW);
					trafficLightObject[1].setColor(Color.YELLOW);
					futureGreenY = true;
				}
				if (greenY == true) {
					greenY = false;
					trafficLightObject[2].setColor(Color.YELLOW);
					trafficLightObject[3].setColor(Color.YELLOW);
					futureGreenX = true;
				}
				if (futureGreenY == true && seconds.getTime() - secondsBackup > 2000) {
					trafficLightObject[0].setColor(Color.RED);
					trafficLightObject[1].setColor(Color.RED);
				}
				if (futureGreenY == true && seconds.getTime() - secondsBackup > 4000) {
					trafficLightObject[2].setColor(Color.YELLOW);
					trafficLightObject[3].setColor(Color.YELLOW);
				}
				if (futureGreenY == true && seconds.getTime() - secondsBackup > 6000) {
					greenY = true;
					trafficLightObject[2].setColor(Color.GREEN);
					trafficLightObject[3].setColor(Color.GREEN);
					futureGreenY = false;
					trafficLightButton.setEnabled(true);
				}
				if (futureGreenX == true && seconds.getTime() - secondsBackup > 2000) {
					trafficLightObject[2].setColor(Color.RED);
					trafficLightObject[3].setColor(Color.RED);
				}
				if (futureGreenX == true && seconds.getTime() - secondsBackup > 4000) {
					trafficLightObject[0].setColor(Color.YELLOW);
					trafficLightObject[1].setColor(Color.YELLOW);
				}
				if (futureGreenX == true && seconds.getTime() - secondsBackup > 6000) {
					greenX = true;
					trafficLightObject[0].setColor(Color.GREEN);
					trafficLightObject[1].setColor(Color.GREEN);
					futureGreenX = false;
					trafficLightButton.setEnabled(true);
				}
			}

		}// End of timer.
		
		
		// The transition of traffic lights after the button is pressed.
		if (z == trafficLightButton) {	
			 seconds = new Date();
			 secondsBackup = seconds.getTime();
			 trafficLightButton.setEnabled(false);
		}
		
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
		if (howManyCarsLeftMap(car, firstCar, lastCar) == lastCar + 1 - firstCar)
			return true;
		else
			return false;	
	}
	
	public static int getTopCrossLine() {
		return topCrossLine;
	}

	public static int getBottomCrossLine() {
		return bottomCrossLine;
	}

	public static int getLeftCrossLine() {
		return leftCrossLine;
	}

	public static int getRightCrossLine() {
		return rightCrossLine;
	}
	
	private boolean isLightGreenFor(Car car) {
		if ((car.getAngle() == 0 || car.getAngle() == 180 || car.getAngle() == 360) && (!car.isGoingToTurn() || !car.hasCrossedLine()))
			return greenX;
		else if ((car.getAngle() == 90 || car.getAngle() == 270) && (!car.isGoingToTurn() || !car.hasCrossedLine()))
			return greenY;
		else
			return true;
	}
	
	public void reset() {
		
		greenX = false;
		greenY = true;
		futureGreenX = false;
		futureGreenY = false;
		
		trafficLightButton.setEnabled(true);
		trafficLightObject[0].setColor(Color.RED);		// left			
		trafficLightObject[1].setColor(Color.RED);		// right
		trafficLightObject[2].setColor(Color.GREEN);	// top
		trafficLightObject[3].setColor(Color.GREEN);	// bottom
		
		for (int i = 0; i < amountOfCars; i++) {
			car[i].setVisible(false);
		}
		
		amountOfCars = newAmountOfCars;
		
		car = new Car[amountOfCars];		
		for (int i = 0; i < amountOfCars; i++) {
			car[i] = new Car();
			car[i].setOpaque(false);
			car[i].setRandomColor();
			backgroundLabel.add(car[i]);
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
			car[i].setCarPosition(rightCrossLine + 100 + (i-amountOfCars/2)*(car[i].getCarWidth()+20), 95, 180);
			car[i].setRespawnPosition(1000, 95, 180);
			car[i].setSpeed(0.4);
		}
		for (int i = amountOfCars * 3 / 4; i < amountOfCars; i++) {					// horizontal bottom line
			car[i].setCarPosition(leftCrossLine - 100 - (i-amountOfCars*3/4)*(car[i].getCarWidth()+20), 195, 0);
			car[i].setRespawnPosition(-150, 195, 0);
			car[i].setSpeed(0.4);
		}
				
		car[0].setTurnLine(topCrossLine + 27);
		car[amountOfCars/4].setTurnLine(bottomCrossLine - 30);
		car[amountOfCars/2].setTurnLine(rightCrossLine - 30);
		car[amountOfCars*3/4].setTurnLine(leftCrossLine + 30);
		
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


