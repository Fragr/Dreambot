package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.script.TaskNode;

public class RunNode extends TaskNode {
    @Override
    public int priority() {
        return super.priority();
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
