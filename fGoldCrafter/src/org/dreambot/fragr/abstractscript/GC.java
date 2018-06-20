package org.dreambot.fragr.abstractscript;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.walking.web.node.impl.bank.WebBankArea;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

@ScriptManifest(author = "Fragr", category = Category.CRAFTING, description = "Crafts bracelets from gold bars", name = "fGoldCrafter", version = 1.0)
public class GC extends AbstractScript {

    private Timer timer;
    private JFrame frame;
    private String runTime;
    private boolean isRunning;
    boolean smelting;

    @Override
    public void onStart() {
        isRunning = false;
        smelting = false;
        createGUI();

        //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
        boolean scriptRunning = getClient().getInstance().getScriptManager().isRunning();
        for(Skill s : Skill.values()){
            getSkillTracker().start(s, !scriptRunning);
        }
    }

    @Override
    public int onLoop() {
        NPC banker = getNpcs().closest(npc -> npc != null && npc.hasAction("Bank"));
        GameObject[] objects = getGameObjects().getObjectsOnTile(new Tile(3110, 3499));
        GameObject furnace = null;
        for( GameObject o : objects ) {
            if( o.hasAction("Smelt") ){
                furnace = o;
                //log("SUCCESS: Furnace object initialized");
            }
        }
        //GameObject furnace = getGameObjects().closest( GameObject -> GameObject != null && GameObject.hasAction("Smelt"));
        GameObject bankBooth = getGameObjects().closest( GameObject -> GameObject != null && GameObject.hasAction("Bank"));
        Tile furnaceTile = new Tile(3109, 3499);
        Area furnaceArea = new Area( new Tile(3109, 3499));
        Area bankArea = new Area(new Tile(3098, 3494), new Tile(3097, 3497));
        //TODO make sure to keep smelting after level gain (use player animating?)
        //TODO Check crafting stats every so often

        if( isRunning ) {
            //Check to seee if inventory does NOT contain bracelet mould
            if( !getInventory().contains("Bracelet mould") ){
                log("Withdrawing bracelet mould");
                banker.interact("Bank");
                getBank().withdraw("Bracelet mould", 1);
                sleepUntil( () -> getBank().isOpen(), 3000);
                if( getInventory().count("Gold bar") < 27 ){
                    getBank().withdrawAll("Gold bar");
                }else{
                    getBank().close();
                    sleep(Calculations.random(700, 1500));
                }
            }else if( !getInventory().contains("Gold bar") ){
                //If inventory has gold bracelets and no more gold bars left
                if( getInventory().contains("Gold bracelet")
                        && !getInventory().contains("Gold bar")){
                    //Bank, deposit bracelets and grab more bars
                    sleep(Calculations.random(500, 950));
                    if( !bankArea.contains(getLocalPlayer().getTile()) ) {
                        log("Walking to bank area");
                        getWalking().walk(bankArea.getRandomTile());
                        sleep(Calculations.random(300, 500));
                        sleepUntil(() -> bankArea.contains(getLocalPlayer().getTile()), 4000);
                    }
                        log("Banking, depositing bracelets and withdrawing bars");
                        banker.interact("Bank");
                        sleepUntil( () -> getBank().isOpen(), 3000);
                        getBank().depositAll("Gold bracelet");
                        sleep(Calculations.random(500, 1200));
                        getBank().withdrawAll("Gold bar");
                        getBank().close();
                }else{
                    //Bank, grab bars
                    log("Banking, grabbing gold bars");
                    banker.interact("Bank");
                    sleepUntil( () -> getBank().isOpen(), 3000);
                    getBank().withdrawAll("Gold bar");
                    getBank().close();
                }
            }else if( getInventory().contains("Gold bar") && !smelting){
                if( furnace != null ) {
                    log("Looking at furnace");
                    //getCamera().rotateToTile(furnace.getTile());
                    sleepUntil( () -> getCamera().rotateToTile(new Tile(3110, 3499)), 5000);
                    sleep(Calculations.random(1000, 1750));
                    log("Running to furnace");
                    furnace.interact("Smelt");
                    smelting = true;
                    log("Smelting: " + smelting);
                }else{
                    log("ERROR: Furnace object NULL");
                }
            }else if(smelting){
                //Sleep until the player is next to the furnace OR 6.5-7 seconds has passed
                sleepUntil( () -> getLocalPlayer().getTile().equals(furnaceTile), Calculations.random(6500, 7000) );
                //If the player is next to the furnace make all ELSE smelting = false
                if( getLocalPlayer().getTile().equals(furnaceTile) ){
                    if( getWidgets().getWidget(446).getChild( 47) != null){
                        log("Smelting all gold bars");
                        WidgetChild smeltWidget = getWidgets().getWidget(446).getChild( 47);
                        sleepUntil( () -> smeltWidget.isVisible(), 4000);
                        smeltWidget.interact("Make-All");
                        //sleep(Calculations.random(100, 500));
                        int x = getMouse().getX();
                        int y = getMouse().getY();
                        log("X: " + x + " Y: " + y);
//                        Random xRand = new Random();
//                        Random yRand = new Random();
//                        x += xRand.nextInt(16);
//                        y += yRand.nextInt(16);
//                        log("newX: " + x + " newY: " + y);
//                        getMouse().move(new Point(x, y));
//                        getMouse().moveMouseOutsideScreen();
                        sleepUntil( () -> !getInventory().contains("Gold bar"), Calculations.random(42000, 50000) );
                        smelting = false;
                        log("Smelting: " + smelting);
                        return Calculations.random(3000, 15000);
                    }
                }else{
                    smelting = false;
                    log("Smelting: " + smelting);
                }

            }
            //if timer is NOT null AND finished stop script
            if( timer != null && timer.finished() ){
                stop();
            }
        }
        return Calculations.random(500, 1500);
    }

    public void createGUI() {
        frame = new JFrame("Gold Bracelet Crafter " + getVersion());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(getClient().getInstance().getCanvas());
        frame.setPreferredSize(new Dimension(325, 100));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.pack();
        frame.setVisible(true);

        JPanel settingPanel = new JPanel();
        settingPanel.setLayout(new GridLayout(0, 2));

        JLabel timeLabel = new JLabel("Run Time (minutes):");
        settingPanel.add(timeLabel);

        JTextField timeTextField = new JTextField();
        settingPanel.add(timeTextField);

        frame.getContentPane().add(settingPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton button = new JButton("Start Script");
        buttonPanel.add(button);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        log("GUI loaded");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if( !timeTextField.getText().isEmpty() ) {
                    timer = new Timer();
                    runTime = timeTextField.getText();
                    timer.setRunTime(Integer.parseInt(runTime) * 60000);
                }

                isRunning = true;
                frame.dispose();
            }
        });
    }

    //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
    private String formatNumber(Integer number) {
        String[] suffix = new String[] { "K", "M", "B" };
        int size = (number.intValue() != 0) ? (int) Math.log10(number) : 0;
        if (size >= 3) {
            while (size % 3 != 0) {
                size = size - 1;
            }
        }
        return (size >= 3) ? +(Math.round((number / Math.pow(10, size)) * 10) / 10d)
                + suffix[(size / 3) - 1]
                : +number + "";
    }

    @Override
    public void onExit() {
        frame.dispose();
        super.onExit();
    }

    @Override
    public void onPaint(Graphics g) {
        //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("fGoldCrafter " + getVersion(), 5 , 260);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Timer: " + timer.formatTime(), 5, 275);

        g.setColor(Color.WHITE);

        int baseY = 290;
        for(Skill s : Skill.values()){
            if(getSkillTracker().getGainedExperience(s) > 0){
                long gainedXP = getSkillTracker().getGainedExperience(s);
                long xpTilLvl = getSkills().experienceToLevel(s);
                long xpPerHour = getSkillTracker().getGainedExperiencePerHour(s);
                int gainedLvl = getSkillTracker().getGainedLevels(s);
                int curLevel = getSkills().getRealLevel(s);
                g.drawString(s.getName() + " " + curLevel + "(" + gainedLvl + ") : " + formatNumber((int)gainedXP) + "(" + formatNumber((int)xpPerHour)+")" + " :: " + formatNumber((int)xpTilLvl),5,baseY);
                baseY+=15;
            }
        }

        Polygon p = getMap().getPolygon(new Tile(3110, 3499));
        g.drawPolygon(p);

    }
}
