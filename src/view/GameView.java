package view;

import model.*;
import model.Record;
import util.ImageLoader;
import util.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * =========================================================
 * GAME VIEW
 * =========================================================
 * giao diện chính của game Egg Catcher
 *
 * chức năng:
 * - hiển thị background, lane, player, vật thể rơi
 * - hiển thị HUD (score, best score, lives, time)
 * - xử lý pause / replay / home / records
 * - hiển thị màn hình pause và game over
 * - hỗ trợ bật tắt âm thanh
 * =========================================================
 */
public class GameView extends JPanel {

    // =========================================================
    // MODEL + CALLBACK
    // =========================================================

    private GameModel model;

    private Runnable onHome;
    private Runnable onRecords;

    // =========================================================
    // TOP BUTTONS
    // =========================================================

    private Rectangle pauseBtn = new Rectangle();
    private Rectangle soundBtn = new Rectangle();

    // =========================================================
    // GAME OVER BUTTONS
    // =========================================================

    private Rectangle recordBtn = new Rectangle();
    private Rectangle replayBtn = new Rectangle();
    private Rectangle homeBtn = new Rectangle();

    // =========================================================
    // PAUSE SCREEN BUTTONS
    // =========================================================

    private Rectangle resumeBtn = new Rectangle();
    private Rectangle restartBtn = new Rectangle();
    private Rectangle soundPauseBtn = new Rectangle();
    private Rectangle homePauseBtn = new Rectangle();

    // =========================================================
    // STATE
    // =========================================================

    private boolean hoverPause = false;

    // =========================================================
    // ADS
    // =========================================================

    private Image gameOverAd;

    /**
     * constructor khởi tạo game view
     */
    public GameView(
            GameModel model,
            Runnable onHome,
            Runnable onRecords
    ) {

        this.model = model;
        this.onHome = onHome;
        this.onRecords = onRecords;

        setBackground(new Color(245, 248, 255));
        setFocusable(true);

        // load ảnh quảng cáo
        gameOverAd =
                ImageLoader.load("/resources/ads/ad1.png");

        initMouse();
    }

    // =========================================================
    // INPUT
    // =========================================================

    /**
     * khởi tạo sự kiện chuột
     */
    private void initMouse() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                Point p = e.getPoint();

                // =================================================
                // TOP BUTTONS
                // =================================================

                // pause
                if (pauseBtn.contains(p)) {

                    model.togglePause();

                    if (model.isPaused()) {
                        SoundManager.stopMusic();
                    } else {
                        SoundManager.playBackgroundMusic(
                                "/resources/music/bgm.wav"
                        );
                    }

                    return;
                }

                // sound
                if (soundBtn.contains(p)) {

                    SoundManager.SOUND_ON =
                            !SoundManager.SOUND_ON;

                    SoundManager.MUSIC_ON =
                            !SoundManager.MUSIC_ON;

                    if (!SoundManager.MUSIC_ON) {

                        SoundManager.stopMusic();

                    } else {

                        SoundManager.playBackgroundMusic(
                                "/resources/music/bgm.wav"
                        );
                    }

                    repaint();
                    return;
                }

                // =================================================
                // PAUSE SCREEN
                // =================================================

                if (model.isPaused()) {

                    // resume
                    if (resumeBtn.contains(p)) {

                        model.togglePause();

                        if (SoundManager.MUSIC_ON) {

                            SoundManager.playBackgroundMusic(
                                    "/resources/music/bgm.wav"
                            );
                        }

                        return;
                    }

                    // restart
                    if (restartBtn.contains(p)) {

                        model.reset();

                        if (SoundManager.MUSIC_ON) {

                            SoundManager.playBackgroundMusic(
                                    "/resources/music/bgm.wav"
                            );
                        }

                        return;
                    }

                    // toggle sound
                    if (soundPauseBtn.contains(p)) {

                        SoundManager.SOUND_ON =
                                !SoundManager.SOUND_ON;

                        SoundManager.MUSIC_ON =
                                !SoundManager.MUSIC_ON;

                        if (!SoundManager.MUSIC_ON) {

                            SoundManager.stopMusic();

                        } else {

                            SoundManager.playBackgroundMusic(
                                    "/resources/music/bgm.wav"
                            );
                        }

                        repaint();
                        return;
                    }

                    // home
                    if (homePauseBtn.contains(p)) {

                        if (onHome != null) {
                            onHome.run();
                        }

                        return;
                    }

                    return;
                }

                // =================================================
                // GAME OVER
                // =================================================

                if (model.isGameOver()) {

                    // records
                    if (recordBtn.contains(p)
                            && onRecords != null) {

                        onRecords.run();
                        return;
                    }

                    // replay
                    if (replayBtn.contains(p)) {

                        model.reset();

                        if (SoundManager.MUSIC_ON) {

                            SoundManager.playBackgroundMusic(
                                    "/resources/music/bgm.wav"
                            );
                        }

                        return;
                    }

                    // home
                    if (homeBtn.contains(p)
                            && onHome != null) {

                        onHome.run();
                    }
                }
            }
        });

        // =========================================================
        // HOVER EFFECT
        // =========================================================

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                hoverPause =
                        pauseBtn.contains(e.getPoint());

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

        if (model.isPaused()) {
            drawPauseScreen(g);
        }

        if (model.isGameOver()) {
            drawGameOver(g);
        }
    }

    // =========================================================
    // BACKGROUND
    // =========================================================

    private void drawBackground(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(
                new GradientPaint(
                        0,
                        0,
                        new Color(245, 248, 255),
                        0,
                        getHeight(),
                        new Color(190, 210, 255)
                )
        );

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

        // =====================================================
        // PAUSE BUTTON
        // =====================================================

        int px = getWidth() - 50;
        int py = 10;

        pauseBtn = new Rectangle(px, py, size, size);

        g2.setColor(
                hoverPause
                        ? new Color(220, 220, 220)
                        : new Color(255, 255, 255, 220)
        );

        g2.fillOval(px, py, size, size);

        g2.setColor(Color.BLACK);

        g2.fillRoundRect(px + 12, py + 10, 4, 20, 2, 2);
        g2.fillRoundRect(px + 22, py + 10, 4, 20, 2, 2);

        // =====================================================
        // SOUND BUTTON
        // =====================================================

        int sx = px - 50;

        soundBtn = new Rectangle(sx, py, size, size);

        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillOval(sx, py, size, size);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        g2.drawString(
                SoundManager.SOUND_ON ? "🔊" : "🔇",
                sx + 10,
                py + 25
        );
    }

    // =========================================================
    // PLAYER
    // =========================================================

    private void drawPlayer(Graphics g) {

        Player p = model.getPlayer();

        p.draw(
                g,
                laneCenter(p.getLane()),
                laneWidth(),
                getHeight()
        );
    }

    // =========================================================
    // OBJECTS
    // =========================================================

    private void drawObjects(Graphics g) {

        for (FallingObject o : model.getObjects()) {

            o.draw(
                    g,
                    laneCenter(o.getLane()),
                    laneWidth()
            );
        }
    }

    // =========================================================
    // HUD
    // =========================================================

    private void drawHUD(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();

        g2.setColor(new Color(255, 255, 255, 220));

        g2.fillRoundRect(
                10,
                10,
                w - 20,
                60,
                18,
                18
        );

        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Arial", Font.BOLD, 13));

        int time = model.getTimeSurvived();

        String mmss =
                String.format("%02d:%02d", time / 60, time % 60);

        Record best = RecordManager.getBestRecord();

        g2.drawString(
                "Score: " + model.getScore(),
                20,
                35
        );

        g2.drawString(
                "Best: " + best.getScore(),
                120,
                35
        );

        g2.drawString(
                "Lives: " + model.getLives(),
                240,
                35
        );

        g2.drawString(
                "Time: " + mmss,
                360,
                35
        );
    }

    // =========================================================
    // PAUSE SCREEN
    // =========================================================

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
        drawButton(g2, soundBtn,
                "SOUND: " + (SoundManager.SOUND_ON ? "ON" : "OFF"));

        drawButton(g2, musicBtn,
                "MUSIC: " + (SoundManager.MUSIC_ON ? "ON" : "OFF"));
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

        int bw = (int) (w * 0.75);
        int bh = (int) (h * 0.78);

        int x = (w - bw) / 2;
        int y = (h - bh) / 2;

        g2.setColor(Color.WHITE);

        g2.fillRoundRect(x, y, bw, bh, 25, 25);

        // =====================================================
        // AD
        // =====================================================

        if (gameOverAd != null) {

            g2.drawImage(
                    gameOverAd,
                    x + 30,
                    y + 25,
                    bw - 60,
                    120,
                    null
            );
        }

        // =====================================================
        // TITLE
        // =====================================================

        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Arial", Font.BOLD, 24));

        String title = "GAME OVER";

        FontMetrics fmTitle = g2.getFontMetrics();

        int titleX =
                x + (bw - fmTitle.stringWidth(title)) / 2;

        g2.drawString(title, titleX, y + 185);

        // =====================================================
        // SCORE
        // =====================================================

        int currentScore = model.getScore();

        boolean highScore =
                currentScore >= RecordManager.getHighScore();

        g2.setFont(new Font("Arial", Font.BOLD, 20));

        String scoreText;

        if (highScore) {

            g2.setColor(new Color(255, 140, 0));

            scoreText =
                    "NEW HIGH SCORE: " + currentScore;

        } else {

            g2.setColor(Color.BLACK);

            scoreText =
                    "SCORE: " + currentScore;
        }

        FontMetrics fmScore = g2.getFontMetrics();

        int scoreX =
                x + (bw - fmScore.stringWidth(scoreText)) / 2;

        g2.drawString(scoreText, scoreX, y + 230);

        // =====================================================
        // BUTTONS
        // =====================================================

        recordBtn =
                new Rectangle(x + 40, y + 300, bw - 80, 40);

        replayBtn =
                new Rectangle(x + 40, y + 350, bw - 80, 40);

        homeBtn =
                new Rectangle(x + 40, y + 400, bw - 80, 40);

        drawButton(g2, recordBtn, "RECORDS");

        drawButton(g2, replayBtn, "PLAY AGAIN");

        drawButton(g2, homeBtn, "HOME");
    }

    // =========================================================
    // BUTTON UI
    // =========================================================

    private void drawButton(
            Graphics2D g2,
            Rectangle r,
            String text
    ) {

        g2.setColor(new Color(220, 220, 220));

        g2.fillRoundRect(
                r.x,
                r.y,
                r.width,
                r.height,
                12,
                12
        );

        g2.setColor(Color.BLACK);

        FontMetrics fm = g2.getFontMetrics();

        int tx =
                r.x + (r.width - fm.stringWidth(text)) / 2;

        int ty =
                r.y + (r.height + fm.getAscent()) / 2 - 3;

        g2.drawString(text, tx, ty);
    }
}
