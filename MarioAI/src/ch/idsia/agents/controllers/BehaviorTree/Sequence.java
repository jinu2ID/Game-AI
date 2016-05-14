package ch.idsia.agents.controllers.BehaviorTree;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by jinuj on 5/11/2016.
 */
public class Sequence extends Composite {

    protected ListIterator<Task> current;

    public Sequence(List<Task> _children){
            super(_children);

        current = children.listIterator();
    }

    public boolean run(){
        //status = Status.RUNNING;
        if (!current.hasNext()) {     // reached end of list
            status = Status.SUCCESS;
            return true;
        }
        if (current.next().run()) {
            status = Status.RUNNING;
            return false;
        }
        else {                        // a child failed
            status = Status.FAILURE;
            return false;
        }
    }

    public void reset(){
        current = children.listIterator();      // Reset current to head of the list

        while (current.hasNext()){              // while the list is not empty
            Task child = current.next();
            if (child instanceof Composite)     // If the child is a Composite
                ((Composite) child).reset();    // Reset it as well
        }

        current = children.listIterator();      // Reset current to head of the list
    }
}
