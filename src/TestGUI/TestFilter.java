package TestGUI;

import Filters.SSRFilter;
import Filters.ThresholdFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by ptaxom on 10.11.2018.
 */
public class TestFilter {

    static BufferedImage image;
    static double scale = 0.4;

    public static void main(String[] args) {
        try {
//            image = ImageIO.read(new java.io.File("res\\src11.jpg"));

            image = ImageIO.read(new java.io.File("res\\test2.jpg"));
            image = new SSRFilter().Apply(image,1);

            imgToDraw = image;
            new Frame();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static BufferedImage imgToDraw = null;

    static class Frame extends JFrame{

        Panel panel = new Panel();


        public Frame(){
            super("test");
            setSize((int) (image.getWidth()+210), (int) (image.getHeight()+100));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            panel = new Panel();
            this.add(panel);
            setVisible(true);

            JSlider slider = new JSlider(0,255);
            slider.setBounds(10+image.getWidth(),10,200,40);

            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int threshold = (int) ((double)slider.getValue()/(double)slider.getMaximum() * 255.0);
                    imgToDraw = new ThresholdFilter().Apply(image,threshold);
                    panel.repaint();
                }
            });

            panel.add(slider);

        }
    }

    static class Panel extends JPanel{

        public Panel() {
            super(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D grph = (Graphics2D)g;

            grph.scale(scale, scale);

            grph.drawImage(imgToDraw,0,0,null);

            grph.scale(1/scale, 1/scale);
        }
    }


}
