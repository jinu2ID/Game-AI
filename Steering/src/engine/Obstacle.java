package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author santi
 */
public class Obstacle extends GameObject {
    double m_width, m_height;
    Color m_color;
    RotatedRectangle m_collision_box;
   
    public Obstacle(double x, double y, double width, double height, Color c) {
        m_x = x+width/2;
        m_y = y+height/2;
        m_width = width;
        m_height = height;
        m_color = c;
        m_collision_box = new RotatedRectangle(m_x, m_y, m_width/2, m_height/2, 0);
    }

    public void update(Game game, double delta_t) {
    }

    public void draw(Graphics2D g) {
        g.setColor(m_color);
        g.fillRect((int)(m_x-m_width/2), (int)(m_y-m_height/2), (int)m_width, (int)m_height);
    }
    
    public RotatedRectangle getCollisionBox() {
        return m_collision_box;
    }
    
        
}
