package gamemodes;

import gui.steeringWheel.SteeringWheel;
import sprites.car.CarModel;
import sprites.trafficLight.TrafficLightModel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class RescueMode implements GameMode {
    private final CarModel[] carModels;
    private final TrafficLightModel[] trafficLightModels;
    private boolean changeTrafficLights;
    private long timeBackup;

    public RescueMode(CarModel[] carModels, TrafficLightModel[] trafficLightModels) {
        this.carModels = carModels;
        this.trafficLightModels = trafficLightModels;
        changeTrafficLights = false;
        timeBackup = System.currentTimeMillis();
        for (TrafficLightModel trafficLightModel : trafficLightModels) {
            trafficLightModel.allowToChange(true);
        }
        trafficLightModels[0].setColor(Color.GREEN);
        trafficLightModels[1].setColor(Color.GREEN);
        trafficLightModels[2].setColor(Color.RED);
        trafficLightModels[3].setColor(Color.RED);
    }

    public void pickClickedCar(MouseEvent e, SteeringWheel steeringWheel) {
        Rectangle clickedArea = new Rectangle(e.getX(), e.getY(), 2, 2);
        for (CarModel carModel : carModels) {
            if (carModel.collides(clickedArea))
                carModel.setUnderPlayerControl(true, steeringWheel);
            else if (carModel.isUnderPlayerControl())
                carModel.setUnderPlayerControl(false, null);
        }
    }

    public void changeTrafficLights() {
        if (!changeTrafficLights) {
            timeBackup = System.currentTimeMillis();
            changeTrafficLights = true;
        }
    }

    public void shakeRandomCar() {
        if (System.currentTimeMillis() - timeBackup > 4000) {
            timeBackup = System.currentTimeMillis();
            int a;
            do {
                a = new Random().nextInt(24);
            } while (carModels[a].isNowTurning() || !carModels[a].isOnMap() || !carModels[a].hasGreenLight());
            carModels[a].setShaking(true);
        }
    }

    public void advance() {
        moveCars();
        initiateChangingLights();
        finishChangingLight();
        shakeRandomCar();
    }

    private void moveCars() {
        for (CarModel carModel : carModels) {
            carModel.moveAhead();
            carModel.avoidCollision();
            carModel.accelerateIfNoObstacles();
            carModel.stopAtRedLight();
            carModel.turnRightIfAtPosition();
            carModel.respawnAfterLeavingMap();
            carModel.obeyIfUnderPlayerControl();
            carModel.shake();
        }
    }

    private void initiateChangingLights() {
        if (System.currentTimeMillis() - timeBackup > 14000) {
            timeBackup = System.currentTimeMillis();
            changeTrafficLights = true;
        }
    }

    private void finishChangingLight() {
        if (changeTrafficLights) {
            for (TrafficLightModel trafficLightModel : trafficLightModels) {
                trafficLightModel.changeGreenIntoYellow();
                if (Math.abs(System.currentTimeMillis() - timeBackup - 3000) < 1000 && trafficLightModel.getColor() == Color.YELLOW) {
                    trafficLightModel.changeYellowIntoRed();
                    trafficLightModel.allowToChange(false);
                }
                if (System.currentTimeMillis() - timeBackup > 4000)
                    trafficLightModel.changeRedIntoYellow();
                if (System.currentTimeMillis() - timeBackup > 6000) {
                    trafficLightModel.changeYellowIntoGreen();
                    trafficLightModel.allowToChange(true);
                    changeTrafficLights = false;
                }
            }
        }
    }
}