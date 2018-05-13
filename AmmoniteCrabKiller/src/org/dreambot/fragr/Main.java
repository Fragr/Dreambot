package org.dreambot.fragr;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@ScriptManifest(author = "Fragr", category = Category.COMBAT, description = "Kills Ammonite Crabs at Fossile Island", name = "Ammonite Crab Killer", version = 1.0)
public class Main extends AbstractScript {

    private boolean isRunning;
    private boolean usePoitions;
    private String runTime; //in minutes
    private String enemyName = "Ammonite Crab";

    @Override
    public void onStart() {
        createGUI();
    }

    @Override
    public int onLoop() {
        if(isRunning) {

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
        log("Running: " + isRunning);
        log("Run Time: " + runTime);
        log("Use Super Strength: " + usePoitions);
        log("Enemy: " + enemyName);
    }

    @Override
    public void onResume() {
        super.onResume();
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
