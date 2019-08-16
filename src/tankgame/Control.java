package tankgame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Control implements KeyListener {
    private final int up;
    private final int down;
    private final int right;
    private final int left;
    private final int shoot;
    private Tank tank;

    Control(Tank tank, int up, int down, int left, int right, int shoot) {
        this.tank = tank;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == up) {
            this.tank.setUpKeyPressed(true);
        }
        if (key == down) {
            this.tank.setDownKeyPressed(true);
        }
        if (key == left) {
            this.tank.setLeftKeyPressed(true);
        }
        if (key == right) {
            this.tank.setRightKeyPressed(true);
        }
        if (key == shoot) {
            this.tank.setShootKeyPressed(true);
        }
    }


    @Override
    public void keyReleased(KeyEvent ke) {
        int key = ke.getKeyCode();
        if (key == up) {
            this.tank.setUpKeyPressed(false);
        }
        if (key == down) {
            this.tank.setDownKeyPressed(false);
        }
        if (key == left) {
            this.tank.setLeftKeyPressed(false);
        }
        if (key == right) {
            this.tank.setRightKeyPressed(false);
        }
        if (key == shoot) {
            this.tank.setShootKeyPressed(false);
        }
    }
}
