package Filters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 13.11.2018.
 */
public class MorphologicalFilters {

    private int type;
    private int size;

    public MorphologicalFilters(int type, int size) {
        this.type = type;
        this.size = size;
    }

    private boolean[][] getMask(int param){
        int kernelSize = 2 * param + 1;
        boolean[][] mask = new boolean[kernelSize][kernelSize];

        for(int i = 0; i < kernelSize; i++)
            for(int j = 0; j < kernelSize; j++)
                mask[i][j] = true;

        if (type == 1)
        {
            for(int i = 0; i < param; i++)
            {
                mask[i][0] = false; mask[kernelSize - 1 - i][0] = false;
                mask[0][i] = false; mask[0][kernelSize - 1 - i] = false;

                mask[i][kernelSize - 1] = false; mask[kernelSize - 1 - i][kernelSize - 1] = false;
                mask[kernelSize - 1][i] = false; mask[kernelSize - 1][kernelSize - 1 - i] = false;
            }
        }

        return mask;
    }

    public BufferedImage Erosion(BufferedImage src){
        BufferedImage buffer = AbstractFilter.getImageContinuation(src,size);
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);

        boolean[][] mask = getMask(size);

        for(int x = 0; x < out.getWidth(); x++)
            for(int y = 0; y < out.getHeight(); y++)
            {
                double minBit = 255;
                for(int i = -size; i <= size; i++)
                    for(int j = -size; j <= size; j++)
                        if (mask[i+size][j+size] &&
                                AbstractFilter.getAvg(buffer.getRGB(x+size,y+size)) < minBit)
                            minBit = AbstractFilter.getAvg(buffer.getRGB(x+size,y+size));
                out.setRGB(x,y,new Color((int) minBit, (int) minBit, (int) minBit).getRGB());
            }

        return out;
    }

    public BufferedImage Dilation(BufferedImage src){
        BufferedImage buffer = AbstractFilter.getImageContinuation(src,size);
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);

        boolean[][] mask = getMask(size);

        for(int x = 0; x < out.getWidth(); x++)
            for(int y = 0; y < out.getHeight(); y++)
            {
                double maxBit = 0;
                for(int i = -size; i <= size; i++)
                    for(int j = -size; j <= size; j++)
                        if (mask[i+size][j+size] &&
                                AbstractFilter.getAvg(buffer.getRGB(x+size,y+size)) > maxBit)
                            maxBit = AbstractFilter.getAvg(buffer.getRGB(x+size,y+size));
                out.setRGB(x,y,new Color((int) maxBit, (int) maxBit, (int) maxBit).getRGB());
            }

        return out;
    }

    public BufferedImage Opening(BufferedImage img){
        return Dilation(Erosion(img));
    }

    public BufferedImage Closing(BufferedImage img){
        return Erosion(Dilation(img));
    }


    public void setType(int type) {
        this.type = type;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
