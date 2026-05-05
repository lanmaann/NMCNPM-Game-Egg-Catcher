package model;

import java.awt.*;
import util.ImageLoader;

public class Egg extends FallingObject {

    private SpriteAnimation breakAnim;
    private Image idle;
    private boolean broken = false;

    public Egg(int lane) {
        super(lane);

        idle = ImageLoader.load("/resources/images/egg.png");

        Image[] frames = new Image[]{
                ImageLoader.load("/resources/images/eggBreak1.png"),
                ImageLoader.load("/resources/images/eggBreak2.png"),
                ImageLoader.load("/resources/images/eggBreak3.png")
        };

        breakAnim = new SpriteAnimation(frames, 8);
    }

    @Override
    public void onCatch() {
        // 👉 hứng KHÔNG vỡ
        broken = false;
        finish(); // hoặc remove luôn
    }

    @Override
    public void onMiss() {
        // 👉 hứng hụt = vỡ animation
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

        int size = (int)(laneWidth * 0.5f);   // 🔥 scale theo lane
        int x = centerX - size / 2;
        int yDraw = (int) y;

        if (state == State.ANIMATING && broken) {
            g.drawImage(breakAnim.getCurrentFrame(), x, yDraw, size, size, null);
        } else {
            g.drawImage(idle, x, yDraw, size, size, null);
        }
    }
}