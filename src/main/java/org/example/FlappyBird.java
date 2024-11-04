import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird extends JPanel implements ActionListener {
    private final int WIDTH = 400, HEIGHT = 600;
    private final int BIRD_DIAMETER = 34; // Измените на ширину текстуры птицы
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = -15;
    private final int PIPE_WIDTH = 52; // Измените на ширину текстуры трубы
    private final int PIPE_GAP = 150;

    private Image birdImage;
    private Image pipeImage;
    private Image backgroundImage;

    private int birdY, birdVelocity, score;
    private ArrayList<Rectangle> pipes;
    private boolean gameOver;

    public FlappyBird() {
        loadImages();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.cyan);
        setFocusable(true);
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        pipes = new ArrayList<>();
        gameOver = false;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                    birdVelocity = JUMP_STRENGTH;
                } else if (gameOver) {
                    resetGame();
                }
            }
        });

        Timer timer = new Timer(20, this);
        timer.start();
        spawnPipe();
    }

    private void loadImages() {
        birdImage = new ImageIcon("path/to/bird.png").getImage(); // Путь к текстуре птицы
        pipeImage = new ImageIcon("path/to/pipe.png").getImage(); // Путь к текстуре трубы
        backgroundImage = new ImageIcon("path/to/background.png").getImage(); // Путь к текстуре фона
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        g.drawImage(birdImage, 100, birdY, BIRD_DIAMETER, BIRD_DIAMETER, null);
        for (Rectangle pipe : pipes) {
            g.drawImage(pipeImage, pipe.x, pipe.y, PIPE_WIDTH, pipe.height, null);
        }
        g.setColor(Color.black);
        g.drawString("Score: " + score, 10, 20);
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over", WIDTH / 4, HEIGHT / 2);
            g.drawString("Press SPACE to Restart", WIDTH / 8, HEIGHT / 2 + 40);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            birdY += birdVelocity;
            birdVelocity += GRAVITY;

            for (int i = 0; i < pipes.size(); i++) {
                Rectangle pipe = pipes.get(i);
                pipe.x -= 5;

                if (pipe.x + PIPE_WIDTH < 0) {
                    pipes.remove(i);
                    i--;
                    score++;
                }

                if (pipe.intersects(new Rectangle(100, birdY, BIRD_DIAMETER, BIRD_DIAMETER)) || birdY > HEIGHT) {
                    gameOver = true;
                }
            }

            if (pipes.isEmpty() || pipes.get(pipes.size() - 1).x < WIDTH - 200) {
                spawnPipe();
            }
        }
        repaint();
    }

    private void spawnPipe() {
        int height = new Random().nextInt(300) + 100;
        pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, height));
        pipes.add(new Rectangle(WIDTH, height + PIPE_GAP, PIPE_WIDTH, HEIGHT - height - PIPE_GAP));
    }

    private void resetGame() {
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        pipes.clear();
        gameOver = false;
        spawnPipe();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird gamePanel = new FlappyBird();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
