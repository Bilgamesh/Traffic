import javax.swing.*;

public class Window extends JFrame {
    private final GUI gui;
    private final Map map;
    private final MovingCars cars;
    private final TrafficLightsOnMap trafficLights;

    public Window() {

        setSize(800, 600);
        setLayout(null);
        setResizable(false);
        setTitle("Traffic simulator");

        map = new Map();
        trafficLights = new TrafficLightsOnMap(map);
        cars = new MovingCars(24, map, trafficLights);
        gui = new GUI(map, cars, trafficLights);

        add(gui);
        add(trafficLights);
        add(cars);
        add(map);

        map.setLayout(null);
        map.setSize(800, 600);
        cars.setSize(800, 600);
        cars.setOpaque(false);
        cars.setLayout(null);
        trafficLights.setSize(800, 600);
        trafficLights.setOpaque(false);
        trafficLights.setLayout(null);
        gui.setSize(800, 600);
        gui.setOpaque(false);
        gui.setLayout(null);

    }
}
