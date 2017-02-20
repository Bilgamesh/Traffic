import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SpritesView extends JPanel {
    private final Map map;
    private TrafficLightSprite[] trafficLightSprites;
    private TrafficLightModel[] trafficLightModels;
    private int amountOfCars;
    private CarModel[] carModels;
    private CarSprite[] carSprites;
    private GameMode gameMode;


    public SpritesView(Map map) {
        setSize(map.getSize());
        setOpaque(false);
        setLayout(null);

        this.amountOfCars = 24;
        this.map = map;

        declareNewTrafficLights();
        declareNewCars(amountOfCars);
        spawnCarsAtDefaultPositions();
        spawnTrafficLightsAtDefaultPositions();
    }

    private void declareNewCars(int amountOfCars) {
        carModels = new CarModel[amountOfCars];
        carSprites = new CarSprite[amountOfCars];
        for (int i = 0; i < amountOfCars; i++) {
            carModels[i] = new CarModel();
            carModels[i].setRandomColor();
            carModels[i].setOtherCars(carModels);
            carModels[i].setMap(map);
            carSprites[i] = new CarSprite();
        }
    }

    private void declareNewTrafficLights() {
        trafficLightModels = new TrafficLightModel[4];
        trafficLightSprites = new TrafficLightSprite[4];
        for (int i = 0; i < 4; i++) {
            trafficLightModels[i] = new TrafficLightModel();
            trafficLightSprites[i] = new TrafficLightSprite();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < amountOfCars; i++) {
            carSprites[i].updateVariables(carModels[i]);
            carSprites[i].drawAll(g);
        }
        for (int i = 0; i < 4; i++) {
            trafficLightSprites[i].draw(g, trafficLightModels[i]);
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