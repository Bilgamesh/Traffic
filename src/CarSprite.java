import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

public class CarSprite {
    private final Image carOutline = LoadImage("src//car.png");
    private Color carColor = Color.WHITE;
    private Double angle = 0.0;
    private int x = 0;
    private int y = 0;
    private int width = 70;
    private int height = 30;
    private Rectangle carRec = new Rectangle(x, y - height / 2, width, height);
    private Boolean isGoingToTurn = false;
    private AffineTransform at = new AffineTransform();

    public void updateVariables(CarModel carModel) {
        carColor = carModel.getCarColor();
        angle = carModel.getAngle();
        x = carModel.getX();
        y = carModel.getY();
        height = carModel.getHeight();
        width = carModel.getWidth();
        carRec = carModel.getCarRectangle();
        isGoingToTurn = carModel.isGoingToTurn();
        carModel.setAt(at);
    }

    public void drawAll(Graphics g) {
        drawCar(g);
        drawTurnSignals(g);
    }

    private void drawCar(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(carColor);
        g2d.rotate(Math.toRadians(angle), x, y);
        g2d.fill(carRec);
        g2d.drawImage(carOutline, x, y - height / 2, null);
        g2d.setColor(Color.RED);
        at = g2d.getTransform();
        g2d.rotate(-Math.toRadians(angle), x, y);
    }

    private void drawArrow(Graphics g) {

    }

    private void drawTurnSignals(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.rotate(Math.toRadians(angle), x, y);
        if (isGoingToTurn && (int) (System.currentTimeMillis() / 500) % 2 == 0) {
            g2d.fillOval(x + width, y + height / 2, 5, 5);
            g2d.fillOval(x - 5, y + height / 2, 5, 5);
        }
        g2d.rotate(-Math.toRadians(angle), x, y);
    }


    private Image LoadImage(String FileName) {
        Image img = null;
        try {
            img = ImageIO.read(new File(FileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

}
