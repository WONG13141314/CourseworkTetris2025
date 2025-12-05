package com.comp2042.enums;

/**
 * Defines all possible game event types.
 * Represents the different actions that can be performed on bricks.
 */
public enum EventType {
    /** Move brick downward */
    DOWN,
    /** Move brick left */
    LEFT,
    /** Move brick right */
    RIGHT,
    /** Rotate brick */
    ROTATE,
    /** Instantly drop brick to bottom */
    HARD_DROP,
    /** Hold current brick and swap with held brick */
    HOLD
}
