import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Car extends JPanel {
	private int width, height, frameWidth, respawnX, respawnY, respawnAngle, turnLine;
	private double angle, Xposition, Yposition, speed;
	private Color carColor;
	private Random rd;
	private boolean isGoingToTurn;
	private Map map;
	
	public Car() {
		angle = 0;
		width = 70;
		height = 30;
		isGoingToTurn = false;
		carColor = Color.YELLOW;
		frameWidth = ((int) Math.sqrt(Math.pow(width, 2) + Math.pow(height/2, 2)) + 1) * 2;
		speed = 0.3;
		rd = new Random();
		map = new Map();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform rotation = new AffineTransform();
		rotation.setToTranslation(frameWidth/2, frameWidth/2);
		rotation.rotate(Math.toRadians(angle));
		g2d.transform(rotation);
		Rectangle carRec = new Rectangle(0, -height/2, width, height);
		BufferedImage carImage = LoadImage("src//car.png");
		g2d.setColor(carColor);
		g2d.fill(carRec);
		g2d.drawImage(carImage, 0, -height/2, null);
	}
	
	BufferedImage LoadImage (String FileName) {
		BufferedImage img = null;
		
		try {
			img = ImageIO.read(new File(FileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return img;
		
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public int getTurnLine() {
		return turnLine;
	}

	public void setTurnLine(int turnLine) {
		this.turnLine = turnLine;
	}

	// Returns the width of the car rectangle (not of the frame).
	public int getCarWidth() {
		return width;
	}

	// Returns the height of the car rectangle (not of the frame).
	public int getCarHeight() {
		return height;
	}

	// Returns the 'X' coordinate of the car rectangle (not of the frame).
	public int getCarX() {
		return getX()+frameWidth/2;
	}

	// Returns the 'Y' coordinate of the car rectangle (not of the frame).
	public int getCarY() {
		return getY()+frameWidth/2;
	}

	public double getCarFrontY() {
		return getCarY() + (width * Math.sin(Math.toRadians(angle)));
	}

	public double getCarFrontX() {
		return getCarX() + (width * Math.cos(Math.toRadians(angle)));
	}

	public double getCarFront() {
		if (angle == 0 || angle == 360 || angle == 180)
			return getCarFrontX();
		else if (angle == 90 || angle == 270)
			return getCarFrontY();
		else
			return 0;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public void setRespawnPosition(int x, int y, int angle) {
		respawnX = x;
		respawnY = y;
		respawnAngle = angle;
	}

	public int getRespawnAngle() {
		return respawnAngle;
	}

	public int getRespawnX() {
		return respawnX;
	}

	public int getRespawnY() {
		return respawnY;
	}

	public void respawn() {
		setCarPosition(respawnX, respawnY, respawnAngle);
	}

	public void setRandomSpeed() {
		setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
	}
	
	public boolean isAtTurningPosition() {
		if ((angle >= 0) && (angle < 90) && (getCarFrontX() > turnLine))
			return true;
		else if ((angle >= 90) && (angle < 180) && (getCarFrontY() > turnLine))
			return true;
		else if ((angle >= 180 && angle < 270) && (getCarFrontX() < turnLine))
			return true;
		else return (angle >= 270 && angle < 360) && (getCarFrontY() < turnLine);
	}

	public void moveAsSlowlyAs(Car car) {
		if (speed > car.getSpeed() && car.getSpeed() != 0)
			speed = Math.abs(car.getSpeed() - 0.03);
		if (car.getSpeed() == 0)
			speed = 0;
	}
	
	public void accelerateBy(double a) {
		speed = speed + a;
	}
	
	public boolean isOnMap() {
		return getCarX() > 0 && getCarX() < 800 && getCarY() > 0 && getCarY() < 600;
	}
	
	public boolean hasLeftMap() {
		if ((angle == 0 || angle == 360) && getCarX() > 800)
			return true;
		else if (angle == 90 && getCarY() > 600)
			return true;
		else if (angle == 180 && getCarX() < 0)
			return true;
		else return angle == 270 && getCarY() < 0;
	}
	
	public boolean hasCrossedLine() {
		if ((angle == 0 || angle == 360) && getCarFrontX() > map.getLeftCrossLine())
			return true;
		else if (angle == 180 && getCarFrontX() < map.getRightCrossLine())
			return true;
		else if (angle == 90 && getCarFrontY() > map.getTopCrossLine())
			return true;
		else return angle == 270 && getCarFrontY() < map.getBottomCrossLine();
	}
	
	public boolean isNearCrossing() {
		if (angle == 0 && getCarFrontX() < map.getLeftCrossLine() && getCarFrontX() > map.getLeftCrossLine() - 5)
			return true;
		else if (angle == 180 && getCarFrontX() > map.getRightCrossLine() && getCarFrontX() < map.getRightCrossLine() + 5)
			return true;
		else if (angle == 90 && getCarFrontY() < map.getTopCrossLine() && getCarFrontY() > map.getTopCrossLine() - 5)
			return true;
		else
			return angle == 270 && getCarFrontY() > map.getBottomCrossLine() && getCarFrontY() < map.getBottomCrossLine() + 5;
	}
	
	// Returns true if the distance on 'Y' axis between the instance of this class
	// and the parameter car is smaller than value of the distance parameter. 
	public boolean isCloseY(Car car, double distance) {
		return Math.abs(this.getCarY() - car.getCarY()) < distance;
	}
	
	// Returns true if the distance on 'X' axis between the instance of this class
	// and the parameter car is smaller than value of the distance parameter. 
	public boolean isCloseX(Car car, double distance) {
		return Math.abs(this.getCarX() - car.getCarX()) < distance;
	}
	
	public boolean isFollowing(Car car, double distance) {
		if ((angle == 0 || angle == 180 || angle == 360) && Math.abs(angle - car.getAngle()) < 90)
			return isCloseX(car, distance);
		else if ((angle == 90 || angle == 270) && Math.abs(angle - car.getAngle()) < 90)
			return isCloseY(car, distance);
		else
			return false;
	}
	
	public void stopNearCrossing() {
		while (getSpeed() >= 0.3)
			accelerateBy(-0.01);	
		if (getSpeed() <= 0.3 && isNearCrossing())
			setSpeed(0);
	}
	
	public void setCarSize(int width, int height) {
		this.width = width;
		this.height = height;
		frameWidth = ((int) Math.sqrt(Math.pow(width, 2) + Math.pow(height/2, 2)) + 1) * 2;
		repaint();
	}
	
	public void setCarColor(Color color) {
		carColor = color;
		repaint();
	}
	
	public void rotateBy(double degree) {
		angle = angle + degree;
		if ((angle > 0 && angle < 0.45) || (angle > 90 && angle < 90.45) || (angle > 360 && angle < 360.45) || (angle > 180 && angle < 180.45) || (angle > 270 && angle < 270.45))
			angle = (int) angle;
		repaint();
	}
	
	// Sets the angle and the position of the car rectangle (not of the frame) to a specified set of coordinates.
	public void setCarPosition(int x, int y, int angle) {
		setBounds(x-frameWidth/2, y-frameWidth/2, frameWidth, frameWidth);
		this.angle = angle;
		repaint();
		Xposition = x;
		Yposition = y;
	}
	
	public boolean isGoingToTurn() {
		return isGoingToTurn;
	}

	public void setGoingToTurn(boolean isGoingToTurn) {
		this.isGoingToTurn = isGoingToTurn;
	}

	// Moves the origin point of the car rectangle (not of the frame) by amount of pixels specified by the 'speed' variable.
	public void moveAhead() {
		Xposition = Xposition + speed * Math.cos(Math.toRadians(angle));
		Yposition = Yposition + speed * Math.sin(Math.toRadians(angle));
		setBounds((int)Xposition-frameWidth/2, (int)Yposition-frameWidth/2, frameWidth, frameWidth);
	}
	
	public void setRandomColor() {
		int number = rd.nextInt(10);
		Color color;
		switch (number) {
			case 1:
				color = Color.DARK_GRAY;
				break;
			case 2:
				color = Color.DARK_GRAY;
				break;
			case 3:
				color = Color.GRAY;
				break;
			case 4:
				color = Color.GREEN;
				break;
			case 5:
				color = Color.CYAN;
				break;
			case 6:
				color = Color.ORANGE;
				break;
			case 7:
				color = Color.ORANGE;
				break;
			case 8:
				color = Color.WHITE;
				break;
			case 9:
				color = Color.WHITE;
				break;
			default:
				color = Color.WHITE;
				break;
		}
		setCarColor(color);	
	}

}
