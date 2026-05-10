package model;

import java.awt.Image;

/**
 * =========================================================
 * SPRITE ANIMATION
 * =========================================================
 * lớp quản lý animation sprite theo frame
 * 
 * chức năng:
 * - lưu danh sách frame
 * - cập nhật animation theo thời gian
 * - lấy frame hiện tại
 * - kiểm tra animation kết thúc
 * - reset animation
 * =========================================================
 */
public class SpriteAnimation {

    // danh sách frame animation
    private Image[] frames;

    // frame hiện tại
    private int currentFrame = 0;

    // thời gian frame trước đó
    private long lastTime;

    // thời gian delay giữa các frame
    private int frameDelay;

    // trạng thái animation kết thúc
    private boolean finished = false;

    /**
     * constructor khởi tạo animation
     */
    public SpriteAnimation(Image[] frames, int fps) {

        // tránh null
        this.frames = frames != null ? frames : new Image[0];

        // tính delay theo fps
        this.frameDelay = Math.max(1, 1000 / fps);

        // lưu thời gian bắt đầu
        this.lastTime = System.currentTimeMillis();
    }

    // =========================================================
    // UPDATE
    // =========================================================

    /**
     * cập nhật animation
     */
    public void update() {

        // không update nếu đã finish
        // hoặc chỉ có 1 frame
        if (finished || frames.length <= 1) return;

        long now = System.currentTimeMillis();

        // đủ thời gian đổi frame
        if (now - lastTime >= frameDelay) {

            currentFrame++;

            // =========================================================
            // FINISH ANIMATION
            // =========================================================

            if (currentFrame >= frames.length) {

                // giữ frame cuối cùng
                currentFrame = frames.length - 1;

                finished = true;
            }

            // cập nhật thời gian frame mới
            lastTime = now;
        }
    }

    // =========================================================
    // GET CURRENT FRAME
    // =========================================================

    /**
     * lấy frame hiện tại
     */
    public Image getCurrentFrame() {

        // tránh lỗi mảng rỗng
        if (frames.length == 0) return null;

        return frames[currentFrame];
    }

    // =========================================================
    // CHECK FINISHED
    // =========================================================

    /**
     * kiểm tra animation đã kết thúc chưa
     */
    public boolean isFinished() {

        return finished;
    }

    // =========================================================
    // RESET
    // =========================================================

    /**
     * reset animation về trạng thái ban đầu
     */
    public void reset() {

        currentFrame = 0;

        finished = false;

        lastTime = System.currentTimeMillis();
    }
}