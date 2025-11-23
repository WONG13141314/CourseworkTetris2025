package com.comp2042.controller.mode;

import javafx.scene.control.Label;

/**
 * Manages Zen mode specific logic (elapsed time tracking, board clear achievements)
 */
public class ZenModeManager {

    private final Label timerLabel;
    private int totalBoardClears = 0;
    private Runnable onBoardCleared;

    public ZenModeManager(Label timerLabel) {
        this.timerLabel = timerLabel;
    }

    /**
     * Called when the board is cleared in Zen mode
     */
    public void notifyBoardCleared() {
        totalBoardClears++;
        if (onBoardCleared != null) {
            onBoardCleared.run();
        }
    }

    /**
     * Get total number of times the board has been cleared
     */
    public int getTotalBoardClears() {
        return totalBoardClears;
    }

    /**
     * Reset Zen mode stats
     */
    public void reset() {
        totalBoardClears = 0;
    }

    /**
     * Set callback for when board is cleared
     */
    public void setOnBoardCleared(Runnable callback) {
        this.onBoardCleared = callback;
    }

    public boolean shouldClearBoard() {
        return true; // In Zen mode, always clear board on game over
    }
}