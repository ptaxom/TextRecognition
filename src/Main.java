import Filters.*;
import TestGUI.CompFrame;
import TestGUI.TestFrame;
import TextDetector.ConnectionComponent;
import TextDetector.HoughTransform;
import TextDetector.Literal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.IOException;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class Main {

    //Image preprocess

    //TODO Erosion filter for preprocess
    //TODO Fix false aligment for ~0deg pictures (WTF??)

    //TODO Image detection (Optinal)


    //Literal preprocess

    //TODO Sort literals by height and combine in lines
    //TODO Split long literals in line to separate literals


    //Main part

    //TODO Text recognize (Actually?)


    //Conclusion

    //TODO Save in .html file with same image\text location


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
        try {
            BufferedImage test = ImageIO.read(new java.io.File("res\\src90deg.png"));

//            new TestFrame(test);

            test = AbstractFilter.grayScale(test);
//            test = BoxBlur.BoxBlur(test,1);
            test = ThresholdFilter.adaptiveThreshold(test,7,-7);

            ConnectionComponent component = new ConnectionComponent(test);

            int p1 = 180, p2 = 300;

            double scale = Math.sqrt(1700.0*900.0/(double)test.getHeight()/(double)test.getWidth());
            scale = 1;

            HoughTransform t1 = new HoughTransform(p1,p2,test);
            HoughTransform t2 = new HoughTransform(p1,p2,component.getEqualHoughImage());

            System.out.println("Transform 1: "+Math.toDegrees(Math.PI/2 - t1.getTheta()));
            System.out.println("Transform 2: "+Math.toDegrees(Math.PI/2 - t2.getTheta()));

            BufferedImage rotared1 = AbstractFilter.rotate(test,Math.PI/2 - t1.getTheta());
            BufferedImage rotared2 = AbstractFilter.rotate(test,Math.PI/2 - t2.getTheta());


            new TestFrame(rotared1,scale);
            new TestFrame(rotared2,scale);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void blurtests(){
        try{
            BufferedImage test = ImageIO.read(new java.io.File("res\\ErosianTest.png"));

            MorphologicalFilters mFilter = new MorphologicalFilters(0,3);

            BoxBlur blur = new BoxBlur();

            int size = 1;

            new TestFrame(test);
            BufferedImage boxBlured = blur.BoxBlur(test,size);
            BufferedImage myBlur = blur.Apply(test,size);
            BufferedImage gaussBlured = blur.GaussBlur(test,size,1.5);
            BufferedImage out = mFilter.Closing(gaussBlured);

            new TestFrame(boxBlured);
            new TestFrame(myBlur);
            new TestFrame(gaussBlured);
            new TestFrame(out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void test1(){

//        String name = "src4.png";
        String name = "test2.jpg";
        BufferedImage image, src;
        try {
            image = ImageIO.read(new java.io.File("res\\"+name));
            src = ImageIO.read(new java.io.File("res\\"+name));

            new TestFrame(image,0.7);

//            Intereference(image, 10);

//            new TestFrame(image);

//            image = new BoxBlur().Apply(image,1);
//            image = new BoxBlur().Apply(image,1);

//            new TestFrame(image);

            image = new ThresholdFilter().Apply(image,200);

            new TestFrame(image,0.8);

            ConnectionComponent connectionComponent = new ConnectionComponent(image);

            new CompFrame(connectionComponent);

            int param = 20;

            HoughTransform houghTransform = new HoughTransform(param,param,image);


            System.out.println(Math.toDegrees(Math.PI/2 - houghTransform.getTheta()));

            BufferedImage rotated = AbstractFilter.rotate(image,Math.PI/2 - houghTransform.getTheta());

            ConnectionComponent component2 = new ConnectionComponent(rotated);

            new CompFrame(component2);

            new TestFrame(houghTransform.getHoughSpace(),0.4);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
