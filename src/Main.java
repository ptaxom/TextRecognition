import Filters.AbstractFilter;
import Filters.BoxBlur;
import Filters.ThresholdFilter;
import TestGUI.CompFrame;
import TestGUI.TestFrame;
import TextDetector.ConnectionComponent;
import TextDetector.HoughTransform;
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

//        String name = "src4.png";
        String name = "test3.jpg";
        BufferedImage image, src;
        try {
            image = ImageIO.read(new java.io.File("res\\"+name));
            src = ImageIO.read(new java.io.File("res\\"+name));

//            Intereference(image, 10);

//            new TestFrame(image);

//            image = new BoxBlur().Apply(image,1);
//            image = new BoxBlur().Apply(image,1);

//            new TestFrame(image);

            image = new ThresholdFilter().Apply(image,135);

            new TestFrame(image,0.8);

            ConnectionComponent connectionComponent = new ConnectionComponent(image);

            new CompFrame(connectionComponent);

            int param = 250;

            HoughTransform houghTransform = new HoughTransform(param,param,connectionComponent);


            System.out.println(Math.toDegrees(Math.PI/2 - houghTransform.getTheta()));

            BufferedImage rotated = AbstractFilter.rotate(image,Math.PI/2 - houghTransform.getTheta());

            ConnectionComponent component2 = new ConnectionComponent(rotated);

            new CompFrame(component2);

            new TestFrame(rotated,0.7);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
