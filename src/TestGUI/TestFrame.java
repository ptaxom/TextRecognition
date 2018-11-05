package TestGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class TestFrame extends JFrame {

    private BufferedImage image = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
    private TestPanel panel;
    private double scale = 1;

    public TestFrame()  {
        super("Test");
        setSize((int) (image.getWidth()+100), (int) (image.getHeight()+100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new TestPanel(image,scale);
        this.add(panel);
        setVisible(true);
    }

    public TestFrame(BufferedImage image, double scale) {
        super("Test");
        this.scale = scale;
        this.image = image;
        setSize((image.getWidth()+100), (int) (image.getHeight()+100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new TestPanel(image,scale);
        this.add(panel);
        setVisible(true);
    }

    public TestFrame(BufferedImage image) throws HeadlessException {
        super("Test");
        this.image = image;
        setSize((image.getWidth()+100), (int) (image.getHeight()+100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new TestPanel(image,scale);
        this.add(panel);
        setVisible(true);
    }
}
