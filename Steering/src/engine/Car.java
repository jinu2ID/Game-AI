package engine;

import controllers.Controller;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author santi
 */
public class Car extends GameObject {
    Controller m_controller;
    BufferedImage m_img;
    
    // physics:
    double m_alpha;     // rotation
    double m_speed;
    double m_max_velocity = 250;
    double m_min_velocity = -100;
    RotatedRectangle m_collision_box;
        
    public Car(String graphicsFileName, double x, double y, double alpha, Controller c) throws Exception {
        m_img = ImageIO.read(new File(graphicsFileName));
        m_x = x;
        m_y = y;
        m_alpha = alpha;
        m_controller = c;
        m_collision_box = new RotatedRectangle(m_x, m_y, m_img.getWidth()/2, m_img.getHeight()/2, m_alpha);
    }
    
    public double getAngle() {
        return m_alpha;
    }
    
    public double getSpeed() {
        return m_speed;
    }
    
    public void update(Game game, double delta_t) {
        double controlVariables[] = {0,0,0};
        m_controller.update(this, game, delta_t, controlVariables);
        
        // remember the old posision, in case there is a collision:
        double old_x = m_x;
        double old_y = m_y;
        double old_angle = m_alpha;
        
        // update velocity, position and angle:
        double brake_strength = (m_speed>0 ? 250:100);
        double acceleration = controlVariables[Controller.VARIABLE_THROTTLE]*100 - 
                              controlVariables[Controller.VARIABLE_BRAKE]*brake_strength;
        m_speed*=0.99;   // drag
        m_speed += acceleration * delta_t;
        if (m_speed>m_max_velocity) m_speed = m_max_velocity;
        if (m_speed<m_min_velocity) m_speed = m_min_velocity;        
        m_x += Math.cos(m_alpha)*m_speed * delta_t;
        m_y += Math.sin(m_alpha)*m_speed * delta_t;
        double turning_rate = controlVariables[Controller.VARIABLE_STEERING]*m_speed;
        m_alpha+=turning_rate*delta_t/50;
        
        // update the collision box:
        m_collision_box.C.x = m_x;
        m_collision_box.C.y = m_y;
        m_collision_box.ang = m_alpha;
        
        // check for collisions:
        if (game.collision(this) != null) {
            m_x = old_x;
            m_y = old_y;
            m_alpha = old_angle;
            m_speed = 0;

            m_collision_box.C.x = m_x;
            m_collision_box.C.y = m_y;
            m_collision_box.ang = m_alpha;
        }
    }

    public void draw(Graphics2D g) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.translate(m_x, m_y);
        gg.rotate(m_alpha);
        gg.translate(-m_img.getWidth()/2,-m_img.getHeight()/2);
        gg.drawImage(m_img, 0, 0, null);
        gg.dispose();        
    }
    
    public RotatedRectangle getCollisionBox() {
        return m_collision_box;
    }
    
}
