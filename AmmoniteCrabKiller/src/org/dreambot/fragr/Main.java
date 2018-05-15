package org.dreambot.fragr;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@ScriptManifest(author = "Fragr", category = Category.COMBAT, description = "Kills Ammonite Crabs at Fossil Island", name = "Ammonite Crab Killer", version = 1.0)
public class Main extends AbstractScript {

    private boolean isRunning;
    private boolean usePoitions;
    private boolean resetAgro;
    private String runTime; //in minutes
    private int crabsKilled;
    private Timer timer;
    private Area crabArea = new Area(/*3257, 3244, 3259, 3247*/3733, 3846, 3734, 3847);
    private Area resetArea = new Area(/*3257, 3226, 3261, 3229*/3736, 3815, 3738, 3814);

    private String enemyName = "Ammonite Crab";

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
        resetAgro = false;
        createGUI();
    }

    @Override
    public int onLoop() {
        //Toggle run on
        if( !getWalking().isRunEnabled() ) {
            //getWalking().toggleRun();
        }

        if(isRunning) {
            //if player is NOT in crab area
            if( !crabArea.contains(getLocalPlayer().getTile()) && !resetAgro) {
                //walk to crab area
                getWalking().walk(crabArea.getRandomTile());
                sleepWhile( () -> !crabArea.contains(getLocalPlayer().getTile()), Calculations.random(3000, 5000));
                log("Walking to crab area");
            }

            //if player is in crab area
            if( crabArea.contains(getLocalPlayer().getTile()) ) {
                //log("In crab area");
                //if player is NOT in combat for 7-12 seconds
                if( !sleepWhile( () -> !getLocalPlayer().isInCombat(), Calculations.random(7000, 12000) ) ) {
                    resetAgro = true;
                    log("Running to reset area");
                }
            }

            if( resetAgro ) {
                //Walk to reset area
                getWalking().walk(resetArea.getRandomTile());
                sleepWhile( () -> !resetArea.contains(getLocalPlayer().getTile()), Calculations.random(3000, 5000));

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
//        log("Running: " + isRunning);
//        log("Run Time: " + runTime);
//        log("Use Super Strength: " + usePoitions);
//        log("Enemy: " + enemyName);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPaint(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Ammonite Crab Killer", 5, 20);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Timer: " + timer.formatTime(), 5, 32);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Crabs killed: " + crabsKilled, 5, 45);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Crabs killed / hour: " + timer.getHourlyRate(crabsKilled), 5, 58);

        //TODO add EXP an hour
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

//        JLabel label = new JLabel("Enemy name:");
//        settingPanel.add(label);
//
//        JComboBox<String> enemyComboBox = new JComboBox<>(new String[]{
//                "Ammonite Crab", "Rock Crab"
//        });
//        settingPanel.add(enemyComboBox);

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
