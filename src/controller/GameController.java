package controller;

import model.*;
import view.*;

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
    // START LOOP
    // =========================
    public void start() {
        if (running) return;

        running = true;

        gameThread = new Thread(() -> {

            final int FPS = 60;
            final int frameTime = 1000 / FPS;

            while (running) {

                long start = System.currentTimeMillis();

                if (!model.isPaused() && !model.isGameOver()) {
                    model.update();
                }

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
    // STOP LOOP
    // =========================
    public void stop() {
        running = false;

        if (gameThread != null) {
            gameThread.interrupt();
            gameThread = null;
        }
    }

    // =========================
    // INPUT
    // =========================
    private void initInput() {

        view.setFocusable(true);
        view.requestFocusInWindow();

        if (keyHandler != null) {
            view.removeKeyListener(keyHandler);
        }

        keyHandler = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                        model.getPlayer().moveLeft();
                        break;

                    case KeyEvent.VK_RIGHT:
                        model.getPlayer().moveRight();
                        break;

                    case KeyEvent.VK_P:
                        model.togglePause();
                        break;

                    case KeyEvent.VK_R:
                        if (model.isGameOver()) {
                            restart();
                        }
                        break;
                }
            }
        };

        view.addKeyListener(keyHandler);
    }

    // =========================
    // RESTART
    // =========================
    private void restart() {
        stop();

        model = new GameModel();
        view.setModel(model);

        initInput();
        start();
    }
}