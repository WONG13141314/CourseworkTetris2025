package com.comp2042.controller.mode;

import com.comp2042.model.mode.BlitzLevel;
import javafx.scene.control.Label;

/**
 * Manages Blitz mode game logic including level progression.
 */
public class BlitzModeManager {

    private final BlitzLevel blitzLevel;
    private final Label levelLabel;
    private final Label progressLabel;
    private Runnable onLevelUp;

    /**
     * Creates a new Blitz mode manager.
     * @param levelLabel label to display current level
     * @param progressLabel label to display line progress
     */
    public BlitzModeManager(Label levelLabel, Label progressLabel) {
        this.blitzLevel = new BlitzLevel();
        this.levelLabel = levelLabel;
        this.progressLabel = progressLabel;
        setupListeners();
    }

    /**
     * Sets up property listeners for level changes.
     */
    private void setupListeners() {
        blitzLevel.levelProperty().addListener((obs, oldVal, newVal) -> {
            if (levelLabel != null) {
                levelLabel.setText("LEVEL " + newVal);
            }
            if (onLevelUp != null) {
                onLevelUp.run();
            }
        });

        blitzLevel.linesClearedProperty().addListener((obs, oldVal, newVal) -> updateProgressLabel());
        blitzLevel.linesNeededProperty().addListener((obs, oldVal, newVal) -> updateProgressLabel());

        updateProgressLabel();
        if (levelLabel != null) {
            levelLabel.setText("LEVEL 1");
        }
    }

    /**
     * Updates the progress label with current line count.
     */
    private void updateProgressLabel() {
        if (progressLabel != null) {
            progressLabel.setText(blitzLevel.getProgressText() + " LINES");
        }
    }

    /**
     * Adds cleared lines and checks for level up.
     * @param lines number of lines cleared
     */
    public void addLines(int lines) {
        blitzLevel.addLines(lines);
    }

    /**
     * Gets the current drop speed for this level.
     * @return drop speed in milliseconds
     */
    public int getDropSpeed() {
        return blitzLevel.getDropSpeed();
    }

    /**
     * Resets to level 1.
     */
    public void reset() {
        blitzLevel.reset();
        updateProgressLabel();
        if (levelLabel != null) {
            levelLabel.setText("LEVEL 1");
        }
    }

    /**
     * Sets callback for level up events.
     * @param callback callback to run on level up
     */
    public void setOnLevelUp(Runnable callback) {
        this.onLevelUp = callback;
    }
}