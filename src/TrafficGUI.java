import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class TrafficGUI extends JFrame implements ActionListener {
	private Timer timer;
	private Car[] carY, carX, car;
	private Random rd;
	private ImageIcon backgroundImage;
	private JLabel backgroundLabel;
	private JButton trafficLightButton;
	private int amountOfCars, amountOfCarsY, amountOfCarsX;
	private static int topCrossLine, bottomCrossLine, leftCrossLine, rightCrossLine;
	private boolean greenX, greenY, futureGreenX, futureGreenY;
	private double passRedLightSpeed;
	private Date seconds;
	private long secondsBackup;
	private TrafficLight trafficLightObjectTop, trafficLightObjectBottom, trafficLightObjectLeft, trafficLightObjectRight;
	
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
		
		trafficLightObjectLeft = new TrafficLight();
		trafficLightObjectLeft.setColor(Color.RED);
		trafficLightObjectLeft.setBounds(leftCrossLine-20, bottomCrossLine-10, 20, 20);
		trafficLightObjectLeft.setAngle(90);
		trafficLightObjectLeft.setOpaque(false);
		backgroundLabel.add(trafficLightObjectLeft);
		
		trafficLightObjectRight = new TrafficLight();
		trafficLightObjectRight.setColor(Color.RED);
		trafficLightObjectRight.setBounds(rightCrossLine, topCrossLine-10, 20, 20);
		trafficLightObjectRight.setAngle(270);
		trafficLightObjectRight.setOpaque(false);
		backgroundLabel.add(trafficLightObjectRight);
		
		trafficLightObjectTop = new TrafficLight();
		trafficLightObjectTop.setColor(Color.GREEN);
		trafficLightObjectTop.setBounds(leftCrossLine-10, topCrossLine-20, 20, 20);
		trafficLightObjectTop.setAngle(180);
		trafficLightObjectTop.setOpaque(false);
		backgroundLabel.add(trafficLightObjectTop);
		
		trafficLightObjectBottom = new TrafficLight();
		trafficLightObjectBottom.setColor(Color.GREEN);
		trafficLightObjectBottom.setBounds(rightCrossLine-10, bottomCrossLine, 20, 20);
		trafficLightObjectBottom.setOpaque(false);
		backgroundLabel.add(trafficLightObjectBottom);
		
		
		
		// Default number of cars. Must be even and must be equal or greater than 6.
		amountOfCarsY = 10;
		amountOfCarsX = 16;
		
		amountOfCars = 24;
		
		car = new Car[amountOfCars];
		
		for (int i = 0; i < amountOfCars; i++) {
			car[i] = new Car();
			car[i].setOpaque(false);
			car[i].setRandomColor();
			backgroundLabel.add(car[i]);
		}	
		
		for (int i = 0; i < amountOfCars/4; i++) {						// left
			car[i].setCarPosition(400, 600 - 100 * i, 90);
			car[i].setRespawnPosition(400, -100, 90);
			car[i].setSpeed(rd.nextDouble() + 1);
		}
		for (int i = amountOfCars/4; i < amountOfCars/2; i++) {			// right
			car[i].setCarPosition(500, 100*i, 270);
			car[i].setRespawnPosition(500, 750, 270);
			car[i].setSpeed(rd.nextDouble() + 1);
		}
		for (int i = amountOfCars/2; i < amountOfCars * 3 / 4; i++) {	// top
			car[i].setCarPosition(i*(car[i].getCarWidth() + 30) - 550, 95, 180);
			car[i].setRespawnPosition(1000, 95, 180);
			car[i].setSpeed(0.4);
		}
		for (int i = amountOfCars * 3 / 4; i < amountOfCars; i++) {		// bottom
			car[i].setCarPosition(2050 - i*(car[i].getCarWidth() + 30), 195, 0);
			car[i].setRespawnPosition(-150, 195, 0);
			car[i].setSpeed(0.4);
		}
		

//		// Assigns speed values to cars.
//		for (int i = 0; i < amountOfCarsY; i++) 
//			carY[i].setSpeed(rd.nextDouble() + 1);
//				
//		for (int i = 0; i < amountOfCarsX; i++)
//			carX[i].setSpeed(0.4);
		
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
				if (!car[i].isFollowing(car[frontCarNumber], 110) && (car[i].getSpeed() > passRedLightSpeed || isLightGreenFor(car[i]) || car[i].hasCrossedLine()) && (i != 0 || car[i].getSpeed() < 0.7)) {
					car[i].accelerateBy(0.007);		
				}
				
				// stops at when lights are changing
				if (!isLightGreenFor(car[i]) && seconds.getTime() - secondsBackup >= 500 && !car[i].hasCrossedLine() && car[i].getSpeed() < passRedLightSpeed) {
					while (car[i].getSpeed() >= 0.3)
						car[i].accelerateBy(-0.01);	
					if (car[i].getSpeed() <= 0.3 && car[i].isNearCrossing())
						car[i].setSpeed(0);
				}
			}
			
			if (car[0].getCarFrontY() > topCrossLine+27 && car[0].getAngle() < car[0].getRespawnAngle() + 90 && car[0].getSpeed() < 0.8) {
				car[0].rotateBy(car[0].getSpeed()/1.5);
			}
			
			
			if (!trafficLightButton.isEnabled()) {
				seconds = new Date();
				if (greenX == true) {
					greenX = false;
					trafficLightObjectLeft.setColor(Color.YELLOW);
					trafficLightObjectRight.setColor(Color.YELLOW);
					futureGreenY = true;
				}
				if (greenY == true) {
					greenY = false;
					trafficLightObjectTop.setColor(Color.YELLOW);
					trafficLightObjectBottom.setColor(Color.YELLOW);
					futureGreenX = true;
				}
				if (futureGreenY == true && seconds.getTime() - secondsBackup > 2000) {
					trafficLightObjectLeft.setColor(Color.RED);
					trafficLightObjectRight.setColor(Color.RED);
				}
				if (futureGreenY == true && seconds.getTime() - secondsBackup > 4000) {
					trafficLightObjectTop.setColor(Color.YELLOW);
					trafficLightObjectBottom.setColor(Color.YELLOW);
				}
				if (futureGreenY == true && seconds.getTime() - secondsBackup > 6000) {
					greenY = true;
					trafficLightObjectTop.setColor(Color.GREEN);
					trafficLightObjectBottom.setColor(Color.GREEN);
					futureGreenY = false;
					trafficLightButton.setEnabled(true);
				}
				if (futureGreenX == true && seconds.getTime() - secondsBackup > 2000) {
					trafficLightObjectTop.setColor(Color.RED);
					trafficLightObjectBottom.setColor(Color.RED);
				}
				if (futureGreenX == true && seconds.getTime() - secondsBackup > 4000) {
					trafficLightObjectLeft.setColor(Color.YELLOW);
					trafficLightObjectRight.setColor(Color.YELLOW);
				}
				if (futureGreenX == true && seconds.getTime() - secondsBackup > 6000) {
					greenX = true;
					trafficLightObjectLeft.setColor(Color.GREEN);
					trafficLightObjectRight.setColor(Color.GREEN);
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
		if (car.getAngle() == 0 || car.getAngle() == 180 || car.getAngle() == 360)
			return greenX;
		else if (car.getAngle() == 90 || car.getAngle() == 270)
			return greenY;
		else
			return false;
	}
	
}


