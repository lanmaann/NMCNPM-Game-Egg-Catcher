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
        // 👉 hứng được: KHÔNG vỡ
        broken = false;
        finish(); // biến mất luôn
    }

    @Override
    public void onMiss() {
        // 👉 rơi đất: vỡ animation
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
    public void draw(Graphics g, int x, int y) {

        if (state == State.ANIMATING && broken) {
            g.drawImage(breakAnim.getCurrentFrame(), x, y, 30, 30, null);
        } else {
            g.drawImage(idle, x, y, 30, 30, null);
        }
    }
}