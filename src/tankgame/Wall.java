package tankgame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Wall extends GameObjects {
    private int wallHP;
    private BufferedImage breakableWall;
    private BufferedImage wall;
    public Wall(int x, int y, int wallHP) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        this.wallHP = wallHP;

        try {
            this.wall = ImageIO.read(getClass().getResource("/resources/00.png"));
            this.breakableWall = ImageIO.read(getClass().getResource("/resources/breakablewall.png"));
            if (wallHP <= 3 || wallHP >= 5) {
                this.img = this.wall;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        this.active = true;

        r = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }


    @Override
    public void update() {
        if (wallHP <= 2) {
            this.vx = 0;
            this.vy = 0;
            this.img = breakableWall;
        }
        r.setLocation(x, y);
    }

    @Override
    public void collision(Class c) {

        if (this.wallHP < 9) {
            if (c.equals(Bullet.class)) {
                this.wallHP -= 1;
                if (wallHP <= 0) {
                    this.active = false;
                }
            }

            if (c.equals(Wall.class) || c.equals(Tank.class)) {
                this.vx = -this.vx;
                this.vy = -this.vy;
                this.x += this.vx;
                this.y += this.vy;
            }
        }

    }


}