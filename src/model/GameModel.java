package model;

import java.util.*;

/**
 * =========================================================
 * GAME MODEL - CORE LOGIC
 * =========================================================
 * FEATURES:
 * - Gameplay logic only (NO UI)
 * - Spawn system (controlled)
 * - Collision + Miss system
 * - Time system (pause + gameOver freeze)
 * - Future-ready PAUSE system
 * =========================================================
 */
public class GameModel {

    // =========================
    // PLAYER STATE
    // =========================
    private Player player;

    private int score = 0;
    private int lives = 3;
    private boolean gameOver = false;
    private boolean paused = false;

    private int tick = 0;
    private int level = 1;

    // =========================
    // TIME SYSTEM (ARCADE STYLE)
    // =========================
    private long startTime = System.currentTimeMillis();
    private long frozenTime = 0; // ❄️ dùng khi pause / game over

    // =========================
    // GAME OBJECTS
    // =========================
    private final Random rand = new Random();
    private final ArrayList<FallingObject> objects = new ArrayList<>();

    public static final int LANE_COUNT = 5;
    private static final int PLAYER_Y = 500;

    // =========================
    // SPAWN CONTROL
    // =========================
    private int spawnCooldown = 0;

    // =========================================================
    // INIT
    // =========================================================
    public GameModel() {
        player = new Player(LANE_COUNT);
    }

    // =========================================================
    // RESET GAME
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
    // PAUSE SYSTEM (FUTURE READY)
    // =========================================================
    public void togglePause() {
        paused = !paused;

        if (paused) {
            frozenTime = System.currentTimeMillis();
        } else {
            // adjust startTime để không bị cộng time lúc pause
            startTime += System.currentTimeMillis() - frozenTime;
        }
    }

    public boolean isPaused() {
        return paused;
    }

    // =========================================================
    // FREEZE TIME (GAME OVER / PAUSE SUPPORT)
    // =========================================================
    private void freezeTime() {
        frozenTime = System.currentTimeMillis();
    }

    // =========================================================
    // MAIN UPDATE LOOP
    // =========================================================
    public void update() {

        if (gameOver || paused) return;

        tick++;
        level = tick / 800 + 1;

        spawn();

        Iterator<FallingObject> it = objects.iterator();

        while (it.hasNext()) {

            FallingObject obj = it.next();

            obj.fall();
            obj.update(this);

            checkCollision(obj);
            checkMiss(obj);

            if (obj.isDead()) {
                it.remove();
            }
        }
    }

    // =========================================================
    // SPAWN SYSTEM (SEQUENTIAL CONTROL)
    // =========================================================
    private void spawn() {

        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        if (!objects.isEmpty()) {
            FallingObject last = objects.get(objects.size() - 1);
            if (last.getY() < 120) return;
        }

        int rate = Math.max(25, 60 - level * 4);

        if (rand.nextInt(rate) == 0) {

            int lane = rand.nextInt(LANE_COUNT);
            int type = rand.nextInt(100);

            FallingObject obj;

            if (type < 50) obj = new Egg(lane);
            else if (type < 75) obj = new BadEgg(lane);
            else if (type < 95) obj = new Bomb(lane);
            else obj = new GoldenEgg(lane);

            obj.setSpeed(Math.min(10, 3 + level / 2));

            objects.add(obj);

            spawnCooldown = 18;
        }
    }

    // =========================================================
    // COLLISION
    // =========================================================
    private void checkCollision(FallingObject obj) {

        if (!obj.isFalling()) return;

        if (obj.getLane() == player.getLane()
                && Math.abs(obj.getY() - PLAYER_Y) < 25) {

            obj.catchObject();
            applyRule(obj, true);
        }
    }

    // =========================================================
    // MISS
    // =========================================================
    private void checkMiss(FallingObject obj) {

        if (!obj.isFalling()) return;

        if (obj.getY() > PLAYER_Y + 20) {

            obj.missObject();
            applyRule(obj, false);
        }
    }

    // =========================================================
    // RULE ENGINE
    // =========================================================
    private void applyRule(FallingObject obj, boolean caught) {

        if (obj instanceof Egg) {
            if (caught) score++;
            else lives--;
        }

        else if (obj instanceof BadEgg) {
            if (caught) score--;
        }

        else if (obj instanceof Bomb) {
            if (caught) lives--;
        }

        else if (obj instanceof GoldenEgg) {
            if (caught) score += 5;
        }

        if (lives <= 0) {
            gameOver = true;
            freezeTime(); // ❄️ đóng băng thời gian
            RecordManager.add(score, getTimeSurvived());
        }
    }

    // =========================================================
    // TIME SYSTEM (FREEZE SAFE)
    // =========================================================
    public int getTimeSurvived() {

        long current;

        if (gameOver || paused) {
            current = frozenTime;
        } else {
            current = System.currentTimeMillis();
        }

        return (int) ((current - startTime) / 1000);
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