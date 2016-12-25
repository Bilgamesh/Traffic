import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Main extends JFrame implements ActionListener {
	private Timer timer;
	private Car[] car, carX;
	private double[] speed, speedX;
	private Random rd;
	private ImageIcon backgroundImage;
	private JLabel backgroundLabel;
	private JButton trafficLightButton;
	private final int amountOfCarsY, amountOfCarsX, topCrossing, bottomCrossing, leftCrossing, rightCrossing;
	private boolean greenX, greenY;
	private double passRedLightSpeed, timerDelay;
	
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
		amountOfCarsX = 12;
		
		speed = new double[amountOfCarsY];
		speedX = new double[amountOfCarsX];
		
		// Assigns random speed to cars.
		for (int i = 0; i < amountOfCarsY; i++) 
			speed[i] = (rd.nextDouble() + 1);
		
		for (int i = 0; i < 10; i++)
			speedX[i] = (0.4);
		
		car = new Car[amountOfCarsY];
		
		// Sets starting positions for cars on the 'Y' axis.
		for (int i = 0; i < amountOfCarsY; i++) {
			car[i] = new Car();
			car[i].setOpaque(false);
			car[i].setRandomColor();
			
			// The first half of amount of cars is placed on the left line, the second half is placed on the right line.
			if (i < amountOfCarsY/2)
				car[i].setCarPosition(400, 600 - 100*i, 90);
			else
				car[i].setCarPosition(500, 100*i +car[i].getCarWidth(), 270);
			
			backgroundLabel.add(car[i]);		
		}
		
		carX = new Car[amountOfCarsX];
		
		for (int i = 0; i < amountOfCarsX; i++) {
			carX[i] = new Car();
			carX[i].setOpaque(false);
			carX[i].setRandomColor();
			
			if (i < amountOfCarsX/2)
				carX[i].setCarPosition(i*100 + 690, 95, 180);
			else
				carX[i].setCarPosition(760 - 100*i, 195, 0);
			backgroundLabel.add(carX[i]);
		}
		
		
		timer = new Timer(10, this);
		timer.addActionListener(this);
		timer.start();		
	}

	public static void main(String[] args) {
		Main app = new Main();
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		
		// The following code is performed every 10 milliseconds.
		if (z == timer) {
			
			for (int i = 0; i < amountOfCarsY; i++) 
				car[i].moveByAmount(speed[i]);
			
			for (int i = 0; i < amountOfCarsX; i++)
				carX[i].moveByAmount(speedX[i]);
			
			
			/* BEGINNING OF SETTINGS FOR CARS ON THE LEFT LINE OF 'Y' AXIS */
			
			for (int i = 1; i < amountOfCarsY/2; i++) {
				
				// Moves cars to their respawn area and gives them a new color and random speed after they leave the map.
				// The requirement is that the frontal car has to be on the map so that there's space in the respawn area. 
				if (car[i].getCarY() > 600 && car[i-1].getCarY() > 0 && car[i-1].getCarY() < 600) {
					car[i].setCarPosition(400, -100, 90);
					speed[i] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
					car[i].setRandomColor();
				}
				
				// Slows car down if it gets too close to another car at a specified distance.
				if (car[i].isCloseY(car[i-1], 100 + speed[i] * 10)) {
					if (speed[i] > speed[i-1] && speed[i-1] != 0)
						speed[i] = Math.abs(speed[i] - 0.03);
					if (speed[i-1] == 0)
						speed[i] = 0;
				}
				
				// Accelerates the car if there's no car ahead of it and the light is green.
				// If the speed is too high (i.e. the car won't stop before crossing the intersection)
				// or the car has already crossed the intersection, it also accelerates.
				else if (speed[i] > passRedLightSpeed || greenY == true || car[i].getCarFrontY() > topCrossing)
					speed[i] = speed[i] + 0.007;
				
			}
			
			
			// Exception - the first car in line (no. [0]) on the left line of Y axis
			// follows (after being respawned) last car in line (default no. [4]), thus it requires different conditionals.
			if (car[0].getCarY() > 600 && car[amountOfCarsY/2 - 1].getCarY() > 0 && car[amountOfCarsY/2 - 1].getCarY() < 600) {
				car[0].setCarPosition(400, -100, 90);
				speed[0] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
				car[0].setRandomColor();
			}
			
			// Slows the first car if it gets too close to the last car at a specified distance.
			if (car[0].isCloseY(car[amountOfCarsY/2 - 1], 100 + speed[0] * 10)) {
				if (speed[0] > speed[amountOfCarsY/2 - 1])
					speed[0] = Math.abs(speed[amountOfCarsY/2 - 1] - 0.03);
				if (speed[0] == 0)
					speed[0] = 0;
			}
			
			// In order to bring variety to the roads, the first car accelerates only untill it reaches a certain speed.
			else if (speed[0] < 0.7 && (greenY == true || car[0].getCarFrontY() > topCrossing))
				speed[0] = speed[0] + 0.007;
			
			/* END OF SETTINGS FOR CARS ON THE LEFT LINE OF 'Y' AXIS */
				
			
			
			
			/* BEGINNING OF SETTINGS FOR CARS ON THE RIGHT LINE OF 'Y' AXIS  */

			for (int i = amountOfCarsY/2 + 1; i < amountOfCarsY; i++) {
				
				// Moves cars to their respawn area and gives them a new color and random speed after they leave the map.
				// The requirement is that the frontal car has to be on the map so that there's space in the respawn area.
				if (car[i].getCarY() < 0 && car[i-1].getCarY() > 0 && car[i-1].getCarY() < 600) {
					car[i].setCarPosition(500, 750, 270);
					speed[i] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
					car[i].setRandomColor();
				}
				
				// Slows car down if it gets too close to another car at a specified distance.
				if (car[i].isCloseY(car[i-1], 100 + speed[i] * 10)) {
					if (speed[i] > speed[i-1])
						speed[i] = Math.abs(speed[i] - 0.03);
					if (speed[i-1] == 0)
						speed[i] = 0;
				}
				
				// Accelerates the car if there's no car ahead of it and the light is green.
				// If the speed is too high (i.e. the car won't stop before crossing the intersection)
				// or the car has already crossed the intersection, it also accelerates.
				else if (speed[i] > passRedLightSpeed || greenY == true || car[i].getCarFrontY() < bottomCrossing)
					speed[i] = speed[i] + 0.007;
			}

			
			// Exception - the first car in line (default no. [5]) on the right line of Y axis
			// follows (after being respawned) last car in line (default no. [9]), thus it requires different conditionals.
			if (car[amountOfCarsY/2].getCarY() < 0 && car[amountOfCarsY - 1].getCarY() > 0 && car[amountOfCarsY - 1].getCarY() < 600) {
				car[amountOfCarsY/2].setCarPosition(500, 750, 270);
				speed[amountOfCarsY/2] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
				car[amountOfCarsY/2].setRandomColor();
			}
			
			// Slows the first car down if it gets too close to the last car at a specified distance.
			if (car[amountOfCarsY/2].isCloseY(car[amountOfCarsY-1], 100 + speed[amountOfCarsY/2] * 10)) {
				if (speed[amountOfCarsY/2] > speed[amountOfCarsY-1])
					speed[amountOfCarsY/2] = Math.abs(speed[amountOfCarsY/2] - 0.03);
				if (speed[amountOfCarsY-1] == 0)
					speed[amountOfCarsY/2] = 0;
			}
			
			// In order to bring variety to the roads, the first car accelerates only untill it reaches a certain speed.
			else if (speed[amountOfCarsY/2] < 0.7 && (greenY == true || car[amountOfCarsY/2].getCarFrontY() < bottomCrossing))
				speed[amountOfCarsY/2] = speed[amountOfCarsY/2] + 0.007;
			
			/* END OF SETTINGS FOR CARS ON THE RIGHT LINE OF 'Y' AXIS */
			
			
			/* BEGINNING OF SETTINGS FOR CARS ON THE TOP LINE OF 'X' AXIS */
			
			for (int i = 1; i < amountOfCarsX/2; i++) {
				
				// Moves cars to their respawn area and gives them a new color and random speed after they leave the map.
				// The requirement is that the frontal car has to be on the map so that there's space in the respawn area. 
				if (carX[i].getCarX() < 0 && carX[i-1].getCarX() > 0 && carX[i-1].getCarX() < 800) {
					carX[i].setCarPosition(1000, 95, 180);
					speedX[i] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
					carX[i].setRandomColor();
				}
				
				// Slows car down if it gets too close to another car at a specified distance.
				if (carX[i].isCloseX(carX[i-1], 100 + speedX[i] * 10)) {
					if (speedX[i] > speedX[i-1] && speedX[i-1] != 0)
						speedX[i] = Math.abs(speedX[i] - 0.03);
					if (speedX[i-1] == 0)
						speedX[i] = 0;
				}
				
				// Accelerates the car if there's no car ahead of it and the light is green.
				// If the speed is too high (i.e. the car won't stop before crossing the intersection)
				// or the car has already crossed the intersection, it also accelerates.
				else if (speedX[i] > passRedLightSpeed || greenX == true || carX[i].getCarFrontX() < rightCrossing)
					speedX[i] = speedX[i] + 0.007;
				
			}
			
			
			// Exception - the first car in line (no. [0]) on the left line of Y axis
			// follows (after being respawned) last car in line (default no. [4]), thus it requires different conditionals.
			if (carX[0].getCarX() < 0 && carX[amountOfCarsX/2 - 1].getCarX() > 0 && carX[amountOfCarsX/2 - 1].getCarX() < 800) {
				carX[0].setCarPosition(1000, 95, 180);
				speedX[0] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
				carX[0].setRandomColor();
			}
			
			// Slows the first car if it gets too close to the last car at a specified distance.
			if (carX[0].isCloseX(carX[amountOfCarsX/2 - 1], 100 + speedX[0] * 10)) {
				if (speedX[0] > speedX[amountOfCarsX/2 - 1])
					speedX[0] = Math.abs(speedX[amountOfCarsX/2 - 1] - 0.03);
				if (speedX[0] == 0)
					speedX[0] = 0;
			}
			
			// In order to bring variety to the roads, the first car accelerates only untill it reaches a certain speed.
			else if (speedX[0] < 0.7 && (greenX == true || carX[0].getCarFrontX() < rightCrossing))
				speedX[0] = speedX[0] + 0.007;
			
			/* END OF SETTINGS FOR CARS ON THE TOP LINE OF 'X' AXIS */
			
			
			/* BEGINNING OF SETTINGS FOR CARS ON THE BOTTOM LINE OF 'X' AXIS  */

			for (int i = amountOfCarsX/2 + 1; i < amountOfCarsX; i++) {
				
				// Moves cars to their respawn area and gives them a new color and random speed after they leave the map.
				// The requirement is that the frontal car has to be on the map so that there's space in the respawn area.
				if (carX[i].getCarX() > 800 && carX[i-1].getCarX() > 0 && carX[i-1].getCarX() < 800) {
					carX[i].setCarPosition(-150, 195, 0);
					speedX[i] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
					carX[i].setRandomColor();
				}
				
				// Slows car down if it gets too close to another car at a specified distance.
				if (carX[i].isCloseX(carX[i-1], 100 + speedX[i] * 10)) {
					if (speedX[i] > speedX[i-1])
						speedX[i] = Math.abs(speedX[i] - 0.03);
					if (speedX[i-1] == 0)
						speedX[i] = 0;
				}
				
				// Accelerates the car if there's no car ahead of it and the light is green.
				// If the speed is too high (i.e. the car won't stop before crossing the intersection)
				// or the car has already crossed the intersection, it also accelerates.
				else if (speedX[i] > passRedLightSpeed || greenX == true || carX[i].getCarFrontX() > leftCrossing)
					speedX[i] = speedX[i] + 0.007;
			}

			
			// Exception - the first car in line (default no. [5]) on the right line of Y axis
			// follows (after being respawned) last car in line (default no. [9]), thus it requires different conditionals.
			if (carX[amountOfCarsX/2].getCarX() > 800 && carX[amountOfCarsY - 1].getCarX() > 0 && carX[amountOfCarsX - 1].getCarX() < 800) {
				carX[amountOfCarsX/2].setCarPosition(-150, 195, 0);
				speedX[amountOfCarsX/2] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
				carX[amountOfCarsX/2].setRandomColor();
			}
			
			// Slows the first car down if it gets too close to the last car at a specified distance.
			if (carX[amountOfCarsX/2].isCloseX(carX[amountOfCarsX-1], 100 + speedX[amountOfCarsX/2] * 10)) {
				if (speedX[amountOfCarsX/2] > speedX[amountOfCarsX-1])
					speedX[amountOfCarsX/2] = Math.abs(speedX[amountOfCarsX/2] - 0.03);
				if (speedX[amountOfCarsX-1] == 0)
					speedX[amountOfCarsX/2] = 0;
			}
			
			// In order to bring variety to the roads, the first car accelerates only untill it reaches a certain speed.
			else if (speedX[amountOfCarsX/2] < 0.7 && (greenX == true || carX[amountOfCarsX/2].getCarFrontX() > leftCrossing))
				speedX[amountOfCarsX/2] = speedX[amountOfCarsX/2] + 0.007;
			
			/* END OF SETTINGS FOR CARS ON THE BOTTOM LINE OF 'X' AXIS */
			
			
			// Gradually stops cars at 'Y' axis as the light changes to red.
			// There are two exceptions: 1) If the car is moving at high enough speed (variable passRedLightSpeed is specified
			// in the constructor) which makes it impossible to gradually stop the car before the intersection,
			// the car is allowed to go.
			// 2) If the car has already entered the intersection, it is allowed to go.
			if (greenY == false) {
				for (int i = 0; i < amountOfCarsY/2; i++) {
					if (car[i].getCarFrontY() < topCrossing && speed[i] > 0.3 && (speed[i] < passRedLightSpeed || Math.abs(topCrossing - car[i].getCarY()) > 100))
						speed[i] = Math.abs(speed[i] - 0.01);
					else if (car[i].getCarFrontY() < topCrossing && car[i].getCarFrontY() >= topCrossing - 5 && speed[i] <= 0.3)
						speed[i] = 0;
				}
				for (int i = amountOfCarsY/2; i < amountOfCarsY ; i++) {
					if (car[i].getCarFrontY() > bottomCrossing + 5 && speed[i] > 0.3 && (speed[i] < passRedLightSpeed || bottomCrossing - car[i].getCarY() > 100))
						speed[i] = Math.abs(speed[i] - 0.01);
					else if (car[i].getCarFrontY() > bottomCrossing && car[i].getCarFrontY() <= bottomCrossing + 5 && speed[i] <= 0.3)
						speed[i] = 0;
				}
			}
			else if (greenX == false) {
				for (int i = 0; i < amountOfCarsX/2; i++) {
					if (carX[i].getCarFrontX() > rightCrossing + 5 && speedX[i] > 0.3 && (speedX[i] < passRedLightSpeed || Math.abs(rightCrossing - carX[i].getCarX()) > 100))
						speedX[i] = Math.abs(speedX[i] - 0.01);
					else if (carX[i].getCarFrontX() > rightCrossing && carX[i].getCarFrontX() <= rightCrossing + 5 && speedX[i] <= 0.3)
						speedX[i] = 0;
				}
				for (int i = amountOfCarsX/2; i < amountOfCarsX ; i++) {
					if (carX[i].getCarFrontX() < leftCrossing && speedX[i] > 0.3 && (speedX[i] < passRedLightSpeed || leftCrossing - carX[i].getCarX() > 100))
						speedX[i] = Math.abs(speedX[i] - 0.01);
					else if (carX[i].getCarFrontX() < leftCrossing && carX[i].getCarFrontX() >= leftCrossing - 5 && speedX[i] <= 0.3)
						speedX[i] = 0;
				}
			}

		}// End of timer.
		
		
		// The transition of traffic lights after the button is pressed.
		if (z == trafficLightButton) {		
			if (greenY == true) {
				greenY = false;
				greenX = true;
			}
			else if (greenX == true) {
				greenY = true;
				greenX = false;
			}

		}
		
	}//end of ActionListener
	
}


