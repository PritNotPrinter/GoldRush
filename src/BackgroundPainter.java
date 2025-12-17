import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * BackgroundPainter - Utility class for managing background images in JPanels
 * 
 * Provides methods to set and paint background images with optional scaling
 * and color overlays for customization.
 */
public class BackgroundPainter {
    
    private BufferedImage backgroundImage;
    private String imagePath;
    private boolean useColor;
    private Color backgroundColor;
    
    /**
     * Constructor with image path
     * 
     * @param imagePath Path to the background image file
     */
    public BackgroundPainter(String imagePath) {
        this.imagePath = imagePath;
        this.useColor = false;
        loadImage();
    }
    
    /**
     * Constructor with fallback color (if image not found)
     * 
     * @param imagePath Path to the background image file
     * @param fallbackColor Color to use if image cannot be loaded
     */
    public BackgroundPainter(String imagePath, Color fallbackColor) {
        this.imagePath = imagePath;
        this.backgroundColor = fallbackColor;
        this.useColor = true;
        loadImage();
    }
    
    /**
     * Load the image from the specified path
     */
    private void loadImage() {
        try {
            // Construct full path to image
            String basePath = "C:\\Users\\sawks\\Home\\School\\Grade 12\\Compsci\\JSwing_v2\\src\\images\\";
            String fullPath = basePath + imagePath;
            File imageFile = new File(fullPath);
            if (imageFile.exists()) {
                backgroundImage = ImageIO.read(imageFile);
                useColor = false;
            } else {
                System.out.println("Image not found: " + imagePath);
                if (backgroundColor != null) {
                    useColor = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
            if (backgroundColor != null) {
                useColor = true;
            }
        }
    }
    
    /**
     * Paint the background on a component
     * 
     * @param component The JPanel to paint on
     * @param g Graphics object
     */
    public void paint(JPanel component, Graphics g) {
        if (useColor && backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, component.getWidth(), component.getHeight());
        } else if (backgroundImage != null) {
            // Draw scaled image to fill the entire component
            g.drawImage(backgroundImage, 0, 0, component.getWidth(), component.getHeight(), component);
        }
    }
    
    /**
     * Check if image loaded successfully
     * 
     * @return true if image is loaded, false otherwise
     */
    public boolean hasImage() {
        return backgroundImage != null;
    }
    
    /**
     * Get the background image
     * 
     * @return The BufferedImage, or null if not loaded
     */
    public BufferedImage getImage() {
        return backgroundImage;
    }
    
    /**
     * Set a new image path
     * 
     * @param imagePath Path to the new image
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        loadImage();
    }
    
    /**
     * Set fallback color
     * 
     * @param color The color to use as fallback
     */
    public void setFallbackColor(Color color) {
        this.backgroundColor = color;
    }
}
