package engine;

import java.awt.Graphics2D;

/**
 *
 * @author santi
 */
public abstract class GameObject {
    double m_x,m_y;
    
    /*
    The only thing we require of game objects is that they define the following three functions:
    - update: that contains their bhavior
    - draw: that draws them in the game window
    - getCollisionBox: that tells the game engine where they physically are in th game space
    */
    public abstract void update(Game game, double delta_t);
    public abstract void draw(Graphics2D g);
    public abstract RotatedRectangle getCollisionBox();

    
    public double getX() {
        return m_x;
    }
    
    public double getY() {
        return m_y;
    }

    public boolean collision(GameObject o) {
        if (getCollisionBox()==null || o.getCollisionBox()==null) return false;
        return RotatedRectangle.RotRectsCollision(getCollisionBox(), o.getCollisionBox());
    }

    public boolean collision(RotatedRectangle r) {
        if (getCollisionBox()==null || r==null) return false;
        return RotatedRectangle.RotRectsCollision(getCollisionBox(), r);
    }
}
