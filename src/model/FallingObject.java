package model;

import java.awt.Graphics;

/**
 * =========================================================
 * FALLING OBJECT
 * =========================================================
 * lớp abstract đại diện cho mọi vật thể rơi trong game
 * 
 * các object kế thừa:
 * - Egg
 * - BadEgg
 * - Bomb
 * - Chicken
 * 
 * chức năng:
 * - quản lý lane
 * - quản lý vị trí rơi
 * - quản lý tốc độ
 * - quản lý trạng thái object
 * - cung cấp logic chung cho object rơi
 * =========================================================
 */
public abstract class FallingObject {

    // lane hiện tại của object
    protected int lane;

    // vị trí Y của object
    protected float y = 0;

    // tốc độ rơi
    protected float speed = 5f;

    // trạng thái hiện tại
    protected State state = State.FALLING;

    /**
     * constructor khởi tạo object theo lane
     */
    public FallingObject(int lane) {

        this.lane = lane;
    }

    // =========================================================
    // MOVE
    // =========================================================

    /**
     * xử lý object rơi xuống
     */
    public void fall() {

        // chỉ rơi khi đang FALLING
        if (state == State.FALLING) {

            y += speed;
        }
    }

    // =========================================================
    // STATE CHECK
    // =========================================================

    /**
     * kiểm tra object có đang rơi không
     */
    public boolean isFalling() {

        return state == State.FALLING;
    }

    /**
     * kiểm tra object đã chết chưa
     */
    public boolean isDead() {

        return state == State.DEAD;
    }

    // =========================================================
    // STATE TRANSITION
    // =========================================================

    /**
     * xử lý khi object được hứng
     */
    public final void catchObject() {

        // tránh xử lý nhiều lần
        if (state != State.FALLING) return;

        // chuyển sang animation
        state = State.ANIMATING;

        // gọi xử lý riêng của object
        onCatch();
    }

    /**
     * xử lý khi object bị rơi hụt
     */
    public final void missObject() {

        // tránh xử lý nhiều lần
        if (state != State.FALLING) return;

        // chuyển sang animation
        state = State.ANIMATING;

        // gọi xử lý riêng của object
        onMiss();
    }

    /**
     * kết thúc object
     */
    protected void finish() {

        state = State.DEAD;
    }

    // =========================================================
    // GETTER / SETTER
    // =========================================================

    /**
     * lấy lane hiện tại
     */
    public int getLane() {

        return lane;
    }

    /**
     * lấy vị trí Y
     */
    public int getY() {

        // ép float sang int khi sử dụng
        return (int) y;
    }

    /**
     * set tốc độ rơi
     */
    public void setSpeed(float speed) {

        this.speed = speed;
    }

    /**
     * lấy trạng thái hiện tại
     */
    public State getState() {

        return state;
    }

    // =========================================================
    // ABSTRACT METHODS
    // =========================================================

    /**
     * cập nhật object
     */
    public abstract void update(GameModel model);

    /**
     * xử lý khi object được hứng
     */
    public abstract void onCatch();

    /**
     * xử lý khi object rơi hụt
     */
    public abstract void onMiss();

    /**
     * vẽ object lên màn hình
     */
    public abstract void draw(Graphics g, int centerX, int laneWidth);
}