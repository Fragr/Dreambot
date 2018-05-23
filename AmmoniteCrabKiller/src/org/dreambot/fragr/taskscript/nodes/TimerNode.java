package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AmmoniteKiller;

public class TimerNode extends TaskNode {
    @Override
    public int priority() {
        return 3;
    }

    @Override
    public boolean accept() {
        return AmmoniteKiller.timer.finished();
    }

    @Override
    public int execute() {
        return 0;
    }
}
