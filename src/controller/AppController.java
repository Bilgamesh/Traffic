package controller;


import gui.view.GUIView;
import map.Map;
import sprites.model.SpritesModel;
import sprites.view.SpritesView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AppController implements ActionListener, MouseListener {
    private final GUIView guiView;
    private final Timer timer;
    private final SpritesView spritesView;
    private final SpritesModel spritesModel;

    public AppController(Map map, GUIView guiView, SpritesView spritesView, SpritesModel spritesModel) {
        this.guiView = guiView;
        this.spritesView = spritesView;
        this.spritesModel = spritesModel;

        timer = new Timer(10, this);

        guiView.initiateGUI(map);

        guiView.addStartButtonListener(this);
        guiView.addBackToMenuButtonListener(this);
        guiView.addMouseListener(this);

        int amountOfCars = 24;
        spritesModel.initiateSpriteModels(map, amountOfCars);
        spritesView.initiateSprites(map, amountOfCars);
        spritesModel.loadAutoMode();

        timer.start();

    }

    public void actionPerformed(ActionEvent e) {
        Object z = e.getSource();
        if (z == guiView.getStartButton()) {
            if (guiView.isAutoModeSelected()) {
                spritesModel.loadAutoMode();
            }
            if (guiView.isRescueModeSelected()) {
                timer.setDelay(15);
                spritesModel.loadRescueMode();
                guiView.setSteeringWheelVisible(true);
            }

            guiView.setGameplayGUI(true);
        }
        if (z == guiView.getBackToMenuButton()) {
            timer.setDelay(10);
            spritesModel.loadAutoMode();
            guiView.setGameplayGUI(false);
            guiView.setSteeringWheelVisible(false);
        }
        if (z == timer) {
            spritesModel.advance();
            spritesModel.updateSpritesWithModels(spritesView.getCarSprites(), spritesView.getTrafficLightSprites());
            spritesView.repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
        Object z = e.getSource();
        if (z == guiView && guiView.isRescueModeSelected()) {
            spritesModel.pickClickedCar(e, guiView.getSteeringWheel());
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


