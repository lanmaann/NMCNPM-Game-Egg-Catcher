package controller;

import model.*;
import view.GameView;
import util.SoundManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Game Controller
 * Lớp điều khiển chính của game Egg Catcher.
 * * Chức năng:
 * - Quản lý game loop (vòng lặp trò chơi)
 * - Xử lý input từ bàn phím người chơi
 * - Điều khiển cơ chế restart game
 * - Cập nhật dữ liệu cho Model
 * - Phát tín hiệu âm thanh hệ thống
 */
public class GameController {
	
	/** Model quản lý dữ liệu và logic game */
	private GameModel model;

	/** View hiển thị giao diện game */
	private final GameView view;

	/** Thread chạy game loop độc lập */
	private Thread gameThread;

	/** Trạng thái kích hoạt của game loop */
	private volatile boolean running = false;

	/** Bộ xử lý sự kiện input bàn phím */
	private KeyAdapter keyHandler;

	/**
	 * Khởi tạo Game Controller
	 *
	 * @param model model game
	 * @param view  giao diện game
	 */
	public GameController(GameModel model, GameView view) {
		this.model = model;
		this.view = view;

		// Khởi tạo hệ thống nhận diện phím bấm
		initInput();
	}

	/** Bắt đầu vòng lặp Game Loop */
	public void start() {
		// Tránh việc khởi chạy trùng lặp nhiều Thread cùng lúc
		if (running) {
			return;
		}

		running = true;

		// Phát nhạc nền hệ thống khi vào trận
		SoundManager.playBackgroundMusic("/resources/music/bgm.wav");

		/** Game Loop chính điều phối nhịp độ khung hình */
		gameThread = new Thread(() -> {
			// Thiết lập mức FPS mục tiêu ổn định ở 60
			final int FPS = 60;

			// Thời gian tiêu chuẩn quy định cho mỗi khung hình (ms)
			final int frameTime = 1000 / FPS;

			while (running) {
				long start = System.currentTimeMillis();

				// Chỉ cập nhật trạng thái logic khi trò chơi không bị tạm dừng hoặc kết thúc
				if (!model.isPaused() && !model.isGameOver()) {
					model.update();
				}

				// Ra lệnh vẽ lại toàn bộ giao diện đồ họa thế giới game
				view.repaint();

				// Tính toán thời gian nghỉ cần thiết để duy trì FPS ổn định
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

		// Chạy tiến trình game thread
		gameThread.start();
	}

	/** Dừng vòng lặp Game Loop */
	public void stop() {
		running = false;

		// Giải phóng và ngắt luồng thread đang chạy nền
		if (gameThread != null) {
			gameThread.interrupt();
			gameThread = null;
		}
	}

	/** Khởi tạo cơ chế bắt sự kiện input bàn phím */
	private void initInput() {
		// Cho phép thành phần View nhận tiêu điểm tương tác từ hệ thống
		view.setFocusable(true);

		/** Ngăn ngừa việc tích tụ/trùng lặp nhiều Listener khi người chơi bấm Restart */
		if (keyHandler != null) {
			view.removeKeyListener(keyHandler);
		}
		
		keyHandler = new KeyAdapter() {
			/** Xử lý sự kiện nhấn phím vật lý */
			@Override
			public void keyPressed(KeyEvent e) {
				// Đóng băng toàn bộ phím bấm điều khiển, không xử lý khi trò chơi đã kết thúc
				if (model.isGameOver()) {
					return;
				}

				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					// [2.1.3] Người chơi điều khiển nhân vật di chuyển sang trái theo trục X
					model.getPlayer().moveLeft();
					SoundManager.playMove();
					break;

				case KeyEvent.VK_RIGHT:
					// [2.1.3] Người chơi điều khiển nhân vật di chuyển sang phải theo trục X
					model.getPlayer().moveRight();
					SoundManager.playMove();
					break;

				case KeyEvent.VK_P:
					model.togglePause();
					SoundManager.playUI();

					// Trạng thái Pause hoạt động -> Tạm dừng nhạc nền game
					if (model.isPaused()) {
						SoundManager.stopMusic();
					}
					// Trạng thái Resume hoạt động -> Tiếp tục phát lại nhạc nền
					else {
						SoundManager.playBackgroundMusic("/resources/music/bgm.wav");
					}
					break;

				case KeyEvent.VK_R:
					restart();
					break;
				}
			}
		};

		// Đăng ký bộ lắng nghe phím bấm vào View
		view.addKeyListener(keyHandler);

		// Yêu cầu cửa sổ nhận quyền focus trực tiếp để đón nhận tín hiệu input hiệu quả nhất
		view.requestFocusInWindow();
	}

    /** Tái khởi động lại trò chơi an toàn */
	public void restart() {
		// Dừng vòng lặp game loop hiện tại để làm sạch tiến trình
		stop();

		// Làm mới và đặt lại toàn bộ các tham số dữ liệu trong Model
		model.reset();

		// Kích hoạt hiệu ứng âm thanh tái khởi động
		SoundManager.playRestart();

		// Làm mới bộ thu thập tín hiệu đầu vào từ thiết bị ngoại vi
		initInput();

		// Khởi chạy vòng lặp luồng game mới
		start();
	}
}
