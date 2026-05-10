package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import util.SoundManager;

/**
 * =========================================================
 * MAIN MENU PANEL
 * =========================================================
 * giao diện menu chính của game
 * 
 * chức năng:
 * - hiển thị title game
 * - hiển thị nút start
 * - hiển thị leaderboard
 * - thoát game
 * - bật/tắt âm thanh
 * =========================================================
 */
public class MainMenuPanel extends JPanel {

    // =========================================================
    // GLOBAL SOUND STATE
    // =========================================================

    // trạng thái âm thanh toàn cục
    public static boolean SOUND_ON = true;

    /**
     * constructor khởi tạo menu chính
     */
    public MainMenuPanel(
            Runnable onStart,
            Runnable onLeaderboard,
            Runnable onExit
    ) {

        // layout chính
        setLayout(new GridBagLayout());

        // màu nền
        setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(10, 0, 10, 0);

        // =========================================================
        // CARD
        // =========================================================

        JPanel card = new JPanel();

        card.setPreferredSize(new Dimension(260, 360));

        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // cho phép thấy background
        card.setOpaque(false);

        // =========================================================
        // TITLE
        // =========================================================

        JLabel title =
                new JLabel(
                        "EGG CATCHER",
                        SwingConstants.CENTER
                );

        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        title.setFont(new Font("Arial", Font.BOLD, 24));


        title.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        20,
                        10
                )
        );

        // =========================================================
        // MAIN BUTTONS
        // =========================================================

        // nút start game
        JButton start =
                createButton("START", onStart);

        // nút leaderboard
        JButton leaderboard =
                createButton("LEADERBOARD", onLeaderboard);

        // nút exit
        JButton exit =
                createButton("EXIT", onExit);

        // =========================================================
        // SOUND SETTING BUTTON
        // =========================================================

        JButton setting =
                new JButton(getSoundText());

        // =========================
        // BUTTONS
        // =========================
        JButton start = createButton("START", onStart);
        
    	//1.4.1. Người chơi chọn chức năng xem điểm số trên giao diện.(khi ở màn hình bắt đầu game)
        JButton leaderboard = createButton("LEADERBOARD", onLeaderboard);
        JButton exit = createButton("EXIT", onExit);


        setting.setFont(new Font("Arial", Font.BOLD, 12));

        setting.setAlignmentX(Component.CENTER_ALIGNMENT);

        setting.setMaximumSize(new Dimension(200, 35));

        // =========================================================
        // SOUND TOGGLE EVENT
        // =========================================================

        setting.addActionListener((ActionEvent e) -> {

            // đổi trạng thái âm thanh
            SOUND_ON = !SOUND_ON;

            // cập nhật text
            setting.setText(getSoundText());

            // =========================================================
            // SOUND OFF
            // =========================================================

            if (!SOUND_ON) {

                SoundManager.stop(null);
            }

            // =========================================================
            // SOUND ON
            // =========================================================

            else {

                SoundManager.play(
                        "/resources/sound/click.wav"
                );
            }
        });

        // =========================================================
        // ADD COMPONENTS
        // =========================================================

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

    // =========================================================
    // SOUND TEXT
    // =========================================================

    /**
     * lấy text trạng thái âm thanh
     */
    private String getSoundText() {

        return SOUND_ON
                ? "🔊 SOUND: ON"
                : "🔇 SOUND: OFF";
    }

    // =========================================================
    // CREATE BUTTON
    // =========================================================

    /**
     * tạo button chuẩn giao diện
     */
    private JButton createButton(
            String text,
            Runnable action
    ) {

        JButton btn = new JButton(text);

        btn.setFocusPainted(false);

        btn.setFont(new Font("Arial", Font.BOLD, 14));

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        btn.setMaximumSize(new Dimension(200, 40));

        btn.setBackground(new Color(230, 230, 230));

        // =========================================================
        // BUTTON EVENT
        // =========================================================

        btn.addActionListener(e -> {

            // chạy action
            if (action != null) {

                action.run();
            }

            // phát âm thanh click
            if (SOUND_ON) {

                SoundManager.play(
                        "/resources/sound/click.wav"
                );
            }
        });

        return btn;
    }

    // =========================================================
    // BACKGROUND
    // =========================================================

    /**
     * vẽ background gradient
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();

        int h = getHeight();

        // gradient background
        g2.setPaint(new GradientPaint(
                0,
                0,
                new Color(245, 248, 255),
                0,
                h,
                new Color(190, 210, 255)
        ));

        g2.fillRect(0, 0, w, h);
    }
}