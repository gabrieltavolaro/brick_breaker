import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;

    // Set game rows and columns
    private int row = 3, col = 7;
    private int totalBricks = row * col;

    private Timer timer;
    private int delay = 8;

    // Player position
    private int playerX = 310;

    // Ball position and direction
    private int ballX = 120;
    private int ballY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    Gameplay() {
        map = new MapGenerator(row, col);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();

    }

    // Draw everything
    public void paint(Graphics g) {

        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Drawing map
        map.draw((Graphics2D)g);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // Paddle
        g.setColor(Color.blue);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballX, ballY, 20, 20);

        // Game ends
        if(totalBricks <= 0) {

            // Stop the ball
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            // Game over title
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("YOU WON! Score: " + score, 190, 300);

            // Restart title
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter do Restart", 235, 330);
        }

        // Ball goes down
        if(ballY > 570) {

            // Stop the ball
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            // Game over title
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over! Score: " + score, 190, 300);

            // Restart title
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter do Restart", 235, 330);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if(play) {

            // Collision with paddle
            if(new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            // Collision with bricks
            A: for(int i = 0; i < map.map.length; i++) {
                for(int j = 0; j < map.map[0].length; j++) {
                    if(map.map[i][j] > 0) {

                        // Get variables from MapGenerator
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        // Rectangle for collision
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballX, ballY, 20, 20);
                        Rectangle brickRect = rect;

                        // Collision detection
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            // Score
                            score += 5;

                            // Ball direction
                            if(ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }

                    }
                }
            }

            // Ball animation
            ballX += ballXdir;
            ballY += ballYdir;

            // Ball direction when collide
            if(ballX < 0) {
                ballXdir = -ballXdir;
            }
            if(ballY < 0) {
                ballYdir = -ballYdir;
            }
            if(ballX > 670) {
                ballXdir = -ballXdir;
            }
        }

        repaint();
    }



    @Override
    public void keyPressed(KeyEvent e) {

        // Click right key
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }

        // Click left key
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX <= 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        // Enter to restart
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!play) {
                // Restart game
                play = true;
                ballX = 120;
                ballY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                map = new MapGenerator(row, col);
                totalBricks = row * col;
                repaint();
            }
        }

    }

    // Move methods
    private void moveRight() {
        play = true;
        playerX += 20;
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
    }

    // Non-used
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }
}
