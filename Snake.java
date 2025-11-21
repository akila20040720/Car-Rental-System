import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/**
 * Snake.java
 *
 * A single-file, object-oriented Snake game implemented using Java Swing.
 *
 * Design overview (OOP applied):
 * - Public class Snake contains main() (entry point) and starts the Game.
 * - Game class contains the game loop and high-level game state management.
 * - GamePanel extends JPanel and handles drawing and input delegation.
 * - SnakeSegment represents one square of the snake's body.
 * - Food is a simple value-type representing the food's position.
 * - Direction is an enum for the four possible movement directions.
 *
 * The code is deliberately written with thorough comments that explain the theory
 * and purpose of each field, method and important code block.
 */
public class Snake {

    // Entry point of the program.
    // We keep main minimal: create a Game and start it on the Swing event thread.
    public static void main(String[] args) {
        // Swing utilities ensure GUI creation runs on Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(() -> {
            Game game = new Game(30, 20, 20); // gridWidth, gridHeight, cellSize
            game.start();
        });
    }
}

/**
 * Game: high-level controller of the Snake application.
 *
 * Responsibilities:
 * - Create window (JFrame) and GamePanel
 * - Own shared configuration (grid size, cell pixel size)
 * - Start and stop the game
 *
 * This centralizes application configuration and separates UI (GamePanel) from
 * application control (Game). This separation of concerns is an OOP best practice.
 */
class Game {
    // Grid (logical) dimensions in number of cells
    private final int gridWidth;
    private final int gridHeight;
    // Visual size of each grid cell in pixels
    private final int cellSize;

    private final JFrame window;
    private final GamePanel panel;

    Game(int gridWidth, int gridHeight, int cellSize) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.cellSize = cellSize;

        // Create and configure the window (JFrame)
        window = new JFrame("Snake - Simple OOP Example");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the panel (view + controller for painting and input)
        panel = new GamePanel(this);
        panel.setPreferredSize(new Dimension(gridWidth * cellSize, gridHeight * cellSize));

        window.setContentPane(panel);
        window.pack();
        window.setResizable(false);
        window.setLocationRelativeTo(null); // center on screen
    }

    // Expose configuration for other objects (read-only)
    public int getGridWidth() { return gridWidth; }
    public int getGridHeight() { return gridHeight; }
    public int getCellSize() { return cellSize; }

    // Show the window and start the game loop inside GamePanel
    public void start() {
        window.setVisible(true);
        panel.startGameLoop();
    }
}

/**
 * GamePanel: JPanel responsible for rendering and user input.
 *
 * Key responsibilities:
 * - Maintain Game state: snake body, direction, food, score, running flag.
 * - Update the model on each Timer tick.
 * - Render current game state in paintComponent.
 * - Listen for keyboard input to change direction or restart.
 *
 * This class demonstrates encapsulation: internal fields (model) and functionality
 * (update/draw) are grouped together and exposed only through public methods.
 */
class GamePanel extends JPanel implements ActionListener, KeyListener {

    // Model objects stored as fields: snake body and food.
    private final ArrayList<SnakeSegment> snake = new ArrayList<>();
    private Food food;

    // The current movement direction of the snake.
    private Direction direction = Direction.RIGHT; // initial movement to the right
    // A queue flag to prevent reversing direction in the same tick.
    private boolean directionLocked = false;

    // Game state variables
    private boolean running = false;
    private int score = 0;

    private final Random rand = new Random();
    private final Game game; // reference to parent for grid sizes

    // Timer is used to create the game loop in Swing.
    // Swing Timers fire ActionEvents on the Event Dispatch Thread.
    private Timer timer;

    // Constructor: store reference to Game for configuration and add input listener
    GamePanel(Game game) {
        this.game = game;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
    }

    // Initialize or reset the game state (called at start and on restart)
    private void initGame() {
        snake.clear();
        // Place a short initial snake centered on the grid.
        int startX = game.getGridWidth() / 2;
        int startY = game.getGridHeight() / 2;
        // Add three segments: head + two body parts
        snake.add(new SnakeSegment(startX, startY));
        snake.add(new SnakeSegment(startX - 1, startY));
        snake.add(new SnakeSegment(startX - 2, startY));

        direction = Direction.RIGHT;
        directionLocked = false;
        score = 0;
        running = true;

        spawnFood();

        // If a timer exists from a previous run, stop it first.
        if (timer != null) timer.stop();
        // Speed (delay in ms) controls difficulty. Lower = faster.
        int delayMs = 150; // ~8.3 ticks per second
        timer = new Timer(delayMs, this);
    }

    // Start the Swing Timer game loop
    public void startGameLoop() {
        initGame();
        timer.start();
    }

    // Game loop tick handler (called by Timer on EDT)
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running) return;

        update(); // update model (movement, collisions, growth)
        repaint(); // request re-render
        directionLocked = false; // allow direction change next tick
    }

    // Update game model per tick
    private void update() {
        moveSnake();
        checkFoodCollision();
        checkSelfCollision();
        checkWallCollision();
    }

    // Move the snake by creating a new head and removing the tail unless we ate food
    private void moveSnake() {
        SnakeSegment head = snake.get(0);
        int newX = head.x + direction.dx;
        int newY = head.y + direction.dy;
        SnakeSegment newHead = new SnakeSegment(newX, newY);

        // Insert new head at front (index 0)
        snake.add(0, newHead);
        // Unless we just ate food this tick, remove last segment to keep length constant
        if (food != null && newHead.x == food.x && newHead.y == food.y) {
            // We will handle growth in checkFoodCollision(); keep tail so snake grows
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    // If head overlaps food: increase score, spawn new food, and allow the snake to grow
    private void checkFoodCollision() {
        SnakeSegment head = snake.get(0);
        if (food != null && head.x == food.x && head.y == food.y) {
            score += 10; // arbitrary scoring
            // When we ate the food we already kept the tail in moveSnake() so the snake grew by 1
            spawnFood();
        }
    }

    // Check collision of head with any body segment (self collision)
    private void checkSelfCollision() {
        SnakeSegment head = snake.get(0);
        // Start from 1 to skip head itself
        for (int i = 1; i < snake.size(); i++) {
            SnakeSegment s = snake.get(i);
            if (head.x == s.x && head.y == s.y) {
                // Collision -> stop the game
                running = false;
                timer.stop();
                break;
            }
        }
    }

    // Check collision with walls (grid boundary). If hit -> game over
    private void checkWallCollision() {
        SnakeSegment head = snake.get(0);
        if (head.x < 0 || head.x >= game.getGridWidth() || head.y < 0 || head.y >= game.getGridHeight()) {
            running = false;
            timer.stop();
        }
    }

    // Randomly place food on a free grid cell (not occupied by the snake)
    private void spawnFood() {
        while (true) {
            int fx = rand.nextInt(game.getGridWidth());
            int fy = rand.nextInt(game.getGridHeight());
            boolean collision = false;
            for (SnakeSegment s : snake) {
                if (s.x == fx && s.y == fy) { collision = true; break; }
            }
            if (!collision) { food = new Food(fx, fy); return; }
            // else loop and try another random position
        }
    }

    // Paint the current game state. Swing calls this on EDT.
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Draw grid (optional visual guide). Keep light and unobtrusive.
        drawGrid(g2);

        // Draw food
        if (food != null) {
            drawCell(g2, food.x, food.y, Color.RED);
        }

        // Draw snake: head brighter than body
        for (int i = 0; i < snake.size(); i++) {
            SnakeSegment s = snake.get(i);
            if (i == 0) drawCell(g2, s.x, s.y, Color.GREEN.brighter());
            else drawCell(g2, s.x, s.y, Color.GREEN.darker());
        }

        // Draw score and messages
        drawHUD(g2);

        g2.dispose();
    }

    // Draw a single grid cell at logical coordinates (x,y) with the given color
    private void drawCell(Graphics2D g2, int x, int y, Color color) {
        int px = x * game.getCellSize();
        int py = y * game.getCellSize();
        g2.setColor(color);
        g2.fillRect(px, py, game.getCellSize(), game.getCellSize());
        // Draw a subtle border to separate cells visually
        g2.setColor(Color.BLACK);
        g2.drawRect(px, py, game.getCellSize(), game.getCellSize());
    }

    // Draw a faint grid to help the player see cells
    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 80)); // semi-transparent
        int w = game.getGridWidth() * game.getCellSize();
        int h = game.getGridHeight() * game.getCellSize();
        for (int x = 0; x <= w; x += game.getCellSize()) g2.drawLine(x, 0, x, h);
        for (int y = 0; y <= h; y += game.getCellSize()) g2.drawLine(0, y, w, y);
    }

    // Heads-up display: score and game over message
    private void drawHUD(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        g2.drawString("Score: " + score, 8, 16);

        if (!running) {
            String msg = "Game Over - Press R to Restart";
            FontMetrics fm = g2.getFontMetrics();
            int w = fm.stringWidth(msg);
            int x = (getWidth() - w) / 2;
            int y = getHeight() / 2;
            g2.drawString(msg, x, y);
        }
    }

    // KeyListener methods: handle user input.
    // We change direction with arrow keys or WASD and restart with R when dead.
    @Override
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();
        // Prevent changing direction multiple times within one tick by using directionLocked
        if (!directionLocked) {
            if ((kc == KeyEvent.VK_LEFT || kc == KeyEvent.VK_A) && direction != Direction.RIGHT) {
                direction = Direction.LEFT; directionLocked = true;
            } else if ((kc == KeyEvent.VK_RIGHT || kc == KeyEvent.VK_D) && direction != Direction.LEFT) {
                direction = Direction.RIGHT; directionLocked = true;
            } else if ((kc == KeyEvent.VK_UP || kc == KeyEvent.VK_W) && direction != Direction.DOWN) {
                direction = Direction.UP; directionLocked = true;
            } else if ((kc == KeyEvent.VK_DOWN || kc == KeyEvent.VK_S) && direction != Direction.UP) {
                direction = Direction.DOWN; directionLocked = true;
            }
        }

        // Restart if game over
        if (!running && (kc == KeyEvent.VK_R)) {
            initGame();
            timer.start();
            repaint();
        }
    }

    // Unused but required by KeyListener
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}

/**
 * SnakeSegment: small value object representing one cell of the snake's body.
 *
 * Theory: In OOP, small classes that model concrete domain entities (here a
 * coordinate on the grid) help keep code readable and maintainable. This class
 * is immutable (fields are final) to avoid accidental changes to positions.
 */
class SnakeSegment {
    public final int x;
    public final int y;

    SnakeSegment(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

/**
 * Food: simple data object representing food position.
 */
class Food {
    public final int x;
    public final int y;

    Food(int x, int y) { this.x = x; this.y = y; }
}

/**
 * Direction enum: encapsulates movement vectors for each direction.
 *
 * Using an enum here is safer and clearer than integer constants. Each enum
 * instance carries the dx,dy offsets used when moving the snake.
 */
enum Direction {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    public final int dx;
    public final int dy;
    Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }
}
