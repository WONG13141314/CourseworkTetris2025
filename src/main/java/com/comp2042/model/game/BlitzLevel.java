package com.comp2042.model.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BlitzLevel {
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty linesCleared = new SimpleIntegerProperty(0);
    private final IntegerProperty linesNeeded = new SimpleIntegerProperty(3);

    private static final int[] LINES_PER_LEVEL = {3, 5, 7, 9, 12, 15, 18, 22, 26, 30, 35, 40, 45, 50};

    private static final int[] SPEED_PER_LEVEL = {400, 350, 300, 250, 200, 150, 120, 100, 80, 60, 50, 45, 40, 35};

    public BlitzLevel() {
        reset();
    }

    public void addLines(int lines) {
        int currentLines = linesCleared.get() + lines;
        linesCleared.set(currentLines);

        while (linesCleared.get() >= linesNeeded.get()) {
            levelUp();
        }
    }

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

    public int getDropSpeed() {
        int currentLevel = level.get();
        if (currentLevel - 1 < SPEED_PER_LEVEL.length) {
            return SPEED_PER_LEVEL[currentLevel - 1];
        } else {
            return Math.max(30, 35 - (currentLevel - SPEED_PER_LEVEL.length) * 2);
        }
    }

    public void reset() {
        level.set(1);
        linesCleared.set(0);
        linesNeeded.set(LINES_PER_LEVEL[0]);
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public IntegerProperty linesClearedProperty() {
        return linesCleared;
    }

    public IntegerProperty linesNeededProperty() {
        return linesNeeded;
    }

    public int getLevel() {
        return level.get();
    }

    public int getLinesCleared() {
        return linesCleared.get();
    }

    public int getLinesNeeded() {
        return linesNeeded.get();
    }

    public String getProgressText() {
        return linesCleared.get() + "/" + linesNeeded.get();
    }
}