import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Main extends JFrame implements ActionListener {
	private Timer timer;
	private Car[] carY, carX;
	private Random rd;
	private ImageIcon backgroundImage;
	private JLabel backgroundLabel;
	private JButton trafficLightButton;
	private final int amountOfCarsY, amountOfCarsX, topCrossing, bottomCrossing, leftCrossing, rightCrossing;
	private boolean greenX, greenY, futureGreenX, futureGreenY;
	private double passRedLightSpeed;
	private Date seconds;
	private long secondsBackup;
	
	public Main() {
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
		topCrossing = 63;
		bottomCrossing = 230;
		leftCrossing = 368;
		rightCrossing = 533;
		
		// The speed at which cars ignore the red light.
		passRedLightSpeed = 1.3;
		
		// The default traffic light setup for 'X' and 'Y' axis.
		greenX = false;
		greenY = true;
		
		trafficLightButton = new JButton("Zmiana œwiate³");
		trafficLightButton.setBounds(100, bottomCrossing, 200, 30);
		trafficLightButton.addActionListener(this);
		backgroundLabel.add(trafficLightButton);
		
		// Default number of cars. Must be even and must be equal or greater than 6.
		amountOfCarsY = 10;
		amountOfCarsX = 16;
		
		carY = new Car[amountOfCarsY];
		carX = new Car[amountOfCarsX];
		
		// Sets starting settings for cars on the 'Y' axis.
		for (int i = 0; i < amountOfCarsY; i++) {
			carY[i] = new Car();
			carY[i].setOpaque(false);
			carY[i].setRandomColor();
			
			// The first half of amount of cars is placed on the left line, the second half is placed on the right line.
			if (i < amountOfCarsY/2)
				carY[i].setCarPosition(400, 600 - 100*i, 90);
			else
				carY[i].setCarPosition(500, 100*i +carY[i].getCarWidth(), 270);
			
			backgroundLabel.add(carY[i]);		
		}
		
		// Sets starting settings for cars on the 'X' axis.
		for (int i = 0; i < amountOfCarsX; i++) {
			carX[i] = new Car();
			carX[i].setOpaque(false);
			carX[i].setRandomColor();
			
			if (i < amountOfCarsX/2)
				carX[i].setCarPosition(rightCrossing + 80 + i*85, 95, 180);
			else
				carX[i].setCarPosition(leftCrossing - 80 - 85*(i-amountOfCarsX/2), 195, 0);
			backgroundLabel.add(carX[i]);
		}
		
		// Assigns speed values to cars.
		for (int i = 0; i < amountOfCarsY; i++) 
			carY[i].setSpeed(rd.nextDouble() + 1);
				
		for (int i = 0; i < amountOfCarsX; i++)
			carX[i].setSpeed(0.4);
		
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
			for (int i = 0; i < amountOfCarsY; i++) 
				carY[i].moveAhead();
			
			for (int i = 0; i < amountOfCarsX; i++)
				carX[i].moveAhead();
			
			
			/* BEGINNING OF SETTINGS FOR CARS ON THE LEFT LINE OF 'Y' AXIS */
			for (int i = 1; i < amountOfCarsY/2; i++) {
				
				// Moves cars to their respawn area and gives them a new color and random speed after they leave the map.
				// The requirement is that the frontal car has to be on the map so that there's space in the respawn area. 
				if (carY[i].hasLeftMap() && carY[i-1].isOnMap() && carY[i-1].getAngle() == 90) {
					carY[i].setCarPosition(400, -100, 90);
					carY[i].setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
					carY[i].setRandomColor();
				}
				
				// Slows car down if it gets too close to another car at a specified distance.
				if (carY[i].isCloseY(carY[i-1], 100 + carY[i].getSpeed() * 10) && carY[i-1].getAngle() - carY[i].getAngle() < 90) {
					carY[i].moveAsSlowlyAs(carY[i-1]);
				}
				
				// Accelerates the car if there's no car ahead of it, the light is green
				// and the light is not changing at the moment (i.e. trafficLightButton is enabled).
				// If the speed is too high (i.e. the car won't stop before crossing the intersection)
				// or the car has already crossed the intersection, it also accelerates, ignoring other conditions.
				else if (carY[i].getSpeed() > passRedLightSpeed || (greenY == true) || carY[i].getCarFrontY() > topCrossing)
						carY[i].accelerateBy(0.007);
			}
			
			// Exception - the first car in line (no. [0]) on the left line of Y axis
			// follows (after being respawned) last car in line (default no. [4]), thus it requires different conditionals.
			// It respawns after leaving the map only if the last car (default no. [4]) has reappeared on the map
			// or if all cars in this line have left the map.
			if (((carY[0].hasLeftMap()) && carY[amountOfCarsY/2 - 1].isOnMap()) || haveAllCarsLeft(carY, 0, amountOfCarsY/2 - 1)) {
				carY[0].setCarPosition(400, -100, 90);
				carY[0].setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
				carY[0].setRandomColor();
			}
			
			// Slows the first car if it gets too close to the last car at a specified distance.
			if (carY[0].isCloseY(carY[amountOfCarsY/2 - 1], 100 + carY[0].getSpeed() * 10)) {
				carY[0].moveAsSlowlyAs(carY[amountOfCarsY/2 - 1]);
			}
			
			// In order to bring variety to the roads, the first car accelerates only untill it reaches a certain speed.
			else if (carY[0].getSpeed() < 0.7 && ( (greenY == true) || carY[0].getCarFrontY() > topCrossing))
				carY[0].accelerateBy(0.007);
			
			// This car turns right when it enters the road intersection if it's randomly generated speed is low enough
			// and stops rotating if the angle has performed a 90 degree turn
			if (carY[0].getCarFrontY() > topCrossing+27 && carY[0].getAngle() < 180 && carY[0].getSpeed() < 0.8) {
				carY[0].rotateBy(carY[0].getSpeed()/1.5);
			}
			
			/* END OF SETTINGS FOR CARS ON THE LEFT LINE OF 'Y' AXIS */
				
			
			
			
			/* BEGINNING OF SETTINGS FOR CARS ON THE RIGHT LINE OF 'Y' AXIS  */

			for (int i = amountOfCarsY/2 + 1; i < amountOfCarsY; i++) {
				
				// Moves cars to their respawn area and gives them a new color and random speed after they leave the map.
				// The requirement is that the frontal car has to be on the map so that there's space in the respawn area.
				if (carY[i].hasLeftMap() && carY[i-1].isOnMap() && carY[i-1].getAngle() == 270) {
					carY[i].setCarPosition(500, 750, 270);
					carY[i].setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
					carY[i].setRandomColor();
				}
				
				// Slows car down if it gets too close to another car at a specified distance.
				if (carY[i].isCloseY(carY[i-1], 100 + carY[i].getSpeed() * 10) && carY[i-1].getAngle() - carY[i].getAngle() < 90) {
					carY[i].moveAsSlowlyAs(carY[i-1]);
				}
				
				// Accelerates the car if there's no car ahead of it, the light is green
				// and the light is not changing at the moment (i.e. trafficLightButton is enabled).
				// If the speed is too high (i.e. the car won't stop before crossing the intersection)
				// or the car has already crossed the intersection, it also accelerates, ignoring other conditions.
				else if (carY[i].getSpeed() > passRedLightSpeed || (greenY == true) || carY[i].getCarFrontY() < bottomCrossing)
					carY[i].accelerateBy(0.007);
			}
			
			// Exception - the first car in this line (default no. [5]) on the left line of Y axis
			// follows (after being respawned) last car in line (default no. [9]), thus it requires different conditionals.
			// It respawns after leaving the map only if the last car (default no. [9]) has reappeared on the map
			// or if all cars in this line have left the map.
			if (((carY[amountOfCarsY/2].hasLeftMap()) && carY[amountOfCarsY-1].isOnMap()) || haveAllCarsLeft(carY, amountOfCarsY/2, amountOfCarsY-1)) {
				carY[amountOfCarsY/2].setCarPosition(500, 750, 270);
				carY[amountOfCarsY/2].setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
				carY[amountOfCarsY/2].setRandomColor();
			}
			
			// Slows the first car down if it gets too close to the last car at a specified distance.
			if (carY[amountOfCarsY/2].isCloseY(carY[amountOfCarsY-1], 100 + carY[amountOfCarsY/2].getSpeed() * 10)) {
				carY[amountOfCarsY/2].moveAsSlowlyAs(carY[amountOfCarsY-1]);
			}
			
			// In order to bring variety to the roads, the first car accelerates only untill it reaches a certain speed.
			else if (carY[amountOfCarsY/2].getSpeed() < 0.7 && ( (greenY == true) || carY[amountOfCarsY/2].getCarFrontY() < bottomCrossing))
				carY[amountOfCarsY/2].accelerateBy(0.007);
			
			// This car turns right when it enters the road intersection if it's randomly generated speed is low enough
			// and stops rotating if the angle has performed a 90 degree turn
			if (carY[amountOfCarsY/2].getCarFrontY() < bottomCrossing-30 && carY[amountOfCarsY/2].getAngle() < 360 && carY[amountOfCarsY/2].getSpeed() < 0.8) {
				carY[amountOfCarsY/2].rotateBy(carY[amountOfCarsY/2].getSpeed()/1.5);
			}
			
			
			/* END OF SETTINGS FOR CARS ON THE RIGHT LINE OF 'Y' AXIS */
			
			
			/* BEGINNING OF SETTINGS FOR CARS ON THE TOP LINE OF 'X' AXIS */
			
			for (int i = 1; i < amountOfCarsX/2; i++) {
				
				// Moves cars to their respawn area and gives them a new color and random speed after they leave the map.
				// The requirement is that the frontal car has to be on the map so that there's space in the respawn area. 
				if (carX[i].hasLeftMap() && carX[i-1].isOnMap() && carX[i-1].getAngle() == 180) {
					carX[i].setCarPosition(1000, 95, 180);
					carX[i].setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
					carX[i].setRandomColor();
				}
				
				// Slows car down if it gets too close to another car at a specified distance.
				if (carX[i].isCloseX(carX[i-1], 100 + carX[i].getSpeed() * 10) && carX[i-1].getAngle() - carX[i].getAngle() < 90) {
					carX[i].moveAsSlowlyAs(carX[i-1]);
				}
				
				// Accelerates the car if there's no car ahead of it, the light is green
				// and the light is not changing at the moment (i.e. trafficLightButton is enabled).
				// If the speed is too high (i.e. the car won't stop before crossing the intersection)
				// or the car has already crossed the intersection, it also accelerates, ignoring other conditions.
				else if (carX[i].getSpeed() > passRedLightSpeed || (greenX == true) || carX[i].getCarFrontX() < rightCrossing)
					carX[i].accelerateBy(0.007);
				
			}
			
			// Exception - the first car in line (no. [0]) on the left line of Y axis
			// follows (after being respawned) last car in line (default no. [4]), thus it requires different conditionals.
			if ((carX[0].hasLeftMap()) && carX[amountOfCarsX/2 - 1].isOnMap() || haveAllCarsLeft(carX, 0, amountOfCarsX/2 - 1)) {
				carX[0].setCarPosition(1000, 95, 180);
				carX[0].setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
				carX[0].setRandomColor();
			}
			
			// Slows the first car if it gets too close to the last car at a specified distance.
			if (carX[0].isCloseX(carX[amountOfCarsX/2 - 1], 100 + carX[0].getSpeed() * 10)) {
				carX[0].moveAsSlowlyAs(carX[amountOfCarsX/2 - 1]);
			}
			
			// In order to bring variety to the roads, the first car accelerates only untill it reaches a certain speed.
			else if (carX[0].getSpeed() < 0.7 && ( (greenX == true) || carX[0].getCarFrontX() < rightCrossing))
				carX[0].accelerateBy(0.007);
			
			// This car turns right when it enters the road intersection if it's randomly generated speed is low enough
			// and stops rotating if the angle has performed a 90 degree turn
			if (carX[0].getCarFrontX() < rightCrossing-25 && carX[0].getAngle() < 270 && carX[0].getSpeed() < 0.8) {
				carX[0].rotateBy(carX[0].getSpeed()/1.5);
			}
			
			/* END OF SETTINGS FOR CARS ON THE TOP LINE OF 'X' AXIS */
			
			
			/* BEGINNING OF SETTINGS FOR CARS ON THE BOTTOM LINE OF 'X' AXIS  */

			for (int i = amountOfCarsX/2 + 1; i < amountOfCarsX; i++) {
				
				// Moves cars to their respawn area and gives them a new color and random speed after they leave the map.
				// The requirement is that the frontal car has to be on the map so that there's space in the respawn area.
				if (carX[i].hasLeftMap() && carX[i-1].isOnMap() && carX[i-1].getAngle() == 0) {
					carX[i].setCarPosition(-150, 195, 0);
					carX[i].setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
					carX[i].setRandomColor();
				}
				
				// Slows car down if it gets too close to another car at a specified distance.
				if (carX[i].isCloseX(carX[i-1], 100 + carX[i].getSpeed() * 10) && carX[i-1].getAngle() - carX[i].getAngle() < 90) {
					carX[i].moveAsSlowlyAs(carX[i-1]);
				}
				
				// Accelerates the car if there's no car ahead of it, the light is green
				// and the light is not changing at the moment (i.e. trafficLightButton is enabled).
				// If the speed is too high (i.e. the car won't stop before crossing the intersection)
				// or the car has already crossed the intersection, it also accelerates, ignoring other conditions.
				else if (carX[i].getSpeed() > passRedLightSpeed || (greenX == true) || carX[i].getCarFrontX() > leftCrossing)
					carX[i].accelerateBy(0.007);
			}

			// Exception - the first car in line (default no. [5]) on the right line of Y axis
			// follows (after being respawned) last car in line (default no. [9]), thus it requires different conditionals.
			if ((carX[amountOfCarsX/2].hasLeftMap()) && carX[amountOfCarsX - 1].isOnMap() || haveAllCarsLeft(carX, amountOfCarsX/2, amountOfCarsX - 1)) {
				carX[amountOfCarsX/2].setCarPosition(-150, 195, 0);
				carX[amountOfCarsX/2].setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
				carX[amountOfCarsX/2].setRandomColor();
			}
			
			// Slows the first car down if it gets too close to the last car at a specified distance.
			if (carX[amountOfCarsX/2].isCloseX(carX[amountOfCarsX-1], 100 + carX[amountOfCarsX/2].getSpeed() * 10)) {
				carX[amountOfCarsX/2].moveAsSlowlyAs(carX[amountOfCarsX-1]);
			}
			
			// In order to bring variety to the roads, the first car accelerates only untill it reaches a certain speed.
			else if (carX[amountOfCarsX/2].getSpeed() < 0.7 && ( (greenX == true) || carX[amountOfCarsX/2].getCarFrontX() > leftCrossing))
				carX[amountOfCarsX/2].accelerateBy(0.007);
			
			// This car turns right when it enters the road intersection if it's randomly generated speed is low enough
			// and stops rotating if the angle has performed a 90 degree turn
			if (carX[amountOfCarsX/2].getCarFrontX() > leftCrossing+25 && carX[amountOfCarsX/2].getAngle() < 90 && carX[amountOfCarsX/2].getSpeed() < 0.8) {
				carX[amountOfCarsX/2].rotateBy(carX[amountOfCarsX/2].getSpeed()/1.5);
			}
			
			/* END OF SETTINGS FOR CARS ON THE BOTTOM LINE OF 'X' AXIS */
			
			
			// Gradually stops cars at 'Y' axis as the light changes to red.
			// There are two exceptions: 1) If the car is moving at high enough speed (variable passRedLightSpeed is specified
			// in the constructor) which makes it impossible to gradually stop the car before the intersection,
			// the car is allowed to go.
			// 2) If the car has already entered the intersection, it is allowed to go.
			
		//	if (trafficLightButton.isEnabled() != true)
		//		seconds = new Date();
			
			//if (seconds.getTime() - secondsBackup >= 6000) {
		//		trafficLightButton.setEnabled(true);
		//	}
			
			if (!trafficLightButton.isEnabled()) {
				seconds = new Date();
				if (greenX == true) {
					greenX = false;
					futureGreenY = true;
				}
				if (greenY == true) {
					greenY = false;
					futureGreenX = true;
				}
				if (futureGreenY == true && seconds.getTime() - secondsBackup > 6000) {
					greenY = true;
					futureGreenY = false;
					trafficLightButton.setEnabled(true);
				}
				if (futureGreenX == true && seconds.getTime() - secondsBackup > 6000) {
					greenX = true;
					futureGreenX = false;
					trafficLightButton.setEnabled(true);
				}
			}
			
			if (greenY == false && seconds.getTime() - secondsBackup >= 500) {
				for (int i = 0; i < amountOfCarsY/2; i++) {
					if (carY[i].getCarFrontY() < topCrossing && carY[i].getSpeed() > 0.3 && (carY[i].getSpeed() < passRedLightSpeed || Math.abs(topCrossing - carY[i].getCarY()) > 100))
						carY[i].accelerateBy(-0.01);
					else if (carY[i].getCarFrontY() < topCrossing && carY[i].getCarFrontY() >= topCrossing - 5 && carY[i].getSpeed() <= 0.3)
						carY[i].setSpeed(0);
				}
				for (int i = amountOfCarsY/2; i < amountOfCarsY ; i++) {
					if (carY[i].getCarFrontY() > bottomCrossing + 5 && carY[i].getSpeed() > 0.3 && (carY[i].getSpeed() < passRedLightSpeed || bottomCrossing - carY[i].getCarY() > 100))
						carY[i].accelerateBy(-0.01);
					else if (carY[i].getCarFrontY() > bottomCrossing && carY[i].getCarFrontY() <= bottomCrossing + 5 && carY[i].getSpeed() <= 0.3)
						carY[i].setSpeed(0);
				}
			}
			if (greenX == false && seconds.getTime() - secondsBackup >= 500) {
				for (int i = 0; i < amountOfCarsX/2; i++) {
					if (carX[i].getCarFrontX() > rightCrossing + 5 && carX[i].getSpeed() > 0.3 && (carX[i].getSpeed() < passRedLightSpeed || Math.abs(rightCrossing - carX[i].getCarX()) > 100))
						carX[i].accelerateBy(-0.01);
					else if (carX[i].getCarFrontX() > rightCrossing && carX[i].getCarFrontX() <= rightCrossing + 5 && carX[i].getSpeed() <= 0.3)
						carX[i].setSpeed(0);
				}
				for (int i = amountOfCarsX/2; i < amountOfCarsX ; i++) {
					if (carX[i].getCarFrontX() < leftCrossing && carX[i].getSpeed() > 0.3 && (carX[i].getSpeed() < passRedLightSpeed || leftCrossing - carX[i].getCarX() > 100))
						carX[i].accelerateBy(-0.01);
					else if (carX[i].getCarFrontX() < leftCrossing && carX[i].getCarFrontX() >= leftCrossing - 5 && carX[i].getSpeed() <= 0.3)
						carX[i].setSpeed(0);
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
		if (howManyCarsLeftMap(car, firstCar, lastCar) == lastCar+1 - firstCar)
			return true;
		else
			return false;	
	}
	
}


