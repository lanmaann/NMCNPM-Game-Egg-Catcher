package model;

import java.awt.*;
import util.ImageLoader;

/**
 * =========================================================
 * BOMB OBJECT
 * =========================================================
 * lớp đại diện cho bom trong game
 * 
 * chức năng:
 * - hiển thị bom rơi
 * - phát animation nổ
 * - xử lý khi bom bị hứng hoặc rơi
 * =========================================================
 */
public class Bomb extends FallingObject {

    // danh sách frame animation bom
    private Image[] frames;

    // animation bom nổ
    private SpriteAnimation anim;

    /**
     * constructor khởi tạo bom theo lane
     */
    public Bomb(int lane) {

        super(lane);

        // load các frame animation
        frames = new Image[]{
                ImageLoader.load("/resources/images/bomb1.png"),
                ImageLoader.load("/resources/images/bomb2.png"),
                ImageLoader.load("/resources/images/bomb3.png"),
                ImageLoader.load("/resources/images/bomb4.png"),
                ImageLoader.load("/resources/images/bomb5.png")
        };

        // tạo animation
        anim = new SpriteAnimation(frames, 10);
    }

    // =========================================================
    // ON CATCH
    // =========================================================

    /**
     * xử lý khi người chơi hứng trúng bom
     */
    @Override
    public void onCatch() {

        // reset animation từ đầu
        anim.reset();
    }

    // =========================================================
    // ON MISS
    // =========================================================

    /**
     * xử lý khi bom rơi xuống đất
     */
    @Override
    public void onMiss() {

        // reset animation
        anim.reset();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    /**
     * cập nhật animation bom
     */
    @Override
    public void update(GameModel model) {

        // update khi đang animation
        if (state == State.ANIMATING) {

            anim.update();

            // animation kết thúc
            if (anim.isFinished()) {

                finish();
            }
        }
    }

    // =========================================================
    // DRAW
    // =========================================================

    /**
     * vẽ bom lên màn hình
     */
    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

        // bomb có kích thước lớn hơn egg
        int size = (int)(laneWidth * 0.6f);

        int x = centerX - size / 2;

        int yDraw = (int) y;

        // vẽ animation khi đang nổ
        if (state == State.ANIMATING) {

            g.drawImage(anim.getCurrentFrame(), x, yDraw, size, size, null);
        }

        // vẽ frame mặc định
        else {

            g.drawImage(frames[0], x, yDraw, size, size, null);
        }
    }
}