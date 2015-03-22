package arkanoid;

import java.awt.Graphics2D;

/**
 *
 * @author Ratomir
 */
public interface GameObject {
    public abstract void move();
    public abstract void draw(Graphics2D g2);
}
