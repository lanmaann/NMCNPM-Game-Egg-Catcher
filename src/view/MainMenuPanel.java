package view;

import util.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main Menu Panel
 * Lớp giao diện menu chính của trò chơi Egg Catcher.
 * * Chức năng:
 * - Hiển thị tiêu đề game mượt mà.
 * - Quản lý và điều phối các nút chức năng chính (Start, Leaderboard, Settings, Exit).
 * - Tích hợp hộp thoại cài đặt (Bật/tắt âm thanh, nhạc nền và hướng dẫn chơi).
 * - Vẽ hiệu ứng đồ họa trang trí và background gradient cho Menu.
 */
public class MainMenuPanel extends JPanel {

    /**
     * Constructor khởi tạo giao diện menu chính.
     * * @param onStart       callback xử lý bắt đầu vào trận game
     * @param onLeaderboard callback xử lý mở bảng xếp hạng kỷ lục
     * @param onExit        callback xử lý thoát ứng dụng hệ thống
     */
    public MainMenuPanel(
            Runnable onStart,
            Runnable onLeaderboard,
            Runnable onExit
    ) {
        // Cấu hình Layout chính của Panel theo dạng GridBagLayout để căn giữa card menu
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        /** Card Container chứa toàn bộ bố cục các nút chức năng */
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(280, 420));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);

        /** Tiêu đề đồ họa của trò chơi */
        JLabel title = new JLabel("EGG CATCHER", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

        /** Khởi tạo các nút bấm chức năng */
        JButton startBtn = createButton("START", onStart);
        JButton leaderboardBtn = createButton("LEADERBOARD", onLeaderboard);
        JButton settingBtn = createSettingButton();
        
        // Cấu hình nút Exit kèm bộ xác nhận thoát an toàn
        JButton exitBtn = createButton("EXIT", () -> {
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to exit the game?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION && onExit != null) {
                onExit.run();
            }
        });

        // Thêm tuần tự các thành phần giao diện vào Card Container
        card.add(title);
        card.add(startBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(leaderboardBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(settingBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(exitBtn);

        // Đẩy Card vào vị trí trung tâm của Main Panel
        add(card, gbc);
    }

    /**
     * Tạo nút bấm UI tiêu chuẩn hóa cho hệ thống Menu
     * * @param text   nội dung chữ hiển thị trên nút
     * @param action hành động thực thi khi người chơi kích hoạt nút
     * @return JButton thực thể nút bấm đã được cấu hình thẩm mỹ
     */
    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 42));
        btn.setBackground(new Color(230, 230, 230));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /** Đăng ký bộ lắng nghe sự kiện click chuột */
        btn.addActionListener(e -> {
            if (action != null) {
                action.run();
            }

            // Kích hoạt phản hồi âm thanh SFX khi tương tác nút bấm
            if (SoundManager.SOUND_ON) {
                SoundManager.playUI();
            }
        });

        return btn;
    }

    /**
     * Tạo nút Settings đặc thù mở hộp thoại bảng điều khiển cấu hình game
     * * @return JButton Settings Panel Trigger
     */
    private JButton createSettingButton() {
        JButton btn = new JButton("SETTINGS");
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setBackground(new Color(220, 220, 220));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /** Khởi tạo tiến trình Dialog cấu hình khi tương tác nút Settings */
        btn.addActionListener((ActionEvent e) -> {
            
            /** Cấu hình nút kiểm tra trạng thái Nhạc nền */
            JCheckBox bgm = new JCheckBox("Background Music", SoundManager.MUSIC_ON);

            /** Cấu hình nút kiểm tra trạng thái Hiệu ứng âm thanh */
            JCheckBox sfx = new JCheckBox("Sound Effects", SoundManager.SOUND_ON);

            /** Khu vực văn bản hiển thị cẩm nang hướng dẫn điều khiển */
            JTextArea guide = new JTextArea(
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
            guide.setFont(new Font("Arial", Font.PLAIN, 13));
            guide.setBackground(new Color(245, 245, 245));

            /** Panel thành phần gom nhóm nội dung hiển thị của Dialog */
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(bgm);
            panel.add(Box.createVerticalStrut(10));
            panel.add(sfx);
            panel.add(Box.createVerticalStrut(15));
            panel.add(new JLabel("Guide:"));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JScrollPane(guide));

            /** Đẩy luồng hiển thị Popup Modal lên màn hình ứng dụng */
            int result = JOptionPane.showConfirmDialog(
                    null,
                    panel,
                    "Settings",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            /** Lưu giữ và đồng bộ các cấu hình thiết lập mới từ người chơi */
            if (result == JOptionPane.OK_OPTION) {
                SoundManager.MUSIC_ON = bgm.isSelected();
                SoundManager.SOUND_ON = sfx.isSelected();

                // Cắt luồng nhạc nền lập tức nếu người chơi chủ động tắt BGM
                if (!SoundManager.MUSIC_ON) {
                    SoundManager.stopMusic();
                }
            }
        });

        return btn;
    }

    /** Pipeline xuất đồ họa nền móng cho Menu Panel */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        /** Vẽ dải màu nền Gradient mượt mà */
        g2.setPaint(new GradientPaint(
                0, 0, new Color(245, 248, 255),
                0, h, new Color(190, 210, 255)
        ));
        g2.fillRect(0, 0, w, h);

        /** Đắp các khối hình học trang trí trừu tượng tạo chiều sâu đồ họa */
        g2.setColor(new Color(255, 255, 255, 70));
        g2.fillOval(-80, -80, 200, 200);
        g2.fillOval(w - 150, h - 150, 220, 220);
    }
}