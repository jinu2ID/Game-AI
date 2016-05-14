package ch.idsia.agents.controllers.BehaviorTree;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

import static ch.idsia.benchmark.mario.engine.sprites.Mario.KEY_SPEED;

/**
 * Created by jinuj on 5/12/2016.
 */
public class Shoot extends Task {

    public boolean run(){
        //System.out.println("Shoot");
        status = Status.SUCCESS;
        agent.action[Mario.KEY_SPEED] = agent.isMarioAbleToShoot;
        return true;
    }
}
