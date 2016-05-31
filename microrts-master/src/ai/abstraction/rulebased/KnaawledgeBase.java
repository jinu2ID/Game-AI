package ai.abstraction.rulebased;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinuj on 5/22/2016.
 */
public class KnaawledgeBase {

    List<Term> facts;

    public KnaawledgeBase(){
        facts = new ArrayList<>();
    }

    void addTerm(Term t){
        facts.add(t);
    }

    void clear(){
        facts.clear();
    }
}
