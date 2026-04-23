package view;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(Runnable onStart, Runnable onLeaderboard, Runnable onExit) {

        setLayout(null);
        setBackground(new Color(10, 15, 30));

        // TITLE
        JLabel title = new JLabel("EGG CATCHER");
        title.setBounds(100, 80, 300, 50);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        add(title);

        // START
        JButton start = new JButton("START");
        start.setBounds(120, 180, 160, 40);
        start.addActionListener(e -> onStart.run());
        add(start);

        // LEADERBOARD
        JButton rank = new JButton("LEADERBOARD");
        rank.setBounds(120, 240, 160, 40);
        rank.addActionListener(e -> onLeaderboard.run());
        add(rank);

        // EXIT
        JButton exit = new JButton("EXIT");
        exit.setBounds(120, 300, 160, 40);
        exit.addActionListener(e -> onExit.run());
        add(exit);
    }
}