package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.script.TaskNode;

public class CraftNode extends TaskNode {
    @Override
    public int priority() {
        return 2;
    }

    @Override
    public boolean accept() {
        return false;
    }

    @Override
    public int execute() {
        return 0;
    }
}
