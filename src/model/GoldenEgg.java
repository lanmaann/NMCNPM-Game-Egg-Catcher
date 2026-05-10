package model;

import java.awt.*;
import util.ImageLoader;

/**
 * =========================================================
 * GOLDEN EGG OBJECT
 * =========================================================
 * lớp đại diện cho trứng vàng trong game
 * 
 * chức năng:
 * - hiển thị trứng vàng đặc biệt
 * - cộng nhiều điểm hơn egg thường
 * - tự biến mất sau một khoảng delay ngắn
 * =========================================================
 */
public class GoldenEgg extends FallingObject {

    // hình ảnh trứng vàng
    private Image img;

    // bộ đếm delay trước khi biến mất
    private int timer = 0;

    /**
     * constructor khởi tạo golden egg theo lane
     */
    public GoldenEgg(int lane) {

        super(lane);

        // load ảnh trứng vàng
        img = ImageLoader.load("/resources/images/goldenEgg.png");
    }

    // =========================================================
    // ON CATCH
    // =========================================================

    /**
     * xử lý khi người chơi hứng được trứng vàng
     */
    @Override
    public void onCatch() {

        // delay nhẹ trước khi biến mất
        timer = 10;
    }

    // =========================================================
    // ON MISS
    // =========================================================

    /**
     * xử lý khi trứng vàng rơi hụt
     */
    @Override
    public void onMiss() {

        // delay nhẹ trước khi biến mất
        timer = 10;
    }

    // =========================================================
    // UPDATE
    // =========================================================

    /**
     * cập nhật trạng thái golden egg
     */
    @Override
    public void update(GameModel model) {

        // giảm timer
        if (timer > 0) {

            timer--;

            // timer kết thúc
            if (timer == 0) {

                finish();
            }
        }
    }

    // =========================================================
    // DRAW
    // =========================================================

    /**
     * vẽ golden egg lên màn hình
     */
    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

        // scale kích thước theo lane
        int size = (int)(laneWidth * 0.5f);

        int x = centerX - size / 2;

        int yDraw = (int) y;

        // vẽ hình trứng vàng
        g.drawImage(
                img,
                x,
                yDraw,
                size,
                size,
                null
        );
    }
}