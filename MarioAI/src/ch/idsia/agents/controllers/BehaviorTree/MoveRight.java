package ch.idsia.agents.controllers.BehaviorTree;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

/**
 * Created by jinuj on 5/12/2016.
 */
public class MoveRight extends Task {

    public boolean run(){
        //System.out.println("Move Right");
        if (!agent.wallRight()) {
            status = Status.SUCCESS;
            agent.action[Mario.KEY_RIGHT] = true;
            agent.stepsTaken += 1;
            return true;
        }

        return false;
    }
}
