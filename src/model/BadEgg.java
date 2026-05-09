package model;

import java.awt.*;
import util.ImageLoader;

public class BadEgg extends FallingObject {

    private SpriteAnimation breakAnim;
    private Image idle;
    private boolean broken = false;

    public BadEgg(int lane) {
        super(lane);

        idle = ImageLoader.load("/resources/images/badEgg1.png");

        Image[] frames = new Image[]{
                ImageLoader.load("/resources/images/badEgg2.png"),
                ImageLoader.load("/resources/images/badEgg3.png"),
                ImageLoader.load("/resources/images/badEgg4.png")
        };

        breakAnim = new SpriteAnimation(frames, 6);
    }

    @Override
    public void onCatch() {
        // 👉 hứng được: không vỡ
        broken = false;
        breakAnim.reset(); // 🔥 tránh bug frame cũ
        finish();
    }

    @Override
    public void onMiss() {
        // 👉 rơi đất: vỡ
        broken = true;
        breakAnim.reset();
    }

    @Override
    public void update(GameModel model) {

        if (state == State.ANIMATING && broken) {

            breakAnim.update();

            if (breakAnim.isFinished()) {
                finish();
            }
        }
    }

    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

        int size = (int)(laneWidth * 0.5f);
        int x = centerX - size / 2;
        int yDraw = (int) y;

        // 👉 chỉ vẽ animation khi thực sự đang vỡ
        if (state == State.ANIMATING && broken) {
            g.drawImage(breakAnim.getCurrentFrame(), x, yDraw, size, size, null);
        } else {
            g.drawImage(idle, x, yDraw, size, size, null);
        }
    }
}