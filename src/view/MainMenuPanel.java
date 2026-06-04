package view;

import util.SoundManager;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public MainMenuPanel(
            Runnable onStart,
            Runnable onLeaderboard,
            Runnable onExit
    ) {

        // =====================================================
        // PANEL
        // =====================================================
        setLayout(new GridBagLayout());

        setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(10, 0, 10, 0);

        // =====================================================
        // CARD
        // =====================================================
        JPanel card = new JPanel();

        card.setPreferredSize(
                new Dimension(280, 420)
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setOpaque(false);

        // =====================================================
        // TITLE
        // =====================================================
        JLabel title =
                new JLabel(
                        "EGG CATCHER",
                        SwingConstants.CENTER
                    );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        10,
                        30,
                        10
                )
        );

        // =====================================================
        // BUTTONS
        // =====================================================
        /* UC 1.1 - Bước 1.1.1: Người chơi chọn nút bắt đầu game (START) từ Menu chính */
        JButton startBtn =
                createButton(
                        "START",
                        onStart
                );

        JButton leaderboardBtn =
                createButton(
                        "LEADERBOARD",
                        onLeaderboard
                );

        JButton settingBtn =
                createSettingButton();

        JButton exitBtn =
                createButton(
                        "EXIT",
                        onExit
                );

        // =====================================================
        // ADD COMPONENTS
        // =====================================================
        card.add(title);

        card.add(startBtn);

        card.add(Box.createVerticalStrut(12));

        card.add(leaderboardBtn);

        card.add(Box.createVerticalStrut(12));

        card.add(settingBtn);

        card.add(Box.createVerticalStrut(12));

        card.add(exitBtn);

        add(card, gbc);
    }

    // =========================================================
    // NORMAL BUTTON
    // =========================================================
    private JButton createButton(
            String text,
            Runnable action
    ) {

        JButton btn =
                new JButton(text);

        btn.setFocusPainted(false);

        btn.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        btn.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btn.setMaximumSize(
                new Dimension(220, 42)
        );

        btn.setBackground(
                new Color(230, 230, 230)
        );

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.addActionListener(e -> {

            if (action != null) {
                action.run();
            }
        });

        return btn;
    }

    // =========================================================
    // SETTINGS BUTTON
    // =========================================================
    private JButton createSettingButton() {

        JButton btn =
                new JButton("SETTINGS");

        btn.setFocusPainted(false);

        btn.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        btn.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        btn.setMaximumSize(
                new Dimension(220, 40)
        );

        btn.setBackground(
                new Color(220, 220, 220)
        );

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.addActionListener(e -> {

            JCheckBox bgm =
                    new JCheckBox(
                            "Background Music",
                            SoundManager.MUSIC_ON
                    );

            JCheckBox sfx =
                    new JCheckBox(
                            "Sound Effects",
                            SoundManager.SOUND_ON
                    );

            JTextArea guide =
                    new JTextArea(
                            """
                            HOW TO PLAY

                            ← → : Move

                            Catch normal eggs
                            Avoid bombs
                            Golden egg = bonus points

                            P : Pause
                            R : Restart
                            """
                    );

            guide.setEditable(false);

            guide.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            13
                    )
            );

            guide.setBackground(
                    new Color(245,245,245)
            );

            JPanel panel =
                    new JPanel();

            panel.setLayout(
                    new BoxLayout(
                            panel,
                            BoxLayout.Y_AXIS
                    )
            );

            panel.add(bgm);

            panel.add(Box.createVerticalStrut(10));

            panel.add(sfx);

            panel.add(Box.createVerticalStrut(15));

            panel.add(
                    new JLabel("Guide:")
            );

            panel.add(Box.createVerticalStrut(5));

            panel.add(
                    new JScrollPane(guide)
            );

            int result =
                    JOptionPane.showConfirmDialog(
                            null,
                            panel,
                            "Settings",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result == JOptionPane.OK_OPTION) {

                SoundManager.MUSIC_ON =
                        bgm.isSelected();

                SoundManager.SOUND_ON =
                        sfx.isSelected();

                // stop music if turned off
                if (!SoundManager.MUSIC_ON) {

                    SoundManager.stopMusic();
                }
            }
        });

        return btn;
    }

    // =========================================================
    // BACKGROUND
    // =========================================================
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;

        int w = getWidth();

        int h = getHeight();

        g2.setPaint(
                new GradientPaint(
                        0,
                        0,
                        new Color(245, 248, 255),
                        0,
                        h,
                        new Color(190, 210, 255)
                )
        );

        g2.fillRect(0, 0, w, h);

        // =====================================================
        // DECORATION
        // =====================================================
        g2.setColor(
                new Color(255, 255, 255, 70)
        );

        g2.fillOval(-80, -80, 200, 200);

        g2.fillOval(
                w - 150,
                h - 150,
                220,
                220
        );
    }
}