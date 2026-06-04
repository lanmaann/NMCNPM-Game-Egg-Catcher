package controller;

import model.*;
import view.GameView;
import util.SoundManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {

    private GameModel model;
    private final GameView view;

    private Thread gameThread;
    private volatile boolean running = false;

    private KeyAdapter keyHandler;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        initInput();
    }

    // =========================
    // START
    // =========================
    public void start() {

        if (running) return;

        running = true;
        SoundManager.playBackgroundMusic("/resources/music/bgm.wav");

        /* UC 1.1 - Bước 1.1.4: Hệ thống kích hoạt bộ đếm thời gian (Game Loop chạy bằng Thread với tần suất 60 FPS) */
        gameThread = new Thread(() -> {

            final int FPS = 60;
            final int frameTime = 1000 / FPS;

            while (running) {

                long start = System.currentTimeMillis();

                if (!model.isPaused() && !model.isGameOver()) {
                    /* Cập nhật logic các đối tượng chuyển động và rơi tự do */
                    model.update();
                }

                /* Vẽ lại toàn bộ giao diện trò chơi ở trạng thái PLAYING */
                view.repaint();

                long sleep = frameTime - (System.currentTimeMillis() - start);

                if (sleep > 0) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });

        gameThread.start();
    }

    // =========================
    // STOP
    // =========================
    public void stop() {
        running = false;

        if (gameThread != null) {
            gameThread.interrupt();
            gameThread = null;
        }
    }

    // =========================
    // INPUT + SOUND
    // =========================
    private void initInput() {

        view.setFocusable(true);

        if (keyHandler != null) {
            view.removeKeyListener(keyHandler);
        }

        keyHandler = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (model.isGameOver()) return;

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                        model.getPlayer().moveLeft();
                        SoundManager.playMove();
                        break;

                    case KeyEvent.VK_RIGHT:
                        model.getPlayer().moveRight();
                        SoundManager.playMove();
                        break;
                }
            }
        };

        view.addKeyListener(keyHandler);
    }
}