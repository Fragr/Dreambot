package org.dreambot.fragr;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.filter.impl.NameFilter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.items.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@ScriptManifest(author = "Fragr", category = Category.COMBAT, description = "Kills Ammonite Crabs at Fossil Island", name = "Ammonite Crab Killer", version = 1.1)
public class Main extends AbstractScript {

    private Timer timer;
    private Timer endTimer;
    private boolean isRunning;
    private boolean usePoitions;
    private boolean resetAgro;

    private String runTime; //in minutes

    private Area crabArea = new Area(/*3257, 3244, 3259, 3247*/3733, 3846, 3733, 3847);
    private Area resetArea = new Area(/*3257, 3226, 3261, 3229*/3736, 3815, 3738, 3814);

    /*
        Not in combat = -1
        Attacking = 390
        Being attacked = 1156

        Positions by crabs (3733, 3847, 0) (3732, 3847, 0) (3733, 3846, 0)
        Positions to reset agro (3737, 3814, 0) (3738, 3814, 0) (3736, 3814, 0)
     */

    @Override
    public void onStart() {
        timer = new Timer();
        timer.setRunTime(180);
        log("Timer: " + timer.formatTime());
        resetAgro = false;
        usePoitions = false;
        isRunning = false;
        createGUI();

        //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
        boolean scriptRunning = getClient().getInstance().getScriptManager().isRunning();
        for(Skill s : Skill.values()){
            getSkillTracker().start(s, !scriptRunning);
        }
    }

    @Override
    public int onLoop() {
        int boostedLevel = getSkills().getBoostedLevels(Skill.STRENGTH);
        int realLevel = getSkills().getRealLevel(Skill.STRENGTH);
        int levelDiff = boostedLevel - realLevel;

        List<Item> allItems = getInventory().all();

        //if run is NOT enabled and run energy is > 50 turn run on
        //else turn it off
        if( !getWalking().isRunEnabled() && getWalking().getRunEnergy() > 50 ) {
            getWalking().toggleRun();
        }

        if(isRunning) {
            log("TIME REMAINING: " + timer.remaining());
            //Drink a super strength potion if boosted level is <= 1
            if( levelDiff <= 1 && allItems != null && usePoitions) {
                for( Item i : allItems) {
                    if( i.getName().contains("Super strength(") ) {
                        log("Super strength drank");
                        getInventory().interact(i.getName(), "Drink");
                        break;
                    }
                }
                sleep(Calculations.random(2000, 3000));
            }
            //if player is NOT in crab area
            if( !crabArea.contains(getLocalPlayer().getTile()) && !resetAgro) {
                //walk to crab area
                getWalking().walk(crabArea.getRandomTile());
                sleepWhile( () -> !crabArea.contains(getLocalPlayer().getTile()), Calculations.random(1000, 4000));
                log("Walking to crab area");
            }

            //if player is in crab area
            if( crabArea.contains(getLocalPlayer().getTile()) ) {
                //log("In crab area");
                //if player is NOT in combat for 7-12 seconds
                if( !sleepWhile( () -> !getLocalPlayer().isInCombat(), Calculations.random(7000, 10000) ) ) {
                    resetAgro = true;
                    log("Running to reset area");
                }
            }

            if( resetAgro ) {
                //Walk to reset area
                getWalking().walk(resetArea.getRandomTile());
                sleepWhile( () -> !resetArea.contains(getLocalPlayer().getTile()), Calculations.random(1000, 4000));

                if( resetArea.contains(getLocalPlayer().getTile()) ) {
                    log("In reset area");
                    sleep(Calculations.random(3000, 5000));
                    resetAgro = false;
                }
            }
        }
        return 300;
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

    @Override
    public void onPaint(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Ammonite Crab Killer " + getVersion(), 5, 17);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Timer: " + timer.formatTime(), 5, 32);

        //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
        int baseY = 45;
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
    }

    //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
    private String formatNumber(Integer number){
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

    private void createGUI() {
        JFrame frame = new JFrame("Ammonite Crab Killer" + getVersion());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(getClient().getInstance().getCanvas());
        frame.setPreferredSize(new Dimension(300, 170));
        frame.getContentPane().setLayout(new BorderLayout());
        frame.pack();
        frame.setVisible(true);

        JPanel settingPanel = new JPanel();
        settingPanel.setLayout(new GridLayout(0, 2));

        JLabel timeLabel = new JLabel("Run Time (minutes):");
        settingPanel.add(timeLabel);

        JTextField timeTextField = new JTextField();
        settingPanel.add(timeTextField);

        JCheckBox potionCheckBox = new JCheckBox("Use Super Strength");
        settingPanel.add(potionCheckBox);

        frame.getContentPane().add(settingPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton button = new JButton("Start Script");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runTime = timeTextField.getText();
                usePoitions = potionCheckBox.isSelected();

                isRunning = true;
                frame.dispose();
            }
        });
        buttonPanel.add(button);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}
