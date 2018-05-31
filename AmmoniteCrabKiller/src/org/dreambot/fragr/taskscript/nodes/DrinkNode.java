package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.fragr.taskscript.AK;

import java.util.List;

public class DrinkNode extends TaskNode {

    List<Item> allItems;

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public boolean accept() {
        int boostedLevel = getSkills().getBoostedLevels(Skill.STRENGTH);
        int realLevel = getSkills().getRealLevel(Skill.STRENGTH);
        int levelDiff = boostedLevel - realLevel;

        allItems = getInventory().all();
        if (AK.isRunning)
            return levelDiff <= 1 && allItems != null && AK.usePoitions;
        return false;
    }

    @Override
    public int execute() {
        for( Item i : allItems) {
            if( i.getName().contains("Super strength(") ) {
                log("Super strength drank");
                getInventory().interact(i.getName(), "Drink");
                Calculations.random(600, 1200);
                getMouse().move(getClient().getViewportTools().getRandomPointOnCanvas());
                break;
            }
        }
        return Calculations.random(500, 1000);
    }
}
