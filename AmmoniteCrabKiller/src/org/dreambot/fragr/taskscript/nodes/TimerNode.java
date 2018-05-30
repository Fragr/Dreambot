package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AK;

public class TimerNode extends TaskNode {
    @Override
    public int priority() {
        return 4;
    }

    @Override
    public boolean accept() {
        if( AK.timer != null && AK.isRunning ) {
            return AK.timer.finished();
        }
        return false;
    }

    @Override
    public int execute() {
        return -1;
    }
}
