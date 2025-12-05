package com.comp2042.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Timer utility for tracking elapsed time in Zen mode.
 * Provides a simple countdown/countup timer with property binding support.
 */
public class GameTimer {
    private final IntegerProperty elapsedSeconds = new SimpleIntegerProperty(0);
    private Timeline timeline;
    private boolean isRunning = false;

    /**
     * Constructs a new game timer.
     * Timer starts at 0 seconds and counts upward.
     */
    public GameTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> incrementTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Starts the timer if not already running.
     * Timer will increment every second.
     */
    public void start() {
        if (!isRunning) {
            timeline.play();
            isRunning = true;
        }
    }

    /**
     * Stops the timer if currently running.
     * Elapsed time is preserved.
     */
    public void stop() {
        if (isRunning) {
            timeline.stop();
            isRunning = false;
        }
    }

    /**
     * Resets the timer to zero and stops it.
     */
    public void reset() {
        stop();
        elapsedSeconds.set(0);
    }

    /**
     * Increments the elapsed time by one second.
     * Called automatically by the timeline.
     */
    private void incrementTime() {
        elapsedSeconds.set(elapsedSeconds.get() + 1);
    }

    /**
     * Gets the elapsed seconds property for binding.
     *
     * @return IntegerProperty representing elapsed seconds
     */
    public IntegerProperty elapsedSecondsProperty() {
        return elapsedSeconds;
    }

    /**
     * Gets the formatted time string in MM:SS format.
     *
     * @return formatted time string (e.g., "05:42")
     */
    public String getFormattedTime() {
        int seconds = elapsedSeconds.get();
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * Checks if the timer is currently running.
     *
     * @return true if timer is active, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }
}