package tankgame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;


public class Background extends GameObjects {

    public Background(int x, int y) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;
        try {
            this.img = ImageIO.read(getClass().getResource("/resources/grass.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        this.active = true;
        r = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }


    @Override
    public void update() {

    }

    @Override
    public void collision(Class c) {

    }


}
