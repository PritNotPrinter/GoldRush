import java.awt.*;
import javax.swing.border.Border;

/**
 * RoundedBorder - Custom border with rounded corners
 */
public class RoundedBorder implements Border {
    
    private int radius;
    private Color color;
    private int thickness;
    
    /**
     * Constructor for RoundedBorder
     * 
     * @param radius The radius of the rounded corners
     * @param color The color of the border
     * @param thickness The thickness of the border line
     */
    public RoundedBorder(int radius, Color color, int thickness) {
        this.radius = radius;
        this.color = color;
        this.thickness = thickness;
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(thickness));
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
    
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }
    
    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
