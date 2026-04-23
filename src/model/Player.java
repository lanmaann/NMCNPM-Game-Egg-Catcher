package model;

import java.awt.*;
import util.ImageLoader;

public class Player {

    private int lane;
    private int maxLane;
    private Image img;

    private int y = 500; // 🔥 chuẩn hoá position player (dùng chung collision system)

    public Player(int laneCount) {
        this.lane = laneCount / 2;
        this.maxLane = laneCount - 1;

        img = ImageLoader.load("/resources/images/basket.png");
    }

    public void moveLeft() {
        if (lane > 0) {
            lane--;
        }
    }

    public void moveRight() {
        if (lane < maxLane) {
            lane++;
        }
    }

    public int getLane() {
        return lane;
    }

    public int getY() {
        return y;
    }


    public void setLane(int lane) {
        if (lane >= 0 && lane <= maxLane) {
            this.lane = lane;
        }
    }

    // 🎯 render
    public void draw(Graphics g, int x, int y) {
        g.drawImage(img, x, y, 50, 50, null);
    }
}