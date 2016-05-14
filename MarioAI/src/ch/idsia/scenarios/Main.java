/*
 * Copyright (c) 2009-2010, Sergey Karakovskiy and Julian Togelius
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Mario AI nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package ch.idsia.scenarios;

import ch.idsia.agents.controllers.BTAgent;
import ch.idsia.agents.controllers.BehaviorTree.*;
import ch.idsia.agents.controllers.BehaviorTree.WallRight;
import ch.idsia.benchmark.tasks.AreaNotSafe;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: Sergey Karakovskiy, sergey at idsia dot ch Date: Mar 17, 2010 Time: 8:28:00 AM
 * Package: ch.idsia.scenarios
 */
public final class Main
{
public static void main(String[] args)
{
//        final String argsString = "-vis on";
    final MarioAIOptions marioAIOptions = new MarioAIOptions(args);
//        final Environment environment = new MarioEnvironment();
//        final Agent agent = new ForwardAgent();

    //[ -----Behavior Leaves----- ]
    Task shoot = new Shoot();
    Task enemyNear = new EnemyNear();
    Task moveRight = new MoveRight();
    Task areaSafe = new AreaSafe();
    Task wallRight = new WallRight();
    Task jump = new Jump();
    Task brickAbove = new BrickAbove();
    Task tacticalPause = new TacticalPause();
    Task onLedge = new OnLedge();
    Task areaNotSafe = new AreaNotSafe();
    Task enemyBehind = new EnemyBehind();
    Task turnLeft = new TurnLeft();

    //[ -----Children----- ]

    // Shoot nearby enemies
    List<Task> childrenSeq1 = new ArrayList<>();
    childrenSeq1.add(enemyNear);
    childrenSeq1.add(jump);
    childrenSeq1.add(shoot);


    // Jump over walls
    List<Task> childrenSeq2 = new ArrayList<>();
    //childrenSeq2.add(shoot);
    childrenSeq2.add(wallRight);
    childrenSeq2.add(jump);
    childrenSeq2.add(shoot);

    // Move right, pause intermittently
    List<Task> childrenSeq3 = new ArrayList<>();
    childrenSeq3.add(shoot);
    childrenSeq3.add(moveRight);
    childrenSeq3.add(tacticalPause);
    //childrenSeq3.add(shoot);

    // Jump if brick above
    List<Task> childrenSeq4 = new ArrayList<>();
    childrenSeq4.add(brickAbove);
    childrenSeq4.add(jump);

    // Jump if on ledge
    List<Task> childrenSeq5 = new ArrayList<>();
    childrenSeq5.add(shoot);
    childrenSeq5.add(onLedge);
    childrenSeq5.add(jump);

    // Clear area
    List<Task> childrenSeq6 = new ArrayList<>();
    childrenSeq6.add(areaNotSafe);
    childrenSeq6.add(shoot);
    //childrenSeq6.add(jump);

    // Cliff right and enemy
    List<Task> childrenSeq7 = new ArrayList<>();
    childrenSeq7.add(areaNotSafe);
    childrenSeq7.add(wallRight);
    childrenSeq7.add(turnLeft);
    childrenSeq7.add(moveRight);

    //[ -----Composite Nodes----- ]
    Composite seq1 = new Sequence(childrenSeq1);    // Shoot nearby enemies
    Composite seq2 = new Sequence(childrenSeq2);    // Jump over walls
    Composite seq3 = new Sequence(childrenSeq3);    // Move right
    Composite seq4 = new Sequence(childrenSeq4);    // Jump for bricks
    Composite seq5 = new Sequence(childrenSeq5);    // Jump off ledge
    Composite seq6 = new Sequence(childrenSeq6);    // Clear unsafe area
    Composite seq7 = new Sequence(childrenSeq7);

    List<Task> childrenSel = new ArrayList<>();
    //childrenSel.add(seq6);
    childrenSel.add(seq1);
    //childrenSel.add(seq7);
    childrenSel.add(seq5);
    //childrenSel.add(seq4);
    childrenSel.add(seq3);
    childrenSel.add(seq2);
    Composite sel = new Selector(childrenSel);
    BTAgent agent = new BTAgent(sel);

    enemyNear.agent = agent;
    shoot.agent = agent;
    areaSafe.agent = agent;
    moveRight.agent = agent;
    wallRight.agent = agent;
    jump.agent = agent;
    brickAbove.agent = agent;
    tacticalPause.agent = agent;
    onLedge.agent = agent;
    areaNotSafe.agent = agent;
    enemyBehind.agent = agent;
    turnLeft.agent = agent;
    seq1.agent = agent;
    seq2.agent = agent;
    seq3.agent = agent;
    seq4.agent = agent;
    seq5.agent = agent;
    seq6.agent = agent;
    sel.agent = agent;


    marioAIOptions.setAgent(agent);
//        final Agent agent = marioAIOptions.getAgent();
//        final Agent a = AgentsPool.loadAgent("ch.idsia.controllers.agents.controllers.ForwardJumpingAgent");
    final BasicTask basicTask = new BasicTask(marioAIOptions);
//        for (int i = 0; i < 10; ++i)
//        {
//            int seed = 0;
//            do
//            {
//                marioAIOptions.setLevelDifficulty(i);
//                marioAIOptions.setLevelRandSeed(seed++);
    basicTask.setOptionsAndReset(marioAIOptions);
//    basicTask.runSingleEpisode(1);
    basicTask.doEpisodes(1,true,1);
//    System.out.println(basicTask.getEnvironment().getEvaluationInfoAsString());
//            } while (basicTask.getEnvironment().getEvaluationInfo().marioStatus != Environment.MARIO_STATUS_WIN);
//        }
//
    System.exit(0);
}

}


/*  OLD TREE

*/

/* NEW TREE(UNFINISHED)

    // Enemy behind
    List<Task> children4Seq1 = new ArrayList<>();
children4Seq1.add(enemyBehind);
        children4Seq1.add(turnLeft);
        children4Seq1.add(jump);
        children4Seq1.add(shoot);
        children4Seq1.add(moveRight);
        Composite fourSeq1 = new Sequence(children4Seq1);

        // Enemy Front
        List<Task> children4Seq2 = new ArrayList<>();
        children4Seq2.add(jump);
        children4Seq2.add(shoot);
        Composite fourSeq2 = new Sequence(children4Seq2);

        // 3Sel1
        List<Task> children3Sel1 = new ArrayList<>();
        //children3Sel1.add(fourSeq1);
        children3Sel1.add(fourSeq2);
        Composite threeSel1 = new Selector(children3Sel1);

        // Enemy Near
        List<Task> children2Seq1 = new ArrayList<>();
        children2Seq1.add(enemyNear);
        children2Seq1.add(threeSel1);
        Composite oneSel1 = new Selector(children2Seq1);

        BTAgent agent = new BTAgent(oneSel1);

        enemyNear.agent = agent;
        shoot.agent = agent;
        areaSafe.agent = agent;
        moveRight.agent = agent;
        wallRight.agent = agent;
        jump.agent = agent;
        brickAbove.agent = agent;
        tacticalPause.agent = agent;
        onLedge.agent = agent;
        areaNotSafe.agent = agent;
        enemyBehind.agent = agent;
        turnLeft.agent = agent;*/
