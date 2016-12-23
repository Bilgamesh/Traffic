import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import javax.swing.JPanel;

public class Car extends JPanel {
	private int Xposition, Yposition, angle;
	private Shape shape;
	
	public Car() {
		angle = 0;
	}

	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform rotation = new AffineTransform();
		rotation.setToTranslation(77, 77);
		rotation.rotate(Math.toRadians(angle), 0, 0);
		g2d.transform(rotation);
		Rectangle car = new Rectangle(0, -15, 70, 30);
		g2d.setColor(Color.YELLOW);
		g2d.fill(car);	
		shape = car;
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
	}
	
	public int getAngle() {
		return angle;
	}
	
	public boolean isCloseY(Car car, int distance) {
		if (Math.abs(this.getY() - car.getY()) < distance)
			return true;
		else
			return false;
	}
	
	public boolean isCloseX(Car car, int distance) {
		if (Math.abs(this.getX() - car.getX()) < distance)
			return true;
		else
			return false;
	}

}
