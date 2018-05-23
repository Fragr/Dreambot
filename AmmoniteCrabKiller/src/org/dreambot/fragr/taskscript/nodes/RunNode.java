package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.TaskNode;

public class RunNode extends TaskNode {
    Area crabArea = new Area(/*3257, 3244, 3259, 3247*/3732, 3846, 3733, 3847);

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean accept() {
        return !crabArea.contains(getLocalPlayer().getTile());
    }

    @Override
    public int execute() {
        //walk to crab area
        getWalking().walk(crabArea.getRandomTile());
        sleepWhile( () -> !crabArea.contains(getLocalPlayer().getTile()), Calculations.random(1000, 4000));
        getCamera().rotateTo(Calculations.random(2400), Calculations.random(getClient().getLowestPitch(), 384));
        log("Running to crab area");
        return 300;
    }
}
