package Filters;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Created by ptaxom on 05.11.2018.
 */
public abstract class AbstractFilter {

    public static int getRed(int rgb){
//        return rgb & (0x00ff0000) >> 16;
        return (new Color(rgb)).getRed();
    }


    public static int getGreen(int rgb){
//        return rgb & (0x0000ff00) >> 8;
        return (new Color(rgb)).getGreen();
    }

    public static int getBlue(int rgb){
//        return rgb &  0x000000ff;
        return (new Color(rgb)).getBlue();
    }


    public static Color RGBtoColor(int rgb){
        return new Color(getRed(rgb),getGreen(rgb),getBlue(rgb));
    }

    public static int FixRGB(double value){
        int rgb = (int) value;
        if (rgb < 0) rgb = 0;
        if (rgb > 255) rgb = 255;
        return rgb;
    }

    public static int ColorToRGB(Color color){
        return color.getRGB();
    }

    public static boolean isBlack(int rgb){
        return getBlue(rgb) == 0 && getGreen(rgb) == 0 && getRed(rgb) == 0;
    }

    public static BufferedImage rotate(BufferedImage src, double angle){
        AffineTransform transform = new AffineTransform();
        transform.rotate(angle,src.getWidth()/2, src.getHeight()/2);

        BufferedImageOp operator = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());

        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src,operator,0,0);

        return dest;
    }

    abstract BufferedImage Apply(BufferedImage image, int param);
}
