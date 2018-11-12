package Filters;

import Util.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 13.11.2018.
 */
public class SSRFilter extends AbstractFilter{

    private Matrix getGaussianKernel(int size, double sigma, double k){
        int mSize = size * 2 + 1;
        double[][] kernel = new double[mSize][mSize];

        for(int i = 0; i < mSize; i++)
            for(int j = 0; j < mSize; j++){
                double distY = (double) (size - i);
                double distX = (double) (size - j);
                kernel[i][j] = k * Math.exp(-(distX * distX + distY * distY) / sigma / sigma);
            }
        return new Matrix(kernel);
    }

    @Override
    public BufferedImage Apply(BufferedImage image, int param) {
        //0.57 for 7x7
        Matrix kernel = getGaussianKernel(param,0.57,1);
//        System.out.println(kernel);
        BufferedImage buffer = AbstractFilter.getImageContinuation(image, param);
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        int mSize = param * 2 + 1;

        for(int x = 0; x < image.getWidth(); x++)
            for(int y = 0; y < image.getHeight(); y++){
                BufferedImage subMatrix = buffer.getSubimage(x,y,mSize,mSize);
                double[] channels = AbstractFilter.getMatrixConvolution(subMatrix, kernel);
                Color cl = new Color(image.getRGB(x,y));
                int     r = cl.getRed(),
                        g = cl.getGreen(),
                        b = cl.getBlue();
                r = (int)(255.0 * Math.log((double)r/channels[0]) + 127.5);
                g = (int)(255.0 * Math.log((double)g/channels[1]) + 127.5);
                b = (int)(255.0 * Math.log((double)b/channels[2]) + 127.5);
//                System.out.println(r+" "+g+" "+b);
                output.setRGB(x,y,new Color(FixRGB(r),FixRGB(g),FixRGB(b)).getRGB());
            }

            return output;
    }
}
