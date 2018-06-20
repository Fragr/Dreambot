package org.dreambot.fragr.taskscript;

import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.fragr.taskscript.nodes.CraftNode;
import org.dreambot.fragr.taskscript.nodes.DepositNode;
import org.dreambot.fragr.taskscript.nodes.WithdrawNode;

import java.awt.*;

@ScriptManifest(author = "Fragr", category = Category.CRAFTING, description = "Crafts bracelets from gold bars", name = "fGoldCrafter", version = 1.0)
public class GC extends TaskScript {

    @Override
    public void onStart() {
        addNodes(new DepositNode(), new WithdrawNode(), new CraftNode());
    }

    @Override
    public void onPaint(Graphics graphics) {

    }
}
