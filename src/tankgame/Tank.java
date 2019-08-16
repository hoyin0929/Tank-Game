package tankgame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Tank extends GameObjects {
    private final int R = 3;
    private final int rotationSpeed = 2;
    private boolean upKeyPressed, downKeyPressed, leftKeyPressed, rightKeyPressed, shootKeyPressed;
    private int timeSinceLastBullet, health, livesRemaining;
    private int origX, origY, origAngle;
    private boolean canMoveUp, canMoveDown, canMoveLeft, canMoveRight;
    private Game tankGame;
    private boolean shielded;


    public Tank(int x, int y, int angle, BufferedImage img, Game tankGame) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
        this.img = img;
        /*try {
            this.img = ImageIO.read(getClass().getResource("/Resources/tank1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        this.tankGame = tankGame;

        this.canMoveDown = true;
        this.canMoveLeft = true;
        this.canMoveRight = true;
        this.canMoveUp = true;

        this.active = true;
        this.shielded = false;
        this.timeSinceLastBullet = 1000;
        this.health = 100;
        this.livesRemaining = 3;

        this.downKeyPressed = false;
        this.leftKeyPressed = false;
        this.rightKeyPressed = false;
        this.upKeyPressed = false;
        this.shootKeyPressed = false;

        this.origAngle = angle;
        this.origX = x;
        this.origY = y;

        r = new Rectangle(x, y, img.getWidth(), img.getHeight());
    }

    public void update() {


        timeSinceLastBullet++;

        if (this.upKeyPressed) {
            this.moveForwards();
        }
        if (this.downKeyPressed) {
            this.moveBackwards();
        }

        if (this.leftKeyPressed) {
            this.rotateLeft();
        }
        if (this.rightKeyPressed) {
            this.rotateRight();
        }
        if (this.shootKeyPressed) {
            if (this.timeSinceLastBullet > 69) {
                Bullet bullet = new Bullet(this.x, this.y, this.angle, this);
                this.tankGame.addBullet(bullet);
                this.timeSinceLastBullet = 0;
            }
        }

        r.setLocation(x, y);

        canMoveRight = true;
        canMoveUp = true;
        canMoveLeft = true;
        canMoveDown = true;

    }

    @Override
    public void collision(Class c) {

        if (c.equals(Tank.class)) {
            x = x - 10 * vx;
            y = y - 10 * vy;
            vx = 0;
            vy = 0;
        }else if (c.equals(Wall.class)){
            x = x - 10 * vx;
            y = y - 10 * vy;
            vx = 0;
            vy = 0;
         }else if (c.equals(Bullet.class)) {
            if (shielded){
                shielded = false;
            }
            if (!shielded) {
                health = health - 20;
            }
        } else if (c.equals(Health.class)) {
            health = health + 20;
            if (health > 100) health = 100;
        } else if (c.equals(Shield.class)) {
            shielded = true;
        } else if (c.equals(Bomb.class)) {
            if (shielded){shielded = false;}
            else { health = health - 50; }
        }

        if (health <= 0) {
            livesRemaining--;
            health = 100;
            x = origX;
            y = origY;
            angle = origAngle;
            vx = 0;
            vy = 0;
        }
    }


    private void rotateLeft() {
        this.angle -= this.rotationSpeed;
        if (this.angle < 0) {
            this.angle += 360;
        } else if (this.angle > 360) {
            this.angle -= 360;
        }
    }

    private void rotateRight() {
        this.angle += this.rotationSpeed;
        if (this.angle < 0) {
            this.angle += 360;
        } else if (this.angle > 360) {
            this.angle -= 360;
        }
    }

    private void moveBackwards() {
        if (angle >= 315 || angle < 45) { // facing right
            if (canMoveLeft) {
                vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
                vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            } else {
                vx = 0;
                vy = 0;
            }
        }
        if (angle >= 45 && angle < 135) { // facing down
            if (canMoveUp) {
                vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
                vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            } else {
                vx = 0;
                vy = 0;
            }
        }
        if (angle >= 135 && angle < 225) { // facing left
            if (canMoveRight) {
                vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
                vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            } else {
                vx = 0;
                vy = 0;
            }
        }
        if (angle >= 225 && angle < 315) { // facing up
            if (canMoveDown) {
                vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
                vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            } else {
                vx = 0;
                vy = 0;
            }
        }
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {

        if (angle >= 315 || angle < 45) { // facing right
            if (canMoveRight) {
                vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
                vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            } else {
                vx = 0;
                vy = 0;
            }
        }
        if (angle >= 45 && angle < 135) { // facing down
            if (canMoveDown) {
                vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
                vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            } else {
                vx = 0;
                vy = 0;
            }
        }
        if (angle >= 135 && angle < 225) { // facing left
            if (canMoveLeft) {
                vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
                vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            } else {
                vx = 0;
                vy = 0;
            }
        }
        if (angle >= 225 && angle < 315) { // facing up
            if (canMoveUp) {
                vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
                vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
            } else {
                vx = 0;
                vy = 0;
            }
        }

        x += vx;
        y += vy;
        checkBorder();
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= Game.WORLD_WIDTH - 88) {
            x = Game.WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= Game.WORLD_HEIGHT - 80) {
            y = Game.WORLD_HEIGHT - 80;
        }
    }

    public void setCanMoveUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }

    public void setCanMoveDown(boolean canMoveDown) {
        this.canMoveDown = canMoveDown;
    }

    public void setCanMoveLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
    }

    public void setCanMoveRight(boolean canMoveRight) {
        this.canMoveRight = canMoveRight;
    }

    public void setLeftKeyPressed(boolean leftKeyPressed) {
        this.leftKeyPressed = leftKeyPressed;
    }

    public void setUpKeyPressed(boolean upKeyPressed) {
        this.upKeyPressed = upKeyPressed;
    }

    public void setDownKeyPressed(boolean downKeyPressed) {
        this.downKeyPressed = downKeyPressed;
    }

    public void setRightKeyPressed(boolean rightKeyPressed) {
        this.rightKeyPressed = rightKeyPressed;
    }

    public void setShootKeyPressed(boolean shootKeyPressed) {
        this.shootKeyPressed = shootKeyPressed;
    }

    public int getHealth() {
        return health;
    }

    public int getLivesRemaining() {
        return livesRemaining;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle + ", health=" + health;
    }

}
