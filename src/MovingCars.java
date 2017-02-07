import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MovingCars extends JPanel implements ActionListener {
    private int amountOfCars, pointOfStop;
    private Car[] cars;
    private Timer timer;
    private double passRedLightSpeed;
    private Random rd;
    private TrafficLightsOnMap trafficLights;
    private Map map;
    private GUI gui;

    public MovingCars(int amountOfCars, Map map, TrafficLightsOnMap trafficLights) {
        this.amountOfCars = amountOfCars;
        this.map = map;
        this.trafficLights = trafficLights;

        passRedLightSpeed = 1.6;
        rd = new Random();
        this.trafficLights = trafficLights;

        cars = new Car[amountOfCars];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car();
            cars[i].setOpaque(false);
            cars[i].setRandomColor();
            add(cars[i]);
        }
        spawnCarsAtDefaultPositions();

        timer = new Timer(10, this);
        timer.setInitialDelay(0);
        timer.addActionListener(this);
        timer.start();
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object z = e.getSource();

        // The following block of code is performed every 10 milliseconds
        if (z == timer) {

            // The purpose of this loop is to perform the following code for every car
            for (Car car : cars) {

                car.moveAhead();

                // This block of code makes the car slow down when it reaches a certain distance from another car
                if (car.isApproachingAnyOfCars(cars, 40)) {
                    car.accelerateBy(-0.04);
                }

                // This block of code prevents cars from changing direction if speed gets negative
                if (car.getSpeed() < 0)
                    car.setSpeed(0);

                // This block of code accelerates cars if several conditions are met
                // 1. The car won't accelerate if there is another car in front of it
                // 2. It won't accelerate if the traffic light is red...
                // 3. ...unless the car has already entered the road intersection...
                // 4. ...or it's speed is so high that there's not enough time to react and stop (thus the passRedLightSpeed variable)...
                // 5. The car that is going to turn doesn't accelerate unless it's speed is very low (less than 0.7 pixels per 10 milliseconds)
                if ((!car.isBehindAnyCarAtDistance(cars, 40))
                        && (car.getSpeed() > passRedLightSpeed || car.hasGreenLight(trafficLights) || car.hasCrossedLine(map))
                        && (!car.isGoingToTurn() || car.getSpeed() < 0.7)) {
                    car.accelerateBy(0.003);
                }

                // This block of code makes cars slow down and stop when the traffic light is changing from green to red if several conditions are met
                // 1. The light for the car is no longer green
                // 2. The car hasn't entered the road intersection
                // 3. The car's speed is low enough so that there is enough time to react to the change of the traffic light
                if ((!car.hasGreenLight(trafficLights)) && (!car.hasCrossedLine(map)) && (car.getSpeed() < passRedLightSpeed)) {
                    car.stopNearCrossing(map);
                }

                // This block of code makes some cars turn right if following conditions are met
                // 1. The car has entered specified position at the road intersection
                // or it has left the position, but started turning (thus it's current angle is larger than it's default angle)
                // 2. Car's speed is low enough (less than 0.8 pixels per 10 milliseconds)
                if ((car.isAtTurningPosition() || (car.getAngle() > car.getRespawnAngle()))
                        && (car.isGoingToTurn())
                        && (car.getSpeed() < 0.8)) {
                    car.rotateBy(car.getSpeed() / 1.5);
                }
                if (car.isGoingToTurn() && car.getAngle() >= (car.getRespawnAngle() + 90)) {
                    car.setGoingToTurn(false); // After the 90 degree turn is performed, the car stops turning
                }

                // This block of code makes the cars that are going to turn right slow down
                if (car.isGoingToTurn() && !car.hasCrossedLine(map) && car.getSpeed() >= 0.8)
                    car.accelerateBy(-0.003);

                // This block of code makes every car respawn with random speed and new color
                // after the car has left the visible map. To prevent collisions this is performed only
                // if there is no other car in the respawn area.
                // This creates the illusion of new cars coming into the streets.
                // Every car gets a 1 in 3 chance of turning right
                if (car.hasLeftMap(map) && car.hasEmptyRespawnArea(cars)) {
                    car.respawn();
                    car.setRandomSpeed();
                    car.setRandomColor();
                    if (rd.nextInt(3) == 0)
                        car.setGoingToTurn(true);
                }
            }
        }// end of timer

        if (z == gui.getRestartButton()) {
            restart();
        }
    }

    public void setAmountOfCars(int newAmountOfCars) {
        this.amountOfCars = newAmountOfCars;
    }

    // This method removes current cars and creates new ones
    public void restart() {
        for (int i = 0; i < cars.length; i++) {
            cars[i].setVisible(false);
        }
        cars = new Car[amountOfCars];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car();
            cars[i].setRandomColor();
            cars[i].setOpaque(false);
            add(cars[i]);
        }
        spawnCarsAtDefaultPositions();
        timer.restart();
    }

    // In this method cars are divided into four groups. Each group gets it's own line on the road, thus each group has it's own coordinates
    public void spawnCarsAtDefaultPositions() {

        // vertical left line
        for (int i = 0; i < cars.length / 4; i++) {
            cars[i].setCarPosition(400, 600 - 100 * i, 90);
            cars[i].setRespawnPosition(400, -100, 90);
            cars[i].setRandomSpeed();
            cars[i].setTurnLine(map.getTopCrossLine() + 27);

        }

        // vertical right line
        for (int i = cars.length / 4; i < cars.length / 2; i++) {
            cars[i].setCarPosition(500, 100 * i, 270);
            cars[i].setRespawnPosition(500, 750, 270);
            cars[i].setRandomSpeed();
            cars[i].setTurnLine(map.getBottomCrossLine() - 30);

        }

        // horizontal top line
        for (int i = cars.length / 2; i < cars.length * 3 / 4; i++) {
            cars[i].setCarPosition(map.getRightCrossLine() + 100 + (i - cars.length / 2) * (cars[i].getCarWidth() + 20), 95, 180);
            cars[i].setRespawnPosition(1000, 95, 180);
            cars[i].setSpeed(0.4);
            cars[i].setTurnLine(map.getRightCrossLine() - 30);

        }

        // horizontal bottom line
        for (int i = cars.length * 3 / 4; i < cars.length; i++) {
            cars[i].setCarPosition(map.getLeftCrossLine() - 100 - (i - cars.length * 3 / 4) * (cars[i].getCarWidth() + 20), 195, 0);
            cars[i].setRespawnPosition(-150, 195, 0);
            cars[i].setSpeed(0.4);
            cars[i].setTurnLine(map.getLeftCrossLine() + 30);

        }
    }

}
