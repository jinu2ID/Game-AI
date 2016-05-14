package ch.idsia.agents.controllers.BehaviorTree;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

/**
 * Created by jinuj on 5/12/2016.
 */
public class AreaSafe extends Task{

    public boolean run(){
        if (!agent.areaSafe() && !agent.pathBlocked()) {
            //System.out.println("Area Not Safe");
            agent.action[Mario.KEY_RIGHT] = false;
        }
        else {
            //System.out.println("Area Safe");
        }
        return (agent.areaSafe());
    }
}
