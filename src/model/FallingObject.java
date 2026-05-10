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

    // =========================================================
    // POSITION
    // =========================================================

    /**
     * lane hiện tại của object
     */
    protected int lane;

    /**
     * vị trí Y của object
     * dùng float để di chuyển mượt hơn
     */
    protected float y = 0;

    /**
     * tốc độ rơi (pixel/frame)
     */
    protected float speed = 5f;

    // =========================================================
    // STATE
    // =========================================================

    /**
     * trạng thái hiện tại của object
     */
    protected State state = State.FALLING;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    /**
     * khởi tạo object theo lane
     *
     * @param lane lane spawn object
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
     *
     * @return true nếu đang FALLING
     */
    public boolean isFalling() {

        return state == State.FALLING;
    }

    /**
     * kiểm tra object đã chết chưa
     *
     * @return true nếu DEAD
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
        if (state != State.FALLING) {
            return;
        }

        // chuyển sang animation
        state = State.ANIMATING;

        // xử lý riêng
        onCatch();
    }

    /**
     * xử lý khi object rơi hụt
     */
    public final void missObject() {

        // tránh xử lý nhiều lần
        if (state != State.FALLING) {
            return;
        }

        // chuyển sang animation
        state = State.ANIMATING;

        // xử lý riêng
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
     *
     * @return lane
     */
    public int getLane() {

        return lane;
    }

    /**
     * lấy vị trí Y
     *
     * @return vị trí y dạng int
     */
    public int getY() {

        // ép float -> int khi sử dụng
        return (int) y;
    }

    /**
     * set tốc độ rơi
     *
     * @param speed tốc độ mới
     */
    public void setSpeed(float speed) {

        this.speed = speed;
    }

    /**
     * lấy trạng thái hiện tại
     *
     * @return state hiện tại
     */
    public State getState() {

        return state;
    }

    // =========================================================
    // ABSTRACT METHODS
    // =========================================================

    /**
     * cập nhật object
     *
     * @param model game model
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
     *
     * @param g graphics
     * @param centerX vị trí giữa lane
     * @param laneWidth chiều rộng lane
     */
    public abstract void draw(
            Graphics g,
            int centerX,
            int laneWidth
    );
}