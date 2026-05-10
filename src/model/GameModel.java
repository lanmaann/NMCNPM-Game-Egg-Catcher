package model;

import java.util.*;

/**
 * =========================================================
 * GAME MODEL
 * =========================================================
 * lớp quản lý toàn bộ dữ liệu và logic game
 * 
 * chức năng:
 * - quản lý player
 * - quản lý điểm số và mạng
 * - quản lý object rơi
 * - xử lý collision
 * - xử lý spawn object
 * - xử lý pause / game over
 * - quản lý thời gian chơi
 * =========================================================
 */
public class GameModel {

    // =========================================================
    // PLAYER STATE
    // =========================================================

    // người chơi
    private Player player;

    // điểm số
    private int score = 0;

    // số mạng
    private int lives = 3;

    // trạng thái game over
    private boolean gameOver = false;

    // trạng thái pause
    private boolean paused = false;

    // tick game loop
    private int tick = 0;

    // level hiện tại
    private int level = 1;

    // =========================================================
    // SCREEN
    // =========================================================

    // chiều cao màn hình
    private int screenHeight = 600;

    // =========================================================
    // TIME SYSTEM
    // =========================================================

    // thời gian bắt đầu
    private long startTime = System.currentTimeMillis();

    // thời gian đóng băng khi pause
    private long frozenTime = 0;

    // =========================================================
    // OBJECT SYSTEM
    // =========================================================

    // random object
    private final Random rand = new Random();

    // danh sách object đang tồn tại
    private final ArrayList<FallingObject> objects = new ArrayList<>();

    // số lượng lane
    public static final int LANE_COUNT = 5;

    // =========================================================
    // SPAWN SYSTEM
    // =========================================================

    // cooldown spawn
    private int spawnCooldown = 0;

    // trạng thái âm thanh
    private boolean soundEnabled = true;

    // =========================================================
    // INIT
    // =========================================================

    /**
     * constructor khởi tạo game model
     */
    public GameModel() {

        player = new Player(LANE_COUNT);
    }

    // =========================================================
    // SCREEN SYNC
    // =========================================================

    /**
     * cập nhật chiều cao màn hình
     */
    public void setScreenHeight(int h) {

        this.screenHeight = h;
    }

    /**
     * lấy vị trí Y của player
     */
    public int getPlayerY() {

        return (int)(screenHeight * 0.82f);
    }

    /**
     * bật / tắt âm thanh
     */
    public void setSoundEnabled(boolean s) {

        this.soundEnabled = s;
    }

    /**
     * kiểm tra âm thanh
     */
    public boolean isSoundEnabled() {

        return soundEnabled;
    }

    // =========================================================
    // RESET
    // =========================================================

    /**
     * reset toàn bộ game
     */
    public void reset() {

        score = 0;

        lives = 3;

        gameOver = false;

        paused = false;

        tick = 0;

        level = 1;

        startTime = System.currentTimeMillis();

        frozenTime = 0;

        objects.clear();

        spawnCooldown = 0;
    }

    // =========================================================
    // PAUSE SYSTEM
    // =========================================================

    /**
     * chuyển đổi trạng thái pause
     */
    public void togglePause() {

        paused = !paused;

        // lưu thời gian pause
        if (paused) {

            frozenTime = System.currentTimeMillis();
        }

        // tiếp tục game
        else {

            startTime += System.currentTimeMillis() - frozenTime;
        }
    }

    /**
     * kiểm tra trạng thái pause
     */
    public boolean isPaused() {

        return paused;
    }

    /**
     * đóng băng thời gian khi game over
     */
    private void freezeTime() {

        frozenTime = System.currentTimeMillis();
    }

    // =========================================================
    // UPDATE LOOP
    // =========================================================

    /**
     * cập nhật game mỗi frame
     */
    public void update() {

        // không update khi pause hoặc game over
        if (gameOver || paused) return;

        // tăng tick
        tick++;

        // tăng level theo thời gian
        level = tick / 800 + 1;

        // spawn object mới
        spawn();

        Iterator<FallingObject> it = objects.iterator();

        while (it.hasNext()) {

            FallingObject obj = it.next();

            // xử lý rơi
            obj.fall();

            // update object
            obj.update(this);

            // kiểm tra va chạm
            checkCollision(obj);

            // kiểm tra miss
            checkMiss(obj);

            // remove object chết
            if (obj.isDead()) {

                it.remove();
            }
        }
    }

    // =========================================================
    // SPAWN
    // =========================================================

    /**
     * sinh object mới
     */
    private void spawn() {

        // cooldown spawn
        if (spawnCooldown > 0) {

            spawnCooldown--;

            return;
        }

        // tránh spawn object quá sát nhau
        if (!objects.isEmpty()) {

            FallingObject last = objects.get(objects.size() - 1);

            if (last.getY() < screenHeight * 0.2f) return;
        }

        // tăng tần suất spawn theo level
        int rate = Math.max(25, 60 - level * 4);

        // random spawn
        if (rand.nextInt(rate) == 0) {

            int lane = rand.nextInt(LANE_COUNT);

            int type = rand.nextInt(100);

            FallingObject obj;

            // =========================================================
            // RANDOM OBJECT TYPE
            // =========================================================

            if (type < 50) {

                obj = new Egg(lane);
            }

            else if (type < 75) {

                obj = new BadEgg(lane);
            }

            else if (type < 90) {

                obj = new Bomb(lane);
            }

            else if (type < 97) {

                obj = new GoldenEgg(lane);
            }

            else {

                obj = new Chicken(lane);
            }

            // tăng tốc độ theo level
            obj.setSpeed(Math.min(10, 3 + level / 2));

            // add object vào game
            objects.add(obj);

            // reset cooldown
            spawnCooldown = 18;
        }
    }

    // =========================================================
    // COLLISION
    // =========================================================

    /**
     * kiểm tra va chạm với player
     */
    private void checkCollision(FallingObject obj) {

        // chỉ check object đang rơi
        if (!obj.isFalling()) return;

        int playerY = getPlayerY();

        // tolerance va chạm
        int tolerance = (int)(screenHeight * 0.025f);

        // bomb có hitbox lớn hơn
        if (obj instanceof Bomb) {

            tolerance *= 1.4;
        }

        // kiểm tra cùng lane và đúng vị trí
        if (obj.getLane() == player.getLane()
                && obj.getY() >= playerY - tolerance
                && obj.getY() <= playerY + tolerance) {

            obj.catchObject();

            applyRule(obj, true);
        }
    }

    // =========================================================
    // MISS CHECK
    // =========================================================

    /**
     * kiểm tra object rơi hụt
     */
    private void checkMiss(FallingObject obj) {

        // chỉ check object đang rơi
        if (!obj.isFalling()) return;

        int playerY = getPlayerY();

        // object vượt qua player
        if (obj.getY() > playerY + screenHeight * 0.04f) {

            obj.missObject();

            applyRule(obj, false);
        }
    }

    // =========================================================
    // GAME RULE
    // =========================================================

    /**
     * áp dụng luật game
     */
    private void applyRule(FallingObject obj, boolean caught) {

        // =========================================================
        // EGG
        // =========================================================

        if (obj instanceof Egg) {

            if (caught) {

                score++;
            }

            else {

                lives--;
            }
        }

        // =========================================================
        // BAD EGG
        // =========================================================

        else if (obj instanceof BadEgg) {

            if (caught) {

                score--;
            }
        }

        // =========================================================
        // BOMB
        // =========================================================

        else if (obj instanceof Bomb) {

            if (caught) {

                lives--;
            }
        }

        // =========================================================
        // GOLDEN EGG
        // =========================================================

        else if (obj instanceof GoldenEgg) {

            if (caught) {

                score += 5;
            }
        }

        // =========================================================
        // CHICKEN
        // =========================================================

        else if (obj instanceof Chicken) {

            if (caught) {

                lives--;
            }
        }

        // không cho điểm âm
        if (score < 0) {

            score = 0;
        }

        // =========================================================
        // GAME OVER
        // =========================================================

        if (lives <= 0) {

            lives = 0;

            gameOver = true;

            freezeTime();

            RecordManager.add(score, getTimeSurvived());
        }
    }

    // =========================================================
    // TIME
    // =========================================================

    /**
     * lấy thời gian sống sót
     */
    public int getTimeSurvived() {

        long current;

        // đóng băng thời gian khi pause/game over
        if (gameOver || paused) {

            current = frozenTime;
        }

        // thời gian hiện tại
        else {

            current = System.currentTimeMillis();
        }

        // đổi sang giây
        return (int) ((current - startTime) / 1000);
    }

    // =========================================================
    // GETTERS
    // =========================================================

    /**
     * lấy player
     */
    public Player getPlayer() {

        return player;
    }

    /**
     * lấy danh sách object
     */
    public ArrayList<FallingObject> getObjects() {

        return objects;
    }

    /**
     * lấy điểm số
     */
    public int getScore() {

        return score;
    }

    /**
     * lấy số mạng
     */
    public int getLives() {

        return lives;
    }

    /**
     * lấy level
     */
    public int getLevel() {

        return level;
    }

    /**
     * kiểm tra game over
     */
    public boolean isGameOver() {

        return gameOver;
    }
}