package org.dreambot.fragr.taskscript.nodes;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.fragr.taskscript.AmmoniteKiller;

import java.util.List;

public class DrinkNode extends TaskNode {
    List<Item> allItems = getInventory().all();
    int boostedLevel = getSkills().getBoostedLevels(Skill.STRENGTH);
    int realLevel = getSkills().getRealLevel(Skill.STRENGTH);
    int levelDiff = boostedLevel - realLevel;

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public boolean accept() {
        return levelDiff <= 1 && allItems != null && AmmoniteKiller.usePoitions;
    }

    @Override
    public int execute() {

        for( Item i : allItems) {
            if( i.getName().contains("Super strength(") ) {
                log("Super strength drank");
                getInventory().interact(i.getName(), "Drink");
                getMouse().move(getClient().getViewportTools().getRandomPointOnCanvas());
                break;
            }
        }
        sleep(Calculations.random(2000, 3000));
        return 0;
    }
}
