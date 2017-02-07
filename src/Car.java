import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Car extends JPanel {
    private int width, height, frameWidth, respawnX, respawnY, respawnAngle, turnLine;
    private double angle, carX, carY, speed;
    private Color carColor;
    private Random rd;
    private boolean isGoingToTurn;

    public Car() {
        angle = 0;
        width = 70;
        height = 30;
        isGoingToTurn = false;
        carColor = Color.YELLOW;
        frameWidth = ((int) Math.sqrt(Math.pow(width, 2) + Math.pow(height / 2, 2)) + 1) * 2; // width of the frame >= the diagonal line of the car rectangle
        speed = 0.3;
        rd = new Random();
        setCarPosition(0, 0, 0);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform rotation = new AffineTransform();
        rotation.setToTranslation(frameWidth / 2, frameWidth / 2);
        rotation.rotate(Math.toRadians(angle));
        g2d.transform(rotation);
        Rectangle carRec = new Rectangle(0, -height / 2, width, height);
        BufferedImage carImage = LoadImage("src//car.png");
        g2d.setColor(carColor);
        g2d.fill(carRec);
        g2d.drawImage(carImage, 0, -height / 2, null);
    }

    BufferedImage LoadImage(String FileName) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(FileName));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return img;

    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public void setTurnLine(int turnLine) {
        this.turnLine = turnLine;
    }

    // Returns the width of the car rectangle (not of the frame).
    public int getCarWidth() {
        return width;
    }

    // Returns the height of the car rectangle (not of the frame).
    public int getCarHeight() {
        return height;
    }

    // Returns the 'X' coordinate of the car rectangle (not of the frame).
    public int getCarX() {
        return getX() + frameWidth / 2;
    }

    // Returns the 'Y' coordinate of the car rectangle (not of the frame).
    public int getCarY() {
        return getY() + frameWidth / 2;
    }

    // This method returns the Y coordinate of the car's front edge
    public double getCarFrontY() {
        return getCarY() + (width * Math.sin(Math.toRadians(angle)));
    }

    // This method returns the X coordinate of the car's front edge
    public double getCarFrontX() {
        return getCarX() + (width * Math.cos(Math.toRadians(angle)));
    }

    // Depending on car's angle this method returns the X or Y coordinate of the car's front edge
    public double getCarFront() {
        if (Math.abs(angle - 0) < 45 || Math.abs(angle - 360) < 45 || Math.abs(angle - 180) < 45)
            return getCarFrontX();
        else if (Math.abs(angle - 90) <= 45 || Math.abs(angle - 270) <= 45)
            return getCarFrontY();
        else
            return 0;
    }

    public void setRespawnPosition(int x, int y, int angle) {
        respawnX = x;
        respawnY = y;
        respawnAngle = angle;
    }

    public int getRespawnAngle() {
        return respawnAngle;
    }

    public int getRespawnX() {
        return respawnX;
    }

    public int getRespawnY() {
        return respawnY;
    }

    public void respawn() {
        setCarPosition(respawnX, respawnY, respawnAngle);
    }

    // This method returns true if none of the cars in the array parameter are located within a specified distance of this car's respawn coordinates
    public boolean hasEmptyRespawnArea(Car[] cars) {
        int range = 100;
        for (int i = 0; i < cars.length; i++) {
            if ((Math.abs(cars[i].getCarX() - getRespawnX()) < range)
                    && (Math.abs(cars[i].getCarY() - getRespawnY()) < range)
                    && (Math.abs(cars[i].getAngle() - getRespawnAngle()) < 35))
                return false;
        }
        return true;
    }

    public void setRandomSpeed() {
        setSpeed(rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2));
    }

    // This method returns true if the car is located 3 pixels before it's area of turn
    public boolean isAtTurningPosition() {
        return ((angle >= 0) && (angle < 90) && (getCarFrontX() > turnLine) && (getCarFrontX() < turnLine + 3))
                || ((angle >= 90) && (angle < 180) && (getCarFrontY() > turnLine) && (getCarFront() < turnLine + 3))
                || ((angle >= 180 && angle < 270) && (getCarFrontX() < turnLine) && (getCarFrontX() > turnLine - 3))
                || ((angle >= 270 && angle < 360) && (getCarFrontY() < turnLine) && (getCarFrontY() > turnLine - 3));
    }

    public void accelerateBy(double a) {
        speed = speed + a;
    }

    // This method returns true if the car is located outside of the visible map and is not directed towards it
    public boolean hasLeftMap(Map map) {
        return ((angle == 0 || angle == 360) && (getCarX() > map.getWidth())) || (angle == 90 && getCarY() > map.getHeight())
                || (angle == 180 && getCarX() < 0) || (angle == 270 && getCarY() < 0);
    }

    // This method returns true if the distance between the front side of this car and the rear side of the parameter-car is less than a specified value.
    public boolean isNearCarAtDistance(Car car, int distance) {
        return ((Math.abs(getCarFront() - car.getCarFront()) - getCarWidth()) < distance);
    }

    // This method returns true if this car is following the parameter-car within a specified distance
    // and if this car's speed is greater than parameter-car's speed at the same time
    public boolean isApproachingAnyOfCars(Car[] cars, int distance) {
        for (int i = 0; i < cars.length; i++) {
            if (isNearCarAtDistance(cars[i], distance) && (speed > cars[i].getSpeed()) && isBehind(cars[i]) && hasSimilarAngleTo(cars[i])) {
                return true;
            }
        }
        return false;
    }

    // This method returns true if the car's angle is different than any of the standard right angles,
    // which means the car is not going straight in it's line but is now turning
    public boolean isNowTurning() {
        return ((Math.abs(respawnAngle - angle) != 0) && Math.abs(respawnAngle - angle) != 90
                && Math.abs(respawnAngle - angle) != 180 && Math.abs(respawnAngle - angle) != 270 && Math.abs(respawnAngle - angle) != 360);
    }

    // This method returns true if the difference between this car's angle and parameter-car's angle is less than 45 degree
    public boolean hasSimilarAngleTo(Car car) {
        return (Math.abs(angle - car.getAngle()) < 45);
    }

    // This method returns true if the traffic light on this car's line is green
    public boolean hasGreenLight(TrafficLightsOnMap trafficLights) {
        if (isNowTurning())
            return true;
        else if ((Math.abs(angle) < 45 || Math.abs(angle - 180) < 45 || Math.abs(angle - 360) < 45))
            return trafficLights.isGreenX();
        else
            return trafficLights.isGreenY();
    }

    // This method returns true if the car has crossed it's line connection to the road intersection
    public boolean hasCrossedLine(Map map) {
        return ((angle == 0 || angle == 360) && getCarFrontX() > map.getLeftCrossLine())
                || (angle == 180 && getCarFrontX() < map.getRightCrossLine())
                || (angle == 90 && getCarFrontY() > map.getTopCrossLine())
                || (angle == 270 && getCarFrontY() < map.getBottomCrossLine());
    }

    // This method returns true if the car is near it's line connection to the road intersection
    public boolean isNearCrossing(Map map) {
        return (angle == 0 && getCarFrontX() < map.getLeftCrossLine() && getCarFrontX() > map.getLeftCrossLine() - 5)
                || (angle == 180 && getCarFrontX() > map.getRightCrossLine() && getCarFrontX() < map.getRightCrossLine() + 5)
                || (angle == 90 && getCarFrontY() < map.getTopCrossLine() && getCarFrontY() > map.getTopCrossLine() - 5)
                || (angle == 270 && getCarFrontY() > map.getBottomCrossLine() && getCarFrontY() < map.getBottomCrossLine() + 5);
    }

    // This method returns true if the parameter-car is ahead of this car and has a similar angle (difference < 90)
    public boolean isBehind(Car car) {
        if ((angle == 0 || angle == 360) && Math.abs(angle - car.getAngle()) < 90) {
            return (car.getCarX() > getCarX());
        } else if ((angle == 180) && Math.abs(angle - car.getAngle()) < 90) {
            return (car.getCarX() < getCarX());
        } else if ((angle == 90) && Math.abs(angle - car.getAngle()) < 90) {
            return (car.getCarY() > getCarY());
        } else if ((angle == 270) && Math.abs(angle - car.getAngle()) < 90) {
            return (car.getCarY() < getCarY());
        } else {
            return false;
        }
    }

    // This method returns true if the parameter-car is in front of this car within a specified distance
    public boolean isBehindAtDistance(Car car, int distance) {
        return (isBehind(car) && isNearCarAtDistance(car, distance));
    }

    // This method returns true if any of the cars in the 'cars' array is in front of this car within a specified distance
    public boolean isBehindAnyCarAtDistance(Car[] cars, int distance) {
        for (int i = 0; i < cars.length; i++) {
            if (isBehindAtDistance(cars[i], distance))
                return true;
        }
        return false;
    }

    // This method makes decreases the speed of the car. If the car reaches the intersection, the speed is set to 0;
    public void stopNearCrossing(Map map) {
        if (getSpeed() >= 0.3)
            accelerateBy(-0.01);
        if (getSpeed() <= 0.3 && isNearCrossing(map))
            setSpeed(0);
    }

    public void setCarColor(Color color) {
        carColor = color;
        repaint();
    }

    // This method increases the angle of the car by the value of'degree' variable.
    // If the angle get's very close (difference < 1 degree) to 90 degree difference from the default angle, it get's rounded into default value + 90 degree.
    public void rotateBy(double degree) {
        angle = angle + degree;
        if (Math.abs(angle - (respawnAngle + 90)) < 1)
            angle = respawnAngle + 90;
        repaint();
    }

    // Sets the angle and the position of the car rectangle (not of the frame) to a specified set of coordinates.
    public void setCarPosition(int x, int y, int angle) {
        setBounds(x - frameWidth / 2, y - frameWidth / 2, frameWidth, frameWidth);
        this.angle = angle;
        repaint();
        carX = x;
        carY = y;
    }

    // isGoingToTurn variable serves as an information whether this car will turn right on the road intersection or go straight ahead
    public boolean isGoingToTurn() {
        return isGoingToTurn;
    }

    // isGoingToTurn variable serves as an information whether this car will turn right on the road intersection or go straight ahead
    public void setGoingToTurn(boolean isGoingToTurn) {
        this.isGoingToTurn = isGoingToTurn;
    }

    // This method moves car rectangle (instead of the frame) by amount of pixels specified by the 'speed' variable.
    public void moveAhead() {
        carX = carX + speed * Math.cos(Math.toRadians(angle));
        carY = carY + speed * Math.sin(Math.toRadians(angle));
        setBounds((int) carX - frameWidth / 2, (int) carY - frameWidth / 2, frameWidth, frameWidth);
    }

    public void setRandomColor() {
        int number = rd.nextInt(10);
        Color color;
        switch (number) {
            case 1:
                color = Color.DARK_GRAY;
                break;
            case 2:
                color = Color.DARK_GRAY;
                break;
            case 3:
                color = Color.GRAY;
                break;
            case 4:
                color = Color.GREEN;
                break;
            case 5:
                color = Color.CYAN;
                break;
            case 6:
                color = Color.ORANGE;
                break;
            case 7:
                color = Color.ORANGE;
                break;
            case 8:
                color = Color.WHITE;
                break;
            case 9:
                color = Color.WHITE;
                break;
            default:
                color = Color.WHITE;
                break;
        }
        setCarColor(color);
    }

}
