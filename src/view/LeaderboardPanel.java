package view;

import model.Record;
import model.RecordManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    public LeaderboardPanel(Runnable onBack) {

        setLayout(new BorderLayout());
        setBackground(new Color(20, 25, 45));

        // =========================
        // TOP TITLE
        // =========================
        JLabel title = new JLabel("TOP 10 LEADERBOARD", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        add(title, BorderLayout.NORTH);

        // =========================
        // CENTER LIST (SCROLL)
        // =========================
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(20, 25, 45));

        List<Record> list = RecordManager.loadAll();

        for (int i = 0; i < Math.min(10, list.size()); i++) {

            Record r = list.get(i);
            int rank = i + 1;

            JPanel row = createRow(rank, r);
            listPanel.add(row);
            listPanel.add(Box.createVerticalStrut(8));
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(new Color(20, 25, 45));

        add(scroll, BorderLayout.CENTER);

        // =========================
        // BACK BUTTON
        // =========================
        JButton back = new JButton("BACK");
        back.setFocusPainted(false);
        back.setFont(new Font("Arial", Font.BOLD, 14));
        back.setBackground(new Color(230, 230, 230));

        back.addActionListener(e -> onBack.run());

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(20, 25, 45));
        bottom.setBorder(BorderFactory.createEmptyBorder(15, 0, 20, 0));
        bottom.add(back);

        add(bottom, BorderLayout.SOUTH);
    }

    // =========================
    // ROW UI CARD
    // =========================
    private JPanel createRow(int rank, Record r) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 45));

        panel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // background card
        panel.setBackground(new Color(40, 45, 70));

        JLabel text = new JLabel(
                rank + ".  Score: " + r.getScore() +
                "   |   Time: " + r.getTime() + "s"
        );

        text.setFont(new Font("Arial", Font.BOLD, 14));

        // =========================
        // COLOR RANK STYLE
        // =========================
        if (rank == 1) text.setForeground(new Color(255, 215, 0));      // gold
        else if (rank == 2) text.setForeground(new Color(180, 180, 180)); // silver
        else if (rank == 3) text.setForeground(new Color(205, 127, 50));  // bronze
        else text.setForeground(Color.WHITE);

        panel.add(text, BorderLayout.CENTER);

        return panel;
    }
}