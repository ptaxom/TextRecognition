package Filters;

import TestGUI.TestFrame;
import Util.Matrix;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

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


    static public BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
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


    public static BufferedImage getImageContinuation(BufferedImage image, int size){
        BufferedImage buffer = new BufferedImage(image.getWidth()+size * 2,
                image.getHeight()+size * 2,
                image.getType());

        //Copying bounds of image

        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < size; j++) {
                buffer.setRGB(size + i, j, image.getRGB(i, size - j - 1));
                buffer.setRGB(size + i, buffer.getHeight() - j - 1,
                        image.getRGB(i, image.getHeight() - 2 - size + j));
            }

        for(int i = 0; i < image.getHeight(); i++)
            for(int j = 0; j < size; j++){
                buffer.setRGB(j,i + size,image.getRGB(size-j-1,i));
                buffer.setRGB(buffer.getWidth()-j-1,i+size,
                        image.getRGB(image.getWidth()-2-size+j, i));
            }

        //Copying corners of image

        for(int i = 0; i < size; i++)
            for(int j = 0; j < size; j++) {
                buffer.setRGB(i, j, image.getRGB(size - i - 1, size - j - 1)); //Left top corner
                buffer.setRGB(i,size+image.getHeight()+j,image.getRGB(size-i-1,image.getHeight() - j - 1));
                buffer.setRGB(size+image.getWidth()+i,j,image.getRGB(image.getWidth()-i-1,size-j-1));
                buffer.setRGB(size+image.getWidth()+i,size+image.getHeight()+j,
                        image.getRGB(image.getWidth()-i-1,image.getHeight() - j - 1));
            }

        //Copying core of image

        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < image.getHeight(); j++)
                buffer.setRGB(i+size,j+size,image.getRGB(i,j));

        return buffer;
    }


    //Свертка

    public static double[] getMatrixConvolution(BufferedImage image, Matrix kernel){
        double[] output = new double[3]; //R G B
        for(int i = 0; i < image.getHeight(); i++)
            for(int j = 0; j < image.getWidth(); j++){
                Color cl = new Color(image.getRGB(j,i));
                double kern = kernel.getVal(i, j);
                output[0] += kern * (double) cl.getRed();
                output[1] += kern * (double) cl.getGreen();
                output[2] += kern * (double) cl.getBlue();
            }
        return output;
    }

    public static double getAverageBrightness(BufferedImage image){
        double avg = 0, size = (double)image.getHeight() * image.getWidth();
        for(int i = 0; i < image.getHeight(); i++)
            for(int j = 0; j < image.getWidth(); j++){
                Color cl = new Color(image.getRGB(j,i));
                double val = (double)(cl.getBlue() + cl.getGreen() + cl.getRed()) / 3.0;
                avg += val;
            }
        return avg / size;
    }



    public static BufferedImage getHistogram(BufferedImage img, int maxHeight){
        int discreteStep = 256;
        int max = 0;
        int[] arr = new int[discreteStep];

        BufferedImage out = new BufferedImage(discreteStep,maxHeight,img.getType());

        for(int x = 0; x < out.getWidth(); x++)
            for(int y = 0; y < out.getHeight(); y++)
                out.setRGB(x,y,Color.WHITE.getRGB());

        for(int x = 0; x < img.getWidth(); x++)
            for(int y = 0; y < img.getHeight(); y++)
            {
                Color cl = new Color(img.getRGB(x,y));
                int px = (int) ((double)(cl.getBlue() + cl.getGreen() + cl.getRed()) / 3.0 * (double)discreteStep / 256.0);
                if (px > discreteStep)
                    px = discreteStep - 1;
                arr[px]++;
                if (arr[px] > max)
                    max = arr[px];
            }

        for(int i = 0; i < discreteStep; i++)
            for(int j = 0; j < (double)arr[i] /(double) max * (double)maxHeight; j++)
                out.setRGB(i,maxHeight - j - 1, Color.BLACK.getRGB());
        return out;
    }

    abstract BufferedImage Apply(BufferedImage image, int param);


    public static BufferedImage grayScaleAVG(BufferedImage img){
        BufferedImage out = deepCopy(img);
        for(int x = 0; x < out.getWidth(); x++)
            for (int y = 0; y < out.getHeight(); y++){
                Color cl = new Color(out.getRGB(x,y));
                int val = (cl.getBlue() + cl.getRed() + cl.getGreen()) / 3;
                out.setRGB(x,y, new Color(val, val, val).getRGB());
            }
        return out;
    }


    public static BufferedImage grayScale(BufferedImage img){
        BufferedImage out = deepCopy(img);
        for(int x = 0; x < out.getWidth(); x++)
            for (int y = 0; y < out.getHeight(); y++){
                Color cl = new Color(out.getRGB(x,y));
                int val = (int) (0.2126 * (double)cl.getRed() +
                                 0.7152 * (double)cl.getGreen() +
                                 0.0722 * (double)cl.getBlue());
                out.setRGB(x,y, new Color(val, val, val).getRGB());
            }
        return out;
    }


    public static double getAvg(int rgb){
        Color cl = new Color(rgb);
        double avg = (double)cl.getBlue() + (double)cl.getRed() + (double)cl.getGreen();
        return avg/3;
    }


    private static double getTileSum(double[][] tile, int x, int y, int tile_param){
        int x1 = x - tile_param - 1, y1 = y - tile_param - 1;
        int x2 = x + tile_param, y2 = y + tile_param;
        double s1 = tile[x2][y2],
               s2 = tile[x1 > 0 ? x1 : 0][y2],
               s3 = tile[x1 > 0 ? x1 : 0][y1 > 0 ? y1 : 0],
               s4 = tile[x2][y1 > 0 ? y1 : 0];
        return s1 - s2 + s3 - s4;
    }

    public static BufferedImage adaptiveThreshold(BufferedImage img, int param, double k){
        BufferedImage buffer = getImageContinuation(img, param);

        double[][] scaleBuffer = new double[buffer.getWidth()][buffer.getHeight()]; //Sum of all pixels in rectangle (0;0) -> (x;y)

        //Preprocces
        scaleBuffer[0][0] = getAvg(buffer.getRGB(0,0));

        for(int x = 1; x < buffer.getWidth(); x++)
            scaleBuffer[x][0] = scaleBuffer[x-1][0] + getAvg(buffer.getRGB(x,0));

        for(int y = 1; y < buffer.getHeight(); y++)
            scaleBuffer[0][y] = scaleBuffer[0][y-1] + getAvg(buffer.getRGB(0,y));

        for(int x = 1; x < buffer.getWidth(); x++)
            for(int y = 1; y < buffer.getHeight(); y++)
                scaleBuffer[x][y] = scaleBuffer[x-1][y] + scaleBuffer[x][y-1] - scaleBuffer[x-1][y-1] + getAvg(buffer.getRGB(x,y));


        double kernel_size = 2.0 * param + 1.0;

        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < out.getWidth(); x++)
            for(int y = 0; y < out.getHeight(); y++)
            {
                double mean = getTileSum(scaleBuffer,x + param,y + param,param) / kernel_size / kernel_size;
                double T = mean + k ;
                if (getAvg(buffer.getRGB(x + param,y + param)) > T)
                     out.setRGB(x,y,Color.WHITE.getRGB());
                else out.setRGB(x,y,Color.BLACK.getRGB());
            }
        return out;
    }


}
