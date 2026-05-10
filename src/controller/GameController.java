package controller;

import model.*;
import view.GameView;
import util.SoundManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * =========================================================
 * GAME CONTROLLER
 * =========================================================
 * lớp điều khiển chính của game
 * 
 * chức năng:
 * - xử lý game loop
 * - xử lý input bàn phím
 * - cập nhật model
 * - điều khiển restart game
 * - phát âm thanh
 * =========================================================
 */
public class GameController {

    // model quản lý dữ liệu game
    private GameModel model;

    // view hiển thị giao diện
    private final GameView view;

    // thread chạy game loop
    private Thread gameThread;

    // trạng thái game loop
    private volatile boolean running = false;

    // xử lý bàn phím
    private KeyAdapter keyHandler;

    /**
     * constructor khởi tạo controller
     */
    public GameController(GameModel model, GameView view) {

        this.model = model;
        this.view = view;

        // khởi tạo input
        initInput();
    }

    // =========================================================
    // START GAME LOOP
    // =========================================================

    /**
     * bắt đầu game loop
     */
    public void start() {

        // tránh chạy nhiều thread
        if (running) return;

        running = true;

        gameThread = new Thread(() -> {

            // FPS mục tiêu
            final int FPS = 60;

            // thời gian mỗi frame
            final int frameTime = 1000 / FPS;

            // game loop
            while (running) {

                long start = System.currentTimeMillis();

                // update game khi không pause hoặc game over
                if (!model.isPaused() && !model.isGameOver()) {
                    model.update();
                }

                // vẽ lại màn hình
                view.repaint();

                // tính thời gian sleep
                long sleep = frameTime - (System.currentTimeMillis() - start);

                // delay để giữ FPS ổn định
                if (sleep > 0) {

                    try {
                        Thread.sleep(sleep);

                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });

        // chạy thread
        gameThread.start();
    }

    // =========================================================
    // STOP GAME LOOP
    // =========================================================

    /**
     * dừng game loop
     */
    public void stop() {

        running = false;

        if (gameThread != null) {

            gameThread.interrupt();

            gameThread = null;
        }
    }

    // =========================================================
    // INPUT + SOUND
    // =========================================================

    /**
     * khởi tạo xử lý input bàn phím
     */
    private void initInput() {

        // cho phép nhận focus
        view.setFocusable(true);

        // tránh add listener nhiều lần
        if (keyHandler != null) {
            view.removeKeyListener(keyHandler);
        }

        // tạo key listener mới
        keyHandler = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                // không xử lý khi game over
                if (model.isGameOver()) return;

                switch (e.getKeyCode()) {

                    // di chuyển trái
                    case KeyEvent.VK_LEFT:
                        model.getPlayer().moveLeft();
                        SoundManager.play("/resources/sound/move.wav");
                        break;

                    // di chuyển phải
                    case KeyEvent.VK_RIGHT:
                        model.getPlayer().moveRight();
                        SoundManager.play("/resources/sound/move.wav");
                        break;

                    // pause game
                    case KeyEvent.VK_P:
                        model.togglePause();
                        SoundManager.play("/resources/sound/click.wav");
                        break;

                    // restart game
                    case KeyEvent.VK_R:
                        restart();
                        break;
                }
            }
        };

        // add listener vào view
        view.addKeyListener(keyHandler);

        // focus để nhận input
        view.requestFocusInWindow();
    }

    // =========================================================
    // RESTART GAME
    // =========================================================

    /**
     * restart game an toàn
     */
    public void restart() {

        // dừng loop hiện tại
        stop();

        // reset model
        model.reset();

        // phát âm thanh restart
        SoundManager.play("/resources/sound/restart.wav");

        // khởi tạo input lại
        initInput();

        // chạy lại game loop
        start();
    }
}