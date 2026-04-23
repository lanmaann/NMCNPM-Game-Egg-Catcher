package model;

import java.awt.*;
import util.ImageLoader;

public class GoldenEgg extends FallingObject {

    private Image img;

    public GoldenEgg(int lane) {
        super(lane);
        img = ImageLoader.load("/resources/images/goldenEgg.png");
    }

    @Override
    public void onCatch() {
        finish();
    }

    @Override
    public void onMiss() {
        finish();
    }

    @Override
    public void update(GameModel model) {
        // không animation
    }

    @Override
    public void draw(Graphics g, int x, int y) {
        g.drawImage(img, x, y, 32, 32, null);
    }
}