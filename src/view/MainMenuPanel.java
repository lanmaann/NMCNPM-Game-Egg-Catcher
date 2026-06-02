package view;

import util.SoundManager;
import util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Main Menu Panel
 * Lớp giao diện menu chính hỗ trợ Volume Sliders và Dark Mode chuyển đổi động.
 */
public class MainMenuPanel extends JPanel {

    private JLabel title;
    private JButton startBtn;
    private JButton leaderboardBtn;
    private JButton settingBtn;
    private JButton exitBtn;
    private Runnable onExitCallback;

    /**
     * Constructor khởi tạo giao diện menu chính.
     */
    public MainMenuPanel(Runnable onStart, Runnable onLeaderboard, Runnable onExit) {
        this.onExitCallback = onExit;
        
        setLayout(new GridBagLayout());
        applyThemeStyles();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(280, 420));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);

        title = new JLabel("EGG CATCHER", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

        startBtn = createButton("START", onStart);
        leaderboardBtn = createButton("LEADERBOARD", onLeaderboard);
        settingBtn = createSettingButton();
        exitBtn = createButton("EXIT", this::handleExitRequest);

        card.add(title);
        card.add(startBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(leaderboardBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(settingBtn);
        card.add(Box.createVerticalStrut(12));
        card.add(exitBtn);

        add(card, gbc);
        updateComponentColors();
    }

    /** 
     * Làm mới màu sắc toàn bộ component dựa trên Theme hiện hành 
     * [3.1.4] Kích hoạt nhánh làm mới đồ họa MainMenuPanel
     */
    private void updateComponentColors() {
        title.setForeground(ThemeManager.getTextColor());
        startBtn.setBackground(ThemeManager.getButtonBg());
        startBtn.setForeground(ThemeManager.getTextColor());
        leaderboardBtn.setBackground(ThemeManager.getButtonBg());
        leaderboardBtn.setForeground(ThemeManager.getTextColor());
        settingBtn.setBackground(ThemeManager.getButtonBg());
        settingBtn.setForeground(ThemeManager.getTextColor());
        exitBtn.setBackground(ThemeManager.getButtonBg());
        exitBtn.setForeground(ThemeManager.getTextColor());
        
        // Buộc panel vẽ lại layout đồ họa mới
        repaint();
    }

    private void applyThemeStyles() {
        setBackground(ThemeManager.getBgStart());
    }

    private JButton createButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 42));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            if (action != null) action.run();
            SoundManager.playUI();
        });

        return btn;
    }

    private void handleExitRequest() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the game?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION && onExitCallback != null) {
            onExitCallback.run();
        }
    }

    /** Tạo nút Settings mở hộp thoại nâng cấp: Slider + Dark Mode Switch */
    private JButton createSettingButton() {
        JButton btn = new JButton("SETTINGS");
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // [3.1.1] Người chơi click nút SETTINGS, bắt đầu tiến trình khởi tạo Dialog
        btn.addActionListener((ActionEvent e) -> {
            
            // 1. Tạo thanh trượt Volume cho Nhạc nền (0-100)
            JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, SoundManager.MUSIC_VOLUME);
            musicSlider.setMajorTickSpacing(25);
            musicSlider.setPaintTicks(true);
            musicSlider.setPaintLabels(true);

            // 2. Tạo thanh trượt Volume cho SFX (0-100)
            JSlider sfxSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, SoundManager.SOUND_VOLUME);
            sfxSlider.setMajorTickSpacing(25);
            sfxSlider.setPaintTicks(true);
            sfxSlider.setPaintLabels(true);

            // 3. Tạo Checkbox chuyển đổi Dark Mode
            JCheckBox darkModeCheck = new JCheckBox("Enable Dark Mode", ThemeManager.IS_DARK_MODE);

            JTextArea guide = new JTextArea("HOW TO PLAY\n\n← → : Move\nCatch eggs, avoid bombs.\nP : Pause | R : Restart");
            guide.setEditable(false);
            guide.setFont(new Font("Arial", Font.PLAIN, 12));
            guide.setBackground(new Color(240, 240, 240));

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            
            panel.add(new JLabel("Background Music Volume:"));
            panel.add(musicSlider);
            panel.add(Box.createVerticalStrut(10));
            panel.add(new JLabel("Sound Effects Volume:"));
            panel.add(sfxSlider);
            panel.add(Box.createVerticalStrut(10));
            panel.add(darkModeCheck);
            panel.add(Box.createVerticalStrut(15));
            panel.add(new JLabel("Guide:"));
            panel.add(new JScrollPane(guide));

            // [3.1.2] Treo luồng và hiển thị hộp thoại cài đặt lên màn hình
            int result = JOptionPane.showConfirmDialog(
                    this, panel, "Settings Panel",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            // [3.1.3] Nhận phản hồi tương tác từ người chơi
            
            // [3.1.4 alt] Nhánh áp dụng cấu hình nếu người chơi bấm OK
            if (result == JOptionPane.OK_OPTION) {
                // Đồng bộ dữ liệu Volume dạng số nguyên (%)
                SoundManager.MUSIC_VOLUME = musicSlider.getValue();
                SoundManager.SOUND_VOLUME = sfxSlider.getValue();
                SoundManager.updateMusicVolume();

                // Đồng bộ và chuyển đổi giao diện Dark Mode lập tức
                ThemeManager.IS_DARK_MODE = darkModeCheck.isSelected();
                updateComponentColors();
            }
         // [3.2.1 else] Nhánh Hủy cài đặt tự động kích hoạt nếu bấm Cancel hoặc dấu X
        });

        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        g2.setPaint(new GradientPaint(0, 0, ThemeManager.getBgStart(), 0, h, ThemeManager.getBgEnd()));
        g2.fillRect(0, 0, w, h);

        g2.setColor(ThemeManager.getDecorationColor());
        g2.fillOval(-80, -80, 200, 200);
        g2.fillOval(w - 150, h - 150, 220, 220);
    }
}