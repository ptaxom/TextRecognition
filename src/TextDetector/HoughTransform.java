package TextDetector;

import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ptaxom on 06.11.2018.
 */
public class HoughTransform {

    private BufferedImage image;
    private int anglenum;
    private int rhonum;

    private int[][] accum;

    private BufferedImage houghSpace = null;

    public HoughTransform(int anglenum, int rhonum, BufferedImage image) {
        this.anglenum = anglenum;
        this.rhonum = rhonum;
        this.image = image;
        accum = new int[anglenum][rhonum];
        fillAccum();
    }

    private int maxIntencivity = 0;

    private float maxRho;

    private float[] sinTable;
    private float[] cosTable;

    private void fillAccum(){

        sinTable = new float[anglenum];
        cosTable = new float[anglenum];

        float angleStep = (float) (Math.PI / anglenum);

        for(int i = 0; i < anglenum; i++){
            float angle = i * angleStep;
            sinTable[i] = (float) Math.sin(angle);
            cosTable[i] = (float) Math.cos(angle);
        }

        BufferedImage src = image;

        maxRho = (float) Math.sqrt(src.getHeight()*src.getHeight()+src.getWidth()*src.getWidth());

        for(int i = 0; i < src.getHeight(); i++)    // y
            for(int j = 0; j < src.getWidth(); j++) // x
                if (isBlack(i,j))
                    for(int k = 0; k < anglenum; k++){
                        float rho = i * sinTable[k] + j * cosTable[k];
                        rho = rhonum / 2 +  rho / maxRho * rhonum / 2;

                        int rhoIndex = (int) Math.floor(rho);
//                        if (rhoIndex < 0) rhoIndex = 0;
//                        if (rhoIndex >= rhonum) rhoIndex = rhonum - 1;

                        accum[k][rhoIndex]++;
                        }


        for(int i = 0; i < anglenum; i++)
            for(int j = 0; j < rhonum; j++)
                if (accum[i][j] > maxIntencivity)
                    maxIntencivity = accum[i][j];

        houghSpace = getHoughSpaceImage();
        drawLines();
    }

    private BufferedImage getHoughSpaceImage(){
        BufferedImage out = new BufferedImage(rhonum, anglenum, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < anglenum; i++)
            for(int j = 0; j < rhonum; j++) {
                int color = (int) ((float)accum[i][j] / (float)maxIntencivity * 255.0f);
                if (color < 0) color = 0;
                if (color > 255) color = 255;
                out.setRGB(j, i, new Color(color, color, color).getRGB());
            }
        return out;
    }

    public BufferedImage getHoughSpace() {
        return houghSpace;
    }


    private float theta = 0;

    private BufferedImage imgWithLines;

    public void drawLines(){
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for(int i = 0; i < out.getHeight(); i++)
            for(int j = 0; j < out.getWidth(); j++)
                out.setRGB(j,i,image.getRGB(j,i));

        List<Pair<Float, Float>> lines = new ArrayList<>();


        int threshold = (int) (0.8 * maxIntencivity);


        for(int i = 0; i < anglenum; i++)
            for(int j = 0; j < rhonum; j++)
                if (accum[i][j] > threshold && condiction(i,j)
                        ) {
                    float prop = (j - rhonum/2.0f) / (rhonum/2.0f) * -maxRho;
                    lines.add(new Pair<>((float) (Math.PI * i / anglenum), prop));
                }

        for(int i = 0; i < lines.size(); i++){
            Color cl = ConnectionComponent.getCompColor(i+1, lines.size());
            theta += lines.get(i).getKey();
            drawLine(out,lines.get(i),cl,2);
        }
        theta /= lines.size();
        imgWithLines = out;
    }

    private void drawLine(BufferedImage img, Pair<Float, Float> line, Color cl, int sz){
        for(int i = 0; i < img.getWidth(); i++){
            int y = (int) (-Math.cos(line.getKey()) / Math.sin(line.getKey()) * i - line.getValue() / Math.sin(line.getKey()));
            if (y < sz || y+sz >= img.getHeight())
                continue;
             for(int k = -sz; k <= sz; k++)
                img.setRGB(i,y+k, cl.getRGB());
        }
    }

    private boolean condiction(int i, int j){
        boolean ans1 = false;
        boolean ans2 = false;


        if (i == 0)
            ans1 = accum[i][j] > accum[i+1][j];
            else if (i+1 == anglenum)
                ans1 = accum[i][j] > accum[i-1][j];
                    else
                        ans1 = accum[i-1][j] < accum[i][j] && accum[i][j] > accum[i+1][j];

        if (j == 0)
               ans2 = accum[i][j] > accum[i][j+1];
           else if (j+1 == rhonum)
               ans2 = accum[i][j] > accum[i][j-1];
                    else
                        ans2 = accum[i][j-1] < accum[i][j] && accum[i][j] > accum[i][j+1];

        return ans1 && ans2;
    }

    public float getTheta() {
        return theta;
    }

    public BufferedImage getImgWithLines() {
        return imgWithLines;
    }

    public boolean isBlack(int i, int j){
        if (i < 0 || j < 0 || i >= image.getHeight() || j >= image.getWidth())
            return false;
        return ConnectionComponent.isBlack(image.getRGB(j,i));
    }



}
