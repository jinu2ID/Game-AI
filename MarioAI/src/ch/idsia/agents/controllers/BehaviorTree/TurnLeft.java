package ch.idsia.agents.controllers.BehaviorTree;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

/**
 * Created by jinuj on 5/13/2016.
 */
public class TurnLeft extends Task {

    public boolean run(){
        if (!agent.action[Mario.KEY_LEFT]){
            agent.action[Mario.KEY_LEFT] = true;
            status = Status.SUCCESS;
            return true;
        }

        status = Status.FAILURE;
        return false;
    }
}
