package model;

import java.awt.*;
import util.ImageLoader;

/**
 * =========================================================
 * CHICKEN OBJECT
 * =========================================================
 * lớp đại diện cho gà trong game
 * 
 * chức năng:
 * - hiển thị animation bay
 * - hiển thị animation bị đánh
 * - tạo hiệu ứng rung khi bay
 * - xử lý va chạm với người chơi
 * =========================================================
 */
public class Chicken extends FallingObject {

    // animation gà bay
    private SpriteAnimation flyAnim;

    // animation khi gà bị đánh
    private SpriteAnimation hitAnim;

    // trạng thái bị hit
    private boolean hit = false;

    // =========================================================
    // SHAKE EFFECT
    // =========================================================

    // độ rung
    private float shake = 0;

    // hướng rung
    private int shakeDir = 1;

    /**
     * constructor khởi tạo chicken theo lane
     */
    public Chicken(int lane) {

        super(lane);

        // =========================================================
        // LOAD FLY ANIMATION
        // =========================================================

        Image[] flyFrames = new Image[] {

                ImageLoader.load("/resources/images/chickenFly1.png"),
                ImageLoader.load("/resources/images/chickenFly2.png"),
                ImageLoader.load("/resources/images/chickenFly3.png"),
                ImageLoader.load("/resources/images/chickenFly4.png")
        };

        // tạo animation bay
        flyAnim = new SpriteAnimation(flyFrames, 6);

        // =========================================================
        // LOAD HIT ANIMATION
        // =========================================================

        Image[] hitFrames = new Image[] {

                ImageLoader.load("/resources/images/chickenHit1.png"),
                ImageLoader.load("/resources/images/chickenHit2.png")
        };

        // tạo animation bị hit
        hitAnim = new SpriteAnimation(hitFrames, 10);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    /**
     * cập nhật trạng thái chicken
     */
    @Override
    public void update(GameModel model) {

        // không update khi đã chết
        if (state == State.DEAD) return;

        // =========================================================
        // FLYING STATE
        // =========================================================

        if (state == State.FALLING) {

            // update animation bay
            flyAnim.update();

            // xử lý rơi
            fall();

            // =========================================================
            // SHAKE EFFECT
            // =========================================================

            // tạo hiệu ứng rung qua lại
            shake += 0.3f * shakeDir;

            if (Math.abs(shake) > 2) {
                shakeDir *= -1;
            }

            // =========================================================
            // MISS CHECK
            // =========================================================

            // gà bay khỏi màn hình
            if (y > model.getPlayerY() + 200) {

                missObject();
            }
        }

        // =========================================================
        // HIT ANIMATION
        // =========================================================

        else if (state == State.ANIMATING) {

            // update animation hit
            hitAnim.update();

            // animation kết thúc
            if (hitAnim.isFinished()) {

                finish();
            }
        }
    }

    // =========================================================
    // DRAW
    // =========================================================

    /**
     * vẽ chicken lên màn hình
     */
    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

        // chicken lớn hơn egg
        int size = (int)(laneWidth);

        int x = centerX - size / 2;

        // =========================================================
        // SHAKE POSITION
        // =========================================================

        // hiệu ứng rung ngang
        int shakeX = (int) shake;

        int yDraw = (int) y - size / 6;

        // =========================================================
        // DRAW HIT
        // =========================================================

        if (state == State.ANIMATING) {

            g.drawImage(
                    hitAnim.getCurrentFrame(),
                    x + shakeX,
                    yDraw,
                    size,
                    size,
                    null
            );
        }

        // =========================================================
        // DRAW FLY
        // =========================================================

        else {

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

    // =========================================================
    // EVENTS
    // =========================================================

    /**
     * xử lý khi chicken bị người chơi hứng
     */
    @Override
    public void onCatch() {

        state = State.ANIMATING;

        hitAnim.reset();
    }

    /**
     * xử lý khi chicken rơi khỏi màn hình
     */
    @Override
    public void onMiss() {

        state = State.ANIMATING;

        hitAnim.reset();
    }
}