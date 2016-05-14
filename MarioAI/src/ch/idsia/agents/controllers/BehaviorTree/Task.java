package ch.idsia.agents.controllers.BehaviorTree;

import ch.idsia.agents.controllers.BTAgent;

/**
 * Created by jinuj on 5/10/2016.
 */
public abstract class Task {

    public enum Status {
        RUNNING, SUCCESS, FAILURE;

        public String getString(){
            switch (this){
                case RUNNING:
                    return "RUNNING";
                case SUCCESS:
                    return "SUCCESS";
                default:
                    return "FAILURE";
            }
        }
    }

    public BTAgent agent;
    public abstract boolean run();

    public Status status;


}
