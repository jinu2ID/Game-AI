package test;
import engine.GameVector;

/**
 * Created by jinuj on 4/15/2016.
 */
public class GameVectorTest {

    public static void main(String args[]){

        GameVector t1 = new GameVector(.12345,.23456);
        GameVector t2 = new GameVector(.3,.4);

        System.out.println(t1.normalize().toString());
    }
}
