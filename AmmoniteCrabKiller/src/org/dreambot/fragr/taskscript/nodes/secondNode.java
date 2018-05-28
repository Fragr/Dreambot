package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AK;

public class secondNode extends TaskNode {
    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean accept() {
        return !AK.crabArea.contains(getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        log("second node");
        return Calculations.random(500, 1000);
    }
}
