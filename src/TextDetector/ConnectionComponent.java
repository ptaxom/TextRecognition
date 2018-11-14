package TextDetector;

import Filters.AbstractFilter;
import TestGUI.TestFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Created by ptaxom on 05.11.2018.
 */
public class ConnectionComponent {

    private int[][] compArray;

    private List<Literal> literals = new ArrayList<>();

    private int pixelMassThreshold = 4;

    private BufferedImage image;

    public ConnectionComponent(BufferedImage image) {
        this.image = image;
        compArray = new int[image.getHeight()][image.getWidth()];
        fillCompArray();
    }

    private int getMaxComp(){
        int max = 0;
        for(int a[] : compArray)
            for(int x : a)
                if (x > max) max = x;
        return max;
    }


    private int getID(int i, int j){
        return i * image.getWidth() + j;
    }


    private void fillCompArray(){
        int compNum = 0;
        for(int i = 0; i < image.getHeight(); i++)
            for(int j = 0; j < image.getWidth(); j++)
                if (isBlack(i,j) && compArray[i][j] == 0)
                    BFS(i,j,++compNum);

    }

    private void BFS(int i1, int j1, int type){
        LinkedBlockingDeque<Integer> vertexQueue = new LinkedBlockingDeque<>();
        Literal literal = new Literal(j1,i1);
        vertexQueue.add(i1*image.getWidth() + j1);
        int mass = 0;
        while (!vertexQueue.isEmpty()){
            ++mass;
            int vertexID = vertexQueue.getFirst();
            int i = vertexID / image.getWidth();
            int j = vertexID % image.getWidth();

            literal.updateX(j); literal.updateY(i);

            compArray[i][j] = type;
            vertexQueue.removeFirst();

            for(int di = -1; di < 2; di++)
                for(int dj = -1; dj < 2; dj++)
                    if (isBlack(i+di,j+dj) && compArray[i+di][j+dj] == 0){
                        compArray[i+di][j+dj] = type;
                        vertexQueue.add(getID(i+di,j+dj));
                    }

        }
        if (mass > pixelMassThreshold) {
            literal.setMass(mass);
            literal.setImage(getLiteralImage(literal, type));
            literals.add(literal);
        }
    }


    private int maxComp = 0;

    public static Color getCompColor(int num, int max){
        Color cl = null;
        if (num == 0)
            return Color.WHITE;
        if (max < 4){
            if (num == 1) cl = Color.RED;
            if (num == 2) cl = Color.GREEN;
            if (num == 3) cl = Color.BLUE;
        }
        else {
            double a = Math.exp(Math.log(max)/3);
            int numberSystemBase = (int) Math.ceil(a);
            int r = 255 * (num % numberSystemBase) / numberSystemBase; num/=numberSystemBase;
            int g = 255 * (num % numberSystemBase) / numberSystemBase; num/=numberSystemBase;
            int b = 255 * (num % numberSystemBase) / numberSystemBase;
            cl = new Color(r,g,b);
        }
        return cl;
    }

    public BufferedImage getColorizedImage(){
        BufferedImage out = new BufferedImage(image.getWidth(),image.getHeight(), image.getType());
        maxComp = getMaxComp();
        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < image.getHeight(); j++)
                out.setRGB(i,j,AbstractFilter.ColorToRGB(getCompColor(compArray[j][i], maxComp)));
        return out;
    }


    public static boolean isBlack(int rgb){
        return AbstractFilter.getBlue(rgb) == 0 && AbstractFilter.getGreen(rgb) == 0 && AbstractFilter.getRed(rgb) == 0;
    }

    private boolean isBlack(int i, int j){
        if (i < 0 || j < 0 || i >= image.getHeight() || j >= image.getWidth())
            return false;
        return isBlack(image.getRGB(j,i));
    }

    public List<Literal> getLiterals() {
        return literals;
    }

    public BufferedImage getImage() {
        return image;
    }


    private BufferedImage getLiteralImage(Literal l, int type){
        BufferedImage out = new BufferedImage(l.getWidth(),l.getHeight(), image.getType());
        int y = l.getY(); int x = l.getX();
        for(int i = 0; i < l.getHeight(); i++)
            for(int j = 0; j < l.getWidth(); j++)
                if (isBlack(y + i,x + j) && compArray[y + i][x + j] == type)
                    out.setRGB(j,i,AbstractFilter.ColorToRGB(Color.BLACK));
        else out.setRGB(j,i,AbstractFilter.ColorToRGB(Color.WHITE));
        return out;
    }

    public BufferedImage getEqualHoughImage(){
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(),BufferedImage.TYPE_INT_RGB);
//        for(int x = 0; x < out.getWidth(); x++)
//            for(int y = 0; y < out.getHeight(); y++)
//                out.setRGB(x,y,Color.white.getRGB());
        Graphics g = out.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,out.getWidth(),out.getHeight());
        for(Literal l : literals)
            l.drawEqualLiteral(g);
        return out;
    }


    public int getPixelMassThreshold() {
        return pixelMassThreshold;
    }

    public void setPixelMassThreshold(int pixelMassThreshold) {
        this.pixelMassThreshold = pixelMassThreshold;
    }
}
