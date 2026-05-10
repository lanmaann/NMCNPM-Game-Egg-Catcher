package view;

import model.*;
import model.RecordManager;
import model.Record;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import util.ImageLoader;
import java.util.List;

/**
 * =========================================================
 * GAME VIEW
 * =========================================================
 * lớp giao diện chính của game
 * 
 * chức năng:
 * - render toàn bộ game
 * - hiển thị background
 * - hiển thị player và object
 * - hiển thị HUD
 * - xử lý pause screen
 * - xử lý game over screen
 * - xử lý input chuột
 * =========================================================
 */
public class GameView extends JPanel {

    // model game
    private GameModel model;

    // callback về menu
    private Runnable onHome;

    // callback mở leaderboard
    private Runnable onRecords;

    // =========================================================
    // TOP BUTTONS
    // =========================================================

    // nút pause
    private Rectangle pauseBtn = new Rectangle();

    // nút sound
    private Rectangle soundBtn = new Rectangle();

    // =========================================================
    // GAME OVER BUTTONS
    // =========================================================

    // nút leaderboard
    private Rectangle recordBtn = new Rectangle();

    // nút home
    private Rectangle homeBtn = new Rectangle();

    // nút replay
    private Rectangle replayBtn = new Rectangle();

    // =========================================================
    // PAUSE SCREEN BUTTONS
    // =========================================================

    // nút resume
    private Rectangle resumeBtn = new Rectangle();

    // nút sound trong pause
    private Rectangle soundPauseBtn = new Rectangle();

    // nút home trong pause
    private Rectangle homePauseBtn = new Rectangle();

    // =========================================================
    // STATE
    // =========================================================

    // trạng thái âm thanh
    private boolean soundOn = true;

    // hover pause button
    private boolean hoverPause = false;

    // =========================================================
    // ADS
    // =========================================================

    // ảnh quảng cáo game over
    private Image gameOverAd;

    /**
     * constructor khởi tạo game view
     */
    public GameView(GameModel model, Runnable onHome, Runnable onRecords) {

        this.model = model;

        this.onHome = onHome;

        this.onRecords = onRecords;

        // màu nền mặc định
        setBackground(new Color(245, 248, 255));

        setFocusable(true);

        // load quảng cáo
        gameOverAd = ImageLoader.load("/resources/ads/ad1.png");

        // khởi tạo input chuột
        initMouse();
    }

    // =========================================================
    // MOUSE INPUT
    // =========================================================

    /**
     * khởi tạo xử lý chuột
     */
    private void initMouse() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                Point p = e.getPoint();

                // =========================================================
                // TOP BUTTONS
                // =========================================================

                // pause game
                if (pauseBtn.contains(p)) {

                    model.togglePause();

                    return;
                }

                // bật/tắt âm thanh
                if (soundBtn.contains(p)) {

                    soundOn = !soundOn;

                    model.setSoundEnabled(soundOn);

                    return;
                }

                // =========================================================
                // PAUSE SCREEN
                // =========================================================

                if (model.isPaused()) {

                    // resume game
                    if (resumeBtn.contains(p)) {

                        model.togglePause();

                        return;
                    }

                    // sound pause
                    if (soundPauseBtn.contains(p)) {

                        soundOn = !soundOn;

                        model.setSoundEnabled(soundOn);

                        return;
                    }

                    // về menu
                    if (homePauseBtn.contains(p)) {

                        if (onHome != null) {

                            onHome.run();
                        }

                        return;
                    }

                    // click ngoài popup → resume
                    model.togglePause();

                    return;
                }

                // =========================================================
                // GAME OVER
                // =========================================================

                if (model.isGameOver()) {

                    // mở leaderboard
                    if (recordBtn.contains(p) && onRecords != null) {

                        onRecords.run();
                    }

                    // chơi lại
                    if (replayBtn.contains(p)) {

                        model.reset();

                        return;
                    }

                    // về menu
                    if (homeBtn.contains(p) && onHome != null) {

                        onHome.run();
                    }
                }
            }
        });

        // =========================================================
        // MOUSE MOTION
        // =========================================================

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                // hover pause button
                hoverPause = pauseBtn.contains(e.getPoint());

                repaint();
            }
        });
    }

    // =========================================================
    // PAINT
    // =========================================================

    /**
     * render toàn bộ giao diện game
     */
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // đồng bộ chiều cao màn hình
        model.setScreenHeight(getHeight());

        // background
        drawBackground(g);

        // lane
        drawLane(g);

        // object
        drawObjects(g);

        // player
        drawPlayer(g);

        // HUD
        drawHUD(g);

        // top buttons
        drawTopButtons(g);

        // pause screen
        if (model.isPaused()) {

            drawPauseScreen(g);
        }

        // game over screen
        if (model.isGameOver()) {

            drawGameOver(g);
        }
    }

    // =========================================================
    // BACKGROUND
    // =========================================================

    /**
     * vẽ nền gradient
     */
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

    /**
     * tính chiều rộng mỗi lane
     */
    private int laneWidth() {

        return getWidth() / GameModel.LANE_COUNT;
    }

    /**
     * lấy tâm lane
     */
    private int laneCenter(int lane) {

        return lane * laneWidth() + laneWidth() / 2;
    }

    /**
     * vẽ lane
     */
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

    /**
     * vẽ nút pause và sound
     */
    private void drawTopButtons(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int size = 40;

        // =========================================================
        // PAUSE BUTTON
        // =========================================================

        int px = getWidth() - 50;

        int py = 10;

        pauseBtn = new Rectangle(px, py, size, size);

        g2.setColor(new Color(255, 255, 255, 200));

        g2.fillOval(px, py, size, size);

        g2.setColor(Color.BLACK);

        g2.fillRoundRect(px + 12, py + 10, 4, 20, 2, 2);

        g2.fillRoundRect(px + 22, py + 10, 4, 20, 2, 2);

        // =========================================================
        // SOUND BUTTON
        // =========================================================

        int sx = px - 50;

        soundBtn = new Rectangle(sx, py, size, size);

        g2.setColor(new Color(255, 255, 255, 200));

        g2.fillOval(sx, py, size, size);

        g2.setFont(new Font("Arial", Font.BOLD, 14));

        g2.setColor(Color.BLACK);

        g2.drawString(soundOn ? "🔊" : "🔇", sx + 10, py + 25);
    }

    // =========================================================
    // PLAYER
    // =========================================================

    /**
     * vẽ player
     */
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

    /**
     * vẽ tất cả object
     */
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

    /**
     * vẽ HUD game
     */
    private void drawHUD(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();

        // nền HUD
        g2.setColor(new Color(255, 255, 255, 220));

        g2.fillRoundRect(10, 10, w - 20, 60, 18, 18);

        // text
        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Arial", Font.BOLD, 13));

        int time = model.getTimeSurvived();

        String mmss =
                String.format("%02d:%02d", time / 60, time % 60);

        Record best = RecordManager.getBestRecord();

        g2.drawString("Score: " + model.getScore(), 20, 35);

        g2.drawString("Best: " + best.getScore(), 120, 35);

        g2.drawString("Lives: " + model.getLives(), 240, 35);

        g2.drawString("Time: " + mmss, 360, 35);
    }

    // =========================================================
    // PAUSE SCREEN
    // =========================================================

    /**
     * vẽ màn hình pause
     */
    private void drawPauseScreen(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        // nền tối
        g2.setColor(new Color(0, 0, 0, 160));

        g2.fillRect(0, 0, getWidth(), getHeight());

        int w = (int)(getWidth() * 0.75);

        int h = (int)(getHeight() * 0.5);

        int x = (getWidth() - w) / 2;

        int y = (getHeight() - h) / 2;

        // card
        g2.setColor(Color.WHITE);

        g2.fillRoundRect(x, y, w, h, 25, 25);

        // title
        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Arial", Font.BOLD, 20));

        g2.drawString("PAUSED", x + 20, y + 30);

        // button layout
        int btnW = w - 40;

        int btnH = 40;

        int gap = 12;

        // button bounds
        resumeBtn =
                new Rectangle(x + 20, y + 60, btnW, btnH);

        soundPauseBtn =
                new Rectangle(x + 20, y + 60 + btnH + gap, btnW, btnH);

        homePauseBtn =
                new Rectangle(x + 20, y + 60 + (btnH + gap) * 2, btnW, btnH);

        // draw buttons
        drawButton(g2, resumeBtn, "RESUME");

        drawButton(
                g2,
                soundPauseBtn,
                soundOn ? "SOUND ON 🔊" : "SOUND OFF 🔇"
        );

        drawButton(g2, homePauseBtn, "HOME 🏠");
    }

    // =========================================================
    // GAME OVER SCREEN
    // =========================================================

    /**
     * vẽ màn hình game over
     */
    private void drawGameOver(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();

        int h = getHeight();

        // nền tối
        g2.setColor(new Color(0, 0, 0, 170));

        g2.fillRect(0, 0, w, h);

        int bw = (int)(w * 0.75);

        int bh = (int)(h * 0.78);

        int x = (w - bw) / 2;

        int y = (h - bh) / 2;

        // card trắng
        g2.setColor(Color.WHITE);

        g2.fillRoundRect(x, y, bw, bh, 25, 25);

        // =========================================================
        // ADVERTISEMENT
        // =========================================================

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

        // =========================================================
        // TITLE
        // =========================================================

        g2.setColor(Color.BLACK);

        g2.setFont(new Font("Arial", Font.BOLD, 24));

        String title = "GAME OVER!";

        FontMetrics fmTitle = g2.getFontMetrics();

        int titleX =
                x + (bw - fmTitle.stringWidth(title)) / 2;

        g2.drawString(title, titleX, y + 185);

        // =========================================================
        // SCORE
        // =========================================================

        int currentScore = model.getScore();

        boolean isHighScore =
                currentScore >= RecordManager.getHighScore();

        g2.setFont(new Font("Arial", Font.BOLD, 20));

        String scoreText;

        if (isHighScore) {

            g2.setColor(new Color(255, 140, 0));

            scoreText =
                    "NEW HIGH SCORE: " + currentScore;
        }

        else {

            g2.setColor(Color.BLACK);

            scoreText =
                    "SCORE: " + currentScore;
        }

        FontMetrics fmScore = g2.getFontMetrics();

        int scoreX =
                x + (bw - fmScore.stringWidth(scoreText)) / 2;

        g2.drawString(scoreText, scoreX, y + 230);

        // =========================================================
        // BUTTONS
        // =========================================================

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
    // BUTTON
    // =========================================================

    /**
     * vẽ button chung
     */
    private void drawButton(Graphics2D g2, Rectangle r, String text) {

        // nền button
        g2.setColor(new Color(220, 220, 220));

        g2.fillRoundRect(
                r.x,
                r.y,
                r.width,
                r.height,
                12,
                12
        );

        // text
        g2.setColor(Color.BLACK);

        FontMetrics fm = g2.getFontMetrics();

        int tx =
                r.x + (r.width - fm.stringWidth(text)) / 2;

        int ty =
                r.y + (r.height + fm.getAscent()) / 2 - 3;

        g2.drawString(text, tx, ty);
    }
}