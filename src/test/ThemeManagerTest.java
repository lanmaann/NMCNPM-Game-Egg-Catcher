package test;

import org.junit.jupiter.api.Test;
import util.ThemeManager;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class ThemeManagerTest {

    @Test
    void testDarkModeEnable() {
        ThemeManager.IS_DARK_MODE = true;

        Color bg = ThemeManager.getBgStart();

        assertNotNull(bg);
    }

    @Test
    void testDarkModeDisable() {
        ThemeManager.IS_DARK_MODE = false;

        Color bg = ThemeManager.getBgStart();

        assertNotNull(bg);
    }

    @Test
    void testGetTextColor() {
        assertNotNull(ThemeManager.getTextColor());
    }

    @Test
    void testGetButtonColor() {
        assertNotNull(ThemeManager.getButtonBg());
    }
}