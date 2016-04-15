package engine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author santi
 */
public class GameWindow extends JComponent {
    Game m_game;
    
    private GameWindow(Game game) {        
        m_game = game;
        setPreferredSize(new Dimension((int)m_game.getWidth(),(int)m_game.getHeight()));
    }
    

    public static void newWindow(Game game) {
        /*
        Creates a Java window of the appropriate size to render the game:
        */
        JFrame frame = new JFrame("Steering");
        GameWindow c = new GameWindow(game);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(c, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.repaint();

        /*
        Game loop: this updates and redraws the game at the appropriate frequency
        to achieve the desired frames per second.
        */
        long next_update = System.currentTimeMillis();
        int interval = 1000 / game.getFPS();
        while(true) {
            try {
                long current_time = System.currentTimeMillis();
                if (current_time>=next_update) {
                    game.update();
                    frame.repaint();
                    next_update += interval;
                    if (current_time>=next_update + interval*10) next_update = current_time + interval;
                }
                /*
                this is necessary if you don't want this game to use 100% of the CPU,
                even if it only needs a tiny fraction.
                */
                Thread.sleep(1);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0,0, getWidth(), getHeight());
        m_game.draw(g2d);
    }    
}
