package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author santi
 */
public class Marker extends GameObject {
    double m_radius;
    Color m_color;
    RotatedRectangle m_collision_box;
   
    public Marker(double x, double y, double radius, Color c) {
        m_x = x;
        m_y = y;
        m_radius = radius;
        m_color = c;
        m_collision_box = null;
    }

    public void update(Game game, double delta_t) {
    }

    public void draw(Graphics2D g) {
        g.setColor(m_color);
        g.fillOval((int)(m_x-m_radius), (int)(m_y-m_radius), (int)(m_radius*2), (int)(m_radius*2));
    }
    
    public RotatedRectangle getCollisionBox() {
        return m_collision_box;
    }
    
        
}
