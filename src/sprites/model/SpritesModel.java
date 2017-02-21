package sprites.model;

import gamemodes.AutoMode;
import gamemodes.GameMode;
import gamemodes.RescueMode;
import gui.steeringWheel.SteeringWheel;
import map.Map;
import sprites.car.CarModel;
import sprites.car.CarSprite;
import sprites.trafficLight.TrafficLightModel;
import sprites.trafficLight.TrafficLightSprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SpritesModel extends JPanel {
    private Map map;
    private TrafficLightModel[] trafficLightModels;
    private int amountOfCars;
    private CarModel[] carModels;
    private GameMode gameMode;


    public void initiateSpriteModels(Map map, int amountOfCars) {
        setSize(map.getSize());
        setOpaque(false);
        setLayout(null);

        this.amountOfCars = amountOfCars;
        this.map = map;

        declareNewTrafficLights();
        declareNewCars();
        spawnCarsAtDefaultPositions();
        spawnTrafficLightsAtDefaultPositions();
    }

    public void updateSpritesWithModels(CarSprite[] carSprites, TrafficLightSprite[] trafficLightSprites) {
        for (int i = 0; i < amountOfCars; i++) {
            carSprites[i].updateVariables(carModels[i]);
        }
        for (int i = 0; i < trafficLightSprites.length; i++) {
            trafficLightSprites[i].updateVariables(trafficLightModels[i]);
        }
    }

    private void declareNewCars() {
        carModels = new CarModel[amountOfCars];
        for (int i = 0; i < amountOfCars; i++) {
            carModels[i] = new CarModel();
            carModels[i].setRandomColor();
            carModels[i].setOtherCars(carModels);
            carModels[i].setMap(map);
        }
    }

    private void declareNewTrafficLights() {
        trafficLightModels = new TrafficLightModel[4];
        for (int i = 0; i < 4; i++) {
            trafficLightModels[i] = new TrafficLightModel();
        }
    }

    public void loadAutoMode() {
        gameMode = new AutoMode(carModels, trafficLightModels);
        spawnCarsAtDefaultPositions();
    }

    public void loadRescueMode() {
        gameMode = new RescueMode(carModels, trafficLightModels);
        spawnCarsAtDefaultPositions();
    }

    public void pickClickedCar(MouseEvent e, SteeringWheel steeringWheel) {
        gameMode.pickClickedCar(e, steeringWheel);
    }

    public void advance() {
        gameMode.advance();
    }

    /* In this method cars are divided into four groups. Each group gets it's own line on the road, thus each group has it's own coordinates */
    private void spawnCarsAtDefaultPositions() {
        for (int i = 0; i < amountOfCars / 4; i++) {
            carModels[i].setOnVerticalLeftLine(i);
            carModels[i].setTrafficLights(trafficLightModels[0]);
        }
        for (int i = amountOfCars / 4; i < amountOfCars / 2; i++) {
            carModels[i].setOnVerticalRightLine(i);
            carModels[i].setTrafficLights(trafficLightModels[1]);
        }
        for (int i = amountOfCars / 2; i < amountOfCars * 3 / 4; i++) {
            carModels[i].setOnHorizontalTopLine(i);
            carModels[i].setTrafficLights(trafficLightModels[2]);
        }
        for (int i = amountOfCars * 3 / 4; i < amountOfCars; i++) {
            carModels[i].setOnHorizontalBottomLine(i);
            carModels[i].setTrafficLights(trafficLightModels[3]);
        }
        for (CarModel carModel : carModels)
            carModel.setUnderPlayerControl(false, null);
    }

    private void spawnTrafficLightsAtDefaultPositions() {
        trafficLightModels[0].setPosition(368, 63, 180);
        trafficLightModels[0].setColor(Color.GREEN);
        trafficLightModels[1].setPosition(533, 230, 0);
        trafficLightModels[1].setColor(Color.GREEN);
        trafficLightModels[2].setPosition(533, 63, 270);
        trafficLightModels[2].setColor(Color.RED);
        trafficLightModels[3].setPosition(368, 230, 90);
        trafficLightModels[3].setColor(Color.RED);
    }
}