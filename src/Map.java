import sun.util.resources.cldr.en.TimeZoneNames_en_ZA;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {
    private final int topCrossLine, bottomCrossLine, leftCrossLine, rightCrossLine;
    private final ImageIcon backgroundImage;
    private final JLabel backgroundLabel;
    private final Rectangle roadIntersection;

    public Map() {
        setSize(800, 600);
        setLayout(null);

        /* The coordinates of road intersection */
        topCrossLine = 63;
        bottomCrossLine = 230;
        leftCrossLine = 368;
        rightCrossLine = 533;
        roadIntersection = new Rectangle(leftCrossLine, topCrossLine, rightCrossLine - leftCrossLine, bottomCrossLine - topCrossLine);

        backgroundImage = new ImageIcon(("src//background.png"));
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 800, 600);
        backgroundLabel.setLayout(null);
        add(backgroundLabel);
    }

    /* This method returns the top coordinate of the road intersection */
    public int getTopCrossLine() {
        return topCrossLine;
    }

    /* This method returns the bottom coordinate of the road intersection */
    public int getBottomCrossLine() {
        return bottomCrossLine;
    }

    /* This method returns the left coordinate of the road intersection */
    public int getLeftCrossLine() {
        return leftCrossLine;
    }

    /* This method returns the right coordinate of the road intersection */
    public int getRightCrossLine() {
        return rightCrossLine;
    }

    public Rectangle getRoadIntersection() {
        return roadIntersection;
    }
}
