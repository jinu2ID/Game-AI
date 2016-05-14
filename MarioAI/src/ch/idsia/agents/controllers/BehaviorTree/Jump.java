package ch.idsia.agents.controllers.BehaviorTree;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

/**
 * Created by jinuj on 5/12/2016.
 */
public class Jump extends Task {

    public boolean run(){
        //System.out.println("JUMP");
        status = Status.SUCCESS;
        agent.action[Mario.KEY_JUMP] = agent.isMarioAbleToJump || !agent.isMarioOnGround;
        return true;
    }
}
