package com.comp2042.util;

import com.comp2042.enums.GameMode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Manages all game timers including Blitz countdown and Zen elapsed time
 */
public class TimerManager {

    private final GameMode gameMode;
    private final Label timerLabel;
    private GameTimer zenTimer;
    private Timeline blitzTimeline;
    private int blitzTimeRemaining;
    private Runnable onBlitzTimeUp;

    public TimerManager(GameMode gameMode, Label timerLabel) {
        this.gameMode = gameMode;
        this.timerLabel = timerLabel;

        if (gameMode == GameMode.ZEN) {
            setupZenTimer();
        } else if (gameMode == GameMode.BLITZ) {
            setupBlitzTimer();
        }
    }

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

    private void setupBlitzTimer() {
        blitzTimeRemaining = GameConstants.BLITZ_TIME_SECONDS;

        if (timerLabel != null) {
            timerLabel.setText(formatTime(blitzTimeRemaining));
            timerLabel.setStyle("");
        }

        blitzTimeline = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> {
            blitzTimeRemaining--;

            if (timerLabel != null) {
                timerLabel.setText(formatTime(blitzTimeRemaining));

                if (blitzTimeRemaining <= 30) {
                    timerLabel.setStyle("-fx-text-fill: #f7768e;");
                }
            }

            if (blitzTimeRemaining <= 0) {
                stop();
                if (onBlitzTimeUp != null) {
                    onBlitzTimeUp.run();
                }
            }
        }));
        blitzTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        if (gameMode == GameMode.ZEN && zenTimer != null) {
            zenTimer.start();
        } else if (gameMode == GameMode.BLITZ && blitzTimeline != null) {
            blitzTimeline.play();
        }
    }

    public void stop() {
        if (gameMode == GameMode.ZEN && zenTimer != null) {
            zenTimer.stop();
        } else if (gameMode == GameMode.BLITZ && blitzTimeline != null) {
            blitzTimeline.stop();
        }
    }

    public void pause() {
        if (gameMode == GameMode.BLITZ && blitzTimeline != null) {
            blitzTimeline.pause();
        }
        stop();
    }

    public void resume() {
        start();
    }

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

    public void setOnBlitzTimeUp(Runnable callback) {
        this.onBlitzTimeUp = callback;
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public int getBlitzTimeRemaining() {
        return blitzTimeRemaining;
    }
}