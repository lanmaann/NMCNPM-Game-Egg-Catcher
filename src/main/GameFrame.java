package main;

import model.*;
import view.*;
import controller.*;

import javax.swing.*;
import java.awt.*;

/**
 * =========================================================
 * GAME FRAME - RESPONSIVE SCREEN MANAGER
 * =========================================================
 */
public class GameFrame extends JFrame {

    private GameController controller;

    private final CardLayout layout = new CardLayout();
    private final JPanel container = new JPanel(layout);

    private MainMenuPanel menuPanel;
    private GameView gameView;
    private LeaderboardPanel leaderboardPanel;

    private GameModel model;

    public GameFrame() {

        setTitle("Egg Catcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 🔥 cho phép co giãn
        setResizable(true);

        // 🔥 size khởi tạo (mobile ratio)
        setSize(600, 1000);
        setMinimumSize(new Dimension(320, 560));

        setContentPane(container);

        initScreens();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // =========================================================
    // INIT ALL SCREENS (KHÔNG add lại nhiều lần)
    // =========================================================
    private void initScreens() {

        menuPanel = new MainMenuPanel(
                this::startGame,
                this::showLeaderboard,
                this::exitGame
        );

        leaderboardPanel = new LeaderboardPanel(this::backToMenu);

        container.add(menuPanel, "menu");
        container.add(leaderboardPanel, "leaderboard");

        layout.show(container, "menu");
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
                this::showLeaderboardFromGame
        );

        controller = new GameController(model, gameView);
        controller.start();

        container.add(gameView, "game"); // ⚠ chỉ game mới add động
        layout.show(container, "game");

        refreshUI();

        SwingUtilities.invokeLater(() -> gameView.requestFocusInWindow());
    }

    // =========================================================
    // LEADERBOARD
    // =========================================================
    private void showLeaderboard() {

        stopController();

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
    // STOP CONTROLLER
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
    // REFRESH UI
    // =========================================================
    private void refreshUI() {
        container.revalidate();
        container.repaint();
    }
    
    private void showLeaderboardFromGame() {

        stopController();

        leaderboardPanel = new LeaderboardPanel(this::backToMenu);

        container.add(leaderboardPanel, "leaderboard");
        layout.show(container, "leaderboard");

        refreshUI();
    }

    // =========================================================
    // MAIN
    // =========================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}