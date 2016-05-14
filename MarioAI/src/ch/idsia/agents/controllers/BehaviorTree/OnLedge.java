package ch.idsia.agents.controllers.BehaviorTree;

/**
 * Created by jinuj on 5/13/2016.
 */
public class OnLedge extends Task {

    public boolean run(){
        if (agent.onLedge()) {
            //System.out.println("ON LEDGE");
            status = Status.SUCCESS;
            return true;
        }
        status = Status.FAILURE;
        return false;
    }
}
