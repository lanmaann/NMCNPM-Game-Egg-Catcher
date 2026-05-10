package util;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * Lớp quản lý toàn bộ âm thanh của trò chơi.
 * 
 * Chức năng:
 * - Phát hiệu ứng âm thanh
 * - Phát nhạc nền
 * - Dừng nhạc nền
 * - Quản lý âm lượng
 * - Cung cấp các hàm phát âm thanh nhanh
 */
public class SoundManager {

    /**
     * Trạng thái bật/tắt hiệu ứng âm thanh.
     */
    public static boolean SOUND_ON = true;

    /**
     * Trạng thái bật/tắt nhạc nền.
     */
    public static boolean MUSIC_ON = true;

    /**
     * Âm lượng nhạc nền.
     */
    public static float MUSIC_VOLUME = -20.0f;

    /**
     * Âm lượng âm thanh bắt trứng.
     */
    public static final float CATCH_VOLUME = -10.0f;

    /**
     * Âm lượng âm thanh trứng vàng.
     */
    public static final float GOLD_VOLUME = -3.0f;

    /**
     * Âm lượng âm thanh bom nổ.
     */
    public static final float EXPLOSION_VOLUME = 0.0f;

    /**
     * Âm lượng âm thanh trứng vỡ.
     */
    public static final float BREAK_VOLUME = -8.0f;

    /**
     * Âm lượng âm thanh gà xuất hiện.
     */
    public static final float CHICKEN_VOLUME = -5.0f;

    /**
     * Âm lượng âm thanh giao diện.
     */
    public static final float UI_VOLUME = -5.0f;

    /**
     * Âm lượng âm thanh Game Over.
     */
    public static final float GAME_OVER_VOLUME = -8.0f;

    /**
     * Âm lượng âm thanh di chuyển.
     */
    public static final float MOVE_VOLUME = -18.0f;

    /**
     * Âm lượng âm thanh restart.
     */
    public static final float RESTART_VOLUME = -5.0f;

    /**
     * Clip nhạc nền đang phát.
     */
    private static Clip bgmClip;

    /**
     * Phát hiệu ứng âm thanh.
     * 
     * @param path đường dẫn file âm thanh
     * @param volume âm lượng
     */
    public static void play(
            String path,
            float volume
    ) {

        // Không phát nếu tắt âm thanh
        if (!SOUND_ON) {
            return;
        }

        try {

            URL url =
                    SoundManager.class.getResource(path);

            // Không tìm thấy file
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

            /**
             * Điều chỉnh âm lượng.
             */
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

            /**
             * Tự động đóng clip sau khi phát xong.
             */
            clip.addLineListener(event -> {

                if (
                        event.getType()
                                ==
                                LineEvent.Type.STOP
                ) {

                    clip.close();
                }
            });

            // Phát âm thanh
            clip.start();

        } catch (Exception e) {

            System.out.println(
                    "❌ Sound Error: " + path
            );

            e.printStackTrace();
        }
    }

    /**
     * Phát nhạc nền lặp vô hạn.
     * 
     * @param path đường dẫn file nhạc
     */
    public static void playBackgroundMusic(
            String path
    ) {

        // Không phát nếu tắt nhạc
        if (!MUSIC_ON) {
            return;
        }

        try {

            /**
             * Dừng nhạc cũ để tránh chồng nhạc.
             */
            stopMusic();

            URL url =
                    SoundManager.class.getResource(path);

            // Không tìm thấy file
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

            /**
             * Điều chỉnh âm lượng nhạc nền.
             */
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

            /**
             * Phát lặp vô hạn.
             */
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

    /**
     * Dừng nhạc nền hiện tại.
     */
    public static void stopMusic() {

        if (bgmClip != null) {

            bgmClip.stop();

            bgmClip.close();

            bgmClip = null;
        }
    }

    /**
     * Phát nhạc Game Over.
     * 
     * @param path đường dẫn file nhạc
     */
    public static void playGameOverMusic(
            String path
    ) {

        // Không phát nếu tắt nhạc
        if (!MUSIC_ON) {
            return;
        }

        try {

            /**
             * Dừng nhạc nền trước khi phát.
             */
            stopMusic();

            URL url =
                    SoundManager.class.getResource(path);

            // Không tìm thấy file
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

            /**
             * Điều chỉnh âm lượng.
             */
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

            /**
             * Tự động đóng clip sau khi phát xong.
             */
            clip.addLineListener(event -> {

                if (
                        event.getType()
                                ==
                                LineEvent.Type.STOP
                ) {

                    clip.close();
                }
            });

            // Phát âm thanh
            clip.start();

        } catch (Exception e) {

            System.out.println(
                    "❌ Game Over Music Error"
            );

            e.printStackTrace();
        }
    }

    /**
     * Phát âm thanh bắt trứng.
     */
    public static void playCatch() {

        play(
                "/resources/sound/catch.wav",
                CATCH_VOLUME
        );
    }

    /**
     * Phát âm thanh trứng vàng.
     */
    public static void playGold() {

        play(
                "/resources/sound/gold.wav",
                GOLD_VOLUME
        );
    }

    /**
     * Phát âm thanh bom nổ.
     */
    public static void playExplosion() {

        play(
                "/resources/sound/explosion.wav",
                EXPLOSION_VOLUME
        );
    }

    /**
     * Phát âm thanh trứng vỡ.
     */
    public static void playBreak() {

        play(
                "/resources/sound/break.wav",
                BREAK_VOLUME
        );
    }

    /**
     * Phát âm thanh gà xuất hiện.
     */
    public static void playChicken() {

        play(
                "/resources/sound/chicken.wav",
                CHICKEN_VOLUME
        );
    }

    /**
     * Phát âm thanh giao diện.
     */
    public static void playUI() {

        play(
                "/resources/sound/click.wav",
                UI_VOLUME
        );
    }

    /**
     * Phát âm thanh di chuyển.
     */
    public static void playMove() {

        play(
                "/resources/sound/move.wav",
                MOVE_VOLUME
        );
    }

    /**
     * Phát âm thanh restart game.
     */
    public static void playRestart() {

        play(
                "/resources/music/bgm.wav",
                RESTART_VOLUME
        );
    }
}