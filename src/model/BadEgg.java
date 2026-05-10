package model;

import java.awt.*;
import util.ImageLoader;

/**
 * Lớp đại diện cho Bad Egg trong trò chơi.
 * 
 * Chức năng:
 * - Hiển thị trứng xấu rơi xuống
 * - Phát animation vỡ khi chạm đất
 * - Kết thúc object khi animation hoàn tất
 * - Không phát animation khi người chơi hứng được
 */
public class BadEgg extends FallingObject {

    /**
     * Animation vỡ của trứng.
     */
    private SpriteAnimation breakAnim;

    /**
     * Hình ảnh mặc định của trứng.
     */
    private Image idle;

    /**
     * Trạng thái trứng có đang vỡ hay không.
     */
    private boolean broken = false;

    /**
     * Constructor khởi tạo Bad Egg theo lane.
     * 
     * @param lane lane xuất hiện
     */
    public BadEgg(int lane) {

        super(lane);

        /**
         * Load hình ảnh mặc định.
         */
        idle = ImageLoader.load(
                "/resources/images/badEgg1.png"
        );

        /**
         * Load các frame animation vỡ.
         */
        Image[] frames = new Image[]{

                ImageLoader.load(
                        "/resources/images/badEgg2.png"
                ),

                ImageLoader.load(
                        "/resources/images/badEgg3.png"
                ),

                ImageLoader.load(
                        "/resources/images/badEgg4.png"
                )
        };

        /**
         * Khởi tạo animation.
         */
        breakAnim =
                new SpriteAnimation(frames, 6);
    }

    /**
     * Xử lý khi người chơi hứng được trứng.
     * 
     * Bad Egg sẽ:
     * - Không phát animation vỡ
     * - Kết thúc object ngay lập tức
     */
    @Override
    public void onCatch() {

        // Không bị vỡ
        broken = false;

        // Reset animation tránh lỗi frame cũ
        breakAnim.reset();

        // Kết thúc object
        finish();
    }

    /**
     * Xử lý khi trứng rơi xuống đất.
     * 
     * Bad Egg sẽ:
     * - Chuyển sang trạng thái vỡ
     * - Phát animation break
     */
    @Override
    public void onMiss() {

        // Trứng bị vỡ
        broken = true;

        // Reset animation
        breakAnim.reset();
    }

    /**
     * Cập nhật trạng thái object.
     * 
     * @param model model game
     */
    @Override
    public void update(GameModel model) {

        /**
         * Chỉ update animation khi:
         * - Object đang ở trạng thái animation
         * - Trứng đang bị vỡ
         */
        if (
                state == State.ANIMATING
                        &&
                        broken
        ) {

            // Update animation
            breakAnim.update();

            /**
             * Animation kết thúc
             * thì xóa object.
             */
            if (breakAnim.isFinished()) {

                finish();
            }
        }
    }

    /**
     * Vẽ Bad Egg lên màn hình.
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

        int size =
                (int) (laneWidth * 0.5f);

        int x =
                centerX - size / 2;

        int yDraw =
                (int) y;

        /**
         * Vẽ animation khi trứng bị vỡ.
         */
        if (
                state == State.ANIMATING
                        &&
                        broken
        ) {

            g.drawImage(
                    breakAnim.getCurrentFrame(),
                    x,
                    yDraw,
                    size,
                    size,
                    null
            );
        }

        /**
         * Vẽ hình mặc định.
         */
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