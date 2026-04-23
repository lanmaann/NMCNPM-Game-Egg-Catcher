package view;

import model.*;
import model.RecordManager;
import model.Record;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameView extends JPanel {

    private GameModel model;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    // CALLBACK để đổi màn hình
    private Runnable onHome;
    private Runnable onRecords;

    // BUTTON AREA
    private Rectangle pauseBtn = new Rectangle();
    private Rectangle recordBtn = new Rectangle();
    private Rectangle homeBtn = new Rectangle();

    private boolean hoverPause = false;

    // =========================
    // CONSTRUCTOR
    // =========================
    public GameView(GameModel model, Runnable onHome, Runnable onRecords) {
        this.model = model;
        this.onHome = onHome;
        this.onRecords = onRecords;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(245, 248, 255));
        setFocusable(true);

        initMouse();
    }

    public void setModel(GameModel model) {
        this.model = model;
    }

    // =========================
    // INPUT
    // =========================
    private void initMouse() {

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                Point p = e.getPoint();

                // pause
                if (pauseBtn.contains(p)) {
                    model.togglePause();
                    return;
                }

                // game over buttons
                if (model.isGameOver()) {

                    if (recordBtn.contains(p)) {
                        if (onRecords != null) onRecords.run();
                    }

                    if (homeBtn.contains(p)) {
                        if (onHome != null) onHome.run();
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

    // =========================
    // RENDER
    // =========================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);
        drawLane(g);
        drawObjects(g);
        drawPlayer(g);
        drawHUD(g);
        drawPauseButton(g);

        if (model.isPaused()) drawPauseScreen(g);
        if (model.isGameOver()) drawGameOver(g);
    }

    // =========================
    // BACKGROUND
    // =========================
    private void drawBackground(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setPaint(new GradientPaint(
                0, 0, new Color(245, 248, 255),
                0, HEIGHT, new Color(190, 210, 255)
        ));

        g2.fillRect(0, 0, WIDTH, HEIGHT);
    }

    // =========================
    // LANES
    // =========================
    private int laneWidth() {
        return WIDTH / GameModel.LANE_COUNT;
    }

    private int laneCenter(int lane) {
        return lane * laneWidth() + laneWidth() / 2;
    }

    private void drawLane(Graphics g) {
        g.setColor(new Color(0, 0, 0, 25));

        for (int i = 1; i < GameModel.LANE_COUNT; i++) {
            int x = i * laneWidth();
            g.drawLine(x, 0, x, HEIGHT);
        }
    }

    // =========================
    // PLAYER
    // =========================
    private void drawPlayer(Graphics g) {
        Player p = model.getPlayer();

        int x = laneCenter(p.getLane()) - 25;
        int y = p.getY();

        p.draw(g, x, y);
    }

    // =========================
    // OBJECTS
    // =========================
    private void drawObjects(Graphics g) {
        for (FallingObject o : model.getObjects()) {
            int x = laneCenter(o.getLane()) - 20;
            int y = o.getY();
            o.draw(g, x, y);
        }
    }

    // =========================
    // HUD
    // =========================
    private void drawHUD(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(255, 255, 255, 235));
        g2.fillRoundRect(10, 10, WIDTH - 20, 60, 18, 18);

        g2.setColor(new Color(40, 40, 40));
        g2.setFont(new Font("Arial", Font.BOLD, 13));

        int time = model.getTimeSurvived();
        String mmss = String.format("%02d:%02d", time / 60, time % 60);

        int col = WIDTH / 4;

        g2.drawString("Score: " + model.getScore(), 20, 35);
        g2.drawString("Lives: " + model.getLives(), col + 20, 35);
        g2.drawString("Time: " + mmss, col * 2 + 20, 35);
        g2.drawString("Lv: " + model.getLevel(), col * 3 + 20, 35);
    }

    // =========================
    // PAUSE BUTTON
    // =========================
    private void drawPauseButton(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        int size = 36;
        int x = WIDTH - size - 12;
        int y = 12;

        pauseBtn = new Rectangle(x, y, size, size);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(hoverPause
                ? new Color(255, 255, 255, 230)
                : new Color(255, 255, 255, 180));

        g2.fillOval(x, y, size, size);

        g2.setColor(new Color(0, 0, 0, 80));
        g2.drawOval(x, y, size, size);

        g2.setColor(new Color(30, 30, 30));

        int cx = x + size / 2;
        int cy = y + size / 2;

        g2.fillRoundRect(cx - 7, cy - 10, 4, 20, 3, 3);
        g2.fillRoundRect(cx + 3, cy - 10, 4, 20, 3, 3);
    }

    // =========================
    // PAUSE SCREEN
    // =========================
    private void drawPauseScreen(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        int w = 220, h = 120;
        int x = (WIDTH - w) / 2;
        int y = (HEIGHT - h) / 2;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, w, h, 20, 20);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));

        String text = "PAUSED";
        FontMetrics fm = g2.getFontMetrics();

        g2.drawString(text,
                x + (w - fm.stringWidth(text)) / 2,
                y + 50);
    }

    // =========================
    // GAME OVER
    // =========================
    private void drawGameOver(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        int w = 300, h = 280;
        int x = (WIDTH - w) / 2;
        int y = 140;

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(x, y, w, h, 20, 20);

        Record best = RecordManager.getBestRecord();

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.drawString("GAME OVER", x + 70, y + 40);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Score: " + model.getScore(), x + 40, y + 90);
        g2.drawString("Time: " + model.getTimeSurvived(), x + 40, y + 115);

        g2.drawString("Best: " + best.getScore(), x + 40, y + 150);

        // BUTTONS
        recordBtn = new Rectangle(x + 40, y + 180, 220, 35);
        homeBtn = new Rectangle(x + 40, y + 225, 220, 35);

        drawButton(g2, recordBtn, "VIEW RECORDS");
        drawButton(g2, homeBtn, "BACK TO MENU");
    }

    private void drawButton(Graphics2D g2, Rectangle r, String text) {

        g2.setColor(new Color(230, 230, 230));
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);

        g2.setColor(new Color(120, 120, 120));
        g2.drawRoundRect(r.x, r.y, r.width, r.height, 12, 12);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 13));

        FontMetrics fm = g2.getFontMetrics();

        g2.drawString(text,
                r.x + (r.width - fm.stringWidth(text)) / 2,
                r.y + 23);
    }
}