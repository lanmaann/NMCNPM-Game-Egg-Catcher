package model;

/**
 * =========================================================
 * OBJECT STATE
 * =========================================================
 * enum quản lý trạng thái của FallingObject
 * 
 * các trạng thái:
 * - FALLING   : object đang rơi
 * - ANIMATING : object đang phát animation
 * - DEAD      : object đã kết thúc
 * =========================================================
 */
public enum State {

    // object đang rơi xuống
    FALLING,

    // object đang chạy animation
    ANIMATING,

    // object đã kết thúc và sẽ bị remove
    DEAD
}