import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Car extends JPanel {
	private int angle, width, height, frameWidth;
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
	
	public int getAngle() {
		return angle;
	}
	
	public boolean isCloseY(Car car, double distance) {
		if (Math.abs(this.getCarY() - car.getCarY()) < distance)
			return true;
		else
			return false;
	}
	
	public boolean isCloseX(Car car, double distance) {
		if (Math.abs(this.getCarX() - car.getCarX()) < distance)
			return true;
		else
			return false;
	}
	
	public int getCarWidth() {
		return width;
	}
	
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
	
	public int getCarX() {
		return getX()+frameWidth/2;
	}
	
	public int getCarY() {
		return getY()+frameWidth/2;
	}
	
	public int getFrameWidth() {
		return frameWidth;
	}

}
