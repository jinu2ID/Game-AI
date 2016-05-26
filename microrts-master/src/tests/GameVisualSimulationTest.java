 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import ai.abstraction.LightRush;
import ai.abstraction.rulebased.KnowledgeBase;
import ai.abstraction.rulebased.Rule;
import ai.abstraction.rulebased.Term;
import ai.abstraction.rulebased.rulesAI;
import ai.core.AI;
import ai.*;
import ai.abstraction.WorkerRush;
import ai.abstraction.pathfinding.BFSPathFinding;
import ai.mcts.naivemcts.NaiveMCTS;
import ai.mcts.uct.UCT;
import gui.PhysicalGameStatePanel;
import java.io.OutputStreamWriter;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;
import util.XMLWriter;

/**
 *
 * @author santi
 */
public class GameVisualSimulationTest {
    public static void main(String args[]) throws Exception {
        UnitTypeTable utt = new UnitTypeTable();
        PhysicalGameState pgs = PhysicalGameState.load("maps/basesWorkers16x16.xml", utt);
//        PhysicalGameState pgs = MapGenerator.basesWorkers8x8Obstacle();

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 5000;
        int PERIOD = 20;
        boolean gameover = false;

        // Setup rulesAI
        String functor = "Type";
        Unit u = new Unit();
        String uType = "Base";

        String functor2 = "Build";
        Unit u2 = new Unit();


        Term pattern = new Term(functor, u, uType);
        Term[] patterns = new Term[1];
        patterns[0] = pattern;
        Term effect = new Term(functor2, u, null);
        Term[] effects = new Term[1];
        effects[0] = effect;
        int eType = 1;

        Rule r = new Rule(patterns, effects, eType);
        Rule[] rules = new Rule[1];
        rules[0] = r;

        KnowledgeBase kb = new KnowledgeBase();
        rulesAI rAI = new rulesAI(utt, new BFSPathFinding(), kb, rules);


        AI ai1 = new WorkerRush(utt, new BFSPathFinding());        
        AI ai2 = new RandomBiasedAI();

        XMLWriter xml = new XMLWriter(new OutputStreamWriter(System.out));
        pgs.toxml(xml);
        xml.flush();

        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);

        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do{
            if (System.currentTimeMillis()>=nextTimeToUpdate) {
                //PlayerAction pa1 = ai1.getAction(0, gs);
                PlayerAction pa1 = rAI.getAction(0, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
                gs.issueSafe(pa1);
                gs.issueSafe(pa2);

                // simulate:
                gameover = gs.cycle();
                w.repaint();
                nextTimeToUpdate+=PERIOD;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }while(!gameover && gs.getTime()<MAXCYCLES);
        
        System.out.println("Game Over");
    }    
}
