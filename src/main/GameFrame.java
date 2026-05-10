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
 * lớp chính quản lý toàn bộ giao diện game
 * 
 * chức năng:
 * - quản lý chuyển đổi giữa các màn hình
 * - khởi tạo game
 * - điều khiển controller
 * - quản lý responsive window
 * =========================================================
 */
public class GameFrame extends JFrame {

    // controller điều khiển game loop
    private GameController controller;

    // layout dùng để chuyển màn hình
    private final CardLayout layout = new CardLayout();

    // panel chứa toàn bộ screen
    private final JPanel container = new JPanel(layout);

    // các màn hình chính
    private MainMenuPanel menuPanel;
    private GameView gameView;
    private LeaderboardPanel leaderboardPanel;

    // model game
    private GameModel model;

    /**
     * constructor khởi tạo cửa sổ game
     */
    public GameFrame() {

        setTitle("Egg Catcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 🔥 cho phép resize cửa sổ
        setResizable(true);

        // 🔥 kích thước mặc định
        setSize(600, 1000);

        // 🔥 kích thước tối thiểu
        setMinimumSize(new Dimension(320, 560));

        // set panel chính
        setContentPane(container);

        // khởi tạo các màn hình
        initScreens();

        // căn giữa màn hình
        setLocationRelativeTo(null);

        // hiển thị window
        setVisible(true);
    }

    // =========================================================
    // INIT ALL SCREENS
    // =========================================================

    /**
     * khởi tạo các màn hình chính
     */
    private void initScreens() {

        // màn hình menu
        menuPanel = new MainMenuPanel(
                this::startGame,
                this::showLeaderboard,
                this::exitGame
        );

        // màn hình leaderboard
        leaderboardPanel = new LeaderboardPanel(this::backToMenu);

        // add vào container
        container.add(menuPanel, "menu");
        container.add(leaderboardPanel, "leaderboard");

        // hiển thị menu đầu tiên
        layout.show(container, "menu");
    }

    // =========================================================
    // START GAME
    // =========================================================

    /**
     * bắt đầu game mới
     */
    private void startGame() {

        // dừng controller cũ nếu có
        stopController();

        // tạo model mới
        model = new GameModel();

        // tạo game view
        gameView = new GameView(
                model,
                this::backToMenu,
                this::showLeaderboardFromGame
        );

        // tạo controller
        controller = new GameController(model, gameView);

        // bắt đầu game loop
        controller.start();

        // add game screen
        container.add(gameView, "game");

        // chuyển sang màn hình game
        layout.show(container, "game");

        refreshUI();

        // focus để nhận keyboard input
        SwingUtilities.invokeLater(() -> gameView.requestFocusInWindow());
    }

    // =========================================================
    // SHOW LEADERBOARD
    // =========================================================

    /**
     * hiển thị bảng xếp hạng
     */
    private void showLeaderboard() {

        stopController();

        layout.show(container, "leaderboard");

        refreshUI();
    }

    // =========================================================
    // BACK TO MENU
    // =========================================================

    /**
     * quay lại menu chính
     */
    private void backToMenu() {

        stopController();

        layout.show(container, "menu");

        refreshUI();
    }

    // =========================================================
    // STOP CONTROLLER
    // =========================================================

    /**
     * dừng game controller
     */
    private void stopController() {

        if (controller != null) {

            controller.stop();

            controller = null;
        }
    }

    // =========================================================
    // EXIT GAME
    // =========================================================

    /**
     * thoát game
     */
    private void exitGame() {
        System.exit(0);
    }

    // =========================================================
    // REFRESH UI
    // =========================================================

    /**
     * refresh giao diện
     */
    private void refreshUI() {

        container.revalidate();

        container.repaint();
    }

    // =========================================================
    // SHOW LEADERBOARD FROM GAME
    // =========================================================

    /**
     * hiển thị leaderboard sau khi chơi
     */
    private void showLeaderboardFromGame() {

        stopController();

        leaderboardPanel = new LeaderboardPanel(this::backToMenu);

        container.add(leaderboardPanel, "leaderboard");

        layout.show(container, "leaderboard");

        refreshUI();
    }

    // =========================================================
    // MAIN METHOD
    // =========================================================

    /**
     * hàm main chạy chương trình
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(GameFrame::new);
    }
}