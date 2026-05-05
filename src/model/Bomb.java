package model;

import java.awt.*;
import util.ImageLoader;

public class Bomb extends FallingObject {

    private Image[] frames;
    private SpriteAnimation anim;

    public Bomb(int lane) {
        super(lane);

        frames = new Image[]{
                ImageLoader.load("/resources/images/bomb1.png"),
                ImageLoader.load("/resources/images/bomb2.png"),
                ImageLoader.load("/resources/images/bomb3.png"),
                ImageLoader.load("/resources/images/bomb4.png"),
                ImageLoader.load("/resources/images/bomb5.png")
        };

        anim = new SpriteAnimation(frames, 10);
    }

    @Override
    public void onCatch() {
        anim.reset();          // 🔥 đảm bảo animation bắt đầu từ đầu
    }

    @Override
    public void onMiss() {
        anim.reset();          // 🔥 đồng bộ hành vi
    }

    @Override
    public void update(GameModel model) {

        if (state == State.ANIMATING) {

            anim.update();

            if (anim.isFinished()) {
                finish();
            }
        }
    }

    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

        int size = (int)(laneWidth * 0.6f); // 🔥 bomb to hơn
        int x = centerX - size / 2;
        int yDraw = (int) y;

        if (state == State.ANIMATING) {
            g.drawImage(anim.getCurrentFrame(), x, yDraw, size, size, null);
        } else {
            g.drawImage(frames[0], x, yDraw, size, size, null);
        }
    }
}