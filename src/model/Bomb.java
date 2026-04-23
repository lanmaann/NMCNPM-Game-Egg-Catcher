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
        anim.reset();
    }

    @Override
    public void onMiss() {
        anim.reset();
    }

    @Override
    public void update(GameModel model) {

        if (state == State.ANIMATING) {

            anim.update();

            if (anim.isFinished()) {
                finish(); // chỉ mark dead
            }
        }
    }

    @Override
    public void draw(Graphics g, int x, int y) {

        if (state == State.ANIMATING) {
            g.drawImage(anim.getCurrentFrame(), x, y, 40, 40, null);
        } else {
            g.drawImage(frames[0], x, y, 40, 40, null);
        }
    }
}