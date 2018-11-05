package Filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class ThresholdFilter extends AbstractFilter {

    @Override
    public BufferedImage Apply(BufferedImage image, int threshold) {
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        threshold = 255 - threshold;

        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < image.getHeight(); j++){
                Color cl = new Color(image.getRGB(i,j));
                double value = cl.getRed() + cl.getBlue() + cl.getGreen();
                value/=3;
                if (value < threshold)
                    out.setRGB(i,j,ColorToRGB(Color.BLACK));
                else out.setRGB(i,j,ColorToRGB(Color.WHITE));
            }

        return out;
    }
}
