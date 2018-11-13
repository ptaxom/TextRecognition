package Filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class BoxBlur extends AbstractFilter {

    @Override
    public BufferedImage Apply(BufferedImage image, int halfOfMatrixSize) {
        if (halfOfMatrixSize <= 0)
            return image;
        BufferedImage buffer = getImageContinuation(image, halfOfMatrixSize);

        int kernelSize = halfOfMatrixSize * 2 + 1;

        double[][] kernel = new double[kernelSize][kernelSize];

        for(int i = 0; i < kernelSize; i++)
            for(int j = 0; j < kernelSize; j++)
                kernel[i][j] = 1.0d/kernelSize/kernelSize;

        //Bluring



        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());




        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < image.getHeight(); j++)
            {
                int r = 0, g = 0, b = 0;
                for(int x = -halfOfMatrixSize; x < halfOfMatrixSize; x++)
                    for(int y = -halfOfMatrixSize; y < halfOfMatrixSize; y++)
                    {
//                        int rgb = buffer.getRGB(i + halfOfMatrixSize + x, j + halfOfMatrixSize + y);
                        Color cl = new Color(buffer.getRGB(i + halfOfMatrixSize + x, j + halfOfMatrixSize + y));
                        double kern = kernel[halfOfMatrixSize+x][halfOfMatrixSize+y];
                        r += cl.getRed() * kern;
                        g += cl.getGreen() * kern;
                        b += cl.getBlue() * kern;
                    }
                    out.setRGB(i,j,(new Color(FixRGB(r), FixRGB(g), FixRGB(b))).getRGB());
            }

        return out;
    }

    public static BufferedImage BoxBlur(BufferedImage src, int size){
        BufferedImage img = deepCopy(src);

        float kernelSize = 2.0f * size + 1.0f;

        float[] kern = new float[(int) (kernelSize*kernelSize)];
        for(int i = 0; i < kernelSize * kernelSize; i++)
            kern[i] = 1.0f / kernelSize;

        Kernel kernel = new Kernel(size, size, kern);
        BufferedImageOp op = new ConvolveOp(kernel);
        img = op.filter(img, null);
        return img;
    }


    public static BufferedImage GaussBlur(BufferedImage src, int size, double sigma){
        BufferedImage img = deepCopy(src);

        float kernelSize = 2.0f * size + 1.0f;
        int kernelSizeInt = 2 * size + 1;

        float[] kern = new float[kernelSizeInt * kernelSizeInt];

        for(int i = 0; i < kernelSizeInt; i++) {
            for (int j = 0; j < kernelSizeInt; j++) {
                double dX = (double) (size - i);
                double dY = (double) (size - j);
                double dist = dX * dX + dY * dY;
                kern[i * kernelSizeInt + j] = (float) Math.exp(-dist / sigma / sigma);
                System.out.printf("%02f ", kern[i * kernelSizeInt + j]);
            }
            System.out.println();
        }

        Kernel kernel = new Kernel(size, size, kern);
        BufferedImageOp op = new ConvolveOp(kernel);
        img = op.filter(img, null);
        return img;
    }

}
