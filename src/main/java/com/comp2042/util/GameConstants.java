package com.comp2042.util;

/**
 * Central location for game constants
 */
public class GameConstants {

    // UI Constants
    public static final int BRICK_SIZE = 20;
    public static final int BRICK_ARC_SIZE = 5;
    public static final double BRICK_SPACING = 1.0;

    // Board dimensions
    public static final int BOARD_WIDTH = 25;
    public static final int BOARD_HEIGHT = 10;
    public static final int VISIBLE_START_ROW = 2;

    // Preview panel size
    public static final int PREVIEW_PANEL_SIZE = 4;

    // Game positioning
    public static final double GAME_PANEL_X = 160;
    public static final double GAME_PANEL_Y = 120;
    public static final double BRICK_PANEL_X_OFFSET = 5;
    public static final double BRICK_PANEL_Y_OFFSET = -2;

    // Timing
    public static final int BLITZ_TIME_SECONDS = 120; // 2 minutes
    public static final int DEFAULT_DROP_SPEED = 400; // milliseconds

    // Input delays (DAS/ARR)
    public static final int INPUT_DELAY_MS = 170;
    public static final int INPUT_REPEAT_MS = 50;

    // Animation
    public static final int NOTIFICATION_DURATION_MS = 2000;
    public static final int NOTIFICATION_TRANSLATE_DURATION_MS = 2500;
    public static final int HOVER_ANIMATION_DURATION_MS = 200;

    // Shadow opacity
    public static final double SHADOW_OPACITY = 0.3;

    private GameConstants() {
        // Prevent instantiation
    }
}