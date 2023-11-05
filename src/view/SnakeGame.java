package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import saveData.SaveData;
import score.Score;
import score.ScoreData;

public class SnakeGame extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int TILE_SIZE = 10;
    private int previousDirection;

    private LinkedList<Point> snake;
    private Point food;
    private int direction;
    private boolean isRunning;
    private Score score;
    private JLabel scoreLabel;
    private List<Obstacle> obstacles;

    private long lastFoodMoveTime;
    private long lastObstacleMoveTime;

    private JFrame mainFrame;
    private int initialDelay;

    public SnakeGame(JFrame mainFrame, int initialDelay) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        this.initialDelay = initialDelay;
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                changeDirection(e.getKeyCode());
            }
        });

        snake = new LinkedList<>();
        initializeGame();
        score = new Score();
        obstacles = new ArrayList<>();
        initializeObstacles();
        scoreLabel = new JLabel("Score: " + score.getScore());
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(scoreLabel, BorderLayout.NORTH);

        Thread snakeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    move();
                    checkCollision();
                    checkObstacleCollision();
                    updateScoreLabel();
                    repaint();
                    int delay = initialDelay - Math.min(score.getScore() / 5, 15) * 5;

                    if (score.getScore() % 5 == 0) {
                        delay = Math.max(delay - 10, 10);
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        snakeThread.start();
        Thread foodThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastFoodMoveTime >= 30000) {
                        moveFood();
                        lastFoodMoveTime = currentTime;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        foodThread.start();

        Thread obstacleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastObstacleMoveTime >= 20000) {
                        changeObstaclePositions();
                        lastObstacleMoveTime = currentTime;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        obstacleThread.start();
    }

    private void initializeGame() {
        snake.clear();
        snake.add(new Point(10, 10));
        food = new Point(5, 5);
        direction = KeyEvent.VK_RIGHT;
        isRunning = true;
        initialDelay = 100;
    }

    private void changeDirection(int newDirection) {
        if (newDirection == KeyEvent.VK_LEFT && previousDirection != KeyEvent.VK_RIGHT) {
            direction = KeyEvent.VK_LEFT;
        }
        if (newDirection == KeyEvent.VK_RIGHT && previousDirection != KeyEvent.VK_LEFT) {
            direction = KeyEvent.VK_RIGHT;
        }
        if (newDirection == KeyEvent.VK_UP && previousDirection != KeyEvent.VK_DOWN) {
            direction = KeyEvent.VK_UP;
        }
        if (newDirection == KeyEvent.VK_DOWN && previousDirection != KeyEvent.VK_UP) {
            direction = KeyEvent.VK_DOWN;
        }
    }

    private void move() {
        previousDirection = direction;
        Point head = snake.getFirst();
        Point newHead = head;
        switch (direction) {
            case KeyEvent.VK_LEFT:
                newHead = new Point((head.x - 1 + WIDTH / TILE_SIZE) % (WIDTH / TILE_SIZE), head.y);
                break;
            case KeyEvent.VK_RIGHT:
                newHead = aPoint((head.x + 1) % (WIDTH / TILE_SIZE), head.y);
                break;
            case KeyEvent.VK_UP:
                newHead = aPoint(head.x, (head.y - 1 + HEIGHT / TILE_SIZE) % (HEIGHT / TILE_SIZE));
                break;
            case KeyEvent.VK_DOWN:
                newHead = aPoint(head.x, (head.y + 1) % (HEIGHT / TILE_SIZE));
                break;
        }

        snake.addFirst(newHead);
        if (newHead.equals(food)) {
            createFood();
            score.increaseScore();
        } else {
            snake.removeLast();
        }
    }

    private void createFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(WIDTH / TILE_SIZE);
            y = random.nextInt(HEIGHT / TILE_SIZE);
        } while (snake.contains(aPoint(x, y)));
        food.setLocation(x, y);
    }

    private void checkCollision() {
        Point head = snake.getFirst();
        if (snake.subList(1, snake.size()).contains(head)) {
            isRunning = false;
        }
    }

    private void updateScoreLabel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                scoreLabel.setText("Score: " + score.getScore());
            }
        });
    }

    private void initializeObstacles() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int obstacleX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
            int obstacleY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
            int obstacleWidth = TILE_SIZE;
            int obstacleHeight = TILE_SIZE;
            Obstacle obstacle = new Obstacle(obstacleX, obstacleY, obstacleWidth, obstacleHeight);
            obstacles.add(obstacle);
        }
    }

    private void checkObstacleCollision() {
        Rectangle snakeBounds = new Rectangle(snake.get(0).x * TILE_SIZE, snake.get(0).y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = obstacle.getBounds();
            if (obstacleBounds.intersects(snakeBounds)) {
                isRunning = false;
            }
        }
    }

    private void moveFood() {
        Random random = new Random();
        int newX, newY;
        do {
            newX = random.nextInt(WIDTH / TILE_SIZE);
            newY = random.nextInt(HEIGHT / TILE_SIZE);
        } while (snake.contains(aPoint(newX, newY)));
        food.setLocation(newX, newY);
    }

    private void changeObstaclePositions() {
        Random random = new Random();
        for (Obstacle obstacle : obstacles) {
            int newX, newY;
            do {
                newX = random.nextInt(WIDTH / TILE_SIZE) * TILE_SIZE;
                newY = random.nextInt(HEIGHT / TILE_SIZE) * TILE_SIZE;
            } while (isOccupiedBySnake(newX, newY));
            obstacle.setPosition(newX, newY);
        }
    }

    private boolean isOccupiedBySnake(int x, int y) {
        for (Point point : snake) {
            if (point.x == x && point.y == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isRunning) {
            drawSnake(g);
            drawFood(g);
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(g);
            }
        } else {
            gameOver(g);
        }
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Game Over", WIDTH / 2 - 75, HEIGHT / 2 - 12);

        if (!isRunning) {
            ScoreDataInput scoreDataInput = new ScoreDataInput((JFrame) SwingUtilities.getWindowAncestor(this), score.getScore());
            scoreDataInput.setVisible(true);
            ScoreData newScoreData = scoreDataInput.getScoreData();

            if (newScoreData != null) {
                SaveData.saveUserData(newScoreData);
            }

            SwingUtilities.getWindowAncestor(this).setVisible(false);

            mainFrame.setVisible(true);
        }
    }

    private Point aPoint(int x, int y) {
        return new Point(x, y);
    }

    public void resizeGameArea(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        revalidate();
        repaint();
    }


    public int getGameWidth() {
        return WIDTH;
    }

    public int getGameHeight() {
        return HEIGHT;
    }
}
