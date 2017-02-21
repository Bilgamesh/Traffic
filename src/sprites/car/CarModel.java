package sprites.car;

import gui.steeringWheel.SteeringWheel;
import map.Map;
import sprites.trafficLight.TrafficLightModel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.Random;

public class CarModel {
    private final int width = 70;
    private final int height = 30;
    private final Random rd = new Random();
    private final Rectangle carRec = new Rectangle(0, 0, width, height);
    private final Rectangle safetyRec = new Rectangle(0, 0, width, height);
    private final double passRedLightSpeed = 1.6;
    private double angle = 0;
    private double x = 0;
    private double y = 0;
    private double speed = 0.3;
    private double sin = 0;
    private double cos = 1;
    private int respawnX, respawnY, respawnAngle, turnLine;
    private Color carColor = Color.YELLOW;
    private boolean isGoingToTurn = false;
    private boolean isUnderPlayerControl = false;
    private boolean isShaking = false;
    private AffineTransform at = new AffineTransform();
    private CarModel[] otherCars;
    private Map map;
    private TrafficLightModel trafficLightModel;
    private SteeringWheel steeringWheel;

    public void setUnderPlayerControl(boolean arg, SteeringWheel steeringWheel) {
        isUnderPlayerControl = arg;
        if (arg)
            this.steeringWheel = steeringWheel;
    }

    public void setShaking(boolean arg) {
        isShaking = arg;
    }

    public void shake() {
        if (isShaking && !isUnderPlayerControl && Math.abs(respawnAngle - angle) < 30)
            rotateBy(0.01);
    }

    public boolean isUnderPlayerControl() {
        return isUnderPlayerControl;
    }

    public void setOnVerticalLeftLine(int positionNumber) {
        final int x = map.getWidth() / 2;
        final int y = map.getHeight() - (map.getHeight() / 6) * positionNumber;
        setCarPosition(x, y, 90);
        setRespawnPosition(x, -100, 90);
        setRandomSpeed();
        setTurnLine(map.getTopCrossLine() + 27);
    }

    public void setOnVerticalRightLine(int positionNumber) {
        final int x = (map.getWidth() / 2) + (map.getWidth() / 8);
        final int y = (map.getHeight() / 6) * positionNumber;
        setCarPosition(x, y, 270);
        setRespawnPosition(x, map.getHeight() + 150, 270);
        setRandomSpeed();
        setTurnLine(map.getBottomCrossLine() - 30);
    }

    public void setOnHorizontalTopLine(int positionNumber) {
        final int x = map.getRightCrossLine() + (map.getWidth() / 8) + (positionNumber - otherCars.length / 2) * (width * 2);
        final int respawnX = map.getWidth() + 200;
        final int y = (map.getHeight() / 6) - 5;
        setCarPosition(x, y, 180);
        setRespawnPosition(respawnX, y, 180);
        setSpeed(0.4);
        setTurnLine(map.getRightCrossLine() - 30);
    }

    public void setOnHorizontalBottomLine(int positionNumber) {
        final int x = map.getLeftCrossLine() - (map.getWidth() / 8) - (positionNumber - otherCars.length * 3 / 4) * (width * 2);
        final int y = (map.getHeight() / 3) - 5;
        setCarPosition(x, y, 0);
        setRespawnPosition(-150, y, 0);
        setSpeed(0.4);
        setTurnLine(map.getLeftCrossLine() + 30);
    }

    public void setOtherCars(CarModel[] otherCars) {
        this.otherCars = otherCars;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setTrafficLights(TrafficLightModel trafficLight) {
        this.trafficLightModel = trafficLight;
    }

    public void avoidCollision() {
        if (this.isApproachingAnyOf(otherCars) && speed > 0)
            this.accelerateBy(-0.04);
    }

    public void accelerateIfNoObstacles() {
        if ((!this.isApproachingAnyOf(otherCars)) &&
                (speed > passRedLightSpeed || hasGreenLight() || hasEnteredIntersection(map) || !isApproaching(map.getRoadIntersection())) &&
                (!isGoingToTurn || speed < 0.7)) {
            this.accelerateBy(0.003);
        }
    }

    public void stopAtRedLight() {
        if (!this.hasGreenLight() && System.currentTimeMillis() - trafficLightModel.getBackupTime() >= 1000)
            if (this.isApproaching(map.getRoadIntersection()) && !this.hasEnteredIntersection(map) && this.getSpeed() < passRedLightSpeed)
                this.accelerateBy(-0.04);
    }

    public void turnRightIfAtPosition() {
        if (!isUnderPlayerControl) {
            if ((isAtTurningPosition() || (angle > respawnAngle)) &&
                    (isGoingToTurn && speed < 0.8)) {
                this.rotateBy(speed / 1.5);
            }
            if (this.isGoingToTurn() && this.getAngle() >= (respawnAngle + 90)) {
                this.setGoingToTurn(false);      // After the 90 degree turn is performed, the car stops turning
            }
            if (this.isGoingToTurn() && !this.hasEnteredIntersection(map) && this.getSpeed() >= 0.8)
                this.accelerateBy(-0.003);
        }
    }

    public void respawnAfterLeavingMap() {
        if (this.hasLeftMap() && this.hasEmptyRespawnArea(otherCars)) {
            this.respawn();
            this.setRandomSpeed();
            this.setRandomColor();
            if (rd.nextInt(3) == 0)
                this.setGoingToTurn(true);
        }
    }

    private void updateSinAndCosValues() {
        sin = Math.sin(Math.toRadians(angle));
        cos = Math.cos(Math.toRadians(angle));
    }

    /*
     * This method returns true if in front of this car there is another car within a certain distance
     * (the required distance will vary depending on the speed variable)
     */
    public boolean isApproaching(CarModel carModel) {
        if (!isAtDistanceFrom(carModel.getX(), carModel.getY(), 300))     // there is no need to test for intersection if the distance is large
            return false;
        Area area1 = new Area(safetyRec);
        Area area2 = new Area(carModel.getCarRectangle());
        area1.transform(at);
        area2.transform(carModel.getAt());
        area1.intersect(area2);
        return !area1.isEmpty();
    }


    /*
     * This method returns true if in front of this car there is a rectangle object within a certain distance
     * (the required distance will vary depending on the speed variable)
     */
    public boolean isApproaching(Rectangle rec) {
        double distance = Math.max(rec.getWidth(), rec.getHeight()) + 200;
        if (!isAtDistanceFrom(rec.getX() + rec.getWidth() / 2, rec.getY() + rec.getHeight() / 2, distance))
            return false;                             // there is no need to test for intersection if the distance is large

        Area area = new Area(safetyRec);
        area.transform(at);
        return area.intersects(rec);
    }

    /*
     * This method returns true if in front of this car there is a car object from the 'cars' array parameter within a certain distance
     * (the required distance will vary depending on the speed variable)
     */
    public boolean isApproachingAnyOf(CarModel[] carModels) {
        for (CarModel carModel : carModels)
            if (isApproaching(carModel) && this != carModel)
                return true;
        return false;
    }

    public AffineTransform getAt() {
        return at;
    }

    public void setAt(AffineTransform at) {
        this.at = at;
    }

    public Rectangle getCarRectangle() {
        return carRec;
    }

    public Rectangle getSafetyRectangle() {
        return safetyRec;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setRandomSpeed() {
        speed = rd.nextDouble() + 1 - rd.nextDouble() * rd.nextInt(2);
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        updateSinAndCosValues();
    }

    /* the value of turnLine describes the position on the line the car has to reach in order to turn right */
    public void setTurnLine(int turnLine) {
        this.turnLine = turnLine;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return (int) x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getY() {
        return (int) y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /* This method returns the Y coordinate of the car's front edge, instead of the origin point which is at the rear side of the car */
    public double getCarFrontY() {
        return getY() + (width * sin);
    }

    /* This method returns the X coordinate of the car's front edge, instead of the origin point which is at the rear side of the car */
    public double getCarFrontX() {
        return getX() + (width * cos);
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
        isUnderPlayerControl = false;
        isShaking = false;
        setCarPosition(respawnX, respawnY, respawnAngle);
    }

    /*
     * This method returns true if none of the cars in the array parameter that have similar angle
     * are located within a 100 pixel distance of this car's respawn coordinates
     */
    public boolean hasEmptyRespawnArea(CarModel[] carModels) {
        int range = 100;
        for (CarModel carModel : carModels) {
            if ((Math.abs(carModel.getX() - getRespawnX()) < range) &&
                    (Math.abs(carModel.getY() - getRespawnY()) < range) &&
                    (Math.abs(carModel.getAngle() - getRespawnAngle()) < 35))
                return false;
        }
        return true;
    }

    /* This method returns true if the car is located within 3 pixel distance of the area in which the car turns right */
    public boolean isAtTurningPosition() {
        return ((angle >= 0) && (angle < 90) && (getCarFrontX() > turnLine) && (getCarFrontX() < turnLine + 3)) ||
                ((angle >= 90) && (angle < 180) && (getCarFrontY() > turnLine) && (getCarFrontY() < turnLine + 3)) ||
                ((angle >= 180 && angle < 270) && (getCarFrontX() < turnLine) && (getCarFrontX() > turnLine - 3)) ||
                ((angle >= 270 && angle < 360) && (getCarFrontY() < turnLine) && (getCarFrontY() > turnLine - 3));
    }

    /* This method moves the car directly ahead by the amount of pixels specified by the 'speed' variable. */
    public void moveAhead() {
        x += speed * cos;
        y += speed * sin;
        safetyRec.setBounds((int) x + width, (int) y - height / 2, (int) (width * (speed)) + 10, height);
        carRec.setLocation((int) x, (int) y - height / 2);
    }

    public void obeyIfUnderPlayerControl() {
        if (isUnderPlayerControl) {
            rotateBy((speed / 1.5) * steeringWheel.getAngle() / 90);
        }
    }

    public void accelerateBy(double a) {
        speed += a;
        if (speed < 0)
            speed = 0;
    }

    /* This method returns true if the car is located outside of the visible map and is not directed towards it */
    public boolean hasLeftMap() {
        if (isOnMap())
            return false;
        return ((getX() - map.getWidth() / 2) * cos > (map.getWidth() / 2) || (getY() - map.getHeight() / 2) * sin > (map.getHeight() / 2));
    }

    /* This method returns true if the car is inside of the visible map */
    public boolean isOnMap() {
        return (getX() > 0 && getCarFrontX() < map.getWidth() && getY() > 0 && getCarFrontY() < map.getHeight());
    }

    /*
     * This method returns true if the car's angle is different than any of the standard right angles,
     * which means the car is not going straight in it's line but is now turning
     */
    public boolean isNowTurning() {
        return (angle != 0 && angle != 90 && angle != 180 && angle != 270 && angle != 360);
    }

    /* This method returns true if the traffic light on this car's line is green */
    public boolean hasGreenLight() {
        if (isNowTurning())
            return true;
        else
            return trafficLightModel.isGreen();
    }

    /* This method returns true if the car has entered the road intersection */
    public boolean hasEnteredIntersection(Map map) {
        return ((Math.abs(angle) < 45 || Math.abs(angle - 360) < 45) && getCarFrontX() > map.getLeftCrossLine()) ||
                (Math.abs(angle - 180) < 45 && getCarFrontX() < map.getRightCrossLine()) ||
                (Math.abs(angle - 90) <= 45 && getCarFrontY() > map.getTopCrossLine()) ||
                (Math.abs(angle - 270) <= 45 && getCarFrontY() < map.getBottomCrossLine());
    }

    /*
     * This method increases the angle of the car by the value of'degree' variable.
     * If the angle get's very close (difference < 1 degree) to 90 degree difference from the default value, it get's rounded into default value + 90 degree.
     */
    public void rotateBy(double degree) {
        angle += degree;
        if (Math.abs(angle - (respawnAngle + 90)) < 1)
            angle = respawnAngle + 90;
        updateSinAndCosValues();
    }

    /* Sets the angle and the position of the car to a specified set of coordinates. */
    public void setCarPosition(int x, int y, int angle) {
        this.angle = angle;
        updateSinAndCosValues();
        this.x = x;
        this.y = y;
        safetyRec.setBounds(x + width, y - height / 2, (int) (width * (speed)) + 10, height);
        carRec.setLocation(x, y - height / 2);
    }

    /* isGoingToTurn variable serves as an information whether this car will turn right on the road intersection or go straight ahead */
    public boolean isGoingToTurn() {
        return isGoingToTurn;
    }

    /* isGoingToTurn variable serves as an information whether this car will turn right on the road intersection or go straight ahead */
    public void setGoingToTurn(boolean isGoingToTurn) {
        this.isGoingToTurn = isGoingToTurn;
    }

    /* This method returns true if the distance between this car and the specified coordinates is less than the distance parameter */
    public boolean isAtDistanceFrom(double x, double y, double distance) {
        double distanceX = Math.abs(getX() - x);
        double distanceY = Math.abs(getY() - y);
        double dist = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        return dist <= distance;
    }

    /* This method returns true if this car crashes into the car parameter */
    public boolean collides(CarModel carModel) {
        if (!isAtDistanceFrom(carModel.getX(), carModel.getY(), carModel.getWidth() + 20))
            return false;
        Area area1 = new Area(carRec);
        Area area2 = new Area(carModel.getCarRectangle());
        area1.transform(at);
        area2.transform(carModel.getAt());
        area1.intersect(area2);
        return !area1.isEmpty();
    }

    /* This method returns true if this car crashes into the rectangle parameter */
    public boolean collides(Rectangle rec) {
        double distance = Math.max(rec.getWidth(), rec.getHeight()) + 100;
        if (!isAtDistanceFrom(rec.getX() + rec.getWidth() / 2, rec.getY() + rec.getHeight() / 2, distance))
            return false;
        Area area = new Area(carRec);
        area.transform(at);
        return area.intersects(rec);
    }

    /* This method returns true if this car crashes into any of car objects from the 'cars' array parameter */
    public boolean collidesWithAny(CarModel[] carModels) {
        for (CarModel carModel : carModels) {
            if (collides(carModel) && this != carModel)
                return true;
        }
        return false;
    }

    public Color getCarColor() {
        return carColor;
    }

    public void setCarColor(Color color) {
        carColor = color;
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
