package util;

import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {

    public static void play(String path) {
        try {
            URL url = SoundManager.class.getResource(path);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {
            System.out.println("❌ Sound error: " + path);
        }
    }

    public static Clip loop(String path) {
        try {
            URL url = SoundManager.class.getResource(path);
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            return clip;

        } catch (Exception e) {
            System.out.println("❌ BGM error");
            return null;
        }
    }

    public static void stop(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}