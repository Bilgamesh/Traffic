import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MovingCars extends JPanel implements ActionListener {
    private int amountOfCars;
    private Car[] car;
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

        passRedLightSpeed = 1.3;
        rd = new Random();
        this.trafficLights = trafficLights;

        car = new Car[amountOfCars];
        for (int i = 0; i < car.length; i++) {
            car[i] = new Car();
            car[i].setOpaque(false);
            car[i].setRandomColor();
            add(car[i]);
        }
        spawnCarsAtDefaultPositions();

        timer = new Timer(10,this);
        timer.addActionListener(this);
        timer.start();
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

//    public void addOnMap(Map map) {
//        for (int i = 0; i < car.length; i++) {
//        }
//    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object z = e.getSource();
        if (z == timer) {
            for (int i = 0; i < car.length; i++) {
                car[i].moveAhead();

                int frontCarNumber;
                if (i == 0)
                    frontCarNumber = (car.length / 4) - 1;
                else if (i == car.length / 4)
                    frontCarNumber = (car.length / 2) - 1;
                else if (i == car.length / 2)
                    frontCarNumber = (car.length * 3 / 4) - 1;
                else if (i == car.length * 3 / 4)
                    frontCarNumber = car.length - 1;
                else
                    frontCarNumber = i - 1;

                if (car[i].hasLeftMap() && car[frontCarNumber].isOnMap() && car[frontCarNumber].getAngle() == car[frontCarNumber].getRespawnAngle()) {
                    car[i].respawn();
                    car[i].setRandomSpeed();
                    car[i].setRandomColor();
                }
                if (car[i].isFollowing(car[frontCarNumber], 110)) {
                    car[i].moveAsSlowlyAs(car[frontCarNumber]);
                }
                if ( (!car[i].isFollowing(car[frontCarNumber], 110))
                        && ((car[i].getSpeed() > passRedLightSpeed) || (isLightGreenFor(car[i])) || (car[i].hasCrossedLine(map)))
                        && ((!car[i].isGoingToTurn()) || (car[i].getSpeed() < 0.7)) ) {
                    car[i].accelerateBy(0.007);
                }

                // stops cars when lights are changing
                if ( (!isLightGreenFor(car[i])) && (!car[i].hasCrossedLine(map))
                        && (car[i].getSpeed() < passRedLightSpeed) ) {
                    car[i].stopNearCrossing(map);
                }
            }

            for (int i = 0; i < car.length; i = i + car.length/4) {
                if ((car[i].isAtTurningPosition()  || car[i].getAngle() > car[i].getRespawnAngle()) && (car[i].getAngle() < car[i].getRespawnAngle() + 90) && (car[i].getSpeed() < 0.8)) {
                    car[i].rotateBy(car[i].getSpeed()/1.5);
                }
                if (haveAllCarsLeft(car, i, i+(car.length/4 - 1))) {
                    car[i].respawn();
                    car[i].setRandomSpeed();
                    car[i].setRandomColor();
                }

                if (car[i].getSpeed() < 0.8 )
                    car[i].setGoingToTurn(true);
                else
                    car[i].setGoingToTurn(false);
            }
        }// end of timer

        if (z == gui.getRestartButton()) {
            restart();
        }
    }

    public void setAmountOfCars(int newAmountOfCars) {
        this.amountOfCars = newAmountOfCars;
    }

    public void restart() {
        for (int i = 0; i < car.length; i++) {
            car[i].setVisible(false);
        }
        car = new Car[amountOfCars];
        for (int i = 0; i < car.length; i++) {
            car[i] = new Car();
            car[i].setRandomColor();
            car[i].setOpaque(false);
            add(car[i]);
        }
        spawnCarsAtDefaultPositions();
        timer.restart();
    }

    public void spawnCarsAtDefaultPositions() {
        for (int i = 0; i < car.length/4; i++) {									// vertical left line
            car[i].setCarPosition(400, 600 - 100 * i, 90);
            car[i].setRespawnPosition(400, -100, 90);
            car[i].setRandomSpeed();
        }
        for (int i = car.length/4; i < car.length/2; i++) {						// vertical right line
            car[i].setCarPosition(500, 100*i, 270);
            car[i].setRespawnPosition(500, 750, 270);
            car[i].setRandomSpeed();
        }
        for (int i = car.length/2; i < car.length * 3 / 4; i++) {				// horizontal top line
            car[i].setCarPosition(map.getRightCrossLine() + 100 + (i-car.length/2)*(car[i].getCarWidth()+20), 95, 180);
            car[i].setRespawnPosition(1000, 95, 180);
            car[i].setSpeed(0.4);
        }
        for (int i = car.length * 3 / 4; i < car.length; i++) {					// horizontal bottom line
            car[i].setCarPosition(map.getLeftCrossLine() - 100 - (i-car.length*3/4)*(car[i].getCarWidth()+20), 195, 0);
            car[i].setRespawnPosition(-150, 195, 0);
            car[i].setSpeed(0.4);
        }
        car[0].setTurnLine(map.getTopCrossLine() + 27);
        car[car.length/4].setTurnLine(map.getBottomCrossLine() - 30);
        car[car.length/2].setTurnLine(map.getRightCrossLine() - 30);
        car[car.length*3/4].setTurnLine(map.getLeftCrossLine() + 30);
    }

    private boolean haveAllCarsLeft(Car[] car, int firstCar, int lastCar) {
        return howManyCarsLeftMap(car, firstCar, lastCar) == lastCar + 1 - firstCar;
    }

    public void stopTimer() {
        timer.stop();
    }

    public void startTimer() {
        timer.start();
    }

    public void setTrafficLights(TrafficLightsOnMap trafficLights) {
        this.trafficLights = trafficLights;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    private int howManyCarsLeftMap(Car[] car, int firstCar, int lastCar) {
        int counter = 0;
        for (int i = firstCar; i <= lastCar; i++)
            if (car[i].hasLeftMap())
                counter++;
        return counter;
    }

    private boolean isLightGreenFor(Car car) {
        if ((car.getAngle() == 0 || car.getAngle() == 180 || car.getAngle() == 360) && (!car.isGoingToTurn() || !car.hasCrossedLine(map)))
            return trafficLights.isGreenX();
        else
            return !((car.getAngle() == 90 || car.getAngle() == 270) && (!car.isGoingToTurn() || !car.hasCrossedLine(map))) || trafficLights.isGreenY();
    }
}
