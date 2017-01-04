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
	private int width, height, frameWidth, respawnX, respawnY, respawnAngle;
	private double angle, Xposition, Yposition, speed;
	private Color carColor;
	private Random rd;
	
	public Car() {
		angle = 0;
		width = 70;
		height = 30;
		carColor = Color.YELLOW;
		frameWidth = ((int) Math.sqrt(Math.pow(width, 2) + Math.pow(height/2, 2)) + 1) * 2;
		speed = 0.3;
		rd = new Random();
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
	
	public void moveAsSlowlyAs(Car car) {
		if (this.speed > car.getSpeed() && car.getSpeed() != 0)
			speed = Math.abs(car.getSpeed() - 0.03);
		if (car.getSpeed() == 0)
			this.speed = 0;
	}
	
	public void accelerateBy(double a) {
		speed = speed + a;
	}
	
	public boolean isOnMap() {
		if (this.getCarX() > 0 && this.getCarX() < 800 && this.getCarY() > 0 && this.getCarY() < 600)
			return true;
		else
			return false;
	}
	
	public boolean hasLeftMap() {
		if ((this.angle == 0 || this.angle == 360) && this.getCarX() > 800)
			return true;
		else if (this.angle == 90 && this.getCarY() > 600)
			return true;
		else if (this.angle == 180 && this.getCarX() < 0)
			return true;
		else if (this.angle == 270 && this.getCarY() < 0)
			return true;
		else
			return false;
	}
	
	public boolean hasCrossedLine() {
		if (angle == 0 && getCarFrontX() > TrafficGUI.getLeftCrossLine())
			return true;
		else if (angle == 180 && getCarFrontX() < TrafficGUI.getRightCrossLine())
			return true;
		else if (angle == 90 && getCarFrontY() > TrafficGUI.getTopCrossLine())
			return true;
		else if (angle == 270 && getCarFrontY() < TrafficGUI.getBottomCrossLine())
			return true;
		else
			return false;
	}
	
	public boolean isNearCrossing() {
		if (angle == 0 && getCarFrontX() < TrafficGUI.getLeftCrossLine() && getCarFrontX() > TrafficGUI.getLeftCrossLine() - 5)
			return true;
		else if (angle == 180 && getCarFrontX() > TrafficGUI.getRightCrossLine() && getCarFrontX() < TrafficGUI.getRightCrossLine() + 5)
			return true;
		else if (angle == 90 && getCarFrontY() < TrafficGUI.getTopCrossLine() && getCarFrontY() > TrafficGUI.getTopCrossLine() - 5)
			return true;
		else if (angle == 270 && getCarFrontY() > TrafficGUI.getBottomCrossLine() && getCarFrontY() < TrafficGUI.getBottomCrossLine() + 5)
			return true;
		else
			return false;
	}
	
	// Returns true if the distance on 'Y' axis between the instance of this class
	// and the parameter car is smaller than value of the distance parameter. 
	public boolean isCloseY(Car car, double distance) {
		if (Math.abs(this.getCarY() - car.getCarY()) < distance)
			return true;
		else
			return false;
	}
	
	// Returns true if the distance on 'X' axis between the instance of this class
	// and the parameter car is smaller than value of the distance parameter. 
	public boolean isCloseX(Car car, double distance) {
		if (Math.abs(this.getCarX() - car.getCarX()) < distance)
			return true;
		else
			return false;
	}
	
	public boolean isFollowing(Car car, double distance) {
		if ((angle == 0 || angle == 180 || angle == 360) && Math.abs(angle - car.getAngle()) < 90)
			return this.isCloseX(car, distance);
		else if ((angle == 90 || angle == 270) && Math.abs(angle - car.getAngle()) < 90)
			return this.isCloseY(car, distance);
		else
			return false;
	}
	
	// Returns the width of the car rectangle (not of the frame).
	public int getCarWidth() {
		return width;
	}
	
	// Returns the height of the car rectangle (not of the frame).
	public int getCarHeight() {
		return height;
	}
	
	public void setCarSize(int width, int height) {
		this.width = width;
		this.height = height;
		frameWidth = ((int) Math.sqrt(Math.pow(width, 2) + Math.pow(height/2, 2)) + 1) * 2;
		repaint();
	}
	
	public void setCarColor(Color color) {
		carColor = color;
		this.repaint();
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
		this.setCarPosition(respawnX, respawnY, respawnAngle);
	}
	
	public void setRandomSpeed() {
		this.setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
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
	
	// Moves the origin point of the car rectangle (not of the frame) by amount of pixels specified by the 'speed' variable.
	public void moveAhead() {
		Xposition = Xposition + speed * Math.cos(Math.toRadians(angle));
		Yposition = Yposition + speed * Math.sin(Math.toRadians(angle));
		setBounds((int)Xposition-frameWidth/2, (int)Yposition-frameWidth/2, frameWidth, frameWidth);
	}
	
	public void setRandomColor() {
		Random rd = new Random();
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
