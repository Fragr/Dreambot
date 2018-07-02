package org.dreambot.fragr.abstractscript;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

@ScriptManifest(author = "Fragr", category = Category.SMITHING, description = "Smiths cannonballs in Edgeville", name = "fCannonballs", version = 1.0)
public class fCannonballs extends AbstractScript {
    NPC banker;
    GameObject bankBooth;
    GameObject furnace;

    @Override
    public void onStart() {
        banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
        bankBooth = getGameObjects().closest( GameObject -> GameObject != null && GameObject.hasAction("Bank") );
        furnace = getGameObjects().closest( GameObject -> GameObject != null && GameObject.hasAction("Smelt") );
    }

    @Override
    public int onLoop() {
        if( !getInventory().contains("Ammo mould") ){
            //TODO randomly choose between banker or booth
            log("No ammo mould found, grabbing one from bank");
            if( !getBank().isOpen() )
                bankBooth.interact("Bank");
            sleepUntil( () -> getBank().isOpen(), 6000 );
            if( getBank().contains("Ammo mould") ){
                if( getBank().withdraw("Ammo mould") ){
                    log("Grabbed ammo mould");
                    sleep( Calculations.random(750, 1200) );
                    if( getInventory().count("Steel bar") < 27 ){
                        if( getBank().withdrawAll("Steel bar") ){
                            log("Withdrawing steel bars");
                            sleep( Calculations.random(750, 1200) );
                            getBank().close();
                            sleepUntil( () -> !getBank().isOpen(), 6000 );
                        }else{
                            log("FAILED to withdraw steel bars");
                            stop();
                            return 0;
                        }
                    }
                    return 3000;
                }
                log("FAILED to withdraw ammo mould");
            }
            log("No ammo mould in bank. Stopping script");
            stop();
            return 0;
        }else if( !getInventory().contains("Steel bar") ){
            //TODO randomly choose between banker or booth
            if( !getBank().isOpen() )
                bankBooth.interact("Bank");
            sleepUntil( () -> getBank().isOpen(), 6000 );
            if( getBank().count("Steel bar") > 1 ){
                if( getBank().withdrawAll("Steel bar") ){
                    log("Grabbing more steel bars");
                    sleep( Calculations.random(750, 1200) );
                    getBank().close();
                    sleepUntil( () -> !getBank().isOpen(), 6000 );
                    return Calculations.random(750, 1500);
                }
            }else if( getBank().count("Steel bars") <= 1 && getBank().isOpen()){
                log("Out of Steel bars, stopping script");
                stop();
                return 0;
            }
        }else if( getInventory().isFull() ){
            furnace.interact("Smelt");
            sleepUntil( () -> getLocalPlayer().getTile().equals(new Tile(3109, 3499)), 10000 );
            sleep( Calculations.random(500, 750) );
            if( getLocalPlayer().getTile().equals(new Tile(3109, 3499)) ){
                getKeyboard().type(" ");
                log("Making cannon balls");
                sleepUntil( () -> !getInventory().contains("Steel bar"), 40000);
            }
        }
        return 7000;
    }
}
