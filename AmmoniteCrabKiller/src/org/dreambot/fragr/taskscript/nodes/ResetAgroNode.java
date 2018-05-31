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
                !sleepWhile( () -> !getLocalPlayer().isInCombat(), Calculations.random(8000, 10000) );
        return false;
    }

    @Override
    public int execute() {
        AK.resetAgro = true;
        log("Reset agro set to true");
        return Calculations.random(100, 400);
    }
}
