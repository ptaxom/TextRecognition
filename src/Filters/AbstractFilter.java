package Filters;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    abstract BufferedImage Apply(BufferedImage image, int param);
}
