package com.comp2042.controller.mode;

import javafx.scene.control.Label;

/**
 * Manages Zen mode game logic including board clear tracking.
 */
public class ZenModeManager {

    private final Label timerLabel;
    private int totalBoardClears = 0;
    private Runnable onBoardCleared;

    /**
     * Creates a new Zen mode manager.
     * @param timerLabel label to display elapsed time
     */
    public ZenModeManager(Label timerLabel) {
        this.timerLabel = timerLabel;
    }

    /**
     * Called when the board is completely cleared.
     */
    public void notifyBoardCleared() {
        totalBoardClears++;
        if (onBoardCleared != null) {
            onBoardCleared.run();
        }
    }

    /**
     * Gets total number of board clears.
     * @return total board clears
     */
    public int getTotalBoardClears() {
        return totalBoardClears;
    }

    /**
     * Resets Zen mode statistics.
     */
    public void reset() {
        totalBoardClears = 0;
    }

    /**
     * Sets callback for board clear events.
     * @param callback callback to run on board clear
     */
    public void setOnBoardCleared(Runnable callback) {
        this.onBoardCleared = callback;
    }

    /**
     * Determines if board should be cleared on game over.
     * @return true (always clear in Zen mode)
     */
    public boolean shouldClearBoard() {
        return true;
    }
}