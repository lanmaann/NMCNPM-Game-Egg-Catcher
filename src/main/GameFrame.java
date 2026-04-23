package main;

import model.*;
import view.*;
import controller.*;

import javax.swing.*;
import java.awt.*;

/**
 * =========================================================
 * GAME FRAME - CLEAN SCREEN MANAGER
 * =========================================================
 */
public class GameFrame extends JFrame {

    // =========================
    // CONTROLLER
    // =========================
    private GameController controller;

    // =========================
    // LAYOUT
    // =========================
    private final CardLayout layout = new CardLayout();
    private final JPanel container = new JPanel(layout);

    // =========================
    // SCREENS
    // =========================
    private MainMenuPanel menuPanel;
    private GameView gameView;
    private LeaderboardPanel leaderboardPanel;

    // =========================
    // MODEL
    // =========================
    private GameModel model;

    public GameFrame() {

        setTitle("Egg Catcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        container.setPreferredSize(new Dimension(400, 600));
        setContentPane(container);

        initMenu();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // =========================================================
    // MENU
    // =========================================================
    private void initMenu() {

        menuPanel = new MainMenuPanel(
                this::startGame,
                this::showLeaderboard,
                this::exitGame
        );

        container.add(menuPanel, "menu");
        layout.show(container, "menu");

        refreshUI();
    }

    // =========================================================
    // START GAME
    // =========================================================
    private void startGame() {

        stopController();

        model = new GameModel();

        gameView = new GameView(
                model,
                this::backToMenu,
                this::showLeaderboard
        );

        controller = new GameController(model, gameView);
        controller.start();

        container.add(gameView, "game");
        layout.show(container, "game");

        refreshUI();

        // IMPORTANT: focus fix
        SwingUtilities.invokeLater(() -> {
            gameView.requestFocusInWindow();
        });
    }

    // =========================================================
    // LEADERBOARD
    // =========================================================
    private void showLeaderboard() {

        stopController();

        leaderboardPanel = new LeaderboardPanel(this::backToMenu);

        container.add(leaderboardPanel, "leaderboard");
        layout.show(container, "leaderboard");

        refreshUI();
    }

    // =========================================================
    // BACK TO MENU
    // =========================================================
    private void backToMenu() {

        stopController();

        layout.show(container, "menu");

        refreshUI();
    }

    // =========================================================
    // STOP GAME
    // =========================================================
    private void stopController() {
        if (controller != null) {
            controller.stop();
            controller = null;
        }
    }

    // =========================================================
    // EXIT
    // =========================================================
    private void exitGame() {
        System.exit(0);
    }

    // =========================================================
    // UI REFRESH FIX
    // =========================================================
    private void refreshUI() {
        container.revalidate();
        container.repaint();
    }

    // =========================================================
    // MAIN
    // =========================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}