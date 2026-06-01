package model;

import java.util.*;

/**
 * Game Model
 * Lớp quản lý toàn bộ dữ liệu và logic của trò chơi.
 */
public class GameModel {

    /** Player State */
    private Player player;

    private int score = 0;
    private int lives = 3;

    private boolean gameOver = false;
    private boolean paused = false;

    private int tick = 0;
    private int level = 1;

    /** Screen */
    private int screenHeight = 600;

    /** Time System */
    private long startTime = System.currentTimeMillis();
    private long frozenTime = 0;

    /** Object System */
    private final Random rand = new Random();

    private final ArrayList<FallingObject> objects = new ArrayList<>();

    public static final int LANE_COUNT = 5;

    /** Spawn System */
    private int spawnCooldown = 0;

    /** Sound */
    private boolean soundEnabled = true;

    /** Init */
    public GameModel() {
        player = new Player(LANE_COUNT);
    }

    /** Screen Sync */
    public void setScreenHeight(int h) {
        this.screenHeight = h;
    }

    public int getPlayerY() {
        return (int)(screenHeight * 0.82f);
    }

    /** Sound Configuration */
    public void setSoundEnabled(boolean s) {
        this.soundEnabled = s;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    /** Reset */
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

    /** Pause */
    public void togglePause() {
        paused = !paused;

        if (paused) {
            frozenTime = System.currentTimeMillis();
        } else {
            startTime += System.currentTimeMillis() - frozenTime;
        }
    }

    public boolean isPaused() {
        return paused;
    }

    private void freezeTime() {
        frozenTime = System.currentTimeMillis();
    }

    /**
     * Hàm cập nhật trạng thái game theo từng khung hình của Game Loop
     */
    public void update() {
        if (gameOver || paused) return;

        tick++;
        level = tick / 800 + 1;

        // [2.1.1] Hệ thống tạo vật thể ngẫu nhiên (Egg, GoldenEgg, BadEgg, Bomb, Chicken) ở phía trên màn hình
        spawn();

        Iterator<FallingObject> it = objects.iterator();

        while (it.hasNext()) {
            FallingObject obj = it.next();

            // [2.1.2] Vật thể rơi tự do xuống dưới theo trục Y
            obj.fall();

            // update animation
            obj.update(this);

            // [2.1.4] Hệ thống kiểm tra va chạm giữa nhân vật và vật thể
            checkCollision(obj);

            // [2.1.7] & [2.2.1] Kiểm tra khi vật thể lọt xuống cạnh đáy màn hình
            checkMiss(obj);

            // [2.1.7] Hệ thống hủy/remove object đã chết khỏi bộ nhớ
            if (obj.isDead()) {
                it.remove();
            }
        }
    }

    /** Spawn Logic */
    private void spawn() {
        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        // tránh spawn quá sát
        if (!objects.isEmpty()) {
            FallingObject last = objects.get(objects.size() - 1);
            if (last.getY() < screenHeight * 0.2f) {
                return;
            }
        }

        int rate = Math.max(25, 60 - level * 4);

        if (rand.nextInt(rate) == 0) {
            int lane = rand.nextInt(LANE_COUNT);
            int type = rand.nextInt(100);
            FallingObject obj;

            /** [2.1.1] Khởi tạo ngẫu nhiên cụ thể từng thực thể loại vật thể */
            if (type < 50) {
                obj = new Egg(lane);
            } else if (type < 75) {
                obj = new BadEgg(lane);
            } else if (type < 90) {
                obj = new Bomb(lane);
            } else if (type < 97) {
                obj = new GoldenEgg(lane);
            } else {
                obj = new Chicken(lane);
            }

            // tăng tốc độ theo level
            obj.setSpeed(Math.min(10, 3 + level / 2f));
            objects.add(obj);
            spawnCooldown = 18;
        }
    }

    /** Collision Logic */
    private void checkCollision(FallingObject obj) {
        if (!obj.isFalling()) return;

        int playerY = getPlayerY();
        int tolerance = (int)(screenHeight * 0.025f);

        // bomb hitbox to hơn
        if (obj instanceof Bomb) {
            tolerance *= 1.4;
        }

        // [2.1.4] Thuật toán so khớp tọa độ Lane (Trục X) và khoảng cách Y (Hitbox)
        boolean collide = obj.getLane() == player.getLane()
                && obj.getY() >= playerY - tolerance
                && obj.getY() <= playerY + tolerance;

        if (collide) {
            // [2.1.7] Kích hoạt trạng thái chuyển đổi biến mất của vật thể (bắt đầu nổ hoặc vỡ)
            obj.catchObject();

            // Áp dụng luật tính điểm/trừ mạng khi va chạm thành công
            applyRule(obj, true);
        }
    }

    /** Miss Logic */
    private void checkMiss(FallingObject obj) {
        if (!obj.isFalling()) return;

        int playerY = getPlayerY();

        // [2.1.7] & [2.2.1] Xác định vật thể vượt quá tầm giỏ hứng (Rơi khỏi cạnh dưới)
        if (obj.getY() > playerY + screenHeight * 0.04f) {
            // [2.1.7] Đánh dấu vật thể vỡ vụn dưới đất để chuẩn bị hủy
            obj.missObject();

            // Áp dụng luật phạt lọt lưới
            applyRule(obj, false);
        }
    }

    /** Game Rules Processing */
    private void applyRule(FallingObject obj, boolean caught) {
        
        /** [2.1.5] XỬ LÝ ĐỐI TƯỢNG TRỨNG THƯỜNG (EGG) */
        if (obj instanceof Egg) {
            if (caught) {
                score++;
                if (soundEnabled) {
                    util.SoundManager.playCatch();
                }
            } else {
                lives--;
                if (soundEnabled) {
                    util.SoundManager.playBreak();
                }
            }
        }

        /** XỬ LÝ ĐỐI TƯỢNG TRỨNG THỐI (BAD EGG) */
        else if (obj instanceof BadEgg) {
            if (caught) {
                score--;
            }
            // Thỏa mãn [2.2.1]: Nếu người chơi né tránh thành công (lọt lưới), hệ thống hủy vật thể và không trừ mạng.
        }

        /** [2.1.6] XỬ LÝ ĐỐI TƯỢNG CHƯỚNG NGẠI VẬT: BOM (BOMB) */
        else if (obj instanceof Bomb) {
            if (caught) {
                lives--;
                if (soundEnabled) {
                    util.SoundManager.playExplosion();
                }
            }
            // Thỏa mãn [2.2.1]: Người chơi né thành công Quả Bom rơi xuống cạnh dưới -> Hệ thống hủy vật thể và không trừ mạng.
        }

        /** [2.2.2] NGƯỜI CHƠI HỨNG ĐƯỢC TRỨNG VÀNG (GOLDEN EGG) */
        else if (obj instanceof GoldenEgg) {
            if (caught) {
                score += 5;
                if (soundEnabled) {
                    util.SoundManager.playGold();
                }
            }
        }

        /** [2.1.6] XỬ LÝ ĐỐI TƯỢNG CHƯỚNG NGẠI VẬT: CON GÀ (CHICKEN) */
        else if (obj instanceof Chicken) {
            if (caught) {
                lives--;
                if (soundEnabled) {
                    util.SoundManager.playChicken();
                }
            }
            // Thỏa mãn [2.2.1]: Người chơi né thành công Con Gà rơi xuống cạnh dưới -> Hệ thống hủy vật thể và không trừ mạng.
        }

        // Không cho điểm số hiển thị số âm
        if (score < 0) {
            score = 0;
        }

        /** [2.2.3] SỐ MẠNG VỀ BẰNG 0 (MẤT MẠNG CUỐI CÙNG) */
        if (lives <= 0) {
            lives = 0;
            gameOver = true;

            // Đóng băng bộ đếm thời gian sống sót
            freezeTime();

            if (soundEnabled) {
                util.SoundManager.playGameOverMusic("/resources/music/gameover.wav");
            }

            // [2.2.3] Gọi tiến trình lưu trữ dữ liệu điểm số cuối cùng vào file kỉ lục
            RecordManager.add(score, getTimeSurvived());
        }
    }

    /** Time Processing */
    public int getTimeSurvived() {
        long current;

        if (gameOver || paused) {
            current = frozenTime;
        } else {
            current = System.currentTimeMillis();
        }

        return (int)((current - startTime) / 1000);
    }

    /** Getters */
    public Player getPlayer() {
        return player;
    }

    public ArrayList<FallingObject> getObjects() {
        return objects;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getLevel() {
        return level;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}