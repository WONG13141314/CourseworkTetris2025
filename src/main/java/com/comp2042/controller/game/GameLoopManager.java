package com.comp2042.controller.game;

import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.enums.GameMode;
import com.comp2042.util.GameConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Manages the main game loop for automatic piece dropping.
 */
public class GameLoopManager {

    private Timeline dropTimeline;
    private final GameMode gameMode;
    private Runnable onDropTick;
    private BlitzModeManager blitzModeManager;

    /**
     * Creates a new game loop manager.
     * @param gameMode game mode
     */
    public GameLoopManager(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Sets the Blitz mode manager for speed control.
     * @param manager Blitz mode manager
     */
    public void setBlitzModeManager(BlitzModeManager manager) {
        this.blitzModeManager = manager;
    }

    /**
     * Sets the callback for each drop tick.
     * @param callback drop tick callback
     */
    public void setOnDropTick(Runnable callback) {
        this.onDropTick = callback;
    }

    /**
     * Starts the game loop with initial speed.
     */
    public void start() {
        int speed = getInitialSpeed();
        startWithSpeed(speed);
    }

    /**
     * Starts the game loop with specific speed.
     * @param speed drop speed in milliseconds
     */
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

    /**
     * Stops the game loop.
     */
    public void stop() {
        if (dropTimeline != null) {
            dropTimeline.stop();
        }
    }

    /**
     * Updates speed based on current level.
     */
    public void updateSpeed() {
        if (gameMode == GameMode.BLITZ && blitzModeManager != null) {
            startWithSpeed(blitzModeManager.getDropSpeed());
        }
    }

    /**
     * Gets the initial drop speed.
     * @return initial speed in milliseconds
     */
    private int getInitialSpeed() {
        if (gameMode == GameMode.BLITZ && blitzModeManager != null) {
            return blitzModeManager.getDropSpeed();
        }
        return GameConstants.DEFAULT_DROP_SPEED;
    }
}