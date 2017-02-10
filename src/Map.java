import javax.swing.*;

public class Map extends JPanel {
    private final int topCrossLine, bottomCrossLine, leftCrossLine, rightCrossLine;
    private final ImageIcon backgroundImage;
    private final JLabel backgroundLabel;
    private GUI gui;

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

    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    // This method returns the top coordinate of the road intersection
    public int getTopCrossLine() {
        return topCrossLine;
    }

    // This method returns the bottom coordinate of the road intersection
    public int getBottomCrossLine() {
        return bottomCrossLine;
    }

    // This method returns the left coordinate of the road intersection
    public int getLeftCrossLine() {
        return leftCrossLine;
    }

    // This method returns the right coordinate of the road intersection
    public int getRightCrossLine() {
        return rightCrossLine;
    }
}
