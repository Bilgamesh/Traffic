import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class CarControl {
	private double angle, Yposition, Xposition, backupX, backupY, backupAngle;
	private Car car;
	private int width, height;
	
	public CarControl(Car car) {
		this.car = car;
		width = 153;
		height = 153;
	}

	public void goWithSpeed(double amount) {
		Xposition = Xposition + amount * Math.cos(Math.toRadians(car.getAngle()));
		Yposition = Yposition + amount * Math.sin(Math.toRadians(car.getAngle()));
		car.setBounds((int)Xposition, (int)Yposition, width, height);
	}
	
	public  void increaseAngleBy(double amount) {
		angle = angle + amount;
		car.setAngle((int)angle);
		car.repaint();
	}
	
	public  void setCarPosition(int x, int y, int angle) {
		car.setBounds(x, y, width, height);
		car.setAngle(angle);
		car.repaint();
		Xposition = x;
		Yposition = y;
		this.angle = angle;
	}

}
