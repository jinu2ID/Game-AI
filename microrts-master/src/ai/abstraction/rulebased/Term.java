package ai.abstraction.rulebased;

/**
 * Created by jinuj on 5/22/2016.
 */
public class Term {

    String functor;
    Object parameters[];

    public Term(String _functor, Object p1, Object p2){
        functor = _functor;
        parameters = new Object[]{p1, p2};
    }

}
