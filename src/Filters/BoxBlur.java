package Filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class BoxBlur extends AbstractFilter {

    @Override
    public BufferedImage Apply(BufferedImage image, int halfOfMatrixSize) {
        if (halfOfMatrixSize <= 0)
            return image;
        BufferedImage buffer = new BufferedImage(image.getWidth()+halfOfMatrixSize * 2,
                                                image.getHeight()+halfOfMatrixSize * 2,
                                                      image.getType());

        //Copying bounds of image

        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < halfOfMatrixSize; j++) {
                buffer.setRGB(halfOfMatrixSize + i, j, image.getRGB(i, halfOfMatrixSize - j - 1));
                buffer.setRGB(halfOfMatrixSize + i, buffer.getHeight() - j - 1,
                        image.getRGB(i, image.getHeight() - 2 - halfOfMatrixSize + j));
            }

        for(int i = 0; i < image.getHeight(); i++)
            for(int j = 0; j < halfOfMatrixSize; j++){
                buffer.setRGB(j,i + halfOfMatrixSize,image.getRGB(halfOfMatrixSize-j-1,i));
                buffer.setRGB(buffer.getWidth()-j-1,i+halfOfMatrixSize,
                        image.getRGB(image.getWidth()-2-halfOfMatrixSize+j, i));
            }

        //Copying corners of image

        for(int i = 0; i < halfOfMatrixSize; i++)
            for(int j = 0; j < halfOfMatrixSize; j++) {
                buffer.setRGB(i, j, image.getRGB(halfOfMatrixSize - i - 1, halfOfMatrixSize - j - 1)); //Left top corner
                buffer.setRGB(i,halfOfMatrixSize+image.getHeight()+j,image.getRGB(halfOfMatrixSize-i-1,image.getHeight() - j - 1));
                buffer.setRGB(halfOfMatrixSize+image.getWidth()+i,j,image.getRGB(image.getWidth()-i-1,halfOfMatrixSize-j-1));
                buffer.setRGB(halfOfMatrixSize+image.getWidth()+i,halfOfMatrixSize+image.getHeight()+j,
                        image.getRGB(image.getWidth()-i-1,image.getHeight() - j - 1));
        }

        //Copying core of image

        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < image.getHeight(); j++)
                buffer.setRGB(i+halfOfMatrixSize,j+halfOfMatrixSize,image.getRGB(i,j));

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
                double r = 0, g = 0, b = 0, a = 0;
                for(int x = -halfOfMatrixSize; x < halfOfMatrixSize; x++)
                    for(int y = -halfOfMatrixSize; y < halfOfMatrixSize; y++)
                    {
//                        int rgb = buffer.getRGB(i + halfOfMatrixSize + x, j + halfOfMatrixSize + y);
                        Color cl = new Color(buffer.getRGB(i + halfOfMatrixSize + x, j + halfOfMatrixSize + y));
                        double kern = kernel[halfOfMatrixSize+x][halfOfMatrixSize+y];
                        a += cl.getAlpha() * kern;
                        r += cl.getRed() * kern;
                        g += cl.getGreen() * kern;
                        b += cl.getBlue() * kern;
                    }
                    out.setRGB(i,j,(new Color(FixRGB(r), FixRGB(g), FixRGB(b))).getRGB());
            }

        return out;
    }

}
