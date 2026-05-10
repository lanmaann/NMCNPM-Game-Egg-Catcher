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
 * lớp điều khiển chính của game Egg Catcher
 *
 * chức năng:
 * - quản lý game loop
 * - xử lý input bàn phím
 * - điều khiển restart game
 * - cập nhật model
 * - phát âm thanh
 * =========================================================
 */
public class GameController {

    // =========================================================
    // MODEL + VIEW
    // =========================================================

    /**
     * model quản lý dữ liệu và logic game
     */
    private GameModel model;

    /**
     * view hiển thị giao diện game
     */
    private final GameView view;

    // =========================================================
    // GAME LOOP
    // =========================================================

    /**
     * thread chạy game loop
     */
    private Thread gameThread;

    /**
     * trạng thái game loop
     */
    private volatile boolean running = false;

    // =========================================================
    // INPUT
    // =========================================================

    /**
     * bộ xử lý input bàn phím
     */
    private KeyAdapter keyHandler;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    /**
     * khởi tạo game controller
     *
     * @param model model game
     * @param view giao diện game
     */
    public GameController(
            GameModel model,
            GameView view
    ) {

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
        if (running) {
            return;
        }

        running = true;

        // phát nhạc nền
        SoundManager.playBackgroundMusic(
                "/resources/music/bgm.wav"
        );

        /**
         * game loop chính
         */
        gameThread = new Thread(() -> {

            // FPS mục tiêu
            final int FPS = 60;

            // thời gian mỗi frame
            final int frameTime = 1000 / FPS;

            // loop game
            while (running) {

                long start =
                        System.currentTimeMillis();

                // chỉ update khi chưa pause/game over
                if (
                        !model.isPaused()
                                &&
                                !model.isGameOver()
                ) {

                    model.update();
                }

                // repaint giao diện
                view.repaint();

                // tính delay giữ FPS ổn định
                long sleep =
                        frameTime
                                -
                                (
                                        System.currentTimeMillis()
                                                - start
                                );

                if (sleep > 0) {

                    try {

                        Thread.sleep(sleep);

                    } catch (InterruptedException e) {

                        break;
                    }
                }
            }
        });

        // chạy game thread
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

        // interrupt thread
        if (gameThread != null) {

            gameThread.interrupt();

            gameThread = null;
        }
    }

    // =========================================================
    // INPUT
    // =========================================================

    /**
     * khởi tạo input bàn phím
     */
    private void initInput() {

        // cho phép focus
        view.setFocusable(true);

        /**
         * tránh add nhiều listener
         * khi restart
         */
        if (keyHandler != null) {

            view.removeKeyListener(keyHandler);
        }

        // =====================================================
        // KEY LISTENER
        // =====================================================

        keyHandler = new KeyAdapter() {

            /**
             * xử lý sự kiện nhấn phím
             */
            @Override
            public void keyPressed(KeyEvent e) {

                // không xử lý khi game over
                if (model.isGameOver()) {
                    return;
                }

                switch (e.getKeyCode()) {

                    // =================================================
                    // MOVE LEFT
                    // =================================================
                    case KeyEvent.VK_LEFT:

                        model.getPlayer().moveLeft();

                        SoundManager.playMove();

                        break;

                    // =================================================
                    // MOVE RIGHT
                    // =================================================
                    case KeyEvent.VK_RIGHT:

                        model.getPlayer().moveRight();

                        SoundManager.playMove();

                        break;

                    // =================================================
                    // PAUSE / RESUME
                    // =================================================
                    case KeyEvent.VK_P:

                        model.togglePause();

                        SoundManager.playUI();

                        // pause -> stop nhạc
                        if (model.isPaused()) {

                            SoundManager.stopMusic();
                        }

                        // resume -> phát lại nhạc
                        else {

                            SoundManager.playBackgroundMusic(
                                    "/resources/music/bgm.wav"
                            );
                        }

                        break;

                    // =================================================
                    // RESTART
                    // =================================================
                    case KeyEvent.VK_R:

                        restart();

                        break;
                }
            }
        };

        // add listener
        view.addKeyListener(keyHandler);

        // focus nhận input
        view.requestFocusInWindow();
    }

    // =========================================================
    // RESTART
    // =========================================================

    /**
     * restart game an toàn
     */
    public void restart() {

        // dừng game loop hiện tại
        stop();

        // reset dữ liệu game
        model.reset();

        // phát âm thanh restart
        SoundManager.playRestart();

        // reset input
        initInput();

        // chạy lại game
        start();
    }
}