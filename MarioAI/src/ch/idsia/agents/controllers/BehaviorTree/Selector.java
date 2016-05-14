package ch.idsia.agents.controllers.BehaviorTree;

import java.util.List;
import java.util.ListIterator;

import static ch.idsia.agents.controllers.BehaviorTree.Task.Status.RUNNING;

/**
 * Created by jinuj on 5/11/2016.
 */
public class Selector extends Composite {

    ListIterator<Task> current;

    public Selector(List<Task> _children){
        super(_children);

        current = children.listIterator();
    }

    public boolean run(){

        if (current.hasNext()) {
            Task child = current.next();
            child.run();
            if (child.status == Status.SUCCESS) {
                status = Status.SUCCESS;
                return true;
            } else if (child.status == Status.FAILURE) {
                status = Status.RUNNING;
                return false;
            } else if (child.status == RUNNING) {
                status = Status.RUNNING;
                current.previous();
                return false;
            }
        }

        status = Status.FAILURE;
        return false;

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
