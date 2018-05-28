package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AK;

public class firstNode extends TaskNode {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean accept() {
        return !AK.resetArea.contains(getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        log("first node");
        return Calculations.random(500, 1000);
    }
}
