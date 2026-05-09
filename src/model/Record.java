package model;

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