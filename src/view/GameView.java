package view;

import model.*;
import model.RecordManager;
import model.Record;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import util.ImageLoader;
import util.SoundManager;

/**
 * Game View
 * Giao diện đồ họa chính của trò chơi Egg Catcher.
 * * Chức năng:
 * - Hiển thị nền (Background), làn đường (Lane), nhân vật hứng và các vật thể rơi.
 * - Hiển thị bảng HUD thông tin (Score, Best Score, Lives, Time).
 * - Xử lý tương tác chuột cho hệ thống nút nhấn: Pause, Replay, Home, Records.
 * - Thiết kế giao diện phủ màn hình Tạm dừng (Pause) và Kết thúc (Game Over).
 * - Tích hợp bộ chuyển đổi bật/tắt âm thanh hệ thống.
 */
public class GameView extends JPanel {

    /** Model + Navigation Callbacks */
    private GameModel model;
    private Runnable onHome;
    private Runnable onRecords;

    /** Top Controls HUD Buttons */
    private Rectangle pauseBtn = new Rectangle();

    /** Game Over Screen Buttons */
    private Rectangle recordBtn = new Rectangle();
    private Rectangle homeBtn = new Rectangle();
    private Rectangle replayBtn = new Rectangle();
   
    /** Pause Overlay Screen Buttons */
    private Rectangle resumeBtn = new Rectangle();
    private Rectangle restartBtn = new Rectangle();
    private Rectangle homePauseBtn = new Rectangle();
    private Rectangle soundBtn = new Rectangle();
    private Rectangle musicBtn = new Rectangle();

    /** Animation & Hover State */
    private boolean hoverPause = false;

    /** Advertisements / Sponsor System */
    private Image gameOverAd;

    /**
     * Khởi tạo giao diện đồ họa Game View
     *
     * @param model     mô hình quản lý dữ liệu gốc
     * @param onHome    hành động chuyển hướng về màn hình chính
     * @param onRecords hành động chuyển hướng xem bảng kỷ lục
     */
    public GameView(GameModel model, Runnable onHome, Runnable onRecords) {
        this.model = model;
        this.onHome = onHome;
        this.onRecords = onRecords;

        setBackground(new Color(245, 248, 255));
        setFocusable(true);
        
        // Tải tài nguyên hình ảnh quảng cáo hiển thị khi thua trận
        gameOverAd = ImageLoader.load("/resources/ads/ad1.png");

        // Đăng ký bộ lắng nghe sự kiện từ chuột
        initMouse();
    }

    /** Input Mouse Listeners */
    private void initMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();

                /** Top Navigation Buttons Processing */
                if (pauseBtn.contains(p)) {
                    model.togglePause();

                    if (SoundManager.SOUND_ON) {
                        SoundManager.playUI();
                    }

                    if (model.isPaused()) {
                        SoundManager.stopMusic();
                    } else {
                        SoundManager.playMusic("/resources/music/bgm.wav");
                    }
                    return;
                }

                /** Pause Screen Overlay Buttons Processing */
                if (model.isPaused()) {
                    if (resumeBtn.contains(p)) {
                        model.togglePause();
                        if (SoundManager.MUSIC_ON) {
                            SoundManager.playMusic("/resources/music/bgm.wav");
                        }
                        return;
                    }
                    
                    if (restartBtn.contains(p)) {
                        model.reset();
                        if (SoundManager.MUSIC_ON) {
                            SoundManager.playMusic("/resources/music/bgm.wav");
                        }
                        return;
                    }

                    if (homePauseBtn.contains(p)) {
                        if (onHome != null) onHome.run();
                        return;
                    }

                    if (soundBtn.contains(p)) {
                        SoundManager.SOUND_ON = !SoundManager.SOUND_ON;
                        return;
                    }

                    if (musicBtn.contains(p)) {
                        SoundManager.MUSIC_ON = !SoundManager.MUSIC_ON;
                        if (!SoundManager.MUSIC_ON) {
                            SoundManager.stopMusic();
                        } else {
                            SoundManager.playMusic("/resources/music/bgm.wav");
                        }
                        return;
                    }

                    model.togglePause();
                    return;
                }

                /** Game Over Screen Buttons Processing */
                if (model.isGameOver()) {
                    if (recordBtn.contains(p) && onRecords != null) {
                        onRecords.run();
                    }
                    if (replayBtn.contains(p)) {
                        model.reset();
                        if (SoundManager.MUSIC_ON) {
                            SoundManager.playMusic("/resources/music/bgm.wav");
                        }
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

        /** Motion Hover Interactive Tracking */
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoverPause = pauseBtn.contains(e.getPoint());
                repaint();
            }
        });
    }

    /** Paint Rendering Pipeline */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Đồng bộ hóa độ cao màn hình hiện tại vào trong Model logic
        model.setScreenHeight(getHeight());

        // Thực hiện tuần tự các bước vẽ đè giao diện nền tảng
        drawBackground(g);
        drawLane(g);
        drawObjects(g);
        drawPlayer(g);
        drawHUD(g);
        drawTopButtons(g);

        // Hiển thị lớp giao diện phủ tầng trên cùng tùy theo trạng thái trò chơi
        if (model.isPaused()) {
            drawPauseScreen(g);
        }
        
        if (model.isGameOver()) {
            // [2.2.3] Nhận diện trạng thái gameOver = true để kích hoạt màn hình kết quả lên tầng trên cùng
            drawGameOver(g);
        }
    }

    /** Background Graphics Graphic Rendering */
    private void drawBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(
                0, 0, new Color(245, 248, 255),
                0, getHeight(), new Color(190, 210, 255)
        ));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    /** Lane Layout Dimensions Logic */
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

    /** Top Controls Navigation Buttons Drawing */
    private void drawTopButtons(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int size = 40;

        int px = getWidth() - 50;
        int py = 10;

        pauseBtn = new Rectangle(px, py, size, size);

        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillOval(px, py, size, size);

        g2.setColor(Color.BLACK);
        g2.fillRoundRect(px + 12, py + 10, 4, 20, 2, 2);
        g2.fillRoundRect(px + 22, py + 10, 4, 20, 2, 2);
    }

    /** Player Character Rendering */
    private void drawPlayer(Graphics g) {
        Player p = model.getPlayer();
        p.draw(g, laneCenter(p.getLane()), laneWidth(), getHeight());
    }

    /** Active Falling Objects Rendering */
    private void drawObjects(Graphics g) {
        for (FallingObject o : model.getObjects()) {
            o.draw(g, laneCenter(o.getLane()), laneWidth());
        }
    }

    /** HUD Indicators Graphics Overlay */
    private void drawHUD(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));

        int time = model.getTimeSurvived();
        String mmss = String.format("%02d:%02d", time / 60, time % 60);

        Record best = RecordManager.getBestRecord();
        int bestScore = (best != null) ? best.getScore() : 0;

        String[] labels = {
                "Score: " + model.getScore(),
                "Lives: " + model.getLives(),
                "Time: " + mmss,
                "Best: " + bestScore
        };

        int x = 15;
        int y = 15;
        int boxW = 110;
        int boxH = 35;
        int gap = 10;

        for (int i = 0; i < labels.length; i++) {
            int bx = x + i * (boxW + gap);
            int by = y;

            // Tạo hiệu ứng đổ bóng (Shadow Effect) tăng chiều sâu
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillRoundRect(bx + 3, by + 3, boxW, boxH, 12, 12);

            // Nền hộp hiển thị thông số chính
            g2.setColor(new Color(30, 30, 30, 180));
            g2.fillRoundRect(bx, by, boxW, boxH, 12, 12);

            // Vẽ viền cạnh thanh lịch
            g2.setColor(new Color(255, 255, 255, 60));
            g2.drawRoundRect(bx, by, boxW, boxH, 12, 12);

            // Căn lề giữa và kết xuất chuỗi chữ hiển thị
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            int tx = bx + (boxW - fm.stringWidth(labels[i])) / 2;
            int ty = by + (boxH + fm.getAscent()) / 2 - 3;

            g2.drawString(labels[i], tx, ty);
        }
    }

    /** Pause Overlay Popup Interface Menu */
    private void drawPauseScreen(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, getWidth(), getHeight());

        int w = (int)(getWidth() * 0.75);
        int h = (int)(getHeight() * 0.5);

        int x = (getWidth() - w) / 2;
        int y = (getHeight() - h) / 2;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, w, h, 25, 25);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("PAUSED", x + 20, y + 30);

        int btnW = w - 40;
        int btnH = 40;
        int gap = 12;

        resumeBtn = new Rectangle(x + 20, y + 60, btnW, btnH);
        restartBtn = new Rectangle(x + 20, y + 60 + (btnH + gap), btnW, btnH);
        homePauseBtn = new Rectangle(x + 20, y + 60 + (btnH + gap) * 2, btnW, btnH);
        soundBtn = new Rectangle(x + 20, y + 60 + (btnH + gap) * 3, btnW, btnH);
        musicBtn = new Rectangle(x + 20, y + 60 + (btnH + gap) * 4, btnW, btnH);

        drawButton(g2, resumeBtn, "RESUME");
        drawButton(g2, restartBtn, "RESTART");
        drawButton(g2, homePauseBtn, "HOME");
        drawButton(g2, soundBtn, "SOUND: " + (SoundManager.SOUND_ON ? "ON" : "OFF"));
        drawButton(g2, musicBtn, "MUSIC: " + (SoundManager.MUSIC_ON ? "ON" : "OFF"));
    }

    /** Game Over Screen Score Display Card */
    private void drawGameOver(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, w, h);

        int bw = (int) (w * 0.75);
        int bh = (int) (h * 0.78);

        int x = (w - bw) / 2;
        int y = (h - bh) / 2;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, bw, bh, 25, 25);

        /** Sponsor Advertisement Display Element */
        if (gameOverAd != null) {
            g2.drawImage(gameOverAd, x + 30, y + 25, bw - 60, 120, null);
        }

        /** Main Header Title Rendering */
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 24));

        String title = "GAME OVER!";
        FontMetrics fmTitle = g2.getFontMetrics();

        int titleX = x + (bw - fmTitle.stringWidth(title)) / 2;
        g2.drawString(title, titleX, y + 180);

        /** Dynamic Highscore Identification Output */
        int currentScore = model.getScore();
        boolean isHighScore = currentScore >= RecordManager.getHighScore();

        String scoreText = isHighScore
                ? "NEW HIGH SCORE: " + currentScore
                : "SCORE: " + currentScore;

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(isHighScore ? new Color(255, 140, 0) : Color.BLACK);

        FontMetrics fmScore = g2.getFontMetrics();
        int scoreX = x + (bw - fmScore.stringWidth(scoreText)) / 2;
        g2.drawString(scoreText, scoreX, y + 230);

        /** Navigation Functional Buttons Layout Bounds Setup */
        recordBtn = new Rectangle(x + 40, y + 280, bw - 80, 40);
        replayBtn = new Rectangle(x + 40, y + 330, bw - 80, 40);
        homeBtn = new Rectangle(x + 40, y + 380, bw - 80, 40);

        drawButton(g2, recordBtn, "RECORDS");
        drawButton(g2, replayBtn, "PLAY AGAIN");
        drawButton(g2, homeBtn, "HOME");
    }

    /** Generic Standard UI Button Construction Helper Method */
    private void drawButton(Graphics2D g2, Rectangle r, String text) {
        g2.setColor(new Color(220, 220, 220));
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);

        g2.setColor(Color.BLACK);
        FontMetrics fm = g2.getFontMetrics();

        int tx = r.x + (r.width - fm.stringWidth(text)) / 2;
        int ty = r.y + (r.height + fm.getAscent()) / 2 - 3;

        g2.drawString(text, tx, ty);
    }
}