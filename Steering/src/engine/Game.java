package engine;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author santi
 */
public class Game {
    /*
    In this very simple game engine, the only thing that we need to store is
    the list of game objects (Car or obstacles), the dimeions of the map, and
    the desired frames per second at which we want to run.
    */
    List<GameObject> m_objects = new ArrayList<>();
    double m_width, m_height;    
    int m_frames_per_second;
    
    public Game(double width, double height, int frames_per_second) {
        m_width = width;
        m_height = height;
        m_frames_per_second = frames_per_second;
    }
    
    public double getWidth() {
        return m_width;
    }
    
    public double getHeight() {
        return m_height;
    }
    
    public int getFPS() {
        return m_frames_per_second;
    }
    
    public void add(GameObject o) {
        m_objects.add(o);
    }
    
    public void remove(GameObject o) {
        m_objects.remove(o);
    }
    
    // check for collisions:
    // note: this is an awfully inefficient method, if you ever do a real
    //       game engine, you need to use a quad-tree or oct-tree to speed
    //       this up.
    public GameObject collision(GameObject o) {
        for(GameObject o2:m_objects) {
            if (o2!=o && o.collision(o2)) return o2;
        }
        return null;
    }

    
    public GameObject collision(RotatedRectangle r) {
        for(GameObject o2:m_objects) {
            if (o2.collision(r)) return o2;
        }
        return null;
    }
    
    /*
    The bottom two are the two main functions of the game engine:
    - update calls the "update" method of all the game objects, which will
      trigger their movement.
    - draw calls the "draw" method of all the game objects, to display them
      in the game window.
    */
    public void update() {
        double delta_t = 1.0 / m_frames_per_second;        
        for(GameObject go:m_objects) go.update(this, delta_t);        
    }
        
    public void draw(Graphics2D g) {
        for(GameObject go:m_objects) go.draw(g);
    }
}
