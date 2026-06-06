package test;

import org.junit.jupiter.api.Test;
import view.MainMenuPanel;

import static org.junit.jupiter.api.Assertions.*;

class MainMenuPanelTest {

    @Test
    void testCreatePanel() {

        MainMenuPanel panel =
                new MainMenuPanel(
                        () -> {},
                        () -> {},
                        () -> {}
                );

        assertNotNull(panel);
    }

    @Test
    void testPanelHasComponents() {
        MainMenuPanel panel =
                new MainMenuPanel(
                        () -> {},
                        () -> {},
                        () -> {}
                );

        assertTrue(panel.getComponentCount() > 0);
    }
}