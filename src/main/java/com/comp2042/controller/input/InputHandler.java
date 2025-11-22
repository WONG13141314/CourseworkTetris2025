package com.comp2042.controller.input;

import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.util.GameConstants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles all keyboard input with DAS (Delayed Auto Shift) and ARR (Auto Repeat Rate)
 */
public class InputHandler {

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean spacePressed = false;

    // Repeat timelines for DAS/ARR
    private Timeline leftRepeat;
    private Timeline rightRepeat;
    private Timeline downRepeat;
    private Timeline leftDelay;
    private Timeline rightDelay;
    private Timeline downDelay;

    // Callbacks
    private InputCallback callback;

    public interface InputCallback {
        void onMove(MoveEvent event);
        void onRotate(MoveEvent event);
        void onHardDrop(MoveEvent event);
        void onHold(MoveEvent event);
        void onPause();
        void onNewGame();
        void onReturnToMenu();
    }

    public void setCallback(InputCallback callback) {
        this.callback = callback;
    }

    public void handleKeyPressed(KeyEvent keyEvent, boolean isPaused, boolean isGameOver) {
        KeyCode code = keyEvent.getCode();

        // Space (Hard Drop) - special handling to prevent repeat
        if (code == KeyCode.SPACE) {
            if (!spacePressed && !isPaused && !isGameOver) {
                spacePressed = true;
                if (callback != null) {
                    callback.onHardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                }
                keyEvent.consume();
            }
            return;
        }

        // Pause
        if (code == KeyCode.P && !pressedKeys.contains(code)) {
            if (!isGameOver && callback != null) {
                callback.onPause();
                pressedKeys.add(code);
            }
            keyEvent.consume();
            return;
        }

        // Game controls (only when not paused/game over)
        if (!isPaused && !isGameOver) {
            handleGameControls(keyEvent, code);
        }

        // Global controls
        if (code == KeyCode.ESCAPE && callback != null) {
            callback.onReturnToMenu();
            keyEvent.consume();
        }
        if (code == KeyCode.N && callback != null) {
            callback.onNewGame();
            keyEvent.consume();
        }
    }

    private void handleGameControls(KeyEvent keyEvent, KeyCode code) {
        if (pressedKeys.contains(code)) {
            return; // Already pressed
        }

        // Rotation
        if ((code == KeyCode.UP || code == KeyCode.W) && callback != null) {
            callback.onRotate(new MoveEvent(EventType.ROTATE, EventSource.USER));
            pressedKeys.add(code);
            keyEvent.consume();
        }
        // Hold
        else if ((code == KeyCode.C || code == KeyCode.SHIFT) && callback != null) {
            callback.onHold(new MoveEvent(EventType.HOLD, EventSource.USER));
            pressedKeys.add(code);
            keyEvent.consume();
        }
        // Left movement with DAS/ARR
        else if (code == KeyCode.LEFT || code == KeyCode.A) {
            handleLeftMovement(keyEvent, code);
        }
        // Right movement with DAS/ARR
        else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            handleRightMovement(keyEvent, code);
        }
        // Down movement with DAS/ARR
        else if (code == KeyCode.DOWN || code == KeyCode.S) {
            handleDownMovement(keyEvent, code);
        }
    }

    private void handleLeftMovement(KeyEvent keyEvent, KeyCode code) {
        if (callback != null) {
            callback.onMove(new MoveEvent(EventType.LEFT, EventSource.USER));
        }
        pressedKeys.add(code);

        stopTimeline(leftRepeat);
        stopTimeline(leftDelay);

        leftDelay = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_DELAY_MS), e -> {
            if (isLeftPressed()) {
                leftRepeat = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_REPEAT_MS), ev -> {
                    if (isLeftPressed() && callback != null) {
                        callback.onMove(new MoveEvent(EventType.LEFT, EventSource.USER));
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

        keyEvent.consume();
    }

    private void handleRightMovement(KeyEvent keyEvent, KeyCode code) {
        if (callback != null) {
            callback.onMove(new MoveEvent(EventType.RIGHT, EventSource.USER));
        }
        pressedKeys.add(code);

        stopTimeline(rightRepeat);
        stopTimeline(rightDelay);

        rightDelay = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_DELAY_MS), e -> {
            if (isRightPressed()) {
                rightRepeat = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_REPEAT_MS), ev -> {
                    if (isRightPressed() && callback != null) {
                        callback.onMove(new MoveEvent(EventType.RIGHT, EventSource.USER));
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

        keyEvent.consume();
    }

    private void handleDownMovement(KeyEvent keyEvent, KeyCode code) {
        if (callback != null) {
            callback.onMove(new MoveEvent(EventType.DOWN, EventSource.USER));
        }
        pressedKeys.add(code);

        stopTimeline(downRepeat);
        stopTimeline(downDelay);

        downDelay = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_DELAY_MS), e -> {
            if (isDownPressed()) {
                downRepeat = new Timeline(new KeyFrame(Duration.millis(GameConstants.INPUT_REPEAT_MS), ev -> {
                    if (isDownPressed() && callback != null) {
                        callback.onMove(new MoveEvent(EventType.DOWN, EventSource.USER));
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

        keyEvent.consume();
    }

    public void handleKeyReleased(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        if (code == KeyCode.SPACE) {
            spacePressed = false;
        }

        pressedKeys.remove(code);

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            stopTimeline(leftRepeat);
            stopTimeline(leftDelay);
            leftRepeat = null;
            leftDelay = null;
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            stopTimeline(rightRepeat);
            stopTimeline(rightDelay);
            rightRepeat = null;
            rightDelay = null;
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            stopTimeline(downRepeat);
            stopTimeline(downDelay);
            downRepeat = null;
            downDelay = null;
        }

        keyEvent.consume();
    }

    public void stopAllTimers() {
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

    public void reset() {
        stopAllTimers();
        pressedKeys.clear();
        spacePressed = false;
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
}