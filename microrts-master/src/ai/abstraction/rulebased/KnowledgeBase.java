package ai.abstraction.rulebased;

import java.util.List;

/**
 * Created by jinuj on 5/22/2016.
 */
public class KnowledgeBase {

    List<Term> facts;

    void addTerm(Term t){
        facts.add(t);
    }

    void clear(){
        facts.clear();
    }
}
