package model;

import java.awt.*;
import util.ImageLoader;

/**
 * lớp đại diện cho trứng xấu trong game
 * 
 * bad egg sẽ:
 * - không bị vỡ khi người chơi hứng được
 * - phát animation vỡ khi rơi xuống đất
 */
public class BadEgg extends FallingObject {

    // animation khi trứng bị vỡ
    private SpriteAnimation breakAnim;

    // hình ảnh mặc định của trứng
    private Image idle;

    // trạng thái trứng có đang vỡ hay không
    private boolean broken = false;

    /**
     * khởi tạo bad egg theo lane
     */
    public BadEgg(int lane) {
        super(lane);

        // load ảnh mặc định
        idle = ImageLoader.load("/resources/images/badEgg1.png");

        // load các frame animation vỡ
        Image[] frames = new Image[]{
                ImageLoader.load("/resources/images/badEgg2.png"),
                ImageLoader.load("/resources/images/badEgg3.png"),
                ImageLoader.load("/resources/images/badEgg4.png")
        };

        // tạo animation
        breakAnim = new SpriteAnimation(frames, 6);
    }

    /**
     * xử lý khi người chơi hứng được trứng
     */
    @Override
    public void onCatch() {

        // 👉 hứng được thì không vỡ
        broken = false;

        // reset animation tránh lỗi frame cũ
        breakAnim.reset();

        // kết thúc object
        finish();
    }

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

    /**
     * cập nhật trạng thái animation
     */
    @Override
    public void update(GameModel model) {

        // chỉ update khi đang animation và bị vỡ
        if (state == State.ANIMATING && broken) {

            breakAnim.update();

            // animation xong thì kết thúc object
            if (breakAnim.isFinished()) {
                finish();
            }
        }
    }

    /**
     * vẽ bad egg lên màn hình
     */
    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

        int size = (int)(laneWidth * 0.5f);
        int x = centerX - size / 2;
        int yDraw = (int) y;

        // 👉 vẽ animation khi trứng bị vỡ
        if (state == State.ANIMATING && broken) {
            g.drawImage(breakAnim.getCurrentFrame(), x, yDraw, size, size, null);
        }

        // 👉 vẽ hình mặc định
        else {
            g.drawImage(idle, x, yDraw, size, size, null);
        }
    }
}