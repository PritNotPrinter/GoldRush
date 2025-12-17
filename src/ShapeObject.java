import java.awt.*;

/**
 * Abstract class representing a game object that moves around the game board.
 * This is the base class for all game entities like coins, bombs, and the purse.
 * 
 * Defines common properties such as position, size, velocity, and color.
 * Subclasses must implement the abstract methods to define specific behaviors.
 */
public abstract class ShapeObject {
    
    // Position coordinates (center of the object)
    protected double x;
    protected double y;
    
    // Size of the object
    protected int width;
    protected int height;
    
    // Velocity - how fast the object moves per frame
    protected double velocityX;
    protected double velocityY;
    
    // Display color
    protected Color color;
    
    // Game board boundaries
    protected int boardWidth;
    protected int boardHeight;
    
    /**
     * Constructor for ShapeObject.
     * 
     * @param x Initial x-coordinate (center)
     * @param y Initial y-coordinate (center)
     * @param width Width of the object
     * @param height Height of the object
     * @param velocityX Initial x-velocity
     * @param velocityY Initial y-velocity
     * @param color Display color of the object
     * @param boardWidth Width of the game board
     * @param boardHeight Height of the game board
     */
    public ShapeObject(double x, double y, int width, int height, 
                       double velocityX, double velocityY, Color color, 
                       int boardWidth, int boardHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.color = color;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }
    
    /**
     * Update the object's position based on velocity.
     * Subclasses can override this to implement custom movement patterns.
     */
    public void move() {
        x += velocityX;
        y += velocityY;
        
        // Bounce off walls
        if (x - width/2 < 0 || x + width/2 > boardWidth) {
            velocityX = -velocityX;
            x = Math.max(width/2, Math.min(x, boardWidth - width/2));
        }
        if (y - height/2 < 0 || y + height/2 > boardHeight) {
            velocityY = -velocityY;
            y = Math.max(height/2, Math.min(y, boardHeight - height/2));
        }
    }
    
    /**
     * Draw the object on the graphics context.
     * Each subclass implements its own rendering logic.
     * 
     * @param g Graphics object to draw on
     */
    public abstract void draw(Graphics g);
    
    /**
     * Check if a point (such as a mouse click) collides with this object.
     * 
     * @param px X-coordinate of the point
     * @param py Y-coordinate of the point
     * @return true if the point is within the object's bounds, false otherwise
     */
    public boolean collidesWith(int px, int py) {
        return px >= x - width/2 && px <= x + width/2 &&
               py >= y - height/2 && py <= y + height/2;
    }
    
    /**
     * Check if this object collides with another ShapeObject.
     * 
     * @param other The other ShapeObject to check collision with
     * @return true if objects overlap, false otherwise
     */
    public boolean collidesWith(ShapeObject other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (this.width/2 + other.width/2);
    }
    
    // Getters for object properties
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Color getColor() { return color; }
    public double getVelocityX() { return velocityX; }
    public double getVelocityY() { return velocityY; }
    
    // Setters for object properties
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setColor(Color color) { this.color = color; }
    public void setVelocityX(double velocityX) { this.velocityX = velocityX; }
    public void setVelocityY(double velocityY) { this.velocityY = velocityY; }
}
