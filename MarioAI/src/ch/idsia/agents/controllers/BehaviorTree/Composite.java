package ch.idsia.agents.controllers.BehaviorTree;

import java.util.List;

/**
 * Created by jinuj on 5/11/2016.
 */
public abstract class Composite extends Task {

    List<Task> children;

    public Composite(List<Task> _children){
        children = _children;
    }

    // Resets current pointer to first child
    public abstract void reset();
}
