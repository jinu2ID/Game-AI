package ai.abstraction.rulebased;

import ai.abstraction.AbstractAction;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.Harvest;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

import java.util.*;

/**
 * Created by jinuj on 5/23/2016.
 */
public class rulesAI extends AbstractionLayerAI {

    Random rand;
    KnaawledgeBase KB;
    Rule[] rules;
    UnitTypeTable utt;
    Map<String, Integer> TOE;   // Table of Organization and Equipment;

    int mapArea = -1;

    public rulesAI(UnitTypeTable _utt, PathFinding a_pf, KnaawledgeBase _KB, Rule[] _rules){
        super(a_pf);

        rand = new Random();
        KB = _KB;
        rules = _rules;
        utt = _utt;
        TOE = new HashMap<>();

        TOE.put("Base",0);
        TOE.put("Barracks",0);
        TOE.put("Worker",0);
        TOE.put("Light",0);
        TOE.put("Resource", 0);
        TOE.put("Ranged", 0);

    }

    public void reset(){}

    public AI clone(){ return new rulesAI(utt, pf, KB, rules);}

    public PlayerAction getAction(int player, GameState gs){
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);

        // Get map size
        if (mapArea == -1){
            mapArea = pgs.getHeight() * pgs.getWidth();
        }

        if (mapArea >= 576){
            KB.addTerm(new Term("MapSize", "LargeMap", "LargeMap"));
        } else {
            KB.addTerm(new Term("MapSize", "SmallMap", "SmallMap"));
        }

        // Update Knowledge Baase
        perception(player, pgs);

        RuleBasedSystemIteration(p, pgs);

        // Clear TOE and KB
        for (Map.Entry<String, Integer> entry : TOE.entrySet()){
            //System.out.println(entry.getKey() + "/" + entry.getValue());
            entry.setValue(0);
        }

        KB.clear();

        return translateActions(player, gs);
    }

    // Add facts about game state
    public void perception(int player, PhysicalGameState pgs ){

        List<Unit> lightSquad = new ArrayList<>();
        List<Unit> rangedSquad = new ArrayList<>();
        List<Unit> workerSquad = new ArrayList<>();

        for (Unit u : pgs.getUnits()){
            if (u.getPlayer() == player) {
                String name = u.getType().name;
                KB.addTerm(new Term("Type", u, name));    // update KB
                int count = TOE.get(name);
                TOE.put(name, count + 1);                 // update TOE

                if (name == "Light"){
                    lightSquad.add(u);
                } else if (name == "Ranged"){
                    rangedSquad.add(u);
                } else if (name == "Worker" && TOE.get("Worker") > 1){
                    workerSquad.add(u);
                }
            }
        }

        if (lightSquad.size() >= 1){
            KB.addTerm(new Term("Squad", lightSquad,"Light"));
            if (lightSquad.size() >= 5 && lightSquad.size()%5 == 0) {
                KB.addTerm(new Term("3ofType", true, "Light"));
            }
        }
        if (rangedSquad.size() >= 1){
            KB.addTerm(new Term("Squad", rangedSquad, "Ranged"));
        }
        if (workerSquad.size() != 0){
            KB.addTerm(new Term("Squad", workerSquad, "Worker"));
        }

        // If there are no units of a type, add that info to KB
        for (Map.Entry<String, Integer> entry : TOE.entrySet()){
            if(entry.getValue() == 0){
                KB.addTerm(new Term("noUnitType", true, entry.getKey()));
            }
        }


    }

    // Check which rules are met and fire their effects
    public void RuleBasedSystemIteration(Player _p, PhysicalGameState pgs){

        List<Rule> FiredRules = new ArrayList<>();      // List of rules to be fired

        // Check if the conditions/pattern for each rule is met
        for (Rule r : rules){

            boolean patternFound = true;    // assume pattern will be found

            // check each pattern in the rule
            for (Term p : r.pattern) {

                boolean empty = false;
                Object[] bindings = new Object[2];
                bindings = unification(p, bindings);

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

        // Pick a rule and execute it
        if (FiredRules.size() != 0) {
            Rule RuleToExecute = arbitrate(FiredRules);
            execute(RuleToExecute, _p, pgs);
        }



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

    public Rule arbitrate(List<Rule> rules){

        int n = rand.nextInt(rules.size());
        return rules.get(n);

    }

    // Execute an effect type
    public void execute(Rule r, Player p, PhysicalGameState pgs){

        switch(r.effectType){
            // Debug statement
            case 0: System.out.println("TEST");
                break;
            case 1: train(r,p);
                break;
            case 2: build(r,p, pgs);
                break;
            case 3: harvest(r, p, pgs);
                break;
            case 4: meleeAttack(r, p, pgs);
                break;
            case 5: squadAttack(r, p, pgs);
                break;

        }
    }

    public void train(Rule r, Player p){
        String type = (String)r.effect[0].parameters[1];
        UnitType t = utt.getUnitType(type);
        Unit u = (Unit)r.effect[0].parameters[0];
        if (p.getResources() >= t.cost){
            train(u, t);
        }
    }

    public void build(Rule r, Player _p, PhysicalGameState pgs){
        String type = (String)r.effect[0].parameters[1];
        UnitType t = utt.getUnitType(type);
        Unit u = (Unit) r.effect[0].parameters[0];
        if (_p.getResources() >= t.cost) {
            List<Integer> reservedPositions = new LinkedList<>();
            int pos = findBuildingPosition(reservedPositions, (Unit) r.effect[0].parameters[0], _p, pgs);
            build(u, t, pos % pgs.getWidth(), pos / pgs.getWidth());
        }
    }

    public void buildBarracks(Rule r, Player _p, PhysicalGameState pgs){
        UnitType barracksType = utt.getUnitType("Barracks");
        Unit u = (Unit) r.effect[0].parameters[0];
        if (_p.getResources() >= barracksType.cost) {
            List<Integer> reservedPositions = new LinkedList<>();
            int pos = findBuildingPosition(reservedPositions, (Unit) r.effect[0].parameters[0], _p, pgs);
            build(u, barracksType, pos % pgs.getWidth(), pos / pgs.getWidth());
        }
    }

    public void harvest(Rule r, Player p, PhysicalGameState pgs){
        Unit u = (Unit)r.effect[0].parameters[0];
        Unit closestBase = null;
        Unit closestResource = null;
        int closestDistance = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getType().isResource) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestResource == null || d < closestDistance) {
                    closestResource = u2;
                    closestDistance = d;
                }
            }
        }
        closestDistance = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getType().isStockpile && u2.getPlayer()==p.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestBase == null || d < closestDistance) {
                    closestBase = u2;
                    closestDistance = d;
                }
            }
        }
        if (closestResource != null && closestBase != null) {
            AbstractAction aa = getAbstractAction(u);
            if (aa instanceof Harvest) {
                Harvest h_aa = (Harvest)aa;
                if (h_aa.target != closestResource || h_aa.base!=closestBase) harvest(u, closestResource, closestBase);
            } else {
                harvest(u, closestResource, closestBase);
            }
        }
    }

    public void attack(Unit u, Player p, PhysicalGameState pgs){
        Unit closestEnemy = null;
        int closestDistance = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestEnemy == null || d < closestDistance) {
                    closestEnemy = u2;
                    closestDistance = d;
                }
            }
        }
        if (closestEnemy != null) {
            attack(u, closestEnemy);
        }
    }

    public void meleeAttack(Rule r, Player p, PhysicalGameState pgs){
        Unit u = (Unit)r.effect[0].parameters[0];
        attack(u, p, pgs);
    }

    public void lightAttack(Rule r, Player p, PhysicalGameState pgs){
        Unit u = (Unit)r.effect[0].parameters[0];
        attack(u, p, pgs);
    }

    public void squadAttack(Rule r, Player p, PhysicalGameState pgs){
        List<Unit> squad = (List<Unit>) r.effect[0].parameters[0];
        for (Unit member : squad){
            attack(member, p, pgs);
        }
    }

    // Finds the nearest available location at which a building can be placed:
    public int findBuildingPosition(List<Integer> reserved, Unit u, Player p, PhysicalGameState pgs) {
        int bestPos = -1;
        int bestScore = 0;

        for (int x = 0; x < pgs.getWidth(); x++) {
            for (int y = 0; y < pgs.getHeight(); y++) {
                int pos = x + y * pgs.getWidth();
                if (!reserved.contains(pos) && pgs.getUnitAt(x, y) == null) {
                    int score = 0;

                    score = -(Math.abs(u.getX() - x) + Math.abs(u.getY() - y));

                    if (bestPos == -1 || score > bestScore) {
                        bestPos = pos;
                        bestScore = score;
                    }
                }
            }
        }

        return bestPos;
    }
}
