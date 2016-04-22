package engine;

import java.awt.*;

/**
 * Created by jinuj on 4/21/2016.
 */
public class Ray extends GameObject {

    public double m_width, m_height, m_alpha;
    Color m_color;
    public RotatedRectangle m_collision_box;

    public Ray(double x, double y, double width, double height, double angle, Color c){
        m_x = x;
        m_y = y;
        m_width = width;
        m_height = height;
        m_alpha = angle;
        m_color = c;
        m_collision_box = new RotatedRectangle(m_x, m_y, m_width, m_height, 0);
    }


    public void update(Game game, double delta_t) {

    }


    public void draw(Graphics2D g) {
        g.setColor(m_color);
        //g.rotate(Math.toRadians(-2.999));
        g.translate(m_x,m_y);
        g.fillRect((int)(m_x-m_width), (int)(m_y-m_height), (int)m_width, (int)m_height);

    }


    public RotatedRectangle getCollisionBox() {
        return m_collision_box;
    }
}
