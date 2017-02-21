package gui.view;

import gui.steeringWheel.SteeringWheel;
import map.Map;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;

public class GUIView extends JPanel {
    private JButton backToMenuButton, startButton;
    private SteeringWheel steeringWheel;
    private JRadioButton autoMode, rescueMode;
    private ButtonGroup gameModeGroup;
    private JPanel gameModeButtons;

    public void initiateGUI(Map map) {
        setSize(map.getSize());
        setLayout(null);
        setOpaque(false);

        startButton = new JButton("Start");
        startButton.setBounds(350, 285, 100, 30);
        add(startButton);

        backToMenuButton = new JButton("Back");
        backToMenuButton.setBounds(700, 0, 100, 30);
        backToMenuButton.setLayout(null);
        backToMenuButton.setVisible(false);
        add(backToMenuButton);

        steeringWheel = new SteeringWheel();
        steeringWheel.setBounds(55, 430, 300, 300);
        steeringWheel.setOpaque(false);
        steeringWheel.setVisible(false);
        add(steeringWheel);

        autoMode = new JRadioButton("autoMode");
        rescueMode = new JRadioButton("rescueMode");
        gameModeGroup = new ButtonGroup();
        gameModeGroup.add(autoMode);
        gameModeGroup.add(rescueMode);

        gameModeButtons = new JPanel();
        gameModeButtons.add(autoMode);
        gameModeButtons.add(rescueMode);
        gameModeButtons.setBounds(600, 200, 100, 300);
        gameModeButtons.setOpaque(false);
        add(gameModeButtons);


    }

    public boolean isAutoModeSelected() {
        return autoMode.isSelected();
    }

    public boolean isRescueModeSelected() {
        return rescueMode.isSelected();
    }

    public void setGameplayGUI(boolean arg) {
        setStartButtonVisible(!arg);
        setBackToMenuButtonVisible(arg);
        setGameModeButtonsVisible(!arg);
    }

    public void setSteeringWheelVisible(boolean arg) {
        steeringWheel.setVisible(arg);
    }

    public void setGameModeButtonsVisible(boolean arg) {
        gameModeButtons.setVisible(arg);
    }

    public void setBackToMenuButtonVisible(boolean arg) {
        backToMenuButton.setVisible(arg);
    }

    public void setStartButtonVisible(boolean arg) {
        startButton.setVisible(arg);
    }

    public void addSteeringWheelMouseMotionListener(MouseMotionListener listener) {
        steeringWheel.addMouseMotionListener(listener);
    }

    public SteeringWheel getSteeringWheel() {
        return steeringWheel;
    }

    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    public void addBackToMenuButtonListener(ActionListener listener) {
        backToMenuButton.addActionListener(listener);
    }

    public JButton getBackToMenuButton() {
        return backToMenuButton;
    }

    public JButton getStartButton() {
        return startButton;
    }
}


