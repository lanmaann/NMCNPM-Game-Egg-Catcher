package model;

import java.awt.*;
import util.ImageLoader;

public class Player {

    private int lane;
    private int maxLane;
    private Image img;

    private float sizeRatio = 0.7f;   // chiếm 70% lane
    private float yRatio = 0.85f;
    
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

    public int getY(int screenHeight) {
        return (int)(screenHeight * yRatio);
    }


    public void setLane(int lane) {
        if (lane >= 0 && lane <= maxLane) {
            this.lane = lane;
        }
    }

    // 🎯 render
    public void draw(Graphics g, int centerX, int laneWidth, int screenHeight) {

        int size = (int)(laneWidth * sizeRatio);
        int x = centerX - size / 2;
        int y = getY(screenHeight);

        g.drawImage(img, x, y, size, size, null);
    }
}