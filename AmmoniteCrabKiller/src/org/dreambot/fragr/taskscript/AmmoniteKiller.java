package org.dreambot.fragr.taskscript;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.impl.TaskScript;
import org.dreambot.api.utilities.Timer;
import org.dreambot.fragr.taskscript.nodes.DrinkNode;
import org.dreambot.fragr.taskscript.nodes.ResetNode;
import org.dreambot.fragr.taskscript.nodes.RunNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AmmoniteKiller extends TaskScript {

    private boolean isRunning;
    private boolean resetAgro;

    private String runTime; //in minutes

    public static Timer timer;
    public static boolean usePoitions;

    @Override
    public void onStart() {
        timer = new Timer();
        resetAgro = false;
        usePoitions = false;
        isRunning = false;
        createGUI();

        addNodes(new DrinkNode(), new ResetNode(), new RunNode());

        //Taken from https://dreambot.org/forums/index.php?/topic/820-experience-tracker-plugin/
        boolean scriptRunning = getClient().getInstance().getScriptManager().isRunning();
        for(Skill s : Skill.values()){
            getSkillTracker().start(s, !scriptRunning);
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
                runTime = timeTextField.getText();
                usePoitions = potionCheckBox.isSelected();

                timer.setRunTime(Integer.parseInt(runTime) * 60000);
                isRunning = true;
                frame.dispose();
            }
        });
        buttonPanel.add(button);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
}
