package com.comp2042.util;

import com.comp2042.enums.GameMode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Manages game timers for Blitz countdown and Zen elapsed time.
 */
public class TimerManager {

    private final GameMode gameMode;
    private final Label timerLabel;
    private GameTimer zenTimer;
    private Timeline blitzTimeline;
    private int blitzTimeRemaining;
    private Runnable onBlitzTimeUp;
    private boolean isCleanedUp = false;

    /**
     * Creates a new timer manager.
     * @param gameMode game mode (ZEN or BLITZ)
     * @param timerLabel label to display timer
     */
    public TimerManager(GameMode gameMode, Label timerLabel) {
        this.gameMode = gameMode;
        this.timerLabel = timerLabel;

        if (gameMode == GameMode.ZEN) {
            setupZenTimer();
        } else if (gameMode == GameMode.BLITZ) {
            setupBlitzTimer();
        }
    }

    /**
     * Sets up timer for Zen mode (counts up).
     */
    private void setupZenTimer() {
        zenTimer = new GameTimer();
        zenTimer.elapsedSecondsProperty().addListener((observable, oldValue, newValue) -> {
            if (timerLabel != null) {
                timerLabel.setText(formatTime(newValue.intValue()));
            }
        });
        if (timerLabel != null) {
            timerLabel.setText(formatTime(0));
        }
    }

    /**
     * Sets up timer for Blitz mode (counts down).
     */
    private void setupBlitzTimer() {
        if (blitzTimeline != null) {
            blitzTimeline.stop();
        }

        blitzTimeRemaining = GameConstants.BLITZ_TIME_SECONDS;
        isCleanedUp = false;

        if (timerLabel != null) {
            timerLabel.setText(formatTime(blitzTimeRemaining));
            timerLabel.setStyle("");
        }

        blitzTimeline = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> {
            if (isCleanedUp) {
                return;
            }

            blitzTimeRemaining--;

            if (timerLabel != null) {
                timerLabel.setText(formatTime(blitzTimeRemaining));

                if (blitzTimeRemaining <= 30) {
                    timerLabel.setStyle("-fx-text-fill: #f7768e;");
                }
            }

            if (blitzTimeRemaining <= 0) {
                stop();
                if (onBlitzTimeUp != null && !isCleanedUp) {
                    onBlitzTimeUp.run();
                }
            }
        }));
        blitzTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts the timer.
     */
    public void start() {
        if (isCleanedUp) {
            return;
        }

        if (gameMode == GameMode.ZEN && zenTimer != null) {
            zenTimer.start();
        } else if (gameMode == GameMode.BLITZ && blitzTimeline != null) {
            blitzTimeline.play();
        }
    }

    /**
     * Stops the timer.
     */
    public void stop() {
        if (gameMode == GameMode.ZEN && zenTimer != null) {
            zenTimer.stop();
        } else if (gameMode == GameMode.BLITZ && blitzTimeline != null) {
            blitzTimeline.stop();
        }
    }

    /**
     * Pauses the timer.
     */
    public void pause() {
        if (gameMode == GameMode.BLITZ && blitzTimeline != null) {
            blitzTimeline.pause();
        }
        stop();
    }

    /**
     * Resumes the timer.
     */
    public void resume() {
        start();
    }

    /**
     * Resets the timer to initial state.
     */
    public void reset() {
        if (gameMode == GameMode.ZEN && zenTimer != null) {
            zenTimer.reset();
            if (timerLabel != null) {
                timerLabel.setText(formatTime(0));
                timerLabel.setStyle("");
            }
        } else if (gameMode == GameMode.BLITZ) {
            if (blitzTimeline != null) {
                blitzTimeline.stop();
            }
            setupBlitzTimer();
        }
    }

    /**
     * Cleans up timer resources.
     */
    public void cleanup() {
        isCleanedUp = true;

        if (gameMode == GameMode.ZEN && zenTimer != null) {
            zenTimer.stop();
        } else if (gameMode == GameMode.BLITZ && blitzTimeline != null) {
            blitzTimeline.stop();
            blitzTimeline = null;
        }
    }

    /**
     * Sets callback for when Blitz time runs out.
     * @param callback callback to run
     */
    public void setOnBlitzTimeUp(Runnable callback) {
        this.onBlitzTimeUp = callback;
    }

    /**
     * Formats time in MM:SS format.
     * @param totalSeconds total seconds
     * @return formatted time string
     */
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Gets remaining time in Blitz mode.
     * @return remaining seconds
     */
    public int getBlitzTimeRemaining() {
        return blitzTimeRemaining;
    }
}