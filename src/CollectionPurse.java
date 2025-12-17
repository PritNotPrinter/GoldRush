import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * CollectionPurse class representing the player's collection tool.
 * The purse follows the mouse cursor and collects coins when clicked.
 * It doesn't move on its own; it's controlled directly by mouse movement.
 * 
 * Inherits from ShapeObject and implements player-controlled behavior.
 */
public class CollectionPurse extends ShapeObject {
    
    private static final int PURSE_SIZE = 25;
    private static BufferedImage purseImage;
    private boolean isActive = true;  // Whether the purse can collect items
    
    static {
        try {
            purseImage = ImageIO.read(new File("c:\\Users\\sawks\\Home\\School\\Grade 12\\Compsci\\JSwing_v2\\src\\images\\purse.png"));
        } catch (IOException e) {
            System.err.println("Failed to load purse image: " + e.getMessage());
        }
    }
    
    /**
     * Constructor for CollectionPurse.
     * 
     * @param boardWidth Width of the game board
     * @param boardHeight Height of the game board
     */
    public CollectionPurse(int boardWidth, int boardHeight) {
        // Initialize purse at center of board with no velocity
        // (velocity is overridden by mouse movement)
        super(boardWidth / 2.0, boardHeight / 2.0, PURSE_SIZE, PURSE_SIZE,
              0, 0, new Color(100, 150, 255), boardWidth, boardHeight);
    }
    
    /**
     * Update the purse position to follow the mouse.
     * This overrides the default move() method from ShapeObject.
     * 
     * @param mouseX The current mouse X position
     * @param mouseY The current mouse Y position
     */
    public void followMouse(int mouseX, int mouseY) {
        // Keep the purse within board boundaries while following mouse
        this.x = Math.max(PURSE_SIZE / 2, Math.min(mouseX, boardWidth - PURSE_SIZE / 2));
        this.y = Math.max(PURSE_SIZE / 2, Math.min(mouseY, boardHeight - PURSE_SIZE / 2));
    }
    
    /**
     * Draw the purse using the image loaded from file.
     * 
     * @param g Graphics object to draw on
     */
    @Override
    public void draw(Graphics g) {
        if (isActive) {
            Graphics2D g2 = (Graphics2D) g;
            
            if (purseImage != null) {
                // Draw image centered at (x, y)
                g2.drawImage(purseImage, (int)(x - PURSE_SIZE / 2), (int)(y - PURSE_SIZE / 2), 
                            PURSE_SIZE, PURSE_SIZE, null);
            } //else {
            //     // Fallback to shape if image failed to load
            //     g2.setColor(color);
            //     g2.fillOval((int)(x - PURSE_SIZE / 2.5), (int)(y - PURSE_SIZE / 2.5), 
            //                 (int)(PURSE_SIZE / 1.25), (int)(PURSE_SIZE / 1.25));
                
            //     // Draw purse outline for definition
            //     g2.setColor(Color.BLACK);
            //     g2.setStroke(new BasicStroke(2));
            //     g2.drawOval((int)(x - PURSE_SIZE / 2.5), (int)(y - PURSE_SIZE / 2.5),
            //                 (int)(PURSE_SIZE / 1.25), (int)(PURSE_SIZE / 1.25));
            // }
        }
    }
    
    /**
     * Check if a coin or bomb collides with the purse collection area.
     * This is used when the player clicks to determine what was collected.
     * 
     * @param other The ShapeObject to check collision with
     * @return true if the object overlaps with the purse, false otherwise
     */
    @Override
    public boolean collidesWith(ShapeObject other) {
        // Purse has a larger effective collision radius for easier collection
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double collisionRange = (this.width/2 + other.width/2) * 1.5;  // 50% larger collection area
        return distance < collisionRange;
    }
    
    /**
     * Set whether the purse is active and able to collect items.
     * 
     * @param active true to activate, false to deactivate
     */
    public void setActive(boolean active) {
        this.isActive = active;
    }
    
    /**
     * Check if the purse is currently active.
     * 
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Override move() since purse movement is handled by followMouse().
     * This prevents the default bouncing behavior.
     */
    @Override
    public void move() {
        // Purse doesn't move automatically - controlled by mouse
    }
}
