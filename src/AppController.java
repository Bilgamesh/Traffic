import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class AppController implements ActionListener, MouseListener {
    private final GUIView guiView;
    private final Timer timer;
    private final SpritesView spritesView;
    private int gameModeNumber = 0;

    public AppController(GUIView guiView, SpritesView spritesView) {
        this.guiView = guiView;
        this.spritesView = spritesView;

        timer = new Timer(10, this);

        guiView.addStartButtonListener(this);
        guiView.addBackToMenuButtonListener(this);
        guiView.addMouseListener(this);

        spritesView.loadAutoMode();

        timer.start();

    }

    public void actionPerformed(ActionEvent e) {
        Object z = e.getSource();
        if (z == guiView.getStartButton()) {
            if (guiView.isAutoModeSelected()) {
                spritesView.loadAutoMode();
            }
            if (guiView.isRescueModeSelected()) {
                timer.setDelay(15);
                spritesView.loadRescueMode();
                guiView.setSteeringWheelVisible(true);
            }

            guiView.setGameplayGUI(true);
        }
        if (z == guiView.getBackToMenuButton()) {
            timer.setDelay(10);
            spritesView.loadAutoMode();
            guiView.setGameplayGUI(false);
            guiView.setSteeringWheelVisible(false);
        }
        if (z == timer) {
            spritesView.advance();
            spritesView.repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
        Object z = e.getSource();
        if (z == guiView && guiView.isRescueModeSelected()) {
            spritesView.pickClickedCar(e, guiView.getSteeringWheel());
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}


