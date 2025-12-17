import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Bomb class representing a dangerous object in the game.
 * When collected, bombs cause the player to lose points and a life.
 * 
 * Inherits from ShapeObject and implements bomb-specific behavior.
 */
public class Bomb extends ShapeObject {
    
    private static BufferedImage bombImage;
    
    static {
        try {
            bombImage = ImageIO.read(new File("c:\\Users\\sawks\\Home\\School\\Grade 12\\Compsci\\JSwing_v2\\src\\images\\bomb.png"));
        } catch (IOException e) {
            System.err.println("Failed to load bomb image: " + e.getMessage());
        }
    }
    
    private int pointPenalty = 25;  // Points lost when bomb is collected
    private boolean detonated = false;
    private int explosionFrame = 0;
    private final int explosionDuration = 10;  // Frames for explosion animation
    private int creationFrame;  // Frame when this bomb was created
    private static final int LIFETIME_FRAMES = 180;  // 3 seconds at 60 FPS
    
    /**
     * Constructor for Bomb.
     * 
     * @param x Initial x-coordinate (center)
     * @param y Initial y-coordinate (center)
     * @param boardWidth Width of the game board
     * @param boardHeight Height of the game board
     */
    public Bomb(double x, double y, int boardWidth, int boardHeight, String imagePath) {
        // Initialize bomb with dark red color and moderate speed
        super(x, y, 50, 50,
              (Math.random() - 0.5) * 4,  // Random x velocity between -2 and 2
              (Math.random() - 0.5) * 4,  // Random y velocity between -2 and 2
              new Color(150, 50, 50), boardWidth, boardHeight);
        this.creationFrame = 0;  // Will be set by GamePanel when spawned
    }
    
    /**
     * Draw the bomb using the image loaded from file.
     * When detonated, shows an explosion effect.
     * 
     * @param g Graphics object to draw on
     */
    @Override
    public void draw(Graphics g) {
        if (!detonated) {
            Graphics2D g2 = (Graphics2D) g;
            
            if (bombImage != null) {
                // Draw image centered at (x, y)
                g2.drawImage(bombImage, (int)(x - width/2), (int)(y - height/2), 
                            width, height, null);
             }// else {
            //     // Fallback to shape if image failed to load
            //     g2.setColor(color);
            //     g2.fillOval((int)(x - width/2), (int)(y - height/2), width, height);
                
            //     // Draw dark outline and cross pattern to indicate bomb
            //     g2.setColor(Color.BLACK);
            //     g2.setStroke(new BasicStroke(2));
            //     g2.drawOval((int)(x - width/2), (int)(y - height/2), width, height);
                
            //     // Draw a cross pattern to make bombs clearly distinguishable
            //     g2.drawLine((int)(x - 5), (int)y, (int)(x + 5), (int)y);
            //     g2.drawLine((int)x, (int)(y - 5), (int)x, (int)(y + 5));
            // }
        } else if (explosionFrame < explosionDuration) {
            // Draw explosion effect: expanding circles with fading color
            Graphics2D g2 = (Graphics2D) g;
            int radius = (int)(10 + explosionFrame * 5);
            
            // Outer explosion circle (bright yellow/orange)
            g2.setColor(new Color(255, 165, 0, 255 - (explosionFrame * 20)));
            g2.fillOval((int)(x - radius), (int)(y - radius), radius * 2, radius * 2);
            
            // Inner explosion circle (bright white)
            int innerRadius = (int)(radius * 0.6);
            g2.setColor(new Color(255, 255, 255, 255 - (explosionFrame * 20)));
            g2.fillOval((int)(x - innerRadius), (int)(y - innerRadius), 
                        innerRadius * 2, innerRadius * 2);
            
            explosionFrame++;
        }
    }
    
    /**
     * Detonate the bomb, triggering the explosion effect.
     */
    public void detonate() {
        detonated = true;
        explosionFrame = 0;
    }
    
    /**
     * Check if the bomb has fully detonated and its effect is complete.
     * 
     * @return true if detonation animation is complete, false otherwise
     */
    public boolean isDetonationComplete() {
        return detonated && explosionFrame >= explosionDuration;
    }
    
    /**
     * Get the number of points lost when this bomb is collected.
     * 
     * @return The point penalty for collecting this bomb
     */
    public int getPointPenalty() {
        return pointPenalty;
    }
    
    /**
     * Check if the bomb has been detonated.
     * 
     * @return true if bomb is detonated, false otherwise
     */
    public boolean isDetonated() {
        return detonated;
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
     * Check if this bomb has expired (exceeded its 10-second lifetime).
     * 
     * @param currentFrame The current game frame count
     * @return true if expired, false otherwise
     */
    public boolean isExpired(int currentFrame) {
        return (currentFrame - creationFrame) >= LIFETIME_FRAMES;
    }
}
