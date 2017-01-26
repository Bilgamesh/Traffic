import javax.swing.*;
import java.awt.*;

public class Map extends JFrame {
    private ImageIcon backgroundImage;
    private JLabel backgroundLabel;
    private static int topCrossLine, bottomCrossLine, leftCrossLine, rightCrossLine;

    public Map() {
        setSize(800, 600);
        setLayout(null);
        setResizable(false);
        setTitle("Traffic simulator");

        // The coordinates of road intersection
        topCrossLine = 63;
        bottomCrossLine = 230;
        leftCrossLine = 368;
        rightCrossLine = 533;

        backgroundImage = new ImageIcon(getClass().getResource("background.png"));
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 800, 600);
        add(backgroundLabel);
    }

    public void addOnTop(Component comp) {
        backgroundLabel.add(comp);
    }

    public static int getTopCrossLine() {
        return topCrossLine;
    }

    public static int getBottomCrossLine() {
        return bottomCrossLine;
    }

    public static int getLeftCrossLine() {
        return leftCrossLine;
    }

    public static int getRightCrossLine() {
        return rightCrossLine;
    }
}
