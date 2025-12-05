package com.comp2042.util;

/**
 * Central location for game constants.
 * Contains all magic numbers and configuration values used throughout the application.
 */
public class GameConstants {

    // UI Constants
    /** Size of each brick cell in pixels */
    public static final int BRICK_SIZE = 20;

    /** Arc size for rounded corners on bricks */
    public static final int BRICK_ARC_SIZE = 5;

    /** Spacing between bricks in pixels */
    public static final double BRICK_SPACING = 1.0;

    // Board dimensions
    /** Total height of game board in cells */
    public static final int BOARD_WIDTH = 25;

    /** Total width of game board in cells */
    public static final int BOARD_HEIGHT = 10;

    /** First visible row (rows 0-1 are hidden spawn area) */
    public static final int VISIBLE_START_ROW = 2;

    // Preview panel size
    /** Size of preview panels for next/hold bricks */
    public static final int PREVIEW_PANEL_SIZE = 4;

    // Game positioning
    /** X position of game panel in pixels */
    public static final double GAME_PANEL_X = 160;

    /** Y position of game panel in pixels */
    public static final double GAME_PANEL_Y = 120;

    /** X offset for brick panel positioning */
    public static final double BRICK_PANEL_X_OFFSET = 5;

    /** Y offset for brick panel positioning */
    public static final double BRICK_PANEL_Y_OFFSET = -2;

    // Timing
    /** Total time for Blitz mode in seconds (2 minutes) */
    public static final int BLITZ_TIME_SECONDS = 120;

    /** Default speed for brick falling in milliseconds */
    public static final int DEFAULT_DROP_SPEED = 400;

    // Input delays (DAS/ARR)
    /** Delayed Auto Shift - initial delay before repeat in milliseconds */
    public static final int INPUT_DELAY_MS = 170;

    /** Auto Repeat Rate - time between repeats in milliseconds */
    public static final int INPUT_REPEAT_MS = 50;

    // Animation
    /** Duration for score notification display in milliseconds */
    public static final int NOTIFICATION_DURATION_MS = 2000;

    /** Duration for notification movement animation in milliseconds */
    public static final int NOTIFICATION_TRANSLATE_DURATION_MS = 2500;

    /** Duration for hover animation effects in milliseconds */
    public static final int HOVER_ANIMATION_DURATION_MS = 200;

    // Shadow opacity
    /** Opacity level for ghost piece shadow (0.0-1.0) */
    public static final double SHADOW_OPACITY = 0.3;

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static members.
     */
    private GameConstants() {
        // Prevent instantiation
    }
}