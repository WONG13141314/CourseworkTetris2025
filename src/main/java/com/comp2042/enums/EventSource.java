package com.comp2042.enums;

/**
 * Defines the source of game events.
 * Used to distinguish between user-initiated actions and system-generated events.
 */
public enum EventSource {
    /** Event triggered by user input */
    USER,
    /** Event triggered by game thread (automatic actions) */
    THREAD
}
