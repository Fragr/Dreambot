package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AmmoniteKiller;

public class ResetNode extends TaskNode {

    Area crabArea = new Area(/*3257, 3244, 3259, 3247*/3732, 3846, 3733, 3847);
    Area resetArea = new Area(/*3257, 3226, 3261, 3229*/3736, 3815, 3738, 3814);

    boolean resetAgro;

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public boolean accept() {
        if( crabArea.contains(getLocalPlayer().getTile())
                && !sleepWhile( () -> !getLocalPlayer().isInCombat(), Calculations.random(7000, 10000) ) ){
            resetAgro = true;
            return true;
        }
        return false;
    }

    @Override
    public int execute() {
        while( resetAgro ){
            //Walk to reset area
            getWalking().walk(resetArea.getRandomTile());
            log("Running to reset area");

            if( resetArea.contains(getLocalPlayer().getTile()) ) {
                resetAgro = false;
                log("In reset area");
                sleep(Calculations.random(3000, 5000));
            }
            sleepWhile( () -> !resetArea.contains(getLocalPlayer().getTile()), Calculations.random(1000, 4000));
        }
        return 300;
    }
}
