package ai.abstraction.rulebased;

import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinuj on 5/23/2016.
 */
public class rulesAI extends AbstractionLayerAI {

    KnowledgeBase KB;

    public rulesAI(PathFinding a_pf, KnowledgeBase _KB){
        super(a_pf);

        KB = _KB;
    }

    public void reset(){}

    public AI clone(){return new rulesAI(pf, KB);}

    public PlayerAction getAction(int player, GameState gs){
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);

        // Add facts to game state
        for (Unit u : pgs.getUnits()){
            KB.addTerm(new Term("Type", u, u.getType()));
        }

        return translateActions(player, gs);
    }

    // Check which rules are met and fire their effects
    public void RuleBasedSystemIteration(Rule[] rules){

        List<Rule> FiredRules = new ArrayList<>();      // List of rules to be fired

        // Check if the conditions/pattern for each rule is met
        for (Rule r : rules){

            boolean patternFound = true;    // assume pattern will be found

            // check each pattern in the rule
            for (Term p : r.pattern) {

                boolean empty = false;
                Object[] bindings = new Object[2];
                bindings = this.unification(p, bindings);

                // If a unification exists the binding will not be empty
                for (Object o : bindings){
                    if (o == null){
                        empty = true;
                        break;
                    }
                }

                // A pattern unification did not exist
                if (empty){
                    patternFound = false;
                    break;
                }

                // unification exists, update the variables in effects[]
                r.instantiate(bindings);
            }

            // One of the patterns was not found so continue to next rule
            if (!patternFound){
                continue;
            }

            FiredRules.add(r);

        }

        /* TO IMPLEMENT:
            RulesToExecute = arbitrate(FiredRules);
            for each e in RulesToExecute
                Execute(r.action)

        */
    }

    // Unify a pattern with the knowledge base and return the binding
    public Object[] unification(Term pattern, Object[] bindings){

        for (Term fact : KB.facts){
            if (unifyVariable(pattern, fact)){
                bindings[0] = pattern.parameters[0];
                bindings[1] = fact.parameters[0];
            }
        }
        return bindings;
    }

    // Check if there is unification between a pattern and a fact in KB
    public boolean unifyVariable(Term pattern, Term fact){
        // If the functors are not the same return an empty binding
        if (!(pattern.functor.equals(fact.functor))){
            return false;
        }  // If the number of parameters is not the same return empty binding
        else if (pattern.parameters.length != fact.parameters.length){
            return false;
        }  // If the type of
        else if(pattern.parameters[1] == fact.parameters[1]){
            return true;
        }

        return false;
    }


}
