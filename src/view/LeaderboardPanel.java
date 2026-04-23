package view;

import model.Record;
import model.RecordManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    public LeaderboardPanel(Runnable onBack) {

        setLayout(null);
        setBackground(new Color(30, 30, 30));

        // TITLE
        JLabel title = new JLabel("TOP 10");
        title.setBounds(150, 20, 200, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title);

        // LOAD DATA
        List<Record> list = RecordManager.loadAll();

        int y = 80;
        int rank = 1;

        for (int i = 0; i < Math.min(10, list.size()); i++) {

            Record r = list.get(i);

            JLabel row = new JLabel(
                    rank + ". Score: " + r.getScore() +
                    " | Time: " + r.getTime() + "s"
            );

            row.setBounds(80, y, 300, 30);
            row.setForeground(Color.WHITE);

            add(row);

            y += 30;
            rank++;
        }

        // BACK BUTTON
        JButton back = new JButton("BACK");
        back.setBounds(140, 450, 120, 40);
        back.addActionListener(e -> onBack.run());
        add(back);
    }
}