package TextDetector;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * Created by ptaxom on 05.11.2018.
 */
public class Literal {

    private int x, y, x1, y1;
    private int mass = 0;

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

    public void setMass(int mass) {
        this.mass = mass;
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

    public void drawEqualLiteral(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.BLACK); g2.setColor(Color.BLACK);
        double r = Math.sqrt(mass/Math.PI/4.0), x = Math.abs((this.x1 + this.x)/2.0), y = Math.abs((this.y1 + this.y)/2.0);
        Ellipse2D el = new Ellipse2D.Double(x-r,y-r,2*r,2*r);
        g2.fill(el);
        g2.draw(el);
    }
}
