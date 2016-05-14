package ch.idsia.agents.controllers.BehaviorTree;

import ch.idsia.benchmark.mario.engine.sprites.Mario;

/**
 * Created by jinuj on 5/13/2016.
 */
public class TacticalPause extends Task {

    public boolean run(){
        //System.out.println("Tactical Pause");
        if (agent.stepsTaken < 5)
            return true;
        else if (agent.stepsTaken < 15
                && !agent.enemyNear()
                && !agent.enemyBehind()
                && !agent.wallRight()) {
            agent.action[Mario.KEY_RIGHT] = false;
            agent.stepsTaken += 1;
            return false;
        }
        else {
            agent.stepsTaken = 0;
            return true;
        }
    }
}
