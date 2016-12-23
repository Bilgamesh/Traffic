import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Main extends JFrame implements ActionListener {
	private Timer timer;
	private Car car1, car2, car3, car4, car5, car6;
	private double[] speed;
	private Random rd;
	private CarControl carControl1, carControl2, carControl3, carControl4, carControl5, carControl6;
	
	public Main() {
		setSize(800, 600);
		setLayout(null);
		setResizable(false);
		setBackground(Color.RED);
		
		rd = new Random();
		
		speed = new double[10];
		for (int i = 0; i < 10; i++)
			speed[i] = rd.nextDouble() + 1;
		
		car1 = new Car();
		car1.setOpaque(false);
		carControl1 = new CarControl(car1);
		carControl1.setCarPosition(450, -100, 90);
		add(car1);
		
		car2 = new Car();
		car2.setOpaque(false);
		carControl2 = new CarControl(car2);
		carControl2.setCarPosition(450, 0, 90);
		add(car2);
		
		car3 = new Car();
		car3.setOpaque(false);
		carControl3 = new CarControl(car3);
		carControl3.setCarPosition(450, 100, 90);
		add(car3);
		
		car4 = new Car();
		car4.setOpaque(false);
		carControl4 = new CarControl(car4);
		carControl4.setCarPosition(500, 430, 90);
		add(car4);
		
		car5 = new Car();
		car5.setOpaque(false);
		carControl5 = new CarControl(car5);
		carControl5.setCarPosition(500, 330, 90);
		add(car5);
		
		car6 = new Car();
		car6.setOpaque(false);
		carControl6 = new CarControl(car6);
		carControl6.setCarPosition(500, 230, 90);
		add(car6);
		
		
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
		if (z == timer) {
			carControl1.increaseAngleBy(0.2*Math.cos(car1.getY()));
			carControl1.goWithSpeed(speed[0]);
			carControl2.goWithSpeed(speed[1]);
			carControl3.goWithSpeed(speed[2]);
			
			if (car1.getY() > 600) {
				carControl1.setCarPosition(450, -100, 90);
				speed[0] = rd.nextDouble() + 1;
			}
			if (car2.getY() > 600) {
				carControl2.setCarPosition(450, -200, 90);
				speed[1] = rd.nextDouble() + 1;
			}
			if (car3.getY() > 600) {
				carControl3.setCarPosition(450, -300, 90);
				speed[2] = rd.nextDouble() + 1;
			}
			
			if (car1.isCloseY(car2, 100)) {
				if (speed[0] > speed[1])
					speed[0] = speed[1];
				else
					speed[1] = speed[0];
			}
			if (car2.isCloseY(car3, 100)) {
				if (speed[1] > speed[2])
					speed[1] = speed[2];
				else
					speed[2] = speed[1];
			}
			if (car1.isCloseY(car3, 100)) {
				if (speed[0] > speed[2])
					speed[0] = speed[2];
				else
					speed[2] = speed[0];
			}
		}
		
	}
	
}


