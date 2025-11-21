package com.comp2042.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {
    private final IntegerProperty elapsedSeconds = new SimpleIntegerProperty(0);
    private Timeline timeline;
    private boolean isRunning = false;

    public GameTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> incrementTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        if (!isRunning) {
            timeline.play();
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            timeline.stop();
            isRunning = false;
        }
    }

    public void reset() {
        stop();
        elapsedSeconds.set(0);
    }

    private void incrementTime() {
        elapsedSeconds.set(elapsedSeconds.get() + 1);
    }

    public IntegerProperty elapsedSecondsProperty() {
        return elapsedSeconds;
    }

    public String getFormattedTime() {
        int seconds = elapsedSeconds.get();
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
