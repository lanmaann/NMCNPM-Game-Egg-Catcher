package util;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * Sound Manager
 * Lớp quản lý toàn bộ hệ thống âm thanh, nhạc nền và hiệu ứng của trò chơi.
 * * Chức năng:
 * - Quản lý âm lượng tuyến tính theo tỷ lệ phần trăm (0 - 100%).
 * - Tự động biên dịch phần trăm thành định dạng Decibel (dB) logarit.
 * - Phát/Dừng luồng nhạc nền (BGM) lặp vô hạn và các hiệu ứng âm thanh (SFX).
 */
public class SoundManager {

    /** Mức âm lượng hiệu ứng âm thanh toàn cục (Tuyến tính: 0% - 100%) */
    public static int SOUND_VOLUME = 80;

    /** Mức âm lượng nhạc nền toàn cục (Tuyến tính: 0% - 100%) */
    public static int MUSIC_VOLUME = 60;

    /** Clip nhạc nền đang được thực thi phát trên hệ thống */
    private static Clip bgmClip;

    /** Đường dẫn lưu trữ tài nguyên nhạc nền hiện tại */
    private static String currentMusicPath;

    /**
     * Chuyển đổi giá trị tuyến tính phần trăm (0 - 100) sang giá trị Decibel (dB)
     * Công thức logarit: dB = 20 * log10(volume / 100)
     * Tránh lỗi âm vô hạn bằng cách giới hạn mốc tối thiểu -80.0f dB tại 0% thể tích.
     *
     * @param volumePercent tỷ lệ phần trăm âm lượng đầu vào (0 - 100)
     * @return float trị số decibel tương ứng nạp vào MASTER_GAIN
     */
    private static float convertPercentToDecibel(int volumePercent) {
        if (volumePercent <= 0) {
            return -80.0f; 
        }
        float gain = (float) (20.0 * Math.log10(volumePercent / 100.0));
        // Đảm bảo giá trị gain không vượt quá ngưỡng tối đa an toàn của phần cứng
        return Math.max(-80.0f, Math.min(0.0f, gain));
    }

    /**
     * Phát hiệu ứng âm thanh (SFX) ngắn
     *
     * @param path              đường dẫn file âm thanh tài nguyên
     * @param baseVolumePercent tỷ lệ phần trăm âm lượng đặc trưng của loại vật thể đó
     */
    public static void playSFX(String path, int baseVolumePercent) {
        // Nếu âm lượng tổng bằng 0, hủy tiến trình phát âm thanh
        if (SOUND_VOLUME <= 0) return;

        try {
            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.out.println("❌ Sound not found: " + path);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            // Tính toán âm lượng thực tế kết hợp giữa âm lượng SFX tổng và đặc trưng vật thể
            int finalVolume = (int) (baseVolumePercent * (SOUND_VOLUME / 100.0));

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gain.setValue(convertPercentToDecibel(finalVolume));
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

    /**
     * Cập nhật âm lượng cho nhạc nền đang phát ngay trong thời gian thực (Runtime update)
     */
    public static void updateMusicVolume() {
        if (bgmClip != null && bgmClip.isActive()) {
            if (MUSIC_VOLUME <= 0) {
                bgmClip.stop();
            } else {
                if (!bgmClip.isRunning()) {
                    bgmClip.start();
                }
                if (bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gain = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                    gain.setValue(convertPercentToDecibel(MUSIC_VOLUME));
                }
            }
        } else if (MUSIC_VOLUME > 0 && currentMusicPath != null) {
            // Nếu nhạc nền đang tắt nhưng người chơi kéo tăng âm lượng lên, tái khởi động bài nhạc
            playBackgroundMusic(currentMusicPath);
        }
    }

    /**
     * Khởi chạy nhạc nền lặp vô hạn
     *
     * @param path đường dẫn file âm thanh tài nguyên
     */
    public static void playBackgroundMusic(String path) {
	    	if (bgmClip != null
	                && bgmClip.isRunning()
	                && path.equals(currentMusicPath)) {
	            return;
	        }    
	    	currentMusicPath = path;
        if (MUSIC_VOLUME <= 0) return;

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
                gain.setValue(convertPercentToDecibel(MUSIC_VOLUME));
            }

            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();

        } catch (Exception e) {
            System.out.println("❌ BGM error");
            e.printStackTrace();
        }
    }

    /**
     * Dừng ngay lập tức nhạc nền đang phát và giải phóng tài nguyên
     */
    public static void stopMusic() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
        }
    }

    /**
     * Phát luồng âm nhạc kết thúc trò chơi (Game Over)
     *
     * @param path đường dẫn file âm thanh tài nguyên
     */
    public static void playGameOverMusic(String path) {

        currentMusicPath = path;

        if (MUSIC_VOLUME <= 0) return;

        try {

            stopMusic();

            URL url = SoundManager.class.getResource(path);

            if (url == null) {
                System.out.println("❌ Game over music not found");
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);

            bgmClip = AudioSystem.getClip();
            bgmClip.open(audio);

            if (bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {

                FloatControl gain =
                        (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);

                gain.setValue(convertPercentToDecibel(MUSIC_VOLUME));
            }

            bgmClip.start();

        } catch (Exception e) {

            System.out.println("❌ GameOver music error");
            e.printStackTrace();
        }
    }

    /** Phát âm thanh khi giỏ hứng trúng trứng thường (Cường độ gốc: 70%) */
    public static void playCatch() {
        playSFX("/resources/sound/catch.wav", 70);
    }

    /** Phát âm thanh khi giỏ hứng trúng trứng vàng siêu cấp (Cường độ gốc: 90%) */
    public static void playGold() {
        playSFX("/resources/sound/gold.wav", 90);
    }

    /** Phát âm thanh khi va phải bom nổ (Cường độ gốc: 100%) */
    public static void playExplosion() {
        playSFX("/resources/sound/explosion.wav", 100);
    }

    /** Phát âm thanh khi để trứng thường rơi lọt xuống đất vỡ (Cường độ gốc: 80%) */
    public static void playBreak() {
        playSFX("/resources/sound/break.wav", 80);
    }

    /** Phát âm thanh khi gà đẻ trứng xuất hiện trên thanh điều hướng (Cường độ gốc: 85%) */
    public static void playChicken() {
        playSFX("/resources/sound/chicken.wav", 85);
    }

    /** Phát âm thanh phản hồi khi click nút bấm giao diện (Cường độ gốc: 85%) */
    public static void playUI() {
        playSFX("/resources/sound/click.wav", 85);
    }

    /** Phát âm thanh khi người chơi dịch chuyển giỏ hứng sang trái/phải (Cường độ gốc: 50%) */
    public static void playMove() {
        playSFX("/resources/sound/move.wav", 50);
    }

    /** Phát âm thanh khi nhấn chọn làm mới/chơi lại trận đấu (Cường độ gốc: 85%) */
    public static void playRestart() {
        playSFX("/resources/sound/click.wav", 85);
    }
}