package sprites.view;

import gamemodes.GameMode;
import map.Map;
import sprites.car.CarSprite;
import sprites.trafficLight.TrafficLightSprite;

import javax.swing.*;
import java.awt.*;

public class SpritesView extends JPanel {
    private TrafficLightSprite[] trafficLightSprites;
    private int amountOfCars;
    private CarSprite[] carSprites;
    private GameMode gameMode;


    public void initiateSprites(Map map, int amountOfCars) {
        setSize(map.getSize());
        setOpaque(false);
        setLayout(null);

        this.amountOfCars = amountOfCars;

        declareNewTrafficLightSprites();
        declareNewCarSprites(amountOfCars);
    }

    public CarSprite[] getCarSprites() {
        return carSprites;
    }

    public TrafficLightSprite[] getTrafficLightSprites() {
        return trafficLightSprites;
    }

    private void declareNewCarSprites(int amountOfCars) {
        carSprites = new CarSprite[amountOfCars];
        for (int i = 0; i < amountOfCars; i++) {
            carSprites[i] = new CarSprite();
        }
    }

    private void declareNewTrafficLightSprites() {
        trafficLightSprites = new TrafficLightSprite[4];
        for (int i = 0; i < 4; i++) {
            trafficLightSprites[i] = new TrafficLightSprite();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < amountOfCars; i++) {
            carSprites[i].drawAll(g);
        }
        for (int i = 0; i < 4; i++) {
            trafficLightSprites[i].draw(g);
        }
    }
}