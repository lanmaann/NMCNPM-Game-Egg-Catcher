package model;

import java.awt.Graphics;

/**
 * Falling Object
 * Lớp trừu tượng (abstract) đại diện cho mọi vật thể rơi trong hệ thống game.
 * * Các object kế thừa:
 * - Egg (Trứng thường)
 * - GoldenEgg (Trứng vàng)
 * - BadEgg (Trứng thối)
 * - Bomb (Quả bom)
 * - Chicken (Con gà)
 * * Chức năng chính:
 * - Quản lý chỉ số Lane (Làn đường rơi)
 * - Quản lý vị trí tọa độ rơi tự do
 * - Điều phối vận tốc rơi theo cấp độ trò chơi
 * - Quản lý vòng đời và trạng thái (State) của vật thể
 */
public abstract class FallingObject {

    /** Position State */
    protected int lane;

    /** Vị trí Y của vật thể (sử dụng float để tăng độ mượt mà khi dịch chuyển) */
    protected float y = 0;

    /** Tốc độ rơi tịnh tiến (tính theo pixel/frame) */
    protected float speed = 5f;

    /** Object State Lifecycle */
    protected State state = State.FALLING;

    /**
     * Khởi tạo vật thể theo làn đường (Lane) được chỉ định
     *
     * @param lane làn đường mà vật thể sẽ xuất hiện (spawn)
     */
    public FallingObject(int lane) {
        this.lane = lane;
    }

    /** Object Movement Logic */
    public void fall() {
        // [2.1.2] Vật thể rơi tự do xuống dưới theo trục Y bằng cách cộng dồn vận tốc
        if (state == State.FALLING) {
            y += speed;
        }
    }

    /** State Verification Utilities */
    public boolean isFalling() {
        return state == State.FALLING;
    }

    public boolean isDead() {
        // [2.1.7] Trả về true nếu vật thể chuyển sang trạng thái DEAD để GameModel tiến hành xóa khỏi bộ nhớ
        return state == State.DEAD;
    }

    /** State Transition Triggers */
    public final void catchObject() {
        // Ngăn chặn việc bắt trùng lặp sự kiện nhiều lần khi đang chạy hiệu ứng
        if (state != State.FALLING) {
            return;
        }

        // [2.1.4] & [2.1.7] Chuyển đổi trạng thái sang ANIMATING (Bắt đầu nổ/vỡ để chuẩn bị biến mất)
        state = State.ANIMATING;

        // Kích hoạt hàm xử lý đặc trưng của từng lớp con kế thừa
        onCatch();
    }

    public final void missObject() {
        // Ngăn chặn việc bắt trùng lặp sự kiện nhiều lần khi đang chạy hiệu ứng
        if (state != State.FALLING) {
            return;
        }

        // [2.2.1] & [2.1.7] Vật thể vượt qua giỏ hứng, chuyển trạng thái chạy hiệu ứng vỡ vụn dưới đáy màn hình
        state = State.ANIMATING;

        // Kích hoạt hàm xử lý rơi hụt của từng lớp con kế thừa
        onMiss();
    }

    /**
     * Kết thúc vòng đời của vật thể, đánh dấu sẵn sàng giải phóng
     */
    protected void finish() {
        // [2.1.7] Chuyển đổi trạng thái cuối cùng sang DEAD để hệ thống hủy hoàn toàn
        state = State.DEAD;
    }

    /** Getters / Setters */
    public int getLane() {
        return lane;
    }

    public int getY() {
        // Ép kiểu dữ liệu một cách an toàn từ float về int phục vụ hiển thị đồ họa
        return (int) y;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public State getState() {
        return state;
    }

    /** Abstract Methods Contracts */
    public abstract void update(GameModel model);

    public abstract void onCatch();

    public abstract void onMiss();

    /**
     * Thực hiện tác vụ vẽ đồ họa vật thể lên màn hình ứng dụng
     *
     * @param g         đối tượng Graphics nhận dạng đồ họa từ Canvas
     * @param centerX   vị trí tọa độ trung tâm của làn đường hiện tại
     * @param laneWidth độ rộng tiêu chuẩn của làn đường
     */
    public abstract void draw(Graphics g, int centerX, int laneWidth);
}