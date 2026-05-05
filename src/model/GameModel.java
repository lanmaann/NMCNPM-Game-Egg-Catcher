package model;

import java.util.*;

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
    // SCREEN (🔥 NEW)
    // =========================
    private int screenHeight = 600;

    // =========================
    // TIME SYSTEM
    // =========================
    private long startTime = System.currentTimeMillis();
    private long frozenTime = 0;

    // =========================
    // OBJECTS
    // =========================
    private final Random rand = new Random();
    private final ArrayList<FallingObject> objects = new ArrayList<>();

    public static final int LANE_COUNT = 5;

    // =========================
    // SPAWN
    // =========================
    private int spawnCooldown = 0;
    
    private boolean soundEnabled = true;


    // =========================================================
    // INIT
    // =========================================================
    public GameModel() {
        player = new Player(LANE_COUNT);
    }

    // =========================================================
    // SCREEN SYNC (🔥 QUAN TRỌNG)
    // =========================================================
    public void setScreenHeight(int h) {
        this.screenHeight = h;
    }

    public int getPlayerY() {
        return (int)(screenHeight * 0.82f); // 🔥 vị trí player
    }
    
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

    // =========================================================
    // UPDATE LOOP
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
    // SPAWN
    // =========================================================
    private void spawn() {

        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        if (!objects.isEmpty()) {
            FallingObject last = objects.get(objects.size() - 1);
            if (last.getY() < screenHeight * 0.2f) return;
        }

        int rate = Math.max(25, 60 - level * 4);

        if (rand.nextInt(rate) == 0) {

            int lane = rand.nextInt(LANE_COUNT);
            int type = rand.nextInt(100);

            FallingObject obj;

            if (type < 50) obj = new Egg(lane);
            else if (type < 75) obj = new BadEgg(lane);
            else if (type < 90) obj = new Bomb(lane);
            else if (type < 97) obj = new GoldenEgg(lane);
            else obj = new Chicken(lane);   

            obj.setSpeed(Math.min(10, 3 + level / 2));

            objects.add(obj);

            spawnCooldown = 18;
        }
    }

    // =========================================================
    // COLLISION (🔥 FIX VỠ TRÊN TRỜI)
    // =========================================================
    private void checkCollision(FallingObject obj) {

        if (!obj.isFalling()) return;

        int playerY = getPlayerY();

        // 🔥 tolerance nhỏ lại để không bắt sớm
        int tolerance = (int)(screenHeight * 0.025f);

        // 🔥 bomb to hơn → hitbox lớn hơn
        if (obj instanceof Bomb) {
            tolerance *= 1.4;
        }

        if (obj.getLane() == player.getLane()
                && obj.getY() >= playerY - tolerance
                && obj.getY() <= playerY + tolerance) {

            obj.catchObject();
            applyRule(obj, true);
        }
    }

    // =========================================================
    // MISS (🔥 FIX LỆCH)
    // =========================================================
    private void checkMiss(FallingObject obj) {

        if (!obj.isFalling()) return;

        int playerY = getPlayerY();

        if (obj.getY() > playerY + screenHeight * 0.04f) {

            obj.missObject();
            applyRule(obj, false);
        }
    }

    // =========================================================
    // RULE
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
        
        else if (obj instanceof Chicken) {
            if (caught) lives--;
        }

        // 🔥 không cho điểm âm
        if (score < 0) score = 0;

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