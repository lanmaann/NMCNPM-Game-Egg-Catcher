package view;

import util.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Lớp giao diện menu chính của trò chơi Egg Catcher.
 * 
 * Chức năng:
 * - Hiển thị tiêu đề game
 * - Hiển thị các nút chức năng
 * - Mở bảng xếp hạng
 * - Bật/tắt âm thanh
 * - Hiển thị hướng dẫn chơi
 * - Vẽ background giao diện
 */
public class MainMenuPanel extends JPanel {

    /**
     * Constructor khởi tạo menu chính.
     * 
     * @param onStart callback bắt đầu game
     * @param onLeaderboard callback mở leaderboard
     * @param onExit callback thoát game
     */
    public MainMenuPanel(
            Runnable onStart,
            Runnable onLeaderboard,
            Runnable onExit
    ) {

        // Layout chính
        setLayout(new GridBagLayout());

        // Màu nền panel
        setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(10, 0, 10, 0);

        /**
         * Card chứa toàn bộ menu.
         */
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

        // Cho phép hiển thị background
        card.setOpaque(false);

        /**
         * Tiêu đề game.
         */
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

        /**
         * Nút bắt đầu game.
         */
        JButton startBtn =
                createButton(
                        "START",
                        onStart
                );

        /**
         * Người chơi chọn chức năng xem bảng điểm
         * ở màn hình chính.
         */
        JButton leaderboardBtn =
                createButton(
                        "LEADERBOARD",
                        onLeaderboard
                );

        /**
         * Nút cài đặt âm thanh và hướng dẫn.
         */
        JButton settingBtn =
                createSettingButton();

        /**
         * Nút thoát game.
         */
        JButton exitBtn =
                createButton(
                        "EXIT",
                        onExit
                );

        // Thêm component vào card
        card.add(title);

        card.add(startBtn);

        card.add(Box.createVerticalStrut(12));

        card.add(leaderboardBtn);

        card.add(Box.createVerticalStrut(12));

        card.add(settingBtn);

        card.add(Box.createVerticalStrut(12));

        card.add(exitBtn);

        // Thêm card vào panel
        add(card, gbc);
    }

    /**
     * Tạo button giao diện chuẩn.
     * 
     * @param text nội dung button
     * @param action hành động khi nhấn
     * @return JButton đã thiết kế
     */
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

        /**
         * Xử lý sự kiện click button.
         */
        btn.addActionListener(e -> {

            // Thực hiện callback
            if (action != null) {

                action.run();
            }

            // Phát âm thanh click
            if (SoundManager.SOUND_ON) {

                SoundManager.playUI();
            }
        });

        return btn;
    }

    /**
     * Tạo nút Settings.
     * 
     * Chức năng:
     * - Bật/tắt nhạc nền
     * - Bật/tắt hiệu ứng âm thanh
     * - Hiển thị hướng dẫn chơi
     * 
     * @return JButton Settings
     */
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

        /**
         * Xử lý mở cửa sổ Settings.
         */
        btn.addActionListener((ActionEvent e) -> {

            /**
             * Checkbox bật/tắt nhạc nền.
             */
            JCheckBox bgm =
                    new JCheckBox(
                            "Background Music",
                            SoundManager.MUSIC_ON
                    );

            /**
             * Checkbox bật/tắt hiệu ứng âm thanh.
             */
            JCheckBox sfx =
                    new JCheckBox(
                            "Sound Effects",
                            SoundManager.SOUND_ON
                    );

            /**
             * Text hướng dẫn chơi game.
             */
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
                    new Color(245, 245, 245)
            );

            /**
             * Panel chứa nội dung settings.
             */
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

            /**
             * Hiển thị dialog settings.
             */
            int result =
                    JOptionPane.showConfirmDialog(
                            null,
                            panel,
                            "Settings",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            /**
             * Lưu thay đổi cài đặt.
             */
            if (result == JOptionPane.OK_OPTION) {

                SoundManager.MUSIC_ON =
                        bgm.isSelected();

                SoundManager.SOUND_ON =
                        sfx.isSelected();

                // Dừng nhạc nếu tắt BGM
                if (!SoundManager.MUSIC_ON) {

                    SoundManager.stopMusic();
                }
            }
        });

        return btn;
    }

    /**
     * Vẽ background gradient cho menu.
     * 
     * @param g đối tượng Graphics
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g;

        int w = getWidth();

        int h = getHeight();

        /**
         * Background gradient.
         */
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

        /**
         * Trang trí background.
         */
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