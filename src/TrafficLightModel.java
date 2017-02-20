import java.awt.*;

public class TrafficLightModel {
    private Color color = Color.RED;
    private Color previousColor = Color.YELLOW;
    private long backupTime = 0;
    private int x = 0;
    private int y = 0;
    private int angle = 0;
    private boolean allowedToChange = true;

    public void setPosition(int x, int y, int angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getAngle() {
        return angle;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public long getBackupTime() {
        return backupTime;
    }

    public boolean isGreen() {
        return (color == Color.GREEN);
    }

    public void allowToChange(boolean arg) {
        allowedToChange = arg;
    }

    public void changeGreenIntoYellow() {
        if (color == Color.GREEN && allowedToChange) {
            color = Color.YELLOW;
        }
    }

    public void changeYellowIntoRed() {
        if (color == Color.YELLOW && allowedToChange) {
            color = Color.RED;
        }
    }

    public void changeRedIntoYellow() {
        if (color == Color.RED && allowedToChange) {
            color = Color.YELLOW;
        }
    }

    public void changeYellowIntoGreen() {
        if (color == Color.YELLOW && allowedToChange) {
            color = Color.GREEN;
        }
    }
}
