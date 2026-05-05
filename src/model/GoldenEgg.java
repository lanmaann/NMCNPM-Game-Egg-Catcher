package model;

import java.awt.*;
import util.ImageLoader;

public class GoldenEgg extends FallingObject {

    private Image img;
    private int timer = 0;

    public GoldenEgg(int lane) {
        super(lane);
        img = ImageLoader.load("/resources/images/goldenEgg.png");
    }

    @Override
    public void onCatch() {
        timer = 10; // delay nhẹ
    }

    @Override
    public void onMiss() {
        timer = 10;
    }

    @Override
    public void update(GameModel model) {
        if (timer > 0) {
            timer--;
            if (timer == 0) finish();
        }
    }

    @Override
    public void draw(Graphics g, int centerX, int laneWidth) {

        int size = (int)(laneWidth * 0.5f);
        int x = centerX - size / 2;
        int yDraw = (int) y;

        g.drawImage(img, x, yDraw, size, size, null);
    }
}