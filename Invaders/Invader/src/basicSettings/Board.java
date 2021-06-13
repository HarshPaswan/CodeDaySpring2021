package basicSettings;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import avatarCommands.Plane;
import avatarCommands.Player;
import avatarCommands.Shot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {

    private Dimension dim;
    private List<Plane> planes;
    private Player p;
    private Shot shot;
    public static boolean repeat = false;
    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String explImg = "src/images/explosion.png";
    private String message = "Game Over";

    private Timer timer;


    public Board() {

        initBoard();
        gameInit();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        dim = new Dimension(Coords.BOARD_WIDTH, Coords.BOARD_HEIGHT);
        setBackground(new Color(100, 250, 234));
        timer = new Timer(Coords.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }


    private void gameInit() {

        planes = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {

                var alien = new Plane(Coords.ALIEN_INIT_X + 18 * j,
                        Coords.ALIEN_INIT_Y + 18 * i);
                planes.add(alien);
            }
        }

        p = new Player();
        shot = new Shot();
    }

    private void drawPlane(Graphics g) {

        for (Plane a : planes) {

            if (a.isVisible()) {

                g.drawImage(a.getImage(), a.getX(), a.getY(), this);
            }

            if (a.isDying()) {

                a.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (p.isVisible()) {

            g.drawImage(p.getImage(), p.getX(), p.getY(), this);
        }

        if (p.isDying()) {

            p.die();
            inGame = false;
        }
    }

    private void drawShot(Graphics g) {

        if (shot.isVisible()) {

            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    private void drawBombing(Graphics g) {

        for (Plane a : planes) {

            Plane.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {

                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(new Color(100, 250, 234));
        g.fillRect(0, 0, dim.width, dim.height);
        g.setColor(Color.yellow);
        g.fillOval(0, 0, 80, 80);
        g.setColor(Color.green);

        if (inGame) {

            g.drawLine(0, Coords.GROUND,
                    Coords.BOARD_WIDTH, Coords.GROUND);

            drawPlane(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

    	g.setColor(new Color(100, 250, 234));
        g.fillRect(0, 0, Coords.BOARD_WIDTH, Coords.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Coords.BOARD_WIDTH / 2 - 30, Coords.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Coords.BOARD_WIDTH / 2 - 30, Coords.BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Coords.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Coords.BOARD_WIDTH / 2);
    }

    private void update() {

        if (deaths == Coords.NUMBER_OF_ALIENS_TO_DESTROY) {
        	repeat = true;
            inGame = false;
            timer.stop();
            message = "Game won!";
        }

       
        p.act();

        
        if (shot.isVisible()) {

            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Plane alien : planes) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Coords.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Coords.ALIEN_HEIGHT)) {

                        var ii = new ImageIcon(explImg);
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        for (Plane alien : planes) {

            int x = alien.getX();

            if (x >= Coords.BOARD_WIDTH - Coords.BORDER_RIGHT && direction != -1) {

                direction = -1;

                Iterator<Plane> i1 = planes.iterator();

                while (i1.hasNext()) {

                    Plane a2 = i1.next();
                    a2.setY(a2.getY() + Coords.GO_DOWN);
                }
            }

            if (x <= Coords.BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator<Plane> i2 = planes.iterator();

                while (i2.hasNext()) {

                    Plane a = i2.next();
                    a.setY(a.getY() + Coords.GO_DOWN);
                }
            }
        }

        Iterator<Plane> it = planes.iterator();

        while (it.hasNext()) {

            Plane alien = it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > Coords.GROUND - Coords.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "No where to run! Captured!";
                }

                alien.act(direction);
            }
        }

        
        var generator = new Random();

        for (Plane alien : planes) {

            int shot = generator.nextInt(15);
            Plane.Bomb bomb = alien.getBomb();

            if (shot == Coords.CHANCE && alien.isVisible() && bomb.isDestroyed()) {

                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            int bombX = bomb.getX();
            int bombY = bomb.getY();
            int playerX = p.getX();
            int playerY = p.getY(); 

            if (p.isVisible() && !bomb.isDestroyed()) {

                if (bombX >= (playerX)
                        && bombX <= (playerX + Coords.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Coords.PLAYER_HEIGHT)) {

                    var ii = new ImageIcon(explImg);
                    p.setImage(ii.getImage());
                    p.setDying(true);
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {

                bomb.setY(bomb.getY() + 1);

                if (bomb.getY() >= Coords.GROUND - Coords.BOMB_HEIGHT) {

                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            p.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            p.keyPressed(e);

            int x = p.getX();
            int y = p.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {

                if (inGame) {

                    if (!shot.isVisible()) {

                        shot = new Shot(x, y);
                    }
                }
            }
        }
    }
}
