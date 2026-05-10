package util;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * =========================================================
 * SOUND MANAGER
 * =========================================================
 * lớp quản lý âm thanh trong game
 * 
 * chức năng:
 * - phát sound effect
 * - phát nhạc nền lặp vô hạn
 * - dừng âm thanh
 * =========================================================
 */
public class SoundManager {

    // =========================================================
    // PLAY SOUND EFFECT
    // =========================================================

    /**
     * phát âm thanh một lần
     */
    public static void play(String path) {

        try {

            // lấy file âm thanh từ resource
            URL url = SoundManager.class.getResource(path);

            // đọc audio stream
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);

            // tạo clip
            Clip clip = AudioSystem.getClip();

            // load audio vào clip
            clip.open(audio);

            // phát âm thanh
            clip.start();

        } catch (Exception e) {

            System.out.println("❌ Sound error: " + path);
        }
    }

    // =========================================================
    // LOOP BACKGROUND MUSIC
    // =========================================================

    /**
     * phát nhạc nền lặp vô hạn
     */
    public static Clip loop(String path) {

        try {

            // lấy file âm thanh
            URL url = SoundManager.class.getResource(path);

            // đọc audio stream
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);

            // tạo clip
            Clip clip = AudioSystem.getClip();

            // load audio
            clip.open(audio);

            // lặp vô hạn
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // phát nhạc
            clip.start();

            return clip;

        } catch (Exception e) {

            System.out.println("❌ BGM error");

            return null;
        }
    }

    // =========================================================
    // STOP SOUND
    // =========================================================

    /**
     * dừng clip âm thanh
     */
    public static void stop(Clip clip) {

        // kiểm tra clip hợp lệ
        if (clip != null && clip.isRunning()) {

            clip.stop();
        }
    }
}