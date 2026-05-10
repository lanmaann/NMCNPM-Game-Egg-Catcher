package model;

import java.awt.*;
import util.ImageLoader;

/**
 * =========================================================
 * PLAYER
 * =========================================================
 * lớp đại diện cho người chơi trong game
 * 
 * chức năng:
 * - quản lý vị trí player
 * - xử lý di chuyển trái/phải
 * - hiển thị basket trên màn hình
 * =========================================================
 */
public class Player {

    // lane hiện tại của player
    private int lane;

    // lane tối đa
    private int maxLane;

    // hình ảnh basket
    private Image img;

    // tỉ lệ kích thước player theo lane
    private float sizeRatio = 0.7f;

    // tỉ lệ vị trí Y theo chiều cao màn hình
    private float yRatio = 0.85f;

    /**
     * constructor khởi tạo player
     */
    public Player(int laneCount) {

        // player bắt đầu ở giữa
        this.lane = laneCount / 2;

        // lane cuối cùng
        this.maxLane = laneCount - 1;

        // load ảnh basket
        img = ImageLoader.load("/resources/images/basket.png");
    }

    // =========================================================
    // MOVE LEFT
    // =========================================================

    /**
     * di chuyển player sang trái
     */
    public void moveLeft() {

        // không vượt quá biên trái
        if (lane > 0) {

            lane--;
        }
    }

    // =========================================================
    // MOVE RIGHT
    // =========================================================

    /**
     * di chuyển player sang phải
     */
    public void moveRight() {

        // không vượt quá biên phải
        if (lane < maxLane) {

            lane++;
        }
    }

    // =========================================================
    // GETTER
    // =========================================================

    /**
     * lấy lane hiện tại
     */
    public int getLane() {

        return lane;
    }

    /**
     * lấy vị trí Y của player
     */
    public int getY(int screenHeight) {

        return (int)(screenHeight * yRatio);
    }

    // =========================================================
    // SETTER
    // =========================================================

    /**
     * set lane cho player
     */
    public void setLane(int lane) {

        // kiểm tra lane hợp lệ
        if (lane >= 0 && lane <= maxLane) {

            this.lane = lane;
        }
    }

    // =========================================================
    // DRAW
    // =========================================================

    /**
     * vẽ player lên màn hình
     */
    public void draw(Graphics g, int centerX, int laneWidth, int screenHeight) {

        // scale kích thước theo lane
        int size = (int)(laneWidth * sizeRatio);

        int x = centerX - size / 2;

        int y = getY(screenHeight);

        // vẽ basket
        g.drawImage(
                img,
                x,
                y,
                size,
                size,
                null
        );
    }
}