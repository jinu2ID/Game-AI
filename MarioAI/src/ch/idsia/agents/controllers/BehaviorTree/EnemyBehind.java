package ch.idsia.agents.controllers.BehaviorTree;

/**
 * Created by jinuj on 5/13/2016.
 */
public class EnemyBehind extends Task {

    public boolean run(){
        if (agent.enemyBehind()){
            //System.out.println("Enemy Behind");
            status = Status.SUCCESS;
            return true;
        }

        status = Status.FAILURE;
        return false;
    }
}
