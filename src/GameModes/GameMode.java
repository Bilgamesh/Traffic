package gamemodes;

import gui.steeringWheel.SteeringWheel;

import java.awt.event.MouseEvent;

public interface GameMode {

    public void advance();

    public void pickClickedCar(MouseEvent e, SteeringWheel steeringWheel);

}