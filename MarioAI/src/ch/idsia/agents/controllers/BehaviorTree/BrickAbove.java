package ch.idsia.agents.controllers.BehaviorTree;

/**
 * Created by jinuj on 5/12/2016.
 */
public class BrickAbove extends Task{

    public boolean run(){
        if(agent.brickAbove())
            return true;
        return false;
    }
}
