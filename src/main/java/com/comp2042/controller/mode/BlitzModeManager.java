package com.comp2042.controller.mode;

import com.comp2042.model.mode.BlitzLevel;
import javafx.scene.control.Label;

/**
 * Manages Blitz mode specific logic (levels, progression)
 */
public class BlitzModeManager {

    private final BlitzLevel blitzLevel;
    private final Label levelLabel;
    private final Label progressLabel;
    private Runnable onLevelUp;

    public BlitzModeManager(Label levelLabel, Label progressLabel) {
        this.blitzLevel = new BlitzLevel();
        this.levelLabel = levelLabel;
        this.progressLabel = progressLabel;
        setupListeners();
    }

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

    private void updateProgressLabel() {
        if (progressLabel != null) {
            progressLabel.setText(blitzLevel.getProgressText() + " LINES");
        }
    }

    public void addLines(int lines) {
        blitzLevel.addLines(lines);
    }

    public int getDropSpeed() {
        return blitzLevel.getDropSpeed();
    }

    public void reset() {
        blitzLevel.reset();
        updateProgressLabel();
        if (levelLabel != null) {
            levelLabel.setText("LEVEL 1");
        }
    }

    public void setOnLevelUp(Runnable callback) {
        this.onLevelUp = callback;
    }
}