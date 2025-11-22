package com.comp2042.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Centralized color management for Tetris bricks and UI elements
 */
public class ColorPalette {

    // Brick colors
    private static final Paint TRANSPARENT = Color.TRANSPARENT;
    private static final Paint CYAN = Color.AQUA;           // I-piece
    private static final Paint PURPLE = Color.BLUEVIOLET;   // T-piece
    private static final Paint GREEN = Color.DARKGREEN;     // S-piece
    private static final Paint YELLOW = Color.YELLOW;       // O-piece
    private static final Paint RED = Color.RED;             // Z-piece
    private static final Paint BEIGE = Color.BEIGE;         // L-piece
    private static final Paint BROWN = Color.BURLYWOOD;     // J-piece
    private static final Paint WHITE = Color.WHITE;         // Default

    // Shadow colors (with opacity)
    private static final Paint SHADOW_CYAN = Color.rgb(0, 255, 255, 0.6);
    private static final Paint SHADOW_PURPLE = Color.rgb(138, 43, 226, 0.6);
    private static final Paint SHADOW_GREEN = Color.rgb(0, 80, 0, 0.8);
    private static final Paint SHADOW_YELLOW = Color.rgb(255, 255, 0, 0.6);
    private static final Paint SHADOW_RED = Color.rgb(255, 0, 0, 0.6);
    private static final Paint SHADOW_BEIGE = Color.rgb(245, 245, 220, 0.6);
    private static final Paint SHADOW_BROWN = Color.rgb(222, 184, 135, 0.6);

    /**
     * Get the fill color for a brick type
     * @param brickType The brick type (0-7)
     * @return Paint color for the brick
     */
    public static Paint getBrickColor(int brickType) {
        switch (brickType) {
            case 0: return TRANSPARENT;
            case 1: return CYAN;
            case 2: return PURPLE;
            case 3: return GREEN;
            case 4: return YELLOW;
            case 5: return RED;
            case 6: return BEIGE;
            case 7: return BROWN;
            default: return WHITE;
        }
    }

    /**
     * Get the shadow color for a brick type
     * @param brickType The brick type (1-7)
     * @return Paint color for the shadow
     */
    public static Paint getShadowColor(int brickType) {
        switch (brickType) {
            case 1: return SHADOW_CYAN;
            case 2: return SHADOW_PURPLE;
            case 3: return SHADOW_GREEN;
            case 4: return SHADOW_YELLOW;
            case 5: return SHADOW_RED;
            case 6: return SHADOW_BEIGE;
            case 7: return SHADOW_BROWN;
            default: return TRANSPARENT;
        }
    }
}