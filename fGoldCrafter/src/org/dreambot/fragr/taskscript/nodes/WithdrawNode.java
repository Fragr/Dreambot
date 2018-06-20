package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.script.TaskNode;

public class WithdrawNode extends TaskNode {
    @Override
    public int priority() {
        return 1;
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
