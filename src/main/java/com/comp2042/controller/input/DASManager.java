package com.comp2042.controller.input;

import com.comp2042.util.GameConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

/**
 * Manages Delayed Auto Shift (DAS) and Auto Repeat Rate (ARR) for smooth input
 */
public class DASManager {

    private final Set<KeyCode> pressedKeys = new HashSet<>();

    // Repeat timelines
    private Timeline leftRepeat;
    private Timeline rightRepeat;
    private Timeline downRepeat;
    private Timeline leftDelay;
    private Timeline rightDelay;
    private Timeline downDelay;

    private Runnable onLeftRepeat;
    private Runnable onRightRepeat;
    private Runnable onDownRepeat;

    public void setOnLeftRepeat(Runnable callback) {
        this.onLeftRepeat = callback;
    }

    public void setOnRightRepeat(Runnable callback) {
        this.onRightRepeat = callback;
    }

    public void setOnDownRepeat(Runnable callback) {
        this.onDownRepeat = callback;
    }

    /**
     * Start DAS/ARR for left movement
     */
    public void startLeftDAS(KeyCode keyCode) {
        pressedKeys.add(keyCode);
        stopTimeline(leftRepeat);
        stopTimeline(leftDelay);

        leftDelay = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_DELAY_MS), e -> {
            if (isLeftPressed()) {
                leftRepeat = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_REPEAT_MS), ev -> {
                    if (isLeftPressed() && onLeftRepeat != null) {
                        onLeftRepeat.run();
                    } else {
                        stopTimeline(leftRepeat);
                    }
                }));
                leftRepeat.setCycleCount(Timeline.INDEFINITE);
                leftRepeat.play();
            }
        }));
        leftDelay.setCycleCount(1);
        leftDelay.play();
    }

    /**
     * Start DAS/ARR for right movement
     */
    public void startRightDAS(KeyCode keyCode) {
        pressedKeys.add(keyCode);
        stopTimeline(rightRepeat);
        stopTimeline(rightDelay);

        rightDelay = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_DELAY_MS), e -> {
            if (isRightPressed()) {
                rightRepeat = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_REPEAT_MS), ev -> {
                    if (isRightPressed() && onRightRepeat != null) {
                        onRightRepeat.run();
                    } else {
                        stopTimeline(rightRepeat);
                    }
                }));
                rightRepeat.setCycleCount(Timeline.INDEFINITE);
                rightRepeat.play();
            }
        }));
        rightDelay.setCycleCount(1);
        rightDelay.play();
    }

    /**
     * Start DAS/ARR for down movement
     */
    public void startDownDAS(KeyCode keyCode) {
        pressedKeys.add(keyCode);
        stopTimeline(downRepeat);
        stopTimeline(downDelay);

        downDelay = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_DELAY_MS), e -> {
            if (isDownPressed()) {
                downRepeat = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_REPEAT_MS), ev -> {
                    if (isDownPressed() && onDownRepeat != null) {
                        onDownRepeat.run();
                    } else {
                        stopTimeline(downRepeat);
                    }
                }));
                downRepeat.setCycleCount(Timeline.INDEFINITE);
                downRepeat.play();
            }
        }));
        downDelay.setCycleCount(1);
        downDelay.play();
    }

    /**
     * Stop DAS for left movement
     */
    public void stopLeftDAS(KeyCode keyCode) {
        pressedKeys.remove(keyCode);
        stopTimeline(leftRepeat);
        stopTimeline(leftDelay);
        leftRepeat = null;
        leftDelay = null;
    }

    /**
     * Stop DAS for right movement
     */
    public void stopRightDAS(KeyCode keyCode) {
        pressedKeys.remove(keyCode);
        stopTimeline(rightRepeat);
        stopTimeline(rightDelay);
        rightRepeat = null;
        rightDelay = null;
    }

    /**
     * Stop DAS for down movement
     */
    public void stopDownDAS(KeyCode keyCode) {
        pressedKeys.remove(keyCode);
        stopTimeline(downRepeat);
        stopTimeline(downDelay);
        downRepeat = null;
        downDelay = null;
    }

    /**
     * Stop all DAS timers
     */
    public void stopAll() {
        stopTimeline(leftRepeat);
        stopTimeline(rightRepeat);
        stopTimeline(downRepeat);
        stopTimeline(leftDelay);
        stopTimeline(rightDelay);
        stopTimeline(downDelay);

        leftRepeat = null;
        rightRepeat = null;
        downRepeat = null;
        leftDelay = null;
        rightDelay = null;
        downDelay = null;
    }

    /**
     * Reset all state
     */
    public void reset() {
        stopAll();
        pressedKeys.clear();
    }

    private void stopTimeline(Timeline timeline) {
        if (timeline != null) {
            timeline.stop();
        }
    }

    private boolean isLeftPressed() {
        return pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A);
    }

    private boolean isRightPressed() {
        return pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D);
    }

    private boolean isDownPressed() {
        return pressedKeys.contains(KeyCode.DOWN) || pressedKeys.contains(KeyCode.S);
    }

    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }
}