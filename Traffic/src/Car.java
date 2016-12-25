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
	private int width, height, frameWidth;
	private double angle, Xposition, Yposition;
	private Color carColor;
	
	public Car() {
		angle = 0;
		width = 70;
		height = 30;
		carColor = Color.YELLOW;
		frameWidth = ((int) Math.sqrt(Math.pow(width, 2) + Math.pow(height/2, 2)) + 1) * 2;
	}

	public void paintComponent (Graphics g) {
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
	
	public void rotateBy(double degree) {
		angle = angle + degree;
		repaint();
	}
	
	// Sets the position of the car rectangle (not of the frame) to a specified set of coordinates.
	public void setCarPosition(int x, int y, int angle) {
		setBounds(x-frameWidth/2, y-frameWidth/2, frameWidth, frameWidth);
		this.angle = angle;
		repaint();
		Xposition = x;
		Yposition = y;
	}
	
	// Moves the origin point of the car rectangle (not of the frame) by a specified amount of pixels.
	public void moveByAmount(double delta) {
		Xposition = Xposition + delta * Math.cos(Math.toRadians(angle));
		Yposition = Yposition + delta * Math.sin(Math.toRadians(angle));
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