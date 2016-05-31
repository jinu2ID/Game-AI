package ai.abstraction.rulebased;

import java.util.List;

/**
 * Created by jinuj on 5/23/2016.
 */
public class Rule {


    Term[] pattern;
    Term[] effect;
    int effectType;

    public Rule (Term[] _pattern, Term[] _effect, int _effectType ){
        pattern = _pattern;
        effect = _effect;
        effectType = _effectType;
    }

    // Bind the values to the variables in effect[]
    public void instantiate(Object[] bindings){
        for (Term t : effect){
            if (t.parameters[0] instanceof List)
                t.parameters[0] = bindings[1];
            if (t.parameters[0] == bindings[0]){
                t.parameters[0] = bindings[1];
            }
        }

    }



}
