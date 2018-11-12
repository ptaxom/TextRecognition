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

//        BufferedImage img = deepCopy(image);
//
//        Kernel kernel2 = new Kernel(3, 3, new float[] { 1f / 9f, 1f / 9f, 1f / 9f,
//                1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f, 1f / 9f });
//        BufferedImageOp op = new ConvolveOp(kernel2);
//        img = op.filter(img, null);


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

}
