package TestGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class TestPanel extends JPanel {

    private BufferedImage image;
    private double scale;

    public TestPanel(BufferedImage image, double scale) {
        this.image = image;
        this.scale = scale;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D grph = (Graphics2D)g;

        grph.scale(scale, scale);

        grph.drawImage(image,0,0,null);
    }
}
