package model;

/**
 * =========================================================
 * RECORD
 * =========================================================
 * lớp lưu thông tin kết quả của người chơi
 * 
 * chức năng:
 * - lưu điểm số
 * - lưu thời gian sống sót
 * - dùng cho leaderboard
 * =========================================================
 */
public class Record {

    // điểm số người chơi
    private int score;

    // thời gian sống sót
    private int time;

    /**
     * constructor khởi tạo record
     */
    public Record(int score, int time) {

        this.score = score;

        this.time = time;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    /**
     * lấy điểm số
     */
    public int getScore() {

        return score;
    }

    /**
     * lấy thời gian sống sót
     */
    public int getTime() {

        return time;
    }
}