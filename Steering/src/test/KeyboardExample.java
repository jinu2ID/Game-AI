package test;

import controllers.KeyboardController;
import engine.Car;
import engine.Game;
import engine.GameWindow;
import engine.Obstacle;
import java.awt.Color;

/**
 *
 * @author santi
 */
public class KeyboardExample {
    public static void main(String args[]) throws Exception {
        Game game = new Game(800,600, 25);
        game.add(new Car("graphics/redcar.png",400,300,0, new KeyboardController()));
        game.add(new Obstacle(0,0,800,25,Color.GRAY));
        game.add(new Obstacle(0,575,800,25,Color.GRAY));
        game.add(new Obstacle(0,0,25,600,Color.GRAY));
        game.add(new Obstacle(775,0,25,600,Color.GRAY));
        GameWindow.newWindow(game);
    }
}
