import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * PowerUp class representing a rare autocollect powerup in the game.
 * When collected, enables autocollect for coins and ignores bombs for 5 seconds.
 */
public class PowerUp extends ShapeObject {
    private static final int POWERUP_SIZE = 50;
    private static BufferedImage powerupImage;
    private int creationFrame;
    private static final int LIFETIME_FRAMES = 600; // 10 seconds at 60 FPS
    private boolean active = false;
    private int activationFrame = 0;

    public PowerUp(double x, double y, int boardWidth, int boardHeight) {
        super(x, y, POWERUP_SIZE, POWERUP_SIZE,
              (Math.random() - 0.5) * 10, // vx: -5 to 5
              (Math.random() - 0.5) * 10, // vy: -5 to 5
              new Color(100, 255, 200), boardWidth, boardHeight);
        this.creationFrame = 0;
    }

    static {
        try {
            powerupImage = ImageIO.read(new File("c:\\Users\\sawks\\Home\\School\\Grade 12\\Compsci\\JSwing_v2\\src\\images\\powerup.png"));
        } catch (IOException e) {
            System.err.println("Failed to load powerup image: " + e.getMessage());
        }
    }


    // Move the powerup (bounces off walls)
    @Override
    public void move() {
        x += velocityX;
        y += velocityY;
        if (x < width / 2) {
            x = width / 2;
            velocityX = -velocityX;
        } else if (x > boardWidth - width / 2) {
            x = boardWidth - width / 2;
            velocityX = -velocityX;
        }
        if (y < height / 2) {
            y = height / 2;
            velocityY = -velocityY;
        } else if (y > boardHeight - height / 2) {
            y = boardHeight - height / 2;
            velocityY = -velocityY;
        }
    }

    // Check if a point is inside the powerup (for click detection)
    public boolean containsPoint(int px, int py) {
        double dx = x - px;
        double dy = y - py;
        double r = width / 2.0;
        return dx * dx + dy * dy <= r * r;
    }

    public void setCreationFrame(int frameCount) {
        this.creationFrame = frameCount;
    }

    public boolean isExpired(int currentFrame) {
        return (currentFrame - creationFrame) >= LIFETIME_FRAMES;
    }

    public void activate(int frameCount) {
        this.active = true;
        this.activationFrame = frameCount;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isEffectOver(int frameCount) {
        return active && (frameCount - activationFrame) >= LIFETIME_FRAMES;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (powerupImage != null) {
            g2.drawImage(powerupImage, (int)(x - width/2), (int)(y - height/2), width, height, null);
        }
    }
}
