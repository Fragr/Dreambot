package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AK;

public class CrabNode extends TaskNode {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean accept() {
        if (AK.isRunning)
            return !AK.crabArea.contains(getLocalPlayer().getTile()) && !AK.resetAgro;
        return false;
    }

    @Override
    public int execute() {
        getWalking().walk(AK.crabArea.getRandomTile());
        //getCamera().rotateTo(Calculations.random(2000), Calculations.random(getClient().getLowestPitch(), 384));
        sleepWhile( () -> !AK.crabArea.contains(getLocalPlayer().getTile()), Calculations.random(300, 600));
        log("Running to crab area");
        return Calculations.random(300, 600);
    }
}
