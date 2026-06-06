package util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Theme Manager
 * Bộ quản lý giao diện đồ họa (Sáng/Tối) toàn cục của trò chơi.
 */
public class ThemeManager {
    
    /** Trạng thái Chế độ tối (true: Dark Mode, false: Light Mode) */
    public static boolean IS_DARK_MODE = false;

    private static List<Runnable> listeners = new ArrayList<>();

    public static void addListener(Runnable r) {
        listeners.add(r);
    }

    private static void notifyChange() {
        for (Runnable r : listeners) {
            r.run();
        }
    }

    public static void setDarkMode(boolean dark) {
        IS_DARK_MODE = dark;
        notifyChange();
    }
    
    // Màu nền chính (Background)
    public static Color getBgStart() {
        return IS_DARK_MODE ? new Color(30, 30, 40) : new Color(245, 248, 255);
    }
    
    public static Color getBgEnd() {
        return IS_DARK_MODE ? new Color(15, 15, 25) : new Color(190, 210, 255);
    }

    // Màu chữ chính (Text Color)
    public static Color getTextColor() {
        return IS_DARK_MODE ? new Color(240, 240, 240) : new Color(20, 20, 20);
    }

    // Màu nền của các nút bấm (Button Background)
    public static Color getButtonBg() {
        return IS_DARK_MODE ? new Color(55, 55, 65) : new Color(230, 230, 230);
    }

    // Màu khối hình học trang trí mặt nền
    public static Color getDecorationColor() {
        return IS_DARK_MODE ? new Color(255, 255, 255, 15) : new Color(255, 255, 255, 70);
    }
}