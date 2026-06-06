package model;
/**
 * ============================================================================
 * UC 1.4: XEM BẢNG XẾP HẠNG
 * Đối tượng dữ liệu lưu trữ thông tin kết quả một lượt chơi của người chơi.
 * ============================================================================
 */
public class Record {

    private int score;
    private int time;

    public Record(int score, int time) {
        this.score = score;
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }
}