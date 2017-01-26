import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class TrafficLight extends JPanel {
	private Color color;
	private int width, height, angle;
	
	public TrafficLight() {
		color = Color.RED;
		width = 20;
		height = 20;
		angle = 0;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		at.setToTranslation(width/2, height/2);
		at.rotate(Math.toRadians(angle));
		g2d.transform(at);
		g2d.setColor(color);
		g2d.fillOval(-width/2, -height/2, width, height);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(-width/2, -height/2, width, (int)(height/1.5));
	}
	
	public void setColor(Color color) {
		this.color = color;
		this.repaint();
	}

	public Color getColor() {
		return color;
	}
	
	public void setAngle(int angle) {
		this.angle = angle;
		this.repaint();
	}

}