package TestGUI;

import TextDetector.ConnectionComponent;
import TextDetector.Literal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class CompPanel extends JPanel {

    private ConnectionComponent component;

    int index = 0;

    CompPanel panel = this;

    public CompPanel(ConnectionComponent component) {
        super(null);
        this.component = component;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.drawImage(component.getImage(),0,0,null);
        for(Literal l : component.getLiterals())
            g2.draw(new Rectangle2D.Double(l.getX(),l.getY(),l.getWidth(),l.getHeight()));

    }

    public void saveImg(){
        BufferedImage out = new BufferedImage(component.getImage().getWidth(), component.getImage().getHeight(), 1);
        Graphics g = out.getGraphics();

        g.setColor(Color.BLACK);

        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.BLACK); g2.setColor(Color.BLACK);

        g.drawImage(component.getImage(),0,0,null);
        for(Literal l : component.getLiterals())
            g2.draw(new Rectangle2D.Double(l.getX(),l.getY(),l.getWidth(),l.getHeight()));


        try {
            ImageIO.write(out,"png", new java.io.File("res\\outFile.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
