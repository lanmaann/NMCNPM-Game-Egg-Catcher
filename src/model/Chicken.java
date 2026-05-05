package model;

import java.awt.*;
import util.ImageLoader;

public class Chicken extends FallingObject {

    private SpriteAnimation flyAnim;
    private SpriteAnimation hitAnim;

    private boolean hit = false;

    // ===== EFFECT =====
    private float shake = 0;
    private int shakeDir = 1;

    public Chicken(int lane) {
        super(lane);

        // =========================
        // FLY (4 FRAMES ONLY)
        // =========================
        Image[] flyFrames = new Image[] {
                ImageLoader.load("/resources/images/chickenFly1.png"),
                ImageLoader.load("/resources/images/chickenFly2.png"),
                ImageLoader.load("/resources/images/chickenFly3.png"),
                ImageLoader.load("/resources/images/chickenFly4.png")
        };

        flyAnim = new SpriteAnimation(flyFrames, 6);

        // =========================
        // HIT (2 FRAMES ONLY)
        // =========================
        Image[] hitFrames = new Image[] {
                ImageLoader.load("/resources/images/chickenHit1.png"),
                ImageLoader.load("/resources/images/chickenHit2.png")
        };

        hitAnim = new SpriteAnimation(hitFrames, 10);
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    public void update(GameModel model) {

        if (state == State.DEAD) return;

        // =========================
        // FLYING
        // =========================
        if (state == State.FALLING) {

            flyAnim.update();
            fall();

            // 👉 tạo cảm giác "tức giận bay xuống"
            shake += 0.3f * shakeDir;
            if (Math.abs(shake) > 2) shakeDir *= -1;

            // miss
            if (y > model.getPlayerY() + 200) {
                missObject();
            }
        }

        // =========================
        // HIT ANIMATION
        // =========================
        else if (state == State.ANIMATING) {

            hitAnim.update();

            if (hitAnim.isFinished()) {
                finish();
            }
        }
    }

    // =========================
    // DRAW
    // =========================
    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

    		int size = (int)(laneWidth);

        int x = centerX - size / 2;

        // 👉 shake effect (rất quan trọng làm game "có hồn")
        int shakeX = (int) shake;

        int yDraw = (int) y - size / 6;

        if (state == State.ANIMATING) {

            g.drawImage(
                    hitAnim.getCurrentFrame(),
                    x + shakeX,
                    yDraw,
                    size,
                    size,
                    null
            );

        } else {

            g.drawImage(
                    flyAnim.getCurrentFrame(),
                    x + shakeX,
                    yDraw,
                    size,
                    size,
                    null
            );
        }
    }

    // =========================
    // EVENTS
    // =========================
    @Override
    public void onCatch() {
        state = State.ANIMATING;
        hitAnim.reset();
    }

    @Override
    public void onMiss() {
        state = State.ANIMATING;
        hitAnim.reset();
    }
}