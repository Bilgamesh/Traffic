import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {
    private ImageIcon backgroundImage;
    private JLabel backgroundLabel;
    private int topCrossLine, bottomCrossLine, leftCrossLine, rightCrossLine;

    public Map() {

        // The coordinates of road intersection
        topCrossLine = 63;
        bottomCrossLine = 230;
        leftCrossLine = 368;
        rightCrossLine = 533;

        backgroundImage = new ImageIcon(getClass().getResource("background.png"));
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 800, 600);
        backgroundLabel.setLayout(null);
        add(backgroundLabel);
    }

    public void addOnTop(Component comp) {
        backgroundLabel.add(comp);
    }

    public int getTopCrossLine() {
        return topCrossLine;
    }

    public int getBottomCrossLine() {
        return bottomCrossLine;
    }

    public int getLeftCrossLine() {
        return leftCrossLine;
    }

    public int getRightCrossLine() {
        return rightCrossLine;
    }
}
