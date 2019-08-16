package tankgame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Shield extends GameObjects {
    private BufferedImage shield;

    public Shield(int x, int y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        this.active = true;
        try {
            this.img = ImageIO.read(getClass().getResource("/resources/Shield1.png"));
            //this.shield = ImageIO.read(getClass().getResource("/resources/ShieldProtection.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        r = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }


    @Override
    public void update() {

    }

    @Override
    public void collision(Class c) {
        if (c.equals(Tank.class)) {
            this.active = false;
        }
    }
}

