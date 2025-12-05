package com.comp2042.model.mode;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Manages Blitz mode level progression system.
 * Tracks current level, lines cleared, and calculates drop speed based on level.
 */
public class BlitzLevel {
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty linesCleared = new SimpleIntegerProperty(0);
    private final IntegerProperty linesNeeded = new SimpleIntegerProperty(3);

    private static final int[] LINES_PER_LEVEL = {3, 5, 7, 9, 12, 15, 18, 22, 26, 30, 35, 40, 45, 50};
    private static final int[] SPEED_PER_LEVEL = {400, 350, 300, 250, 200, 150, 120, 100, 80, 60, 50, 45, 40, 35};

    /**
     * Constructs a new BlitzLevel starting at level 1.
     */
    public BlitzLevel() {
        reset();
    }

    /**
     * Adds cleared lines and updates level if threshold is reached.
     * Automatically handles level progression and overflow lines.
     *
     * @param lines number of lines to add
     */
    public void addLines(int lines) {
        if (lines < 0) {
            return;
        }

        int currentLines = linesCleared.get() + lines;
        linesCleared.set(currentLines);

        while (linesCleared.get() >= linesNeeded.get()) {
            levelUp();
        }
    }

    /**
     * Progresses to the next level.
     * Updates lines needed and carries over overflow lines.
     */
    private void levelUp() {
        int currentLevel = level.get();
        int nextLevel = currentLevel + 1;

        int overflow = linesCleared.get() - linesNeeded.get();
        linesCleared.set(overflow);

        level.set(nextLevel);

        if (nextLevel - 1 < LINES_PER_LEVEL.length) {
            linesNeeded.set(LINES_PER_LEVEL[nextLevel - 1]);
        } else {
            linesNeeded.set(linesNeeded.get() + 5);
        }
    }

    /**
     * Calculates drop speed for current level in milliseconds.
     *
     * @return drop speed in milliseconds
     */
    public int getDropSpeed() {
        int currentLevel = level.get();
        if (currentLevel - 1 < SPEED_PER_LEVEL.length) {
            return SPEED_PER_LEVEL[currentLevel - 1];
        } else {
            return Math.max(30, 35 - (currentLevel - SPEED_PER_LEVEL.length) * 2);
        }
    }

    /**
     * Resets level system to initial state (level 1).
     */
    public void reset() {
        level.set(1);
        linesCleared.set(0);
        linesNeeded.set(LINES_PER_LEVEL[0]);
    }

    /**
     * Gets the level property for binding.
     *
     * @return level property
     */
    public IntegerProperty levelProperty() {
        return level;
    }

    /**
     * Gets the lines cleared property for binding.
     *
     * @return lines cleared property
     */
    public IntegerProperty linesClearedProperty() {
        return linesCleared;
    }

    /**
     * Gets the lines needed property for binding.
     *
     * @return lines needed property
     */
    public IntegerProperty linesNeededProperty() {
        return linesNeeded;
    }

    /**
     * Gets current level value.
     *
     * @return current level
     */
    public int getLevel() {
        return level.get();
    }

    /**
     * Gets current lines cleared in this level.
     *
     * @return lines cleared
     */
    public int getLinesCleared() {
        return linesCleared.get();
    }

    /**
     * Gets lines needed to reach next level.
     *
     * @return lines needed
     */
    public int getLinesNeeded() {
        return linesNeeded.get();
    }

    /**
     * Gets formatted progress text (e.g., "5/10").
     *
     * @return progress string
     */
    public String getProgressText() {
        return linesCleared.get() + "/" + linesNeeded.get();
    }
}