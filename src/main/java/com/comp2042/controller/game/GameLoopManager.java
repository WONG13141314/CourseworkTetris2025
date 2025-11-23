package com.comp2042.controller.game;

import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.enums.GameMode;
import com.comp2042.util.GameConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the main game loop (dropping pieces)
 */
public class GameLoopManager {

    private Timeline dropTimeline;
    private final GameMode gameMode;
    private Runnable onDropTick;
    private BlitzModeManager blitzModeManager;

    public GameLoopManager(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setBlitzModeManager(BlitzModeManager manager) {
        this.blitzModeManager = manager;
    }

    public void setOnDropTick(Runnable callback) {
        this.onDropTick = callback;
    }

    public void start() {
        int speed = getInitialSpeed();
        startWithSpeed(speed);
    }

    public void startWithSpeed(int speed) {
        if (dropTimeline != null) {
            dropTimeline.stop();
        }

        dropTimeline = new Timeline(new KeyFrame(
                Duration.millis(speed),
                ae -> {
                    if (onDropTick != null) {
                        onDropTick.run();
                    }
                }
        ));
        dropTimeline.setCycleCount(Timeline.INDEFINITE);
        dropTimeline.play();
    }

    public void stop() {
        if (dropTimeline != null) {
            dropTimeline.stop();
        }
    }

    public void updateSpeed() {
        if (gameMode == GameMode.BLITZ && blitzModeManager != null) {
            startWithSpeed(blitzModeManager.getDropSpeed());
        }
    }

    private int getInitialSpeed() {
        if (gameMode == GameMode.BLITZ && blitzModeManager != null) {
            return blitzModeManager.getDropSpeed();
        }
        return GameConstants.DEFAULT_DROP_SPEED;
    }
}