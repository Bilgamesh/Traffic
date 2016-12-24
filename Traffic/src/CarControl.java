import java.awt.Color;
import java.util.Random;

public class CarControl {
	private double angle, Yposition, Xposition;
	private Car car;
	
	public CarControl(Car car) {
		this.car = car;
		Xposition = car.getCarX();
		Yposition = car.getCarY();
		angle = 0;
	}

	public void moveByAmount(double delta) {
		Xposition = Xposition + delta * Math.cos(Math.toRadians(car.getAngle()));
		Yposition = Yposition + delta * Math.sin(Math.toRadians(car.getAngle()));
		car.setBounds((int)Xposition-car.getFrameWidth()/2, (int)Yposition-car.getFrameWidth()/2, car.getFrameWidth(), car.getFrameWidth());
	}
	
	public void goSlowlyWithSpeed(double delta) {
		Xposition = Xposition + delta * 0.6 * Math.cos(Math.toRadians(car.getAngle()));
		Yposition = Yposition + delta * 0.6 * Math.sin(Math.toRadians(car.getAngle()));
		car.setBounds((int)Xposition-car.getWidth()/2, (int)Yposition-car.getHeight()/2, car.getFrameWidth(), car.getFrameWidth());
	}

	public  void increaseAngleBy(double amount) {
		angle = angle + amount;
		car.setAngle((int)angle);
		car.repaint();
	}
	
	public  void setCarPosition(int x, int y, int angle) {
		car.setBounds(x-car.getFrameWidth()/2, y-car.getFrameWidth()/2, car.getFrameWidth(), car.getFrameWidth());
		car.setAngle(angle);
		car.repaint();
		Xposition = x;
		Yposition = y;
		this.angle = angle;
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
				color = Color.GREEN;
				break;
			case 3:
				color = Color.BLUE;
				break;
			case 4:
				color = Color.GREEN;
				break;
			case 5:
				color = Color.GRAY;
				break;
			case 6:
				color = Color.ORANGE;
				break;
			case 7:
				color = Color.YELLOW;
				break;
			case 8:
				color = Color.RED;
				break;
			case 9:
				color = Color.DARK_GRAY;
				break;
			default:
				color = Color.DARK_GRAY;
				break;
		}
		car.setCarColor(color);	
	}

}
