package test;

import controllers.KeyboardController;
import engine.*;

import java.awt.Color;

/**
 *
 * @author santi
 */
public class KeyboardExample {
    public static void main(String args[]) throws Exception {
        Game game = new Game(800,600, 25);
        game.add(new Car("graphics/redcar.png",400,300,0, new KeyboardController()));
        /*game.add(new Obstacle(0,0,800,25,Color.GRAY));
        game.add(new Obstacle(0,575,800,25,Color.GRAY));
        game.add(new Obstacle(0,0,25,600,Color.GRAY));
        game.add(new Obstacle(775,0,25,600,Color.GRAY));*/

        Obstacle box = new Obstacle(100,100,25,25,Color.YELLOW);
        box.name = "YellowBox";
        game.add(box);

        Obstacle box2 = new Obstacle(675,100,25,25,Color.BLUE);
        box2.name = "BlueBox";
        game.add(box2);

        Obstacle box3 = new Obstacle(100,475,25,25,Color.GREEN);
        box3.name = "GreenBox";
        game.add(box3);

        Obstacle box4 = new Obstacle(657,475,25,25,Color.RED);
        box4.name = "RedBox";
        game.add(box4);
        GameWindow.newWindow(game);
    }
}
