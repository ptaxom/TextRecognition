package TextDetector;

import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class Literal {

    private int x, y, x1, y1;

    private BufferedImage image;


    public Literal(int x, int y) {
        this.x = x;
        this.y = y;
        this.x1 = x;
        this.y1 = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public void updateY(int y){
        if (y < this.y)
            this.y = y;
        if (y > this.y1)
            this.y1 = y;
    }

    public void updateX(int x){
        if (x < this.x)
            this.x = x;
        if (x > this.x1)
            this.x1 = x;
    }

    public int getWidth(){
        return x1-x+1;
    }

    public int getHeight(){
        return y1-y+1;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
