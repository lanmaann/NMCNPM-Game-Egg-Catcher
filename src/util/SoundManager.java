package util;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {

    // =========================
    // SETTINGS
    // =========================
    public static boolean SOUND_ON = true;
    public static boolean MUSIC_ON = true;

    // =========================
    // VOLUME
    // =========================
    public static float MUSIC_VOLUME = -20.0f;

    public static final float CATCH_VOLUME = -10.0f;
    public static final float GOLD_VOLUME = -3.0f;
    public static final float EXPLOSION_VOLUME = 0.0f;
    public static final float BREAK_VOLUME = -8.0f;
    public static final float CHICKEN_VOLUME = -5.0f;
    public static final float UI_VOLUME = -5.0f;
    public static final float GAME_OVER_VOLUME = -8.0f;
    public static final float MOVE_VOLUME = -18.0f;

    // =========================
    // MUSIC CLIP
    // =========================
    private static Clip bgmClip;

    // =========================================================
    // SAFE WRAPPER - SOUND EFFECT
    // =========================================================
    public static void playSFX(String path, float volume) {
        if (!SOUND_ON) return;
        play(path, volume);
    }

    // =========================================================
    // SAFE WRAPPER - MUSIC
    // =========================================================
    public static void playMusic(String path) {
        if (!MUSIC_ON) return;
        playBackgroundMusic(path);
    }

    // =========================================================
    // CORE SOUND EFFECT
    // =========================================================
    private static void play(String path, float volume) {

        try {

            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.out.println("❌ Sound not found: " + path);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            // volume
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(volume);
            }

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            clip.start();

        } catch (Exception e) {
            System.out.println("❌ SFX error: " + path);
            e.printStackTrace();
        }
    }

    // =========================================================
    // MUSIC
    // =========================================================
    public static void playBackgroundMusic(String path) {

        if (!MUSIC_ON) return;

        try {

            stopMusic();

            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.out.println("❌ Music not found: " + path);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audio);

            if (bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(MUSIC_VOLUME);
            }

            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();

        } catch (Exception e) {
            System.out.println("❌ BGM error");
            e.printStackTrace();
        }
    }

    // =========================================================
    // STOP MUSIC
    // =========================================================
    public static void stopMusic() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
    }

    // =========================================================
    // GAME OVER MUSIC
    // =========================================================
    public static void playGameOverMusic(String path) {

        if (!MUSIC_ON) return;

        try {

            stopMusic();

            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.out.println("❌ Game over music not found");
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(GAME_OVER_VOLUME);
            }

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            clip.start();

        } catch (Exception e) {
            System.out.println("❌ GameOver music error");
        }
    }

    // =========================================================
    // SOUND EFFECT FUNCTIONS
    // =========================================================

    public static void playCatch() {
        playSFX("/resources/sound/catch.wav", CATCH_VOLUME);
    }

    public static void playGold() {
        playSFX("/resources/sound/gold.wav", GOLD_VOLUME);
    }

    public static void playExplosion() {
        playSFX("/resources/sound/explosion.wav", EXPLOSION_VOLUME);
    }

    public static void playBreak() {
        playSFX("/resources/sound/break.wav", BREAK_VOLUME);
    }

    public static void playChicken() {
        playSFX("/resources/sound/chicken.wav", CHICKEN_VOLUME);
    }

    public static void playUI() {
        playSFX("/resources/sound/click.wav", UI_VOLUME);
    }

    public static void playMove() {
        playSFX("/resources/sound/move.wav", MOVE_VOLUME);
    }

    public static void playRestart() {
        playSFX("/resources/sound/click.wav", UI_VOLUME);
    }
}