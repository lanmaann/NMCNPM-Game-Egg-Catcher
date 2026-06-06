package view;

import model.*;
import model.RecordManager;
import model.Record;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import util.ImageLoader;
import util.SoundManager;
import util.ThemeManager;

public class GameView extends JPanel {

	private GameModel model;

	private Runnable onHome;
	private Runnable onRecords;

	// =========================
	// BUTTONS
	// =========================
	private Rectangle pauseBtn = new Rectangle();
	private Rectangle recordBtn = new Rectangle();
	private Rectangle homeBtn = new Rectangle();
	private Rectangle replayBtn = new Rectangle();

	// =========================
	// PAUSE SCREEN BUTTONS
	// =========================
	private Rectangle resumeBtn = new Rectangle();
	private Rectangle restartBtn = new Rectangle();
	private Rectangle homePauseBtn = new Rectangle();
	private Rectangle soundBtn = new Rectangle();
	private Rectangle settingPauseBtn = new Rectangle();

	// =========================
	// STATE
	// =========================
	private boolean hoverPause = false;

	// =========================
	// ADS
	// =========================
	private Image gameOverAd;

	public GameView(GameModel model, Runnable onHome, Runnable onRecords) {
        this.model = model;
        this.onHome = onHome;
        this.onRecords = onRecords;

        setBackground(new Color(245, 248, 255));
        setFocusable(true);

        /* UC 1.1 - Bước 1.1.3: Hệ thống nạp (Load) tài nguyên hình ảnh (Ads, Giỏ, Trứng...) lên màn hình thông qua ImageLoader */
        gameOverAd = ImageLoader.load("/resources/ads/ad1.png");

        initMouse();
    }

	// =========================================================
	// INPUT
	// =========================================================
	private void initMouse() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                Point p = e.getPoint();

                // ================= TOP BUTTONS =================
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

                // ================= PAUSE SCREEN =================
                if (model.isPaused()) {
	                	if (resumeBtn.contains(p)) {
	                	    model.togglePause();
	                	    SoundManager.playBackgroundMusic("/resources/music/bgm.wav");
	                	    repaint();
	                	    return;
	                	}
	                	
                    if (restartBtn.contains(p)) {
                        /* UC 1.1 - Bước 1.1.1: Người chơi chọn nút bắt đầu game (RESTART) từ màn hình tạm dừng */
                        model.reset();

                        SoundManager.stopMusic();
                        SoundManager.playBackgroundMusic("/resources/music/bgm.wav");
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

                // ================= GAME OVER =================
                if (model.isGameOver()) {

                    if (recordBtn.contains(p) && onRecords != null) {
                        onRecords.run();
                      
                    }
                    if (replayBtn.contains(p)) {
                        /* UC 1.1 - Bước 1.1.1: Người chơi chọn nút bắt đầu game (PLAY AGAIN) từ màn hình Game Over */
                        model.reset();

                        SoundManager.stopMusic();
                        SoundManager.playBackgroundMusic("/resources/music/bgm.wav");
                        repaint();
                        return;
                    }
                    
                    if (homeBtn.contains(p) && onHome != null) {
                        onHome.run();
                       
                    }
                    if (recordBtn.contains(p) && onRecords != null) {
                        onRecords.run();
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

    // =========================================================
    // PAINT
    // =========================================================
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

    // =========================================================
    // BACKGROUND
    // =========================================================
    private void drawBackground(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(new GradientPaint(
                0, 0, new Color(245, 248, 255),
                0, getHeight(), new Color(190, 210, 255)
        ));

        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    // =========================================================
    // LANE
    // =========================================================
    private int laneWidth() {
        return getWidth() / GameModel.LANE_COUNT;
    }

    private int laneCenter(int lane) {
        return lane * laneWidth() + laneWidth() / 2;
    }

    private void drawLane(Graphics g) {
        g.setColor(new Color(0, 0, 0, 25));

        for (int i = 1; i < GameModel.LANE_COUNT; i++) {
            int x = i * laneWidth();
            g.drawLine(x, 0, x, getHeight());
        }
    }

    // =========================================================
    // TOP BUTTONS
    // =========================================================
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

    // =========================================================
    // PLAYER
    // =========================================================
    private void drawPlayer(Graphics g) {
        Player p = model.getPlayer();
        p.draw(g, laneCenter(p.getLane()), laneWidth(), getHeight());
    }

    // =========================================================
    // OBJECTS
    // =========================================================
    private void drawObjects(Graphics g) {
        for (FallingObject o : model.getObjects()) {
            o.draw(g, laneCenter(o.getLane()), laneWidth());
        }
    }

    // =========================================================
    // HUD
    // =========================================================
    private void drawHUD(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();

        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(10, 10, w - 20, 60, 18, 18);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 13));

        int time = model.getTimeSurvived();
        String mmss = String.format("%02d:%02d", time / 60, time % 60);

        g2.drawString("Score: " + model.getScore(), 20, 35);
        g2.drawString("Lives: " + model.getLives(), 150, 35);
        g2.drawString("Time: " + mmss, 280, 35);
    }

    // =========================================================
    // PAUSE SCREEN (FULL BUTTONS)
    // =========================================================
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
    
    // =========================================================
    // GAME OVER
    // =========================================================
    private void drawGameOver(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, w, h);

        int bw = (int)(w * 0.75);
        int bh = (int)(h * 0.7);

        int x = (w - bw) / 2;
        int y = (h - bh) / 2;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, bw, bh, 25, 25);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("GAME OVER", x + 40, y + 40);

        if (gameOverAd != null) {
            g2.drawImage(gameOverAd, x + 30, y + 70, bw - 60, 120, null);
        }

        recordBtn = new Rectangle(x + 40, y + 220, bw - 80, 40);
        replayBtn = new Rectangle(x + 40, y + 270, bw - 80, 40);
        homeBtn = new Rectangle(x + 40, y + 320, bw - 80, 40);

        drawButton(g2, recordBtn, "RECORDS");
        drawButton(g2, replayBtn, "PLAY AGAIN");
        drawButton(g2, homeBtn, "HOME");
    }

    // =========================================================
    // BUTTON
    // =========================================================
    private void drawButton(Graphics2D g2, Rectangle r, String text) {

        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);

        g2.setColor(Color.BLACK);

        FontMetrics fm = g2.getFontMetrics();

        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent()) / 2 - 3;

        g2.drawString(text, tx, ty);
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

}