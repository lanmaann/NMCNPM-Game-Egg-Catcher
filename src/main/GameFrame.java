package main;

import model.*;
import view.*;
import controller.*;
import util.SoundManager; // Thêm import này để giải phóng âm thanh

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        
        // THAY ĐỔI: Không dùng EXIT_ON_CLOSE trực tiếp nữa để ta có thể chặn lại và hỏi người chơi
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //1.5.1: Người chơi nhấn nút thoát
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGame(); 
            }
        });

        //  cho phép co giãn
        setResizable(true);

        // size khởi tạo (mobile ratio)
        setSize(600, 1000);
        setMinimumSize(new Dimension(320, 560));

        setContentPane(container);

        initScreens();
        SoundManager.playBackgroundMusic(
                "/resources/music/bgm.wav"
        );

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
                this::exitGame // Giữ nguyên callback cũ, nút EXIT ở Menu sẽ gọi vào đây
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

        container.add(gameView, "game"); //  chỉ game mới add động
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
    // EXIT GAME 
    // =========================================================
    private void exitGame() {
        // 1.5.2: Hệ thống hiển thị thông báo xác nhận đóng
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn thoát trò chơi hoàn toàn?",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // --- LUỒNG CHÍNH (Basic Flow) ---
        if (confirm == JOptionPane.YES_OPTION) {
            
            // 1.5.3: Hệ thống thực hiện giải phóng tài nguyên (Memory cleanup)
            System.out.println("Executing memory cleanup...");
            stopController(); // Dừng luồng game thread hiện tại (nếu có)
            SoundManager.stopMusic(); // Dừng nhạc nền để giải phóng tài nguyên âm thanh
            
            if (model != null) {
                model = null; // Huỷ bỏ vùng nhớ của model cũ
            }

            // 1.5.4: Hệ thống đóng cửa sổ ứng dụng và kết thúc tiến trình
            this.dispose(); // Đóng cửa sổ GameFrame
            System.exit(0); // Kết thúc hoàn toàn tiến trình ứng dụng Java
        } 
        
        // --- LUỒNG THAY THẾ (Alternative Flow: 1.5.2a) ---
        else {
            // 1.5.2a.1 -> 1.5.2a.3: Người chơi bấm "No" hoặc tắt popup. 
            // Hệ thống tự đóng cửa sổ thông báo và người chơi ở lại màn hình hiện tại (không có thay đổi gì xảy ra)
            System.out.println("Hủy bỏ lệnh thoát. Tiếp tục ở lại ứng dụng.");
            
            // Nếu người chơi đang trong trận đấu, lấy lại focus cho phím điều khiển
            if (gameView != null && gameView.isShowing()) {
                gameView.requestFocusInWindow();
            }
        }
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