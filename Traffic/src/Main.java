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
	private Car[] car;
	private double[] speed;
	private Random rd;
	private CarControl[] carControl;
	private ImageIcon backgroundImage;
	private JLabel backgroundLabel;
	private JButton trafficLightButton;
	private final int amountOfCars, topCrossing, bottomCrossing;
	private boolean greenX, greenY;
	
	public Main() {
		setSize(800, 600);
		setLayout(null);
		setResizable(false);
		backgroundImage = new ImageIcon(getClass().getResource("background.png"));
		backgroundLabel = new JLabel(backgroundImage);
		backgroundLabel.setBounds(0, 0, 800, 600);
		add(backgroundLabel);
		
		topCrossing = -7;
		bottomCrossing = 290;
		
		greenX = false;
		greenY = true;
		
		trafficLightButton = new JButton("Zmiana œwiate³");
		trafficLightButton.setBounds(100, 500, 200, 30);
		trafficLightButton.addActionListener(this);
		backgroundLabel.add(trafficLightButton);
		
		rd = new Random();
		
		speed = new double[10];
		for (int i = 0; i < 10; i++) 
			speed[i] = (rd.nextDouble() + 1);
		
		car = new Car[10];
		carControl = new CarControl[10];
		
		amountOfCars = 10;
		for (int i = 0; i < amountOfCars; i++) {
			car[i] = new Car();
			car[i].setOpaque(false);
			carControl[i] = new CarControl(car[i]);
			carControl[i].setRandomColor();
			if (i < amountOfCars/2)
				carControl[i].setCarPosition(400, 100*i, 90);
			else
				carControl[i].setCarPosition(500, 100*(9-i)+car[i].getCarWidth(), 270);
			backgroundLabel.add(car[i]);		
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
		if (z == timer) {
			
			for (int i = 0; i < amountOfCars; i++) {
				carControl[i].moveByAmount(speed[i]);
				
				if (i == 4) {
					if (car[4].getCarY() > 600 && car[0].getCarY() > 0 && car[0].getCarY() < 600) {
						carControl[4].setCarPosition(400, -100, 90);
						speed[4] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
						carControl[4].setRandomColor();
					}
					if (car[4].isCloseY(car[0], 100 + speed[4] * 10)) {
						if (speed[4] > speed[0])
							speed[4] = speed[4] - 0.03;
						else if (speed[0] == 0)
							speed[4] = 0;
					}
					else if (speed[4] < 0.7 && (greenY == true || car[4].getCarY() >= topCrossing))
						speed[4] = speed[4] + 0.007;
				}
				else if (i < 5 && i != 4) {
					if (car[i].getCarY() > 600 && car[i+1].getCarY() > 0 && car[i+1].getCarY() < 600) {
						carControl[i].setCarPosition(400, -100, 90);
						speed[i] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
						carControl[i].setRandomColor();
					}
					if (car[i].isCloseY(car[i+1], 100 + speed[i] * 10)) {
						if (speed[i] > speed[i+1] && speed[i+1] != 0)
							speed[i] = speed[i] - 0.03;
						else if (speed[i+1] == 0)
							speed[i] = 0;
					}
					else if (speed[i] > 2 || greenY == true || car[i].getCarY() >= topCrossing)
						speed[i] = speed[i] + 0.007;
				}
				else if (i == 9) {
					if (car[9].getCarY() < 0 && car[amountOfCars/2].getCarY() > 0 && car[amountOfCars/2].getCarY() < 600) {
						carControl[9].setCarPosition(500, 750, 270);
						speed[9] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
						carControl[9].setRandomColor();
					}
					if (car[9].isCloseY(car[5], 100 + speed[9] * 10)) {
						if (speed[9] > speed[5])
							speed[9] = speed[9] - 0.03;
						else if (speed[5] == 0)
							speed[9] = 0;
					}
					else if (speed[9] < 0.7 && (greenY == true || car[i].getCarY() <= bottomCrossing))
						speed[9] = speed[9] + 0.007;
				}
				else if (i >= 5 && i != 9) {
					if (car[i].getCarY() < 0 && car[i+1].getCarY() > 0 && car[i+1].getCarY() < 600) {
						carControl[i].setCarPosition(500, 750, 270);
						speed[i] = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
						carControl[i].setRandomColor();
					}
					if (car[i].isCloseY(car[i+1], 100 + speed[i] * 10)) {
						if (speed[i] > speed[i+1])
							speed[i] = speed[i] - 0.03;
						else if (speed[i+1] == 0)
							speed[i] = 0;
					}
					else if (speed[i] > 2 || greenY == true || car[i].getCarY() <= bottomCrossing)
						speed[i] = speed[i] + 0.007;
				}
				
			}	
			
			if (greenY == false) {
				for (int i = 0; i < 5; i++) {
					if (car[i].getCarY() < topCrossing && speed[i] >= 0.8 && speed[i] < 2)
						speed[i] = Math.abs(speed[i] - 0.02);
					else if (car[i].getCarY() < topCrossing && car[i].getCarY() > topCrossing-3 && speed[i] <= 0.8)
						speed[i] = 0;
				}
				for (int i = 5; i < 10 ; i++) {
					if (car[i].getCarY() > bottomCrossing && speed[i] >= 0.8 && speed[i] < 2)
						speed[i] = Math.abs(speed[i] - 0.02);
					else if (car[i].getCarY() > bottomCrossing && car[i].getCarY() < bottomCrossing+5 && speed[i] <= 0.8)
						speed[i] = 0;
				}
			}
		
		}
		if (z == trafficLightButton) {
			if (greenY == true) {
				greenY = false;
				greenX = true;
			}
			else {
				greenY = true;
				greenX = false;
		}

	}
	}
	
}


