package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.script.TaskNode;

public class ResetNode extends TaskNode {
    @Override
    public int priority() {
        return super.priority();
    }

    @Override
    public boolean accept() {
        return !crabArea.contains(getLocalPlayer().getTile()) && !resetAgro;
    }

    @Override
    public int execute() {
        return 0;
    }
}
