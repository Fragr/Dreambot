package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.NPC;

public class DepositNode extends TaskNode {
    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean accept() {
        return getInventory().count("Gold bracelet") == 27;
    }

    @Override
    public int execute() {
        NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));

        log("Inventory full of bracelets");
        getWalking().walk(getBank().getClosestBankLocation().getCenter());
        return Calculations.random(300, 500);
    }
}
