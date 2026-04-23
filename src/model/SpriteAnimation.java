package model;

import java.awt.Image;

public class SpriteAnimation {

    private Image[] frames;
    private int currentFrame = 0;

    private long lastTime;
    private int frameDelay;

    private boolean finished = false;

    public SpriteAnimation(Image[] frames, int fps) {
        this.frames = frames != null ? frames : new Image[0];
        this.frameDelay = Math.max(1, 1000 / fps);
        this.lastTime = System.currentTimeMillis();
    }

    public void update() {

        if (finished || frames.length <= 1) return;

        long now = System.currentTimeMillis();

        if (now - lastTime >= frameDelay) {

            currentFrame++;

            if (currentFrame >= frames.length) {
                currentFrame = frames.length - 1;
                finished = true;
            }

            lastTime = now;
        }
    }

    public Image getCurrentFrame() {
        if (frames.length == 0) return null;
        return frames[currentFrame];
    }

    public boolean isFinished() {
        return finished;
    }

    public void reset() {
        currentFrame = 0;
        finished = false;
        lastTime = System.currentTimeMillis();
    }
}