package ch.idsia.agents.controllers.BehaviorTree;

/**
 * Created by jinuj on 5/12/2016.
 */
public class WallRight extends Task {

    public boolean run(){
        if (agent.wallRight()) {
            //System.out.println("WALL RIGHT");
            return true;
        }
        return false;
    }
}