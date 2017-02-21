import controller.AppController;
import gui.view.GUIView;
import map.Map;
import sprites.model.SpritesModel;
import sprites.view.SpritesView;

import javax.swing.*;

public class Window extends JFrame {

    public Window() {
        setSize(800, 600);
        setResizable(false);
        setLayout(null);

        Map map = new Map();

        GUIView guiView = new GUIView();
        SpritesView spritesView = new SpritesView();
        SpritesModel spritesModel = new SpritesModel();
        AppController appController = new AppController(map, guiView, spritesView, spritesModel);

        add(guiView);
        add(spritesView);
        add(map);

    }
}
