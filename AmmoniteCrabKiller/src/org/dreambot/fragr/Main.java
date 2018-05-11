package org.dreambot.fragr;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.fragr.api.GUI;

@ScriptManifest(author = "Fragr", category = Category.COMBAT, description = "Kills Ammonite Crabs", name = "Ammonite Crab Killer", version = 1.0)
public class Main extends AbstractScript {
    @Override
    public void onStart() {
        GUI window = new GUI();
    }

    @Override
    public int onLoop() {
        return 0;
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
