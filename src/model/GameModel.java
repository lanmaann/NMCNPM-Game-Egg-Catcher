package model;

import java.util.*;

/**
 * =========================================================
 * GAME MODEL
 * =========================================================
 * lớp quản lý toàn bộ dữ liệu và logic game
 * =========================================================
 */
public class GameModel {

    // =========================================================
    // PLAYER STATE
    // =========================================================
    private Player player;

    private int score = 0;
    private int lives = 3;

    private boolean gameOver = false;
    private boolean paused = false;

    private int tick = 0;
    private int level = 1;

    // =========================================================
    // SCREEN
    // =========================================================
    private int screenHeight = 600;

    // =========================================================
    // TIME SYSTEM
    // =========================================================
    private long startTime = System.currentTimeMillis();
    private long frozenTime = 0;

    // =========================================================
    // OBJECT SYSTEM
    // =========================================================
    private final Random rand = new Random();

    private final ArrayList<FallingObject> objects =
            new ArrayList<>();

    public static final int LANE_COUNT = 5;

    // =========================================================
    // SPAWN SYSTEM
    // =========================================================
    private int spawnCooldown = 0;

    // =========================================================
    // SOUND
    // =========================================================
    private boolean soundEnabled = true;

    // =========================================================
    // INIT
    // =========================================================
    public GameModel() {

        player = new Player(LANE_COUNT);
    }

    // =========================================================
    // SCREEN SYNC
    // =========================================================
    public void setScreenHeight(int h) {

        this.screenHeight = h;
    }

    public int getPlayerY() {

        return (int)(screenHeight * 0.82f);
    }

    // =========================================================
    // SOUND
    // =========================================================
    public void setSoundEnabled(boolean s) {

        this.soundEnabled = s;
    }

    public boolean isSoundEnabled() {

        return soundEnabled;
    }

    // =========================================================
    // RESET
    // =========================================================
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
    // PAUSE
    // =========================================================
    public void togglePause() {

        paused = !paused;

        if (paused) {

            frozenTime = System.currentTimeMillis();
        }

        else {

            startTime +=
                    System.currentTimeMillis()
                            - frozenTime;
        }
    }

    public boolean isPaused() {

        return paused;
    }

    private void freezeTime() {

        frozenTime = System.currentTimeMillis();
    }

    // =========================================================
    // UPDATE LOOP
    // =========================================================
    public void update() {

        if (gameOver || paused) return;

        tick++;

        level = tick / 800 + 1;

        spawn();

        Iterator<FallingObject> it =
                objects.iterator();

        while (it.hasNext()) {

            FallingObject obj = it.next();

            // rơi xuống
            obj.fall();

            // update animation
            obj.update(this);

            // collision
            checkCollision(obj);

            // miss
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
    private void spawn() {

        if (spawnCooldown > 0) {

            spawnCooldown--;

            return;
        }

        // tránh spawn quá sát
        if (!objects.isEmpty()) {

            FallingObject last =
                    objects.get(objects.size() - 1);

            if (last.getY() < screenHeight * 0.2f) {

                return;
            }
        }

        int rate =
                Math.max(25, 60 - level * 4);

        if (rand.nextInt(rate) == 0) {

            int lane =
                    rand.nextInt(LANE_COUNT);

            int type =
                    rand.nextInt(100);

            FallingObject obj;

            // =====================================================
            // RANDOM OBJECT
            // =====================================================
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
            obj.setSpeed(
                    Math.min(10, 3 + level / 2f)
            );

            objects.add(obj);

            spawnCooldown = 18;
        }
    }

    // =========================================================
    // COLLISION
    // =========================================================
    private void checkCollision(FallingObject obj) {

        if (!obj.isFalling()) return;

        int playerY = getPlayerY();

        int tolerance =
                (int)(screenHeight * 0.025f);

        // bomb hitbox to hơn
        if (obj instanceof Bomb) {

            tolerance *= 1.4;
        }

        boolean collide =
                obj.getLane() == player.getLane()
                        &&
                        obj.getY()
                                >= playerY - tolerance
                        &&
                        obj.getY()
                                <= playerY + tolerance;

        if (collide) {

            obj.catchObject();

            applyRule(obj, true);
        }
    }

    // =========================================================
    // MISS
    // =========================================================
    private void checkMiss(FallingObject obj) {

        if (!obj.isFalling()) return;

        int playerY = getPlayerY();

        if (
                obj.getY()
                        >
                        playerY
                                + screenHeight * 0.04f
        ) {

            obj.missObject();

            applyRule(obj, false);
        }
    }

    // =========================================================
    // GAME RULE
    // =========================================================
    private void applyRule(
            FallingObject obj,
            boolean caught
    ) {

        // =====================================================
        // EGG
        // =====================================================
        if (obj instanceof Egg) {

            if (caught) {

                score++;

                if (soundEnabled) {
                    util.SoundManager.playCatch();
                }
            }

            else {

                lives--;

                if (soundEnabled) {
                    util.SoundManager.playBreak();
                }
            }
        }

        // =====================================================
        // BAD EGG
        // =====================================================
        else if (obj instanceof BadEgg) {

            if (caught) {

                score--;
            }
        }

        // =====================================================
        // BOMB
        // =====================================================
        else if (obj instanceof Bomb) {

            if (caught) {

                lives--;

                if (soundEnabled) {
                    util.SoundManager.playExplosion();
                }
            }
        }

        // =====================================================
        // GOLDEN EGG
        // =====================================================
        else if (obj instanceof GoldenEgg) {

            if (caught) {

                score += 5;

                if (soundEnabled) {
                    util.SoundManager.playGold();
                }
            }
        }

        // =====================================================
        // CHICKEN
        // =====================================================
        else if (obj instanceof Chicken) {

            if (caught) {

                lives--;

                if (soundEnabled) {
                    util.SoundManager.playChicken();
                }
            }
        }

        // không cho điểm âm
        if (score < 0) {

            score = 0;
        }

        // =====================================================
        // GAME OVER
        // =====================================================
        if (lives <= 0) {

            lives = 0;

            gameOver = true;

            freezeTime();

            if (soundEnabled) {

                util.SoundManager.playGameOverMusic(
                        "/resources/music/gameover.wav"
                );
            }

            RecordManager.add(
                    score,
                    getTimeSurvived()
            );
        }
    }

    // =========================================================
    // TIME
    // =========================================================
    public int getTimeSurvived() {

        long current;

        if (gameOver || paused) {

            current = frozenTime;
        }

        else {

            current = System.currentTimeMillis();
        }

        return (int)(
                (current - startTime) / 1000
        );
    }

    // =========================================================
    // GETTERS
    // =========================================================
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