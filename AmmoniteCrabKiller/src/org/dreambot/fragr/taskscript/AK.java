package org.dreambot.fragr.taskscript;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.api.utilities.Timer;
import org.dreambot.fragr.taskscript.nodes.firstNode;
import org.dreambot.fragr.taskscript.nodes.secondNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@ScriptManifest(author = "Fragr", category = Category.COMBAT, description = "Kills Ammonite Crabs at Fossil Island", name = "Ammonite Crab Killer", version = 1.0)
public class AK extends TaskScript {

    private boolean isRunning = false;
    private boolean resetAgro = false;

    private String runTime; //in minutes

    public static Timer timer;
    public static boolean usePoitions = false;

    public static Area crabArea = new Area(/*3257, 3244, 3259, 3247*/3732, 3846, 3733, 3847);
    public static Area resetArea = new Area(/*3257, 3226, 3261, 3229*/3736, 3815, 3738, 3814);

    @Override
    public void onStart() {
        //createGUI();

        addNodes(new firstNode(), new secondNode());

//        //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
//        boolean scriptRunning = getClient().getInstance().getScriptManager().isRunning();
//        for(Skill s : Skill.values()){
//            getSkillTracker().start(s, !scriptRunning);
//        }
    }

//    //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
//    private String formatNumber(Integer number){
//        String[] suffix = new String[] { "K", "M", "B" };
//        int size = (number.intValue() != 0) ? (int) Math.log10(number) : 0;
//        if (size >= 3) {
//            while (size % 3 != 0) {
//                size = size - 1;
//            }
//        }
//        return (size >= 3) ? +(Math.round((number / Math.pow(10, size)) * 10) / 10d)
//                + suffix[(size / 3) - 1]
//                : +number + "";
//    }
//
//    @Override
//    public void onPaint(Graphics g) {
//        Tile crabTiles[] = crabArea.getTiles();
//
//        g.setColor(Color.RED);
//        g.setFont(new Font("Arial", Font.PLAIN, 16));
//        g.drawString("Ammonite Crab Killer " + getVersion(), 5, 290);
//
//        g.setColor(Color.GREEN);
//        g.setFont(new Font("Arial", Font.PLAIN, 12));
//        g.drawString("Timer: " + timer.formatTime(), 5, 305);
//
//        //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
//        int baseY = 320;
//        for(Skill s : Skill.values()){
//            if(getSkillTracker().getGainedExperience(s) > 0){
//                long gainedXP = getSkillTracker().getGainedExperience(s);
//                long xpTilLvl = getSkills().experienceToLevel(s);
//                long xpPerHour = getSkillTracker().getGainedExperiencePerHour(s);
//                int gainedLvl = getSkillTracker().getGainedLevels(s);
//                int curLevel = getSkills().getRealLevel(s);
//                g.drawString(s.getName() + " " + curLevel + "(" + gainedLvl + ") : " + formatNumber((int)gainedXP) + "(" + formatNumber((int)xpPerHour)+")" + " :: " + formatNumber((int)xpTilLvl),5,baseY);
//                baseY+=15;
//            }
//        }
//
//        if( crabTiles != null ) {
//            for(int i = 0; i < crabTiles.length; i++) {
//                Polygon tile = getMap().getPolygon(crabTiles[i]);
//                g.drawPolygon(tile);
//            }
//        }
//    }

    //TODO Update GUI to use form
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
                if( !timeTextField.getText().isEmpty() ) {
                    timer = new Timer();
                    runTime = timeTextField.getText();
                    timer.setRunTime(Integer.parseInt(runTime) * 60000);
                }
                usePoitions = potionCheckBox.isSelected();

                isRunning = true;
                frame.dispose();
            }
        });
        buttonPanel.add(button);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}
