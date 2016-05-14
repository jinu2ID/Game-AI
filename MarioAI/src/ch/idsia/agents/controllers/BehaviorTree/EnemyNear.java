package ch.idsia.agents.controllers.BehaviorTree;

import ch.idsia.agents.controllers.BTAgent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;

import static ch.idsia.benchmark.mario.engine.GlobalOptions.marioEgoCol;
import static ch.idsia.benchmark.mario.engine.GlobalOptions.marioEgoRow;

/**
 * Created by jinuj on 5/12/2016.
 */
public class EnemyNear extends Task {



    public boolean run(){
        if (agent.enemyNear()) {
            //System.out.println("Enemy Near");
            return true;
        }
        return false;
    }
}
