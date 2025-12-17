import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * GamePanel class manages the main gameplay loop and rendering.
 * Handles all game logic including collision detection, object movement,
 * score tracking, lives, and time management.
 * 
 * The game runs on a timer that updates the game state at regular intervals.
 */
public class GamePanel extends JPanel {
    
    // Game board dimensions
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 600;
    private static final int BUTTON_PANEL_HEIGHT = 50;  // Height reserved for button panel
    private static final int PLAYABLE_HEIGHT = BOARD_HEIGHT - BUTTON_PANEL_HEIGHT;  // Actual game area height
    
    // Game timing and rules
    private static final int GAME_DURATION_SECONDS = 60;
    private static final int INITIAL_LIVES = 3;
    private static final int SPAWN_RATE = 10;  // Spawn new objects every N frames

    // Game state variables
    private boolean gameRunning = false;
    private boolean gamePaused = false;
    private int remainingTime = GAME_DURATION_SECONDS;
    private int lives = INITIAL_LIVES;
    private int score = 0;
    
    // Player information
    private String currentUsername = "Player";
    private LogoutListener logoutListener;
    private BackgroundPainter backgroundPainter;
    
    // Game objects
    private CollectionPurse purse;
    private ArrayList<Coin> coins;
    private ArrayList<Bomb> bombs;
    private ArrayList<PowerUp> powerUps;
    private boolean powerupActive = false;
    private int powerupEndFrame = 0;
    
    // Timer for game loop
    private Timer gameTimer;
    private int frameCount = 0;
    
    // UI Buttons
    private JButton startButton;
    private JButton replayButton;
    private JButton pauseButton;
    
    // Mouse tracking
    private int mouseX = BOARD_WIDTH / 2;
    private int mouseY = BOARD_HEIGHT / 2;
    
    /**
     * Interface for logout callback
     */
    public interface LogoutListener {
        void onLogout();
    }
    
    /**
     * Set the logged-in username
     * 
     * @param username The username of the current player
     */
    public void setUsername(String username) {
        this.currentUsername = username;
    }
    
    /**
     * Set a listener to be notified when the user logs out
     * 
     * @param listener The logout listener
     */
    public void setLogoutListener(LogoutListener listener) {
        this.logoutListener = listener;
    }
    
    /**
     * Constructor for GamePanel.
     * Initializes the game board, sets up UI elements, and prepares for gameplay.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        setBackground(new Color(245, 245, 250));
        //setFocusable(true);
        
        // Initialize background painter
        backgroundPainter = new BackgroundPainter("game_bg.png");
        
        // Initialize game objects
        coins = new ArrayList<>();
        bombs = new ArrayList<>();
        powerUps = new ArrayList<>();
        purse = new CollectionPurse(BOARD_WIDTH, PLAYABLE_HEIGHT);
        
        // Setup mouse tracking
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                if (gameRunning && purse != null) {
                    purse.followMouse(mouseX, mouseY);
                }
            }
        });
        
        // Setup mouse click detection for collecting coins/bombs
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameRunning && !gamePaused) {
                    handleMouseClick(e.getX(), e.getY());
                }
            }
        });
        
        // Setup UI buttons with layout
        setLayout(new BorderLayout());
        
        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(200, 200, 220));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        replayButton = new JButton("Reset Game");
        replayButton.addActionListener(e -> replayGame());
        replayButton.setFont(new Font("Arial", Font.BOLD, 14));
        replayButton.setEnabled(false);
        
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> togglePause());
        pauseButton.setFont(new Font("Arial", Font.BOLD, 14));
        pauseButton.setEnabled(false);
        
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(replayButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Start a new game.
     * Initializes game state and begins the gameplay loop.
     */
    private void startGame() {
        gameRunning = true;
        gamePaused = false;
        remainingTime = GAME_DURATION_SECONDS;
        lives = INITIAL_LIVES;
        score = 0;
        frameCount = 0;
        coins.clear();
        bombs.clear();
        
        startButton.setEnabled(false);
        pauseButton.setEnabled(true);
        replayButton.setEnabled(true);
        
        // Create and start the game timer (updates every 16ms = ~60 FPS)
        if (gameTimer != null) {
            gameTimer.stop();
        }
        gameTimer = new Timer(16, e -> updateGame());
        gameTimer.start();
    }
    
    /**
     * Replay the game by resetting state and starting fresh.
     */
    private void replayGame() {
        gameRunning = false;
        gamePaused = false;
        remainingTime = GAME_DURATION_SECONDS;
        lives = INITIAL_LIVES;
        score = 0;
        frameCount = 0;
        coins.clear();
        bombs.clear();
        powerUps.clear();
        powerupActive = false;

        startButton.setEnabled(true);
        pauseButton.setEnabled(true);
        replayButton.setEnabled(false);
        
        // Start the game
        //startGame();
    }
    
    /**
     * Toggle pause state of the game.
     */
    private void togglePause() {
        if (gameRunning) {
            gamePaused = !gamePaused;
            pauseButton.setText(gamePaused ? "Resume" : "Pause");
        }
    }
    
    /**
     * Update game state each frame.
     * This is the main game loop that handles:
     * - Spawning coins and bombs
     * - Moving game objects
     * - Checking collisions
     * - Updating time
     * - Checking game over conditions
     */
    private void updateGame() {
                        // Move all powerups
                        for (PowerUp pu : powerUps) {
                            pu.move();
                        }
                // Powerup: 0.05% chance to spawn per frame
                if (Math.random() < 0.0005) {
                    double x = Math.random() * (BOARD_WIDTH - 50) + 25;
                    double y = Math.random() * (PLAYABLE_HEIGHT - 100) + 25;
                    PowerUp pu = new PowerUp(x, y, BOARD_WIDTH, PLAYABLE_HEIGHT);
                    pu.setCreationFrame(frameCount);
                    powerUps.add(pu);
                }

                // Powerup must be clicked to activate (handled in mouseClicked)
                // Remove expired powerups
                powerUps.removeIf(pu -> pu.isExpired(frameCount));

                // Powerup: autocollect coins on mouseover, ignore bombs
                if (powerupActive) {
                    for (Coin coin : coins) {
                        if (!coin.isCollected() && !coin.isExpired(frameCount) && purse.collidesWith(coin)) {
                            coin.collect();
                            score += coin.getPointValue();
                        }
                    }
                    // End powerup after 5 seconds
                    if (frameCount >= powerupEndFrame) {
                        powerupActive = false;
                    }
                }
        if (!gameRunning || gamePaused) {
            repaint();
            return;
        }
        
        frameCount++;
        
        // Decrease remaining time every 60 frames (approximately every second at 60 FPS)
        if (frameCount % 60 == 0) {
            remainingTime--;
            if (remainingTime <= 0) {
                endGame();
                return;
            }
        }
        
        // Spawn new coins and bombs periodically
        if (frameCount % SPAWN_RATE == 0) {
            spawnGameObjects();
        }
        
        // Move all coins and bombs
        for (Coin coin : coins) {
            if (!coin.isCollected() && !coin.isExpired(frameCount)) {
                coin.move();
            }
        }

        for (Bomb bomb : bombs) {
            if (!bomb.isDetonated() && !bomb.isExpired(frameCount)) {
                bomb.move();
            }
        }

        // Handle coin-to-coin collisions (bouncing)
        for (int i = 0; i < coins.size(); i++) {
            Coin coin1 = coins.get(i);
            if (coin1.isCollected() || coin1.isExpired(frameCount)) {
                continue;
            }
            
            for (int j = i + 1; j < coins.size(); j++) {
                Coin coin2 = coins.get(j);
                if (coin2.isCollected() || coin2.isExpired(frameCount)) {
                    continue;
                }
                
                // Check if coins collide
                if (coin1.collidesWith(coin2)) {
                    // Calculate collision response (elastic bounce)
                    double dx = coin2.getX() - coin1.getX();
                    double dy = coin2.getY() - coin1.getY();
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    
                    if (distance > 0) {
                        // Normalize the collision vector
                        double nx = dx / distance;
                        double ny = dy / distance;
                        
                        // Relative velocity
                        double dvx = coin2.getVelocityX() - coin1.getVelocityX();
                        double dvy = coin2.getVelocityY() - coin1.getVelocityY();
                        
                        // Relative velocity in collision normal direction
                        double dvn = dvx * nx + dvy * ny;
                        
                        // Do not resolve if coins are moving apart
                        if (dvn < 0) {
                            // For equal mass elastic collision, exchange velocity components
                            coin1.setVelocityX(coin1.getVelocityX() + dvn * nx);
                            coin1.setVelocityY(coin1.getVelocityY() + dvn * ny);
                            coin2.setVelocityX(coin2.getVelocityX() - dvn * nx);
                            coin2.setVelocityY(coin2.getVelocityY() - dvn * ny);
                            
                            // Separate coins to prevent overlap
                            double overlap = (coin1.getWidth() / 2 + coin2.getWidth() / 2) - distance;
                            double separationX = (overlap / 2) * nx;
                            double separationY = (overlap / 2) * ny;
                            coin1.setX(coin1.getX() - separationX);
                            coin1.setY(coin1.getY() - separationY);
                            coin2.setX(coin2.getX() + separationX);
                            coin2.setY(coin2.getY() + separationY);
                        }
                    }
                }
            }
        }

        // Remove collected, expired, or detonated objects
        coins.removeIf(c -> c.isCollected() || c.isExpired(frameCount));
        bombs.removeIf(b -> b.isDetonationComplete() || b.isExpired(frameCount));

        repaint();
    }
    
    /**
     * Spawn new coins and bombs on the game board.
     * Randomly distributes them across the board.
     */
    private void spawnGameObjects() {
        // Spawn 1-2 coins per spawn interval
        int coinCount = 1 + (Math.random() < 0.5 ? 1 : 0);
        for (int i = 0; i < coinCount; i++) {
            double x = Math.random() * (BOARD_WIDTH - 50) + 25;
            double y = Math.random() * (PLAYABLE_HEIGHT - 100) + 25;

            // Randomly choose coin type (more common coins are more likely)
            double random = Math.random();
            Coin.CoinType type;
            if (random < 0.6) {
                type = Coin.CoinType.BRONZE;  // 60% chance
            } else if (random < 0.85) {
                type = Coin.CoinType.SILVER;  // 25% chance
            } else {
                type = Coin.CoinType.GOLD;    // 15% chance
            }

            Coin coin = new Coin(x, y, type, BOARD_WIDTH, PLAYABLE_HEIGHT);
            coin.setCreationFrame(frameCount);
            coins.add(coin);
        }

        // Spawn a bomb occasionally (20% chance per spawn interval)
        if (Math.random() < 0.2) {
            double x = Math.random() * (BOARD_WIDTH - 50) + 25;
            double y = Math.random() * (PLAYABLE_HEIGHT - 100) + 25;
            Bomb bomb = new Bomb(x, y, BOARD_WIDTH, PLAYABLE_HEIGHT, "images/bomb.png");
            bomb.setCreationFrame(frameCount);
            bombs.add(bomb);
        }
    }
    
    /**
     * Handle mouse click events for collecting coins and bombs.
     * When the player clicks, all coins within collection range are collected.
     * If a bomb is clicked, it detonates and the player loses points and a life.
     * 
     * @param clickX X-coordinate of the mouse click
     * @param clickY Y-coordinate of the mouse click
     */
    private void handleMouseClick(int clickX, int clickY) {
                // Check for powerup click
                java.util.Iterator<PowerUp> it = powerUps.iterator();
                while (it.hasNext()) {
                    PowerUp pu = it.next();
                    if (!pu.isExpired(frameCount) && pu.containsPoint(clickX, clickY)) {
                        powerupActive = true;
                        powerupEndFrame = frameCount + 300; // 5 seconds
                        it.remove();
                        return; // Only one powerup can be activated per click
                    }
                }
        // Check for coin collection
        for (Coin coin : coins) {
            if (!coin.isCollected() && purse.collidesWith(coin)) {
                coin.collect();
                score += coin.getPointValue();
                
                // Visual feedback: briefly change purse color
                Color originalColor = purse.getColor();
                purse.setColor(new Color(0, 255, 0));  // Green for collection
                Timer feedbackTimer = new Timer(200, e -> purse.setColor(originalColor));
                feedbackTimer.setRepeats(false);
                feedbackTimer.start();
            }
        }
        
        // Check for bomb detonation (ignore bombs if powerup is active)
        if (!powerupActive) {
            for (Bomb bomb : bombs) {
                if (!bomb.isDetonated() && purse.collidesWith(bomb)) {
                    bomb.detonate();
                    score -= bomb.getPointPenalty();
                    lives--;
                    // Ensure score doesn't go negative
                    if (score < 0) score = 0;
                    // Visual feedback: briefly change purse color to red
                    Color originalColor = purse.getColor();
                    purse.setColor(new Color(255, 100, 100));  // Red for bomb
                    Timer feedbackTimer = new Timer(300, e -> purse.setColor(originalColor));
                    feedbackTimer.setRepeats(false);
                    feedbackTimer.start();
                    // Check if game is over due to loss of lives
                    if (lives <= 0) {
                        endGame();
                    }
                }
            }
        }
    }
    
    /**
     * End the game and display final results.
     */
    private void endGame() {
        gameRunning = false;
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Save the player's score
        ScoreManager.saveScore(currentUsername, score);
        
        startButton.setEnabled(true);
        pauseButton.setEnabled(false);
        replayButton.setEnabled(true);
        
        // Create custom dialog with logout button
        String gameOverMessage = String.format(
            "Game Over!\n\nFinal Score: %d\nLives Remaining: %d",
            score, Math.max(0, lives)
        );
        
        Object[] options = {"Play Again", "Logout"};
        int result = JOptionPane.showOptionDialog(
            this,
            gameOverMessage,
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        if (result == 0) {  // Play Again
            replayGame();
        } else if (result == 1) {  // Logout
            logout();
        }
    }
    
    /**
     * Render the game board and all game objects.
     * Called by the Swing framework whenever the panel needs to be redrawn.
     * 
     * @param g Graphics object to draw on
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Paint background
        backgroundPainter.paint(this, g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        // Enable anti-aliasing for smoother graphics
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw game objects (skip expired)
        for (Coin coin : coins) {
            if (!coin.isExpired(frameCount)) {
                coin.draw(g2);
            }
        }

        for (Bomb bomb : bombs) {
            if (!bomb.isExpired(frameCount)) {
                bomb.draw(g2);
            }
        }

        for (PowerUp pu : powerUps) {
            pu.draw(g2);
        }
        purse.draw(g2);
        
        // Draw HUD (Heads-Up Display) with game information
        drawHUD(g2);
    }
    
    /**
     * Draw the heads-up display showing score, lives, and remaining time.
     * 
     * @param g Graphics object to draw on
     */
    private void drawHUD(Graphics2D g) {
                // Show timer for powerup effect
                if (powerupActive) {
                    int secondsLeft = Math.max(0, (powerupEndFrame - frameCount) / 60);
                    g.setColor(new Color(0, 180, 255));
                    g.setFont(new Font("Arial", Font.BOLD, 22));
                    String timerText = "Powerup: " + secondsLeft + "s left";
                    g.drawString(timerText, BOARD_WIDTH / 2 - 80, 30);
                }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Draw score
        g.drawString("Score: " + score, 20, 30);
        
        // Draw lives with visual indicators
        g.drawString("Lives: " + Math.max(0, lives), 20, 60);
        
        // Draw remaining time with color coding (red when time is low)
        String timeText = "Time: " + remainingTime + "s";
        if (remainingTime <= 10) {
            g.setColor(new Color(255, 100, 100));  // Red for low time
        }
        g.drawString(timeText, BOARD_WIDTH - 200, 30);
        
        // Draw game status
        if (gamePaused) {
            g.setColor(new Color(255, 165, 0));  // Orange
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fm = g.getFontMetrics();
            String pauseText = "PAUSED";
            int x = (BOARD_WIDTH - fm.stringWidth(pauseText)) / 2;
            int y = (BOARD_HEIGHT - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(pauseText, x, y);
        }
        
        if (!gameRunning) {
            g.setColor(new Color(255, 255, 255, 255));
            g.setFont(new Font("Arial", Font.BOLD, 30));
            FontMetrics fm = g.getFontMetrics();
            String readyText = "Click Start Game to Begin";
            int x = (BOARD_WIDTH - fm.stringWidth(readyText)) / 2;
            int y = (BOARD_HEIGHT - fm.getHeight()) / 2 + fm.getAscent();
            g.drawString(readyText, x, y);
        }
    }
    
    /**
     * Logout the current player and return to the login page
     */
    private void logout() {
        gameRunning = false;
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        if (logoutListener != null) {
            logoutListener.onLogout();
        }
    }
}
