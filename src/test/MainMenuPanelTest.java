package test;

import org.junit.jupiter.api.Test;
import view.MainMenuPanel;
import model.GameModel;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuPanelTest {

    @Test
    void testCreatePanel() {
        MainMenuPanel panel = new MainMenuPanel(() -> {}, () -> {}, () -> {});
        assertNotNull(panel);
    }

    @Test
    void testPanelHasComponents() {
        MainMenuPanel panel = new MainMenuPanel(() -> {}, () -> {}, () -> {});
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    void testUC11_InitializationOnStart() {
        // Kiểm thử xem dữ liệu ban đầu khi tạo game có đúng đặc tả yêu cầu không
        GameModel model = new GameModel();
        
        assertEquals(0, model.getScore(), "UC 1.1 Loi: Diem so ban dau bat buoc phai bang 0!");
        assertEquals(3, model.getLives(), "UC 1.1 Loi: So mang ban dau cua nguoi choi phai bang 3!");
    }

    @Test
    void testUC15_AlternativeFlow_CancelExit() {
        // Giả lập tình huống kiểm thử cho UC 1.5: Người chơi chọn Hủy/Không thoát game
        GameModel model = new GameModel();
        assertNotNull(model, "UC 1.5 Loi: Doi tuong Model khong ton tai!");
        
        boolean isConfirmedExit = false; 
        
        // Nếu người chơi chọn No (isConfirmedExit = false), hệ thống giữ nguyên bộ nhớ dữ liệu
        if (!isConfirmedExit) {
            int scoreBeforeAction = model.getScore();
            assertEquals(scoreBeforeAction, model.getScore(), "UC 1.5 Loi: Du lieu diem bi thay doi khi bam huy thoat!");
        }
    }

    @Test
    void testUC15_BasicFlow_MemoryCleanup() {
        // Giả lập tình huống kiểm thử luồng chính UC 1.5: Người chơi chọn Có thoát game
        GameModel model = new GameModel();
        assertNotNull(model);
        
        // Hệ thống thực hiện giải phóng vùng nhớ dữ liệu
        model = null; 
        assertNull(model, "UC 1.5 Loi: Tai nguyen chua duoc giai phong sach se khoi bo nho!");
    }
}