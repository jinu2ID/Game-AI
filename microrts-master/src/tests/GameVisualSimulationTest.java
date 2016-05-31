 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import ai.abstraction.*;
import ai.abstraction.rulebased.*;
import ai.core.AI;
import ai.*;
import ai.abstraction.pathfinding.BFSPathFinding;
import gui.PhysicalGameStatePanel;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
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
        PhysicalGameState pgs = PhysicalGameState.load("maps/basesWorkers24x24.xml", utt);
//        PhysicalGameState pgs = MapGenerator.basesWorkers8x8Obstacle();

        GameState gs = new GameState(pgs, utt);
        int MAXCYCLES = 5000;
        int PERIOD = 20;
        boolean gameover = false;

        rulesAI rAI = setRules.instantiate(utt);
        // End rulesAI setup -------------------------------------------

        AI ai1 = new WorkerRush(utt, new BFSPathFinding());        
        AI ai2 = new RandomBiasedAI();
        AI ai3 = new HeavyRush(utt, new BFSPathFinding());
        AI ai4 = new LightRush(utt, new BFSPathFinding());
        AI ai6 = new RangedRush(utt, new BFSPathFinding());

        XMLWriter xml = new XMLWriter(new OutputStreamWriter(System.out));
        pgs.toxml(xml);
        xml.flush();

        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_BLACK);
//        JFrame w = PhysicalGameStatePanel.newVisualizer(gs,640,640,false,PhysicalGameStatePanel.COLORSCHEME_WHITE);

        long nextTimeToUpdate = System.currentTimeMillis() + PERIOD;
        do{
            if (System.currentTimeMillis()>=nextTimeToUpdate) {
                PlayerAction pa1 = ai1.getAction(1, gs);
                PlayerAction pa2 = ai2.getAction(1, gs);
                PlayerAction pa3 = ai3.getAction(1, gs);
                PlayerAction pa4 = ai4.getAction(1, gs);
                PlayerAction pa5 = rAI.getAction(0, gs);
                PlayerAction pa6 = ai6.getAction(1, gs);

                gs.issueSafe(pa4);
                gs.issueSafe(pa5);

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
