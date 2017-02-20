import javax.swing.*;

public class Window extends JFrame {

    public Window() {
        setSize(800, 600);
        setResizable(false);
        setLayout(null);

        Map map = new Map();

        GUIView guiView = new GUIView(map);
        SpritesView spritesView = new SpritesView(map);
        AppController appController = new AppController(guiView, spritesView);

        add(guiView);
        add(spritesView);
        add(map);

    }
}
