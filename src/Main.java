import Filters.AbstractFilter;
import Filters.BoxBlur;
import Filters.ThresholdFilter;
import TestGUI.CompFrame;
import TestGUI.TestFrame;
import TextDetector.ConnectionComponent;
import TextDetector.Literal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.IOException;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class Main {


    public static void Intereference(BufferedImage image, int param){
        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < image.getHeight(); j++)
            {
                Color cl = new Color(image.getRGB(i,j));
                int r = cl.getRed(), g = cl.getGreen(), b = cl.getBlue();
                int rn = (int) (1000*Math.random()) % param;
                if (rn == 0) r = 255;
                if (rn == 1) g = 255;
                if (rn == 2) b = 255;
                image.setRGB(i,j,AbstractFilter.ColorToRGB(new Color(r,g,b)));
            }
    }

    public static void main(String[] args) {

        String name = "src7.png";
        BufferedImage image;
        try {
            image = ImageIO.read(new java.io.File("res\\"+name));

//            Intereference(image, 10);

            new TestFrame(image);

//            image = new BoxBlur().Apply(image,1);
//            image = new BoxBlur().Apply(image,1);

            image = new ThresholdFilter().Apply(image,220);

            ConnectionComponent connectionComponent = new ConnectionComponent(image);

//            for(int i = 70; i < 80 && i < connectionComponent.getLiterals().size(); i++) {
//                Literal l = connectionComponent.getLiterals().get(i);
//                BufferedImage img = connectionComponent.getImage().getSubimage(l.getX(), l.getY(), l.getWidth(), l.getHeight());
//                new TestFrame(img, 3);
//            }

            new TestFrame(connectionComponent.getColorizedImage());

            new CompFrame(connectionComponent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
