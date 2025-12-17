import java.awt.*;

/**
 * Coin class representing a collectible item in the game.
 * Coins are worth different point values and move around the board.
 * There are three types of coins: Gold (10 pts), Silver (5 pts), and Bronze (2 pts).
 * 
 * Inherits from ShapeObject and implements specific coin behavior.
 */
public class Coin extends ShapeObject {
    
    // Enum to represent different coin types with their values
    public enum CoinType {
        /* Gold coins worth 10 points - bright yellow color */
        GOLD(10, new Color(255, 215, 0)),
        /* Silver coins worth 5 points - gray color */
        SILVER(5, new Color(192, 192, 192)),
        /* Bronze coins worth 2 points - copper/bronze color */
        BRONZE(2, new Color(205, 127, 50));
        
        // Point value for this coin type
        public final int pointValue;
        // Color for this coin type
        public final Color color;
        
        /**
         * CoinType enum constructor.
         * 
         * @param pointValue Points this coin type is worth
         * @param color Display color for this coin type
         */
        CoinType(int pointValue, Color color) {
            this.pointValue = pointValue;
            this.color = color;
        }
    }
    
    private CoinType type;
    private int pointValue;
    private boolean collected = false;
    private int creationFrame;  // Frame when this coin was created
    private static final int LIFETIME_FRAMES = 240;  // 4 seconds at 60 FPS
    
    /**
     * Constructor for Coin.
     * 
     * @param x Initial x-coordinate (center)
     * @param y Initial y-coordinate (center)
     * @param type CoinType enum specifying the coin type
     * @param boardWidth Width of the game board
     * @param boardHeight Height of the game board
     */
    public Coin(double x, double y, CoinType type, int boardWidth, int boardHeight) {
        // Initialize with coin-specific properties
        super(x, y, 15, 15, 
              (Math.random() - 0.5) * 6,  // Random horizontal velocity between -3 and 3
              (Math.random() - 0.5) * 6,  // Random vertical velocity between -3 and 3
              type.color, boardWidth, boardHeight);
        
        this.type = type;
        this.pointValue = type.pointValue;
        this.creationFrame = 0;  // Will be set by GamePanel when spawned
    }
    
    /**
     * Draw the coin as a circle on the game board.
     * The color depends on the coin type (Gold, Silver, or Bronze).
     * Only draws if the coin has not been collected and has not expired.
     * 
     * @param g Graphics object to draw on
     */
    @Override
    public void draw(Graphics g) {
        if (!collected) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fillOval((int)(x - width/2), (int)(y - height/2), width, height);
            
            // Draw a dark outline for visibility
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval((int)(x - width/2), (int)(y - height/2), width, height);
        }
    }
    
    /**
     * Draw the coin with lifetime tracking.
     * Only renders if the coin has not expired and has not been collected.
     * 
     * @param g Graphics object to draw on
     * @param currentFrame The current game frame count
     */
    public void draw(Graphics g, int currentFrame) {
        if (!collected && !isExpired(currentFrame)) {
            draw(g);
        }
    }
    
    /**
     * Get the point value of this coin.
     * 
     * @return The number of points this coin is worth
     */
    public int getPointValue() {
        return pointValue;
    }
    
    /**
     * Get the type of this coin.
     * 
     * @return The CoinType of this coin
     */
    public CoinType getCoinType() {
        return type;
    }
    
    /**
     * Mark this coin as collected (collected coins are no longer drawn or active).
     */
    public void collect() {
        collected = true;
    }
    
    /**
     * Check if this coin has been collected.
     * 
     * @return true if collected, false otherwise
     */
    public boolean isCollected() {
        return collected;
    }
    
    /**
     * Set the creation frame for lifetime tracking.
     * 
     * @param frameCount The current game frame count
     */
    public void setCreationFrame(int frameCount) {
        this.creationFrame = frameCount;
    }
    
    /**
     * Check if this coin has expired (exceeded its 10-second lifetime).
     * 
     * @param currentFrame The current game frame count
     * @return true if expired, false otherwise
     */
    public boolean isExpired(int currentFrame) {
        return (currentFrame - creationFrame) >= LIFETIME_FRAMES;
    }
}
