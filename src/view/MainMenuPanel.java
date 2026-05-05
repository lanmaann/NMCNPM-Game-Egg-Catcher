package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import util.SoundManager;

public class MainMenuPanel extends JPanel {

    // =========================
    // GLOBAL SOUND STATE
    // =========================
    public static boolean SOUND_ON = true;

    public MainMenuPanel(Runnable onStart, Runnable onLeaderboard, Runnable onExit) {

        setLayout(new GridBagLayout());
        setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // =========================
        // CARD
        // =========================
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(260, 360));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);

        // =========================
        // TITLE
        // =========================
        JLabel title = new JLabel("EGG CATCHER", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // =========================
        // BUTTONS
        // =========================
        JButton start = createButton("START", onStart);
        JButton leaderboard = createButton("LEADERBOARD", onLeaderboard);
        JButton exit = createButton("EXIT", onExit);

        // =========================
        // SETTINGS BUTTON (SOUND TOGGLE)
        // =========================
        JButton setting = new JButton(getSoundText());
        setting.setFont(new Font("Arial", Font.BOLD, 12));
        setting.setAlignmentX(Component.CENTER_ALIGNMENT);
        setting.setMaximumSize(new Dimension(200, 35));

        setting.addActionListener((ActionEvent e) -> {

            SOUND_ON = !SOUND_ON;

            setting.setText(getSoundText());

            if (!SOUND_ON) {
                SoundManager.stop(null);
            } else {
                SoundManager.play("/resources/sound/click.wav");
            }
        });

        // =========================
        // ADD UI
        // =========================
        card.add(title);
        card.add(start);
        card.add(Box.createVerticalStrut(10));
        card.add(leaderboard);
        card.add(Box.createVerticalStrut(10));
        card.add(exit);
        card.add(Box.createVerticalStrut(15));
        card.add(setting);

        add(card, gbc);
    }

    // =========================
    // TEXT SOUND BUTTON
    // =========================
    private String getSoundText() {
        return SOUND_ON ? "🔊 SOUND: ON" : "🔇 SOUND: OFF";
    }

    // =========================
    // BUTTON STYLE
    // =========================
    private JButton createButton(String text, Runnable action) {

        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setMaximumSize(new Dimension(200, 40));
        btn.setBackground(new Color(230, 230, 230));

        btn.addActionListener(e -> {

            if (action != null) action.run();

            if (SOUND_ON) {
                SoundManager.play("/resources/sound/click.wav");
            }
        });

        return btn;
    }

    // =========================
    // BACKGROUND
    // =========================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        g2.setPaint(new GradientPaint(
                0, 0, new Color(245, 248, 255),
                0, h, new Color(190, 210, 255)
        ));

        g2.fillRect(0, 0, w, h);
    }
}