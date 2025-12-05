package com.comp2042.controller.input;

import com.comp2042.util.GameConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages Delayed Auto Shift (DAS) and Auto Repeat Rate (ARR) for smooth continuous input.
 */
public class DASManager {

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private Timeline leftRepeat;
    private Timeline rightRepeat;
    private Timeline downRepeat;
    private Timeline leftDelay;
    private Timeline rightDelay;
    private Timeline downDelay;

    private Runnable onLeftRepeat;
    private Runnable onRightRepeat;
    private Runnable onDownRepeat;

    /**
     * Sets callback for left repeat.
     * @param callback callback
     */
    public void setOnLeftRepeat(Runnable callback) {
        this.onLeftRepeat = callback;
    }

    /**
     * Sets callback for right repeat.
     * @param callback callback
     */
    public void setOnRightRepeat(Runnable callback) {
        this.onRightRepeat = callback;
    }

    /**
     * Sets callback for down repeat.
     * @param callback callback
     */
    public void setOnDownRepeat(Runnable callback) {
        this.onDownRepeat = callback;
    }

    /**
     * Starts DAS for left movement.
     * @param keyCode key code
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
     * Starts DAS for right movement.
     * @param keyCode key code
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
     * Starts DAS for down movement.
     * @param keyCode key code
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
     * Stops DAS for left movement.
     * @param keyCode key code
     */
    public void stopLeftDAS(KeyCode keyCode) {
        pressedKeys.remove(keyCode);
        stopTimeline(leftRepeat);
        stopTimeline(leftDelay);
        leftRepeat = null;
        leftDelay = null;
    }

    /**
     * Stops DAS for right movement.
     * @param keyCode key code
     */
    public void stopRightDAS(KeyCode keyCode) {
        pressedKeys.remove(keyCode);
        stopTimeline(rightRepeat);
        stopTimeline(rightDelay);
        rightRepeat = null;
        rightDelay = null;
    }

    /**
     * Stops DAS for down movement.
     * @param keyCode key code
     */
    public void stopDownDAS(KeyCode keyCode) {
        pressedKeys.remove(keyCode);
        stopTimeline(downRepeat);
        stopTimeline(downDelay);
        downRepeat = null;
        downDelay = null;
    }

    /**
     * Stops all DAS timers.
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
     * Resets all state.
     */
    public void reset() {
        stopAll();
        pressedKeys.clear();
    }

    /**
     * Stops a timeline safely.
     * @param timeline timeline to stop
     */
    private void stopTimeline(Timeline timeline) {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Checks if left is pressed.
     * @return true if left pressed
     */
    private boolean isLeftPressed() {
        return pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A);
    }

    /**
     * Checks if right is pressed.
     * @return true if right pressed
     */
    private boolean isRightPressed() {
        return pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D);
    }

    /**
     * Checks if down is pressed.
     * @return true if down pressed
     */
    private boolean isDownPressed() {
        return pressedKeys.contains(KeyCode.DOWN) || pressedKeys.contains(KeyCode.S);
    }

    /**
     * Checks if a key is pressed.
     * @param keyCode key code
     * @return true if pressed
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }
}