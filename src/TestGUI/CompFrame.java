package TestGUI;

import TextDetector.ConnectionComponent;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class CompFrame extends JFrame {

    private ConnectionComponent component;
    private CompPanel panel;

    public CompFrame(ConnectionComponent component) {
        super("CompFrame");
        this.component = component;

        setSize(component.getImage().getWidth()+200, component.getImage().getHeight()+200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        panel = new CompPanel(component);
        this.add(panel);

//        panel.saveImg();

        setVisible(true);
    }

}
