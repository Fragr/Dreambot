package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AK;

public class ResetNode extends TaskNode {
    @Override
    public int priority() {
        return 2;
    }

    @Override
    public boolean accept() {
        if (AK.isRunning)
            return AK.resetAgro;
        return false;
    }

    @Override
    public int execute() {
        //Walk to reset area
        getWalking().walk(AK.resetArea.getRandomTile());
        //getCamera().rotateTo(Calculations.random(2000), Calculations.random(getClient().getLowestPitch(), 384));
        sleepWhile( () -> !AK.resetArea.contains(getLocalPlayer().getTile()), Calculations.random(300, 600));
        log("Running to reset area");

        if( AK.resetArea.contains(getLocalPlayer().getTile()) ) {
            log("In reset area");
            sleep(Calculations.random(700, 1200));
            AK.resetAgro = false;
        }
        return Calculations.random(300, 600);
    }
}
