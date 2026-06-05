package model;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Lớp đại diện cho vật phẩm hồi phục mạng (HeartItem).
 * Khi người chơi bắt được vật phẩm này, số lượng mạng sẽ được tăng thêm.
 */
public class HeartItem extends FallingObject {

    /**
     * Khởi tạo vật phẩm hồi phục tại làn được chỉ định.
     * @param lane Làn đường mà vật phẩm sẽ rơi xuống.
     */
    public HeartItem(int lane) {
        super(lane);
        // Tốc độ rơi được thiết lập cố định là 4f
        this.speed = 4f; 
    }

    /**
     * Cập nhật trạng thái logic của vật phẩm trong vòng lặp game.
     * @param model Đối tượng GameModel chứa thông tin hệ thống hiện tại.
     */
    @Override
    public void update(GameModel model) {
        // Hiện tại chưa cần logic xử lý đặc biệt khi rơi
    }

    /**
     * Xử lý hành động khi vật phẩm được người chơi hứng thành công.
     * Chuyển trạng thái vật phẩm sang kết thúc (DEAD).
     */
    @Override
    public void onCatch() {
        // Logic tăng mạng sẽ được xử lý trong GameModel thông qua collision check
        finish(); 
    }

    /**
     * Xử lý hành động khi vật phẩm trôi qua khỏi màn hình mà không được bắt.
     */
    @Override
    public void onMiss() {
        // Hủy vật phẩm khi không được bắt
        finish();
    }

    /**
     * Vẽ hình ảnh đại diện cho vật phẩm lên màn hình.
     * @param g Đối tượng Graphics để vẽ.
     * @param centerX Vị trí trung tâm của làn.
     * @param laneWidth Độ rộng của làn.
     */
    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {
        g.setColor(Color.RED);
        // Vẽ một hình tròn đỏ làm biểu tượng trái tim
        g.fillOval(centerX - 10, getY(), 20, 20); 
    }
 // =========================================================
    // PHƯƠNG THỨC HỖ TRỢ KIỂM THỬ (DEBUG & TEST)
    // =========================================================
    
    /**
     * Phương thức main này giúp test nhanh ngay tại file này.
     */
    public static void main(String[] args) {
        System.out.println("--- Bắt đầu kiểm thử HeartItem ---");

        // 1. Test khởi tạo
        HeartItem testHeart = new HeartItem(2);
        boolean laneOk = (testHeart.getLane() == 2);
        System.out.println("Test 1 - Khởi tạo đúng làn: " + (laneOk ? "PASSED" : "FAILED"));

        // 2. Test hành vi bắt được
        testHeart.catchObject();
        boolean catchOk = testHeart.isDead();
        System.out.println("Test 2 - Trạng thái sau khi bắt (DEAD): " + (catchOk ? "PASSED" : "FAILED"));

        // 3. Test hành vi bỏ lỡ
        HeartItem testHeart2 = new HeartItem(0);
        testHeart2.missObject();
        boolean missOk = testHeart2.isDead();
        System.out.println("Test 3 - Trạng thái sau khi bỏ lỡ (DEAD): " + (missOk ? "PASSED" : "FAILED"));

        if (laneOk && catchOk && missOk) {
            System.out.println(">>> TẤT CẢ CÁC BÀI TEST ĐÃ VƯỢT QUA <<<");
        } else {
            System.out.println(">>> CÓ LỖI XẢY RA TRONG QUÁ TRÌNH TEST <<<");
        }
    }
}
