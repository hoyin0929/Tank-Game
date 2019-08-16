package tankgame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class Game extends JPanel {


    public static final int WORLD_WIDTH = 2240;
    public static final int WORLD_HEIGHT = 1020;

    private static final int SCREEN_WIDTH = 1280;
    private static final int SCREEN_HEIGHT = 720;

    private Tank t1;
    private Tank t2;

    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jf;

    private int greenValue  = 255;

    private BufferedImage heart = null;
    private BufferedImage shield = null;
    private BufferedImage wall = null;
    private BufferedImage breakableWall = null;


    private ArrayList<GameObjects> ActiveGameObjects;


    public static void main(String[] args) {
        Game GameWorld = new Game();
        GameWorld.init();

        try {
            while (GameWorld.t1.getLivesRemaining() > 0 && GameWorld.t2.getLivesRemaining() > 0) {

                int i = 0;
                // removes bullets if collided with object or edge.
                while (i < GameWorld.ActiveGameObjects.size()) {
                    GameWorld.ActiveGameObjects.get(i).update();
                    if (!GameWorld.ActiveGameObjects.get(i).isActive()) {
                        GameWorld.ActiveGameObjects.remove(i);
                    } else {
                        i++;
                    }
                }


                for (i = 0; i < GameWorld.ActiveGameObjects.size(); i++) {
                    for (int j = i; j < GameWorld.ActiveGameObjects.size(); j++) {
                        GameObjects obj1 = GameWorld.ActiveGameObjects.get(i);
                        GameObjects obj2 = GameWorld.ActiveGameObjects.get(j);

                        Collision collision = new Collision(obj1, obj2);
                        collision.checkForCollision(); // Also looks ahead if its a Tank and  Wall or  Wall and Tank
                    }
                }

                GameWorld.repaint();

                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {

        }

        long temp = System.currentTimeMillis();
        while (System.currentTimeMillis() - temp < 10000) {
            GameWorld.repaint();
        }
        Window winow = SwingUtilities.getWindowAncestor(GameWorld);
        winow.setVisible(false);
        winow.dispose();

    }

    public void addBullet(Bullet bullet) {
        ActiveGameObjects.add(bullet);
    }


    private void init() {


        this.jf = new JFrame("Tank Game");
        this.world = new BufferedImage(Game.WORLD_WIDTH, Game.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);

        ActiveGameObjects = new ArrayList<>();
        initMap();
        BufferedImage tank1 = null, tank2 = null;
        try {
            tank1 = ImageIO.read(getClass().getResource("/resources/tank1.png"));
            tank2 = ImageIO.read(getClass().getResource("/resources/tank2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        t1 = new Tank(WORLD_WIDTH * 3 / 32, WORLD_HEIGHT * 3 / 32, 90, tank1,this);
        t2 = new Tank(WORLD_WIDTH * 29 / 32, WORLD_HEIGHT * 29 / 32, 270, tank2,this);
        Control tankPlayerControls1 = new Control(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        Control tankPlayerControls2 = new Control(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_Q);

        this.ActiveGameObjects.add(t1);
        this.ActiveGameObjects.add(t2);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);


        this.jf.addKeyListener(tankPlayerControls1);
        this.jf.addKeyListener(tankPlayerControls2);

        this.jf.setSize(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT + 32);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);


    }

    private void initMap() {

        for (int i = 0; i < WORLD_WIDTH; i = i + 320) {
            for (int j = 0; j < WORLD_HEIGHT; j = j + 240) {
                Background b = new Background(i, j);
                ActiveGameObjects.add(b);
            }
        }


        for (int i = WORLD_WIDTH * 2 / 5; i < WORLD_WIDTH * 3 / 5 + 10; i = i + WORLD_WIDTH / 5) {
            for (int j = WORLD_HEIGHT * 2 / 5; j < WORLD_HEIGHT * 3 / 5 + 10; j = j + WORLD_HEIGHT / 5) {
                Health healthPowerUp = new Health(i, j);
                ActiveGameObjects.add(healthPowerUp);
            }
        }

        for (int i = WORLD_WIDTH / 5; i < WORLD_WIDTH * 4 / 5 + 10; i = i + WORLD_WIDTH * 3 / 5) {
            for (int j = WORLD_HEIGHT / 5; j < WORLD_HEIGHT * 4 / 5 + 10; j = j + WORLD_HEIGHT * 3 / 5) {
                Shield shieldPowerUp = new Shield(i, j);
                ActiveGameObjects.add(shieldPowerUp);
            }
        }


        for (int i = 0; i < WORLD_WIDTH; i = i + 32) {
            for (int j = 0; j < WORLD_HEIGHT; j = j + 32) {
                if (i == 0 || (WORLD_WIDTH - i) <= 32 || j == 0 || (WORLD_HEIGHT - j) <= 32) {
                    Wall  frame= new Wall(i, j, 5);
                    ActiveGameObjects.add(frame);

                    // middle of the map walls
                } else if (i < (WORLD_WIDTH / 2 + 17) && i > (WORLD_WIDTH / 2 - 17)) {
                    Wall w = new Wall(i, j, 2);
                    ActiveGameObjects.add(w);
                } else if (j < (WORLD_WIDTH / 2 + 17) && j > (WORLD_WIDTH / 2 - 17)) {
                    Wall w = new Wall(i, j, 2);
                    ActiveGameObjects.add(w);
                } else if (i < (WORLD_WIDTH / 4 + 85) && i > (WORLD_WIDTH / 4 - 85) && j % 9 == 0) {//left
                    Wall w = new Wall(i, j, 2);
                    ActiveGameObjects.add(w);
                } else if (i < (WORLD_WIDTH * 3 / 4 + 85) && i > (WORLD_WIDTH * 3 / 4 - 85) && j % 9 == 0) {//right
                    Wall w = new Wall(i, j, 2);
                    ActiveGameObjects.add(w);
                } else if (i < (WORLD_WIDTH * 3 / 8 + 16) && i > (WORLD_WIDTH * 3 / 8 - 17) && j % 3 == 0) {//left bomb
                    Bomb bomb = new Bomb(i, j, 2);
                    ActiveGameObjects.add(bomb);
                } else if (i < (WORLD_WIDTH * 5 / 8 + 17) && i > (WORLD_WIDTH * 5 / 8 - 16) && j % 3 == 0) {//right bomb
                    Bomb bomb = new Bomb(i, j, 2);
                    ActiveGameObjects.add(bomb);
                }
            }
        }


    }

    /*public static int clamp(int var, int min, int max){
        if(var >= max)
            return var = max;
        else if (var <= min)
            return var = min;
        else

            return var;
    }*/


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        this.buffer = this.world.createGraphics();
        super.paintComponent(g2);

        //if (this.t1.getLivesRemaining() > 0 && this.t2.getLivesRemaining() > 0) {
        for (int i = 0; i < this.ActiveGameObjects.size(); i++) {
            this.ActiveGameObjects.get(i).drawImage(this.buffer);
        }


        // calculating mini map display.
        int sub1x = t1.getX() - SCREEN_WIDTH * 7 / 40;
        if ((t1.getX() - SCREEN_WIDTH * 7 / 40) < 0) {
            sub1x = 0;
        } else if ((t1.getX() + SCREEN_WIDTH * 7 / 40) > (WORLD_WIDTH)) {
            sub1x = WORLD_WIDTH - SCREEN_WIDTH * 7 / 20;
        }

        int sub1y = t1.getY() - SCREEN_HEIGHT / 2;
        if ((t1.getY() - SCREEN_HEIGHT / 2) < 0) {
            sub1y = 0;
        } else if ((t1.getY() + SCREEN_HEIGHT / 2) > (WORLD_HEIGHT)) {
            sub1y = WORLD_HEIGHT - SCREEN_HEIGHT;
        }


        int sub2x = t2.getX() - SCREEN_WIDTH * 7 / 40;
        if ((t2.getX() - SCREEN_WIDTH * 7 / 40) < 0) {
            sub2x = 0;
        } else if ((t2.getX() + SCREEN_WIDTH * 7 / 40) > (WORLD_WIDTH)) {
            sub2x = WORLD_WIDTH - SCREEN_WIDTH * 7 / 20;
        }

        int sub2y = t2.getY() - SCREEN_HEIGHT / 2;
        if ((t2.getY() - SCREEN_HEIGHT / 2) < 0) {
            sub2y = 0;
        } else if ((t2.getY() + SCREEN_HEIGHT / 2) > (WORLD_HEIGHT)) {
            sub2y = WORLD_HEIGHT - SCREEN_HEIGHT;
        }

        BufferedImage sub1 = this.world.getSubimage(sub1x, sub1y, SCREEN_WIDTH * 7 / 20, SCREEN_HEIGHT);
        BufferedImage sub2 = this.world.getSubimage(sub2x, sub2y, SCREEN_WIDTH * 7 / 20, SCREEN_HEIGHT);
        BufferedImage miniMap = this.world.getSubimage(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        g2.drawImage(sub1, 0, 0, null);
        g2.drawImage(sub2, SCREEN_WIDTH * 13 / 20, 0, null);
        g2.drawImage(miniMap.getScaledInstance(SCREEN_WIDTH * 3 / 10, SCREEN_WIDTH * 3 / 10, BufferedImage.TYPE_INT_RGB), SCREEN_WIDTH * 7 / 20, SCREEN_HEIGHT - SCREEN_HEIGHT / 2 - 20, null);
        try {
            heart = ImageIO.read(getClass().getResource("/resources/Heart.png"));
            shield = ImageIO.read(getClass().getResource("/resources/Shield1.png"));
            wall = ImageIO.read(getClass().getResource("/resources/00.png"));
            breakableWall = ImageIO.read(getClass().getResource("/resources/breakablewall.png"));


        } catch (
                IOException e) {
            e.printStackTrace();
        }

        HeadUpDisplay(g);

        gameInstruction(g2);

        end(g2);

    }

    public void HeadUpDisplay(Graphics g){
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.gray);
            g2.fillRect(15, 15,200, 32);
            g2.setColor(new Color(75, greenValue, 0));
            g2.fillRect(15, 15,this.t1.getHealth() * 2, 32);
            g2.setColor(Color.white);
            g2.drawRect(15, 15,200, 32);
            g2.setColor(Color.black);
            g2.drawString("Health: " + this.t1.getHealth(), 15, 64);
            g2.drawString("Lives: " + this.t1.getLivesRemaining(), 115, 64);
            /*for (int i =  this.t1.getLivesRemaining(); i > 0; i--) {
                int x = 155;
                x = x + 50;
                g2.drawImage(heart, x, 45, null);
            }*/ //draw heart represent remaining life

            g2.setColor(Color.gray);
            g2.fillRect(1060, 15,200, 32);
            g2.setColor(new Color(75, greenValue, 0));
            g2.fillRect(1060, 15,this.t2.getHealth() * 2 , 32);
            g2.setColor(Color.white);
            g2.drawRect(1060, 15,200, 32);
            g2.setColor(Color.black);
            g2.drawString("Health: " + this.t2.getHealth(), 1060, 64);
            g2.drawString("Lives: " + this.t2.getLivesRemaining(), 1160, 64);
    }

    public void gameInstruction(Graphics g){
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.GRAY);
        g2.fillRect(SCREEN_WIDTH * 7 / 20, 0, SCREEN_WIDTH * 3 / 10, SCREEN_HEIGHT /2 -15);
        g2.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 50));
        g2.setColor(Color.BLACK);
        g2.drawString("Game Manual: ", SCREEN_WIDTH * 29 / 80, 40);
        g2.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 15));
        g2.drawString("Control for Player 1: "  , SCREEN_WIDTH * 29 / 80, 65);
        g2.drawString("Control for Player 2: "  , SCREEN_WIDTH/2, 65);
        g2.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 12));
        g2.drawString("Forward:          W Key                                            Up Key "  , SCREEN_WIDTH * 29 / 80, 100);
        g2.drawString("Backward:      S Key                                              Down Key "  , SCREEN_WIDTH * 29 / 80, 115);
        g2.drawString("Rotate Left:     A Key                                              Left Key "  , SCREEN_WIDTH * 29 / 80, 130);
        g2.drawString("Rotate Right:   D Key                                             Right Key "  , SCREEN_WIDTH * 29 / 80, 145);
        g2.drawString("Shoot:               Q Key                                             Enter Key "  , SCREEN_WIDTH * 29 / 80, 160);

        g2.drawImage(heart,SCREEN_WIDTH * 29 / 80,190, null  );
        g2.drawString(":   Recovers 20 Health",SCREEN_WIDTH * 29 / 80 + 30,210 );
        g2.drawImage(shield,SCREEN_WIDTH * 29 / 80 + 5,215, 16,16,null  );
        g2.drawString(":   Immune From 1 Attack (either boom or bullet) ",SCREEN_WIDTH * 29 / 80 + 30,230 );
        g2.drawString("Each bullet causes 20 damage; Each bomb cause 50 damage",SCREEN_WIDTH * 29 / 80,250 );
        g2.drawImage(wall,SCREEN_WIDTH * 29 / 80 + 5,265, 16,16,null  );
        g2.drawImage(breakableWall,SCREEN_WIDTH * 29 / 80 + 5,285, 16,16,null  );
        g2.drawString(": Unbreakable Wall ",SCREEN_WIDTH * 29 / 80 + 30,275 );
        g2.drawString(": Breakable Wall",SCREEN_WIDTH * 29 / 80 + 30,295 );

    }

    public void end(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        if (this.t1.getLivesRemaining() == 0) {
            g2.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 84));
            g2.setColor(Color.RED);
            g2.drawString("PLAYER 2 WINS", SCREEN_WIDTH /3 - 100, SCREEN_HEIGHT/2);
        } else if (this.t2.getLivesRemaining() == 0) {
            g2.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 84));
            g2.setColor(Color.RED);
            g2.drawString("PLAYER 1 WINS", SCREEN_WIDTH / 3 - 100, SCREEN_HEIGHT / 2);
        }

    }




}