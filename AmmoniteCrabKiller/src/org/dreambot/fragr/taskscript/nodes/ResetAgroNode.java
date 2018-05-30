package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AK;

public class ResetAgroNode extends TaskNode {
    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean accept() {
        if (AK.isRunning)
            return AK.crabArea.contains(getLocalPlayer().getTile()) &&
                !sleepWhile( () -> !getLocalPlayer().isInCombat(), Calculations.random(10000, 12000) );
        return false;
    }

    @Override
    public int execute() {
        AK.resetAgro = true;
        log("Running to reset area");
        return Calculations.random(500, 1000);
    }
}
