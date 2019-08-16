package tankgame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;


public class Bomb extends GameObjects {
    private int bombHP;
    private BufferedImage breakableWall;
    private BufferedImage explosionImg;
    public Bomb(int x, int y, int bombHP) {
        this.x = x;
        this.y = y;

        Random rand = new Random();
        this.vx = rand.nextInt(3) -1;
        this.vy = rand.nextInt(3) -1;

        this.angle = 0;
        this.bombHP = bombHP;
        try {
            this.img = ImageIO.read(getClass().getResource("/resources/bomb64.png"));
            this.breakableWall = ImageIO.read(getClass().getResource("/resources/breakablewall.png"));
            this.explosionImg = ImageIO.read(getClass().getResource("/resources/Explosion_large.gif"));
            if (bombHP <= 1) {
                this.img = this.breakableWall;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        this.active = true;

        r = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }


    @Override
    public void update() {
        if (bombHP <= 1) {
            this.vx = 0;
            this.vy = 0;
            this.img = breakableWall;
        }

        this.x += this.vx;
        this.y += this.vy;
        r.setLocation(x, y);
    }

    @Override
    public void collision(Class c) {

        if (this.bombHP < 9) {
            if (c.equals(Bullet.class)) {
                this.bombHP -= 1;
                if (bombHP <= 0) {
                    this.active = false;
                }
            }

            if (c.equals(Wall.class)) {
                this.vx = -this.vx;
                this.vy = -this.vy;
                this.x += this.vx;
                this.y += this.vy;
            }
            if (c.equals(Tank.class)) {
                this.img = explosionImg;
                this.active = false;
            }
            if (c.equals(Bomb.class)) {
                this.vx = -this.vx;
                this.vy = -this.vy;
                this.x += this.vx;
                this.y += this.vy;;
            }
        }

    }


}