package util;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {

    // =========================================================
    // SETTINGS
    // =========================================================
    public static boolean SOUND_ON = true;

    public static boolean MUSIC_ON = true;

    // =========================================================
    // GLOBAL VOLUME
    // =========================================================
    public static float MUSIC_VOLUME = -20.0f;

    // =========================================================
    // SOUND EFFECT VOLUME
    // =========================================================
    public static final float CATCH_VOLUME = -10.0f;

    public static final float GOLD_VOLUME = -3.0f;

    public static final float EXPLOSION_VOLUME = 0.0f;

    public static final float BREAK_VOLUME = -8.0f;

    public static final float CHICKEN_VOLUME = -5.0f;

    public static final float UI_VOLUME = -5.0f;

    public static final float GAME_OVER_VOLUME = -8.0f;

    public static final float MOVE_VOLUME = -18.0f;

    public static final float RESTART_VOLUME = -5.0f;

    // =========================================================
    // MUSIC CLIP
    // =========================================================
    private static Clip bgmClip;

    // =========================================================
    // PLAY SOUND EFFECT
    // =========================================================
    public static void play(
            String path,
            float volume
    ) {

        if (!SOUND_ON) return;

        try {

            URL url =
                    SoundManager.class.getResource(path);

            if (url == null) {

                System.out.println(
                        "❌ Sound file not found: " + path
                );

                return;
            }

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);

            Clip clip =
                    AudioSystem.getClip();

            clip.open(audio);

            // =================================================
            // VOLUME
            // =================================================
            if (
                    clip.isControlSupported(
                            FloatControl.Type.MASTER_GAIN
                    )
            ) {

                FloatControl gainControl =
                        (FloatControl)
                                clip.getControl(
                                        FloatControl.Type.MASTER_GAIN
                                );

                gainControl.setValue(volume);
            }

            // =================================================
            // AUTO CLOSE
            // =================================================
            clip.addLineListener(event -> {

                if (
                        event.getType()
                                ==
                                LineEvent.Type.STOP
                ) {

                    clip.close();
                }
            });

            // =================================================
            // PLAY
            // =================================================
            clip.start();

        } catch (Exception e) {

            System.out.println(
                    "❌ Sound Error: " + path
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // PLAY BACKGROUND MUSIC
    // =========================================================
    public static void playBackgroundMusic(
            String path
    ) {

        if (!MUSIC_ON) return;

        try {

            // tránh chồng nhạc
            stopMusic();

            URL url =
                    SoundManager.class.getResource(path);

            if (url == null) {

                System.out.println(
                        "❌ Music file not found: " + path
                );

                return;
            }

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);

            bgmClip =
                    AudioSystem.getClip();

            bgmClip.open(audio);

            // =================================================
            // MUSIC VOLUME
            // =================================================
            if (
                    bgmClip.isControlSupported(
                            FloatControl.Type.MASTER_GAIN
                    )
            ) {

                FloatControl gainControl =
                        (FloatControl)
                                bgmClip.getControl(
                                        FloatControl.Type.MASTER_GAIN
                                );

                gainControl.setValue(MUSIC_VOLUME);
            }

            // =================================================
            // LOOP
            // =================================================
            bgmClip.loop(
                    Clip.LOOP_CONTINUOUSLY
            );

            bgmClip.start();

        } catch (Exception e) {

            System.out.println(
                    "❌ BGM Error"
            );

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
    public static void playGameOverMusic(
            String path
    ) {

        if (!MUSIC_ON) return;

        try {

            // dừng nhạc nền
            stopMusic();

            URL url =
                    SoundManager.class.getResource(path);

            if (url == null) {

                System.out.println(
                        "❌ Game over music not found: " + path
                );

                return;
            }

            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(url);

            Clip clip =
                    AudioSystem.getClip();

            clip.open(audio);

            // =================================================
            // VOLUME
            // =================================================
            if (
                    clip.isControlSupported(
                            FloatControl.Type.MASTER_GAIN
                    )
            ) {

                FloatControl gainControl =
                        (FloatControl)
                                clip.getControl(
                                        FloatControl.Type.MASTER_GAIN
                                );

                gainControl.setValue(
                        GAME_OVER_VOLUME
                );
            }

            // =================================================
            // AUTO CLOSE
            // =================================================
            clip.addLineListener(event -> {

                if (
                        event.getType()
                                ==
                                LineEvent.Type.STOP
                ) {

                    clip.close();
                }
            });

            // =================================================
            // PLAY
            // =================================================
            clip.start();

        } catch (Exception e) {

            System.out.println(
                    "❌ Game Over Music Error"
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // READY FUNCTIONS
    // =========================================================

    // bắt trứng
    public static void playCatch() {

        play(
                "/resources/sound/catch.wav",
                CATCH_VOLUME
        );
    }

    // trứng vàng
    public static void playGold() {

        play(
                "/resources/sound/gold.wav",
                GOLD_VOLUME
        );
    }

    // bom nổ
    public static void playExplosion() {

        play(
                "/resources/sound/explosion.wav",
                EXPLOSION_VOLUME
        );
    }

    // trứng vỡ
    public static void playBreak() {

        play(
                "/resources/sound/break.wav",
                BREAK_VOLUME
        );
    }

    // gà xuất hiện
    public static void playChicken() {

        play(
                "/resources/sound/chicken.wav",
                CHICKEN_VOLUME
        );
    }

    // UI
    public static void playUI() {

        play(
                "/resources/sound/click.wav",
                UI_VOLUME
        );
    }

    // MOVE
    public static void playMove() {

        play(
                "/resources/sound/move.wav",
                MOVE_VOLUME
        );
    }

    // RESTART
    public static void playRestart() {

        play(
                "/resources/music/bgm.wav",
                RESTART_VOLUME
        );
    }
}