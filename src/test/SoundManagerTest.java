package test;

import org.junit.jupiter.api.Test;
import util.SoundManager;

import static org.junit.jupiter.api.Assertions.*;

class SoundManagerTest {

    @Test
    void testMusicVolumeAssignment() {
        SoundManager.MUSIC_VOLUME = 75;
        assertEquals(75, SoundManager.MUSIC_VOLUME);
    }

    @Test
    void testSoundVolumeAssignment() {
        SoundManager.SOUND_VOLUME = 50;
        assertEquals(50, SoundManager.SOUND_VOLUME);
    }

    @Test
    void testStopMusicDoesNotThrow() {
        assertDoesNotThrow(() -> SoundManager.stopMusic());
    }

    @Test
    void testUpdateMusicVolumeDoesNotThrow() {
        assertDoesNotThrow(() -> SoundManager.updateMusicVolume());
    }
}