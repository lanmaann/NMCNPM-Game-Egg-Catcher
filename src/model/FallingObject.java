package model;

import java.awt.Graphics;

public abstract class FallingObject {

    protected int lane;
    protected int y = 0;
    protected int speed = 5;

    protected State state = State.FALLING;

    public FallingObject(int lane) {
        this.lane = lane;
    }

    // =====================
    // MOVE
    // =====================
    public void fall() {
        if (state == State.FALLING) {
            y += speed;
        }
    }

    // =====================
    // STATE CHECK
    // =====================
    public boolean isFalling() {
        return state == State.FALLING;
    }

    public boolean isDead() {
        return state == State.DEAD;
    }

    // =====================
    // TRANSITION
    // =====================
    public final void catchObject() {
        if (state != State.FALLING) return;

        state = State.ANIMATING;
        onCatch();
    }

    public final void missObject() {
        if (state != State.FALLING) return;

        state = State.ANIMATING;
        onMiss();
    }

    protected void finish() {
        state = State.DEAD;
    }

    // =====================
    // GETTER
    // =====================
    public int getLane() { return lane; }
    public int getY() { return y; }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public State getState() {
        return state;
    }

    // =====================
    // ABSTRACT
    // =====================
    public abstract void update(GameModel model);
    public abstract void onCatch();
    public abstract void onMiss();
    public abstract void draw(Graphics g, int x, int y);}