package model;

import java.awt.*;
import util.ImageLoader;

/**
 * Lớp đại diện cho Bomb trong trò chơi.
 * 
 * Chức năng:
 * - Hiển thị bom rơi xuống
 * - Phát animation bom nổ
 * - Xử lý khi bom bị hứng hoặc rơi
 * - Kết thúc object sau animation
 */
public class Bomb extends FallingObject {

    /**
     * Danh sách frame animation bom.
     */
    private Image[] frames;

    /**
     * Animation bom nổ.
     */
    private SpriteAnimation anim;

    /**
     * Constructor khởi tạo Bomb theo lane.
     * 
     * @param lane lane xuất hiện
     */
    public Bomb(int lane) {

        super(lane);

        /**
         * Load các frame animation.
         */
        frames = new Image[]{

                ImageLoader.load(
                        "/resources/images/bomb1.png"
                ),

                ImageLoader.load(
                        "/resources/images/bomb2.png"
                ),

                ImageLoader.load(
                        "/resources/images/bomb3.png"
                ),

                ImageLoader.load(
                        "/resources/images/bomb4.png"
                ),

                ImageLoader.load(
                        "/resources/images/bomb5.png"
                )
        };

        /**
         * Khởi tạo animation bom nổ.
         */
        anim =
                new SpriteAnimation(frames, 10);
    }

    /**
     * Xử lý khi người chơi hứng trúng bom.
     * 
     * Animation sẽ được reset
     * để phát lại từ đầu.
     */
    @Override
    public void onCatch() {

        // Reset animation
        anim.reset();
    }

    /**
     * Xử lý khi bom rơi xuống đất.
     * 
     * Đồng bộ hành vi animation.
     */
    @Override
    public void onMiss() {

        // Reset animation
        anim.reset();
    }

    /**
     * Cập nhật trạng thái bom.
     * 
     * @param model model game
     */
    @Override
    public void update(GameModel model) {

        /**
         * Chỉ update khi
         * object đang animation.
         */
        if (state == State.ANIMATING) {

            // Update animation
            anim.update();

            /**
             * Kết thúc object
             * sau khi animation hoàn tất.
             */
            if (anim.isFinished()) {

                finish();
            }
        }
    }

    /**
     * Vẽ Bomb lên màn hình.
     * 
     * @param g graphics
     * @param centerX vị trí giữa lane
     * @param laneWidth chiều rộng lane
     */
    @Override
    public void draw(
            Graphics g,
            int centerX,
            int laneWidth
    ) {

        /**
         * Bomb lớn hơn Egg.
         */
        int size =
                (int) (laneWidth * 0.6f);

        int x =
                centerX - size / 2;

        int yDraw =
                (int) y;

        /**
         * Vẽ animation khi bom nổ.
         */
        if (state == State.ANIMATING) {

            g.drawImage(
                    anim.getCurrentFrame(),
                    x,
                    yDraw,
                    size,
                    size,
                    null
            );
        }

        /**
         * Vẽ frame mặc định.
         */
        else {

            g.drawImage(
                    frames[0],
                    x,
                    yDraw,
                    size,
                    size,
                    null
            );
        }
    }
}