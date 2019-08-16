package tankgame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public abstract class GameObjects {


    int x;
    int y;
    int vx;
    int vy;
    int angle;
    Boolean active;
    BufferedImage img;
    Rectangle r;

    public abstract void update();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

 /*   public int getWidth() {
        return img.getWidth();
    }

    public int getHeight() {
        return img.getHeight();
    }*/

    public abstract void collision(Class c);

    public Rectangle getR() {
        return (new Rectangle(this.x, this.y, img.getWidth(), img.getHeight()));
    }

    public boolean isActive() {
        return active;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        //g.setColor(Color.RED);
        //g.drawRect(this.x, this.y, this.img.getWidth(), this.img.getHeight());
    }


}