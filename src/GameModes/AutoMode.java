package gamemodes;

import gui.steeringWheel.SteeringWheel;
import sprites.car.CarModel;
import sprites.trafficLight.TrafficLightModel;

import java.awt.*;
import java.awt.event.MouseEvent;

public class AutoMode implements GameMode {
    private final CarModel[] carModels;
    private final TrafficLightModel[] trafficLightModels;
    private boolean changeTrafficLights;
    private long timeBackup;

    public AutoMode(CarModel[] carModels, TrafficLightModel[] trafficLightModels) {
        this.carModels = carModels;
        this.trafficLightModels = trafficLightModels;
        changeTrafficLights = false;
        timeBackup = System.currentTimeMillis() - 6000;
        for (TrafficLightModel trafficLightModel : trafficLightModels) {
            trafficLightModel.allowToChange(true);
        }
        trafficLightModels[0].setColor(Color.GREEN);
        trafficLightModels[1].setColor(Color.GREEN);
        trafficLightModels[2].setColor(Color.RED);
        trafficLightModels[3].setColor(Color.RED);
    }

    public void advance() {
        moveCars();
        initiateChangingLights();
        finishChangingLight();
    }

    public void pickClickedCar(MouseEvent e, SteeringWheel steeringWheel) {

    }

    private void moveCars() {
        for (CarModel carModel : carModels) {
            carModel.moveAhead();
            carModel.avoidCollision();
            carModel.accelerateIfNoObstacles();
            carModel.stopAtRedLight();
            carModel.turnRightIfAtPosition();
            carModel.respawnAfterLeavingMap();
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