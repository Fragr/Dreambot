package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.fragr.taskscript.AK;

import java.util.Random;

public class CameraNode extends TaskNode {

    @Override
    public int priority() {
        return 5;
    }

    @Override
    public boolean accept() {
        Random r = new Random();
        int val = r.nextInt(100) + 1;
        val = val % 10;

        log("" + val);

        if( AK.crabArea.contains(getLocalPlayer().getTile()) && val == 0)
            return true;
        return false;
    }

    @Override
    public int execute() {
        log("Moving camera");
        getCamera().rotateTo(Calculations.random(2400), Calculations.random(getClient().getLowestPitch(), 384));
        return Calculations.random(3000, 5000);
    }
}
