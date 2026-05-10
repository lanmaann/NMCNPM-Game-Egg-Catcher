package model;

import java.awt.*;
import util.ImageLoader;

/**
 * =========================================================
 * EGG OBJECT
 * =========================================================
 * lớp đại diện cho trứng thường trong game
 * 
 * chức năng:
 * - hiển thị trứng rơi
 * - phát animation vỡ khi rơi xuống đất
 * - xử lý trạng thái hứng trúng hoặc hứng hụt
 * =========================================================
 */
public class Egg extends FallingObject {

    // animation trứng vỡ
    private SpriteAnimation breakAnim;

    // hình ảnh mặc định của trứng
    private Image idle;

    // trạng thái trứng có bị vỡ hay không
    private boolean broken = false;

    /**
     * constructor khởi tạo egg theo lane
     */
    public Egg(int lane) {

        super(lane);

        // load ảnh mặc định
        idle = ImageLoader.load("/resources/images/egg.png");

        // load các frame animation vỡ
        Image[] frames = new Image[]{
                ImageLoader.load("/resources/images/eggBreak1.png"),
                ImageLoader.load("/resources/images/eggBreak2.png"),
                ImageLoader.load("/resources/images/eggBreak3.png")
        };

        // tạo animation
        breakAnim = new SpriteAnimation(frames, 8);
    }

    // =========================================================
    // ON CATCH
    // =========================================================

    /**
     * xử lý khi người chơi hứng được trứng
     */
    @Override
    public void onCatch() {

        // 👉 hứng thành công thì không vỡ
        broken = false;

        // kết thúc object
        finish();
    }

    // =========================================================
    // ON MISS
    // =========================================================

    /**
     * xử lý khi trứng rơi xuống đất
     */
    @Override
    public void onMiss() {

        // 👉 trứng bị vỡ
        broken = true;

        // reset animation
        breakAnim.reset();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    /**
     * cập nhật animation của egg
     */
    @Override
    public void update(GameModel model) {

        // chỉ update khi đang animation và bị vỡ
        if (state == State.ANIMATING && broken) {

            breakAnim.update();

            // animation kết thúc
            if (breakAnim.isFinished()) {

                finish();
            }
        }
    }

    // =========================================================
    // DRAW
    // =========================================================

    /**
     * vẽ egg lên màn hình
     */
    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

        // scale kích thước theo lane
        int size = (int)(laneWidth * 0.5f);

        int x = centerX - size / 2;

        int yDraw = (int) y;

        // vẽ animation khi trứng bị vỡ
        if (state == State.ANIMATING && broken) {

            g.drawImage(
                    breakAnim.getCurrentFrame(),
                    x,
                    yDraw,
                    size,
                    size,
                    null
            );
        }

        // vẽ ảnh mặc định
        else {

            g.drawImage(
                    idle,
                    x,
                    yDraw,
                    size,
                    size,
                    null
            );
        }
    }
}