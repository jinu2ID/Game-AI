package ch.idsia.agents.controllers;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BehaviorTree.Composite;
import ch.idsia.agents.controllers.BehaviorTree.Task;
import ch.idsia.benchmark.mario.engine.sprites.Sprite;

import static java.lang.Math.abs;

/**
 * Created by jinuj on 5/11/2016.
 * An agent that uses a behavior tree
 */
public class BTAgent extends BasicMarioAIAgent implements Agent {

    Composite current;
    public int stepsTaken = 0;

    public BTAgent(Composite root)
    {
        super("BTAgent");
        current = root;
        reset();
    }

    public boolean[] getAction(){
        current.run();
        //System.out.println(stepsTaken);
        //System.out.println(getReceptiveFieldCellValue(marioEgoRow+1,marioEgoCol+1));
        if(current.status == Composite.Status.SUCCESS) {
            //System.out.println(current.status.getString());
            current.reset();
        } else if (current.status == Composite.Status.FAILURE){
            //System.out.println(current.status.getString());
            current.reset();
        }
        return action;
    }

    private boolean isCreature(int c)
    {
        switch (c)
        {
            case Sprite.KIND_GOOMBA:
            case Sprite.KIND_RED_KOOPA:
            case Sprite.KIND_RED_KOOPA_WINGED:
            case Sprite.KIND_GREEN_KOOPA_WINGED:
            case Sprite.KIND_GREEN_KOOPA:
                return true;
        }
        return false;
    }

    public boolean enemyNear(){

        int x = marioEgoRow;
        int y = marioEgoCol;

        if (isCreature(enemies[x][y+3])
                || isCreature(enemies[x][y+2])
                || isCreature(enemies[x][y+1])
                || isCreature(enemies[x+1][y+1]))
            return true;

        return false;
    }

    public boolean enemyBehind(){

        int x = marioEgoRow;
        int y = marioEgoCol;

        if (isCreature(enemies[x][y-3])
                || isCreature(enemies[x][y-2])
                || isCreature(enemies[x][y-1]))
            return true;

        return false;
    }

    // Checks for creatures 3-9 cells away
    public boolean areaSafe(){
        for (int i = 0; i < enemiesFloatPos.length;i++){

            if (i%6 == 1 && abs(enemiesFloatPos[i]) < 75)
                return true;
        }
        return false;
    }

    public boolean wallRight(){
        if (getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+1) == -60            // ground
                || getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+1) == -85     // pip
                || getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+1) == -24) {  // bricks
            return true;
        }
        return false;
    }

    public boolean brickAbove(){
        if (getReceptiveFieldCellValue(marioEgoRow-1,marioEgoCol) == -24
                || getReceptiveFieldCellValue(marioEgoRow-2,marioEgoCol) == -24
                || getReceptiveFieldCellValue(marioEgoRow-3,marioEgoCol) == -24)
            return true;

        return  false;
    }

    public boolean pathBlocked(){
        if (getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+2) == -60            // ground
                || getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+2) == -85     // pip
                || getReceptiveFieldCellValue(marioEgoRow,marioEgoCol+2) == -24) {  // bricks
            return true;
        }
        return false;
    }

    public boolean onLedge(){
        if (getReceptiveFieldCellValue(marioEgoRow+1,marioEgoCol+1) == 0 && isMarioOnGround)
            return  true;

        return false;
    }

}
