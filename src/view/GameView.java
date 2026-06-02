package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import util.ImageLoader;
import util.SoundManager;
import util.ThemeManager;

/**
 * Game View
 * Giao diện hiển thị trực tiếp và xử lý vòng lặp đồ họa runtime của trò chơi Egg Catcher.
 * * Chức năng nâng cao:
 * - Vẽ môi trường, HUD, người chơi và các vật thể rơi.
 * - Hiển thị Pop-up Settings lồng ngay trên màn hình tạm dừng (Pause Screen).
 * - Đồng bộ hóa Volume Sliders tuyến tính (0-100%) và chuyển đổi Dark Mode thời gian thực.
 */
public class GameView extends JPanel {

    private GameModel model;
    private Runnable onHome;
    private Runnable onRecords;

    // Vùng tương tác các nút bấm màn hình chính
    private Rectangle pauseBtn = new Rectangle();
    private Rectangle recordBtn = new Rectangle();
    private Rectangle homeBtn = new Rectangle();
    private Rectangle replayBtn = new Rectangle();
    
    // Vùng tương tác các nút bấm trên màn hình Pause
    private Rectangle resumeBtn = new Rectangle();
    private Rectangle restartBtn = new Rectangle();
    private Rectangle homePauseBtn = new Rectangle();
    private Rectangle settingPauseBtn = new Rectangle(); // Nút Settings mới thay thế nút Sound/Music ON-OFF cũ

    private boolean hoverPause = false;
    private Image gameOverAd;

    /**
     * Constructor khởi tạo Game View.
     */
    public GameView(GameModel model, Runnable onHome, Runnable onRecords) {
        this.model = model;
        this.onHome = onHome;
        this.onRecords = onRecords;

        setFocusable(true);
        gameOverAd = ImageLoader.load("/resources/ads/ad1.png");

        initMouse();
    }

    /**
     * Khởi tạo và liên kết các sự kiện tương tác chuột.
     */
    private void initMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                // 1. XỬ LÝ NÚT TẠM DỪNG (PAUSE BUTTON) Ở HUD TOP
                if (pauseBtn.contains(p)) {
                    model.togglePause();
                    SoundManager.playUI();

                    if (model.isPaused()) {
                        SoundManager.stopMusic();
                    } else {
                        SoundManager.playGameOverMusic("/resources/music/bgm.wav");
                    }
                    repaint();
                    return;
                }

                // 2. XỬ LÝ TƯƠNG TÁC TRÊN MÀN HÌNH TẠM DỪNG (PAUSE SCREEN)
                if (model.isPaused()) {
                    if (resumeBtn.contains(p)) {
                        model.togglePause();
                        SoundManager.playGameOverMusic("/resources/music/bgm.wav");
                        repaint();
                        return;
                    }

                    if (restartBtn.contains(p)) {
                        model.reset();
                        SoundManager.playGameOverMusic("/resources/music/bgm.wav");
                        repaint();
                        return;
                    }

                    if (homePauseBtn.contains(p)) {
                        if (onHome != null) onHome.run();
                        return;
                    }

                    // [3.1.1] Người chơi click chọn nút settings lồng trên màn hình pause
                    if (settingPauseBtn.contains(p)) {
                        showEmbeddedSettingsDialog();
                        return;
                    }
                    return;
                }

                // 3. XỬ LÝ TƯƠNG TÁC KHI THUA CUỘC (GAME OVER SCREEN)
                if (model.isGameOver()) {
                    if (replayBtn.contains(p)) {
                        model.reset();
                        SoundManager.playGameOverMusic("/resources/music/bgm.wav");
                        repaint();
                        return;
                    }
                    if (homeBtn.contains(p) && onHome != null) {
                        onHome.run();
                        return;
                    }
                    if (recordBtn.contains(p) && onRecords != null) {
                        onRecords.run();
                        return;
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoverPause = pauseBtn.contains(e.getPoint());
                repaint();
            }
        });
    }

    /**
     * Triển khai xuất hộp thoại cấu hình nâng cao tích hợp Volume Sliders và Dark Mode Switch.
     */
    private void showEmbeddedSettingsDialog() {
        // Tạo thanh trượt Volume cho Nhạc nền
        JSlider musicSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, SoundManager.MUSIC_VOLUME);
        musicSlider.setMajorTickSpacing(25);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);

        // Tạo thanh trượt Volume cho Hiệu ứng âm thanh (SFX)
        JSlider sfxSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, SoundManager.SOUND_VOLUME);
        sfxSlider.setMajorTickSpacing(25);
        sfxSlider.setPaintTicks(true);
        sfxSlider.setPaintLabels(true);

        // Tạo Checkbox tùy chọn giao diện tối
        JCheckBox darkModeCheck = new JCheckBox("Enable Dark Mode", ThemeManager.IS_DARK_MODE);

        JTextArea guide = new JTextArea("HOW TO PLAY\n\n← → : Move Basket\nCatch eggs, avoid bombs.\nP : Pause Game | R : Restart Match");
        guide.setEditable(false);
        guide.setFont(new Font("Arial", Font.PLAIN, 12));
        guide.setBackground(new Color(235, 235, 235));

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
        panel.add(new JLabel("Game Control Guide:"));
        panel.add(new JScrollPane(guide));

        // [3.1.2] Treo luồng dựng form lồng lên trên màn hình đồ họa gameview hiện thời
        int result = JOptionPane.showConfirmDialog(
                this, panel, "In-Game Settings",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        
        // [3.1.3] Nhận tín hiệu tương tác hành động click từ người chơi
        // [3.1.4 alt] Nhánh chấp thuận lưu cấu hình (Người chơi bấm OK)
        if (result == JOptionPane.OK_OPTION) {
            // Lưu dữ liệu phần trăm mới vào core sound
            SoundManager.MUSIC_VOLUME = musicSlider.getValue();
            SoundManager.SOUND_VOLUME = sfxSlider.getValue();
            
            // Ép hệ thống âm thanh cập nhật cường độ decibel lập tức
            SoundManager.updateMusicVolume();

            // Cập nhật cấu hình Dark Mode
            ThemeManager.IS_DARK_MODE = darkModeCheck.isSelected();
            
         // [3.1.4 alt] Vẽ lại toàn bộ màn hình để áp màu sắc giao diện mới            
            repaint();
        }
     // [3.2.1 else] Nhánh hủy bỏ (Bấm Cancel hoặc dấu X) tự động đóng dialog, giữ nguyên biến tĩnh
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        model.setScreenHeight(getHeight());

        drawBackground(g);
        drawLane(g);
        drawObjects(g);
        drawPlayer(g);
        drawHUD(g);
        drawTopButtons(g);

        if (model.isPaused()) drawPauseScreen(g);
        if (model.isGameOver()) drawGameOver(g);
    }

    private void drawBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        // Tự động điều chỉnh màu nền đồng bộ theo ThemeManager sáng/tối hiện thời
        g2.setPaint(new GradientPaint(0, 0, ThemeManager.getBgStart(), 0, getHeight(), ThemeManager.getBgEnd()));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    private int laneWidth() {
        return getWidth() / GameModel.LANE_COUNT;
    }

    private int laneCenter(int lane) {
        return lane * laneWidth() + laneWidth() / 2;
    }

    private void drawLane(Graphics g) {
        // Màu đường phân làn động thích ứng theo Dark Mode
        g.setColor(ThemeManager.IS_DARK_MODE ? new Color(255, 255, 255, 20) : new Color(0, 0, 0, 25));
        for (int i = 1; i < GameModel.LANE_COUNT; i++) {
            int x = i * laneWidth();
            g.drawLine(x, 0, x, getHeight());
        }
    }

    private void drawTopButtons(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int size = 40;
        int px = getWidth() - 50;
        int py = 10;

        pauseBtn = new Rectangle(px, py, size, size);

        // Đổi màu nút tạm dừng dựa trên trạng thái hover và giao diện tối/sáng
        g2.setColor(hoverPause ? new Color(200, 200, 200, 230) : (ThemeManager.IS_DARK_MODE ? new Color(60, 60, 70, 220) : new Color(255, 255, 255, 220)));
        g2.fillOval(px, py, size, size);

        g2.setColor(ThemeManager.getTextColor());
        g2.fillRoundRect(px + 13, py + 11, 4, 18, 2, 2);
        g2.fillRoundRect(px + 23, py + 11, 4, 18, 2, 2);
    }

    private void drawPlayer(Graphics g) {
        Player p = model.getPlayer();
        p.draw(g, laneCenter(p.getLane()), laneWidth(), getHeight());
    }

    private void drawObjects(Graphics g) {
        for (FallingObject o : model.getObjects()) {
            o.draw(g, laneCenter(o.getLane()), laneWidth());
        }
    }

    private void drawHUD(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();

        g2.setColor(ThemeManager.IS_DARK_MODE ? new Color(45, 45, 55, 230) : new Color(255, 255, 255, 230));
        g2.fillRoundRect(10, 10, w - 20, 60, 18, 18);

        g2.setColor(ThemeManager.getTextColor());
        g2.setFont(new Font("Arial", Font.BOLD, 13));

        int time = model.getTimeSurvived();
        String mmss = String.format("%02d:%02d", time / 60, time % 60);

        g2.drawString("Score: " + model.getScore(), 20, 35);
        g2.drawString("Lives: " + model.getLives(), 150, 35);
        g2.drawString("Time: " + mmss, 280, 35);
    }

    /**
     * Vẽ màn hình tạm dừng.
     * Tích hợp nút SETTINGS thay cho cấu hình Sound/Music ON-OFF thủ công cũ.
     */
    private void drawPauseScreen(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, getWidth(), getHeight());

        int w = (int) (getWidth() * 0.80);
        int h = (int) (getHeight() * 0.48);
        int x = (getWidth() - w) / 2;
        int y = (getHeight() - h) / 2;

        g2.setColor(ThemeManager.IS_DARK_MODE ? new Color(35, 35, 45) : Color.WHITE);
        g2.fillRoundRect(x, y, w, h, 25, 25);

        g2.setColor(ThemeManager.getTextColor());
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("PAUSED", x + 25, y + 35);

        int btnW = w - 50;
        int btnH = 38;
        int gap = 10;
        int startBtnY = y + 55;

        resumeBtn       = new Rectangle(x + 25, startBtnY, btnW, btnH);
        restartBtn      = new Rectangle(x + 25, startBtnY + (btnH + gap), btnW, btnH);
        settingPauseBtn = new Rectangle(x + 25, startBtnY + (btnH + gap) * 2, btnW, btnH);
        homePauseBtn    = new Rectangle(x + 25, startBtnY + (btnH + gap) * 3, btnW, btnH);

        drawButton(g2, resumeBtn, "RESUME");
        drawButton(g2, restartBtn, "RESTART GAME");
        drawButton(g2, settingPauseBtn, "SETTINGS (VOLUME / THEME)");
        drawButton(g2, homePauseBtn, "QUIT TO MAIN MENU");
    }

    private void drawGameOver(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRect(0, 0, w, h);

        int bw = (int) (w * 0.80);
        int bh = (int) (h * 0.72);
        int x = (w - bw) / 2;
        int y = (h - bh) / 2;

        g2.setColor(ThemeManager.IS_DARK_MODE ? new Color(35, 35, 45) : Color.WHITE);
        g2.fillRoundRect(x, y, bw, bh, 25, 25);

        g2.setColor(ThemeManager.getTextColor());
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("GAME OVER", x + 35, y + 45);

        if (gameOverAd != null) {
            g2.drawImage(gameOverAd, x + 25, y + 70, bw - 50, 120, null);
        }

        int btnY = y + 210;
        recordBtn = new Rectangle(x + 35, btnY, bw - 70, 38);
        replayBtn = new Rectangle(x + 35, btnY + 48, bw - 70, 38);
        homeBtn   = new Rectangle(x + 35, btnY + 96, bw - 70, 38);

        drawButton(g2, recordBtn, "LEADERBOARD RECORDS");
        drawButton(g2, replayBtn, "PLAY AGAIN");
        drawButton(g2, homeBtn, "BACK TO MENU");
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text) {
        g2.setColor(ThemeManager.getButtonBg());
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);

        g2.setColor(ThemeManager.getTextColor());
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();

        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent()) / 2 - 2;

        g2.drawString(text, tx, ty);
    }
}