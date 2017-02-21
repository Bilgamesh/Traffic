package sprites.trafficLight;

import java.awt.*;

public class TrafficLightSprite {
    private int width = 20;
    private int height = 20;
    private int angle = 0;
    private Color color = Color.RED;
    private int x = 0;
    private int y = 0;

    public void draw(Graphics g) {
        //updateVariables(trafficLightModel);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.rotate(Math.toRadians(angle), x, y);
        g2d.fillOval(x, y, height, width);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y, height, (int) (width / 1.5));
        g2d.rotate(-Math.toRadians(angle), x, y);
    }

    public void updateVariables(TrafficLightModel trafficLightModel) {
        color = trafficLightModel.getColor();
        x = trafficLightModel.getX();
        y = trafficLightModel.getY();
        angle = trafficLightModel.getAngle();
    }

    public void setColor(Color color) {
        this.color = color;
    }

}