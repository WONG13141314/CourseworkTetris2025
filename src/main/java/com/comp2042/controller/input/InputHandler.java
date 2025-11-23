package com.comp2042.controller.input;

import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.model.data.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles all keyboard input
 */
public class InputHandler {

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final DASManager dasManager = new DASManager();
    private boolean spacePressed = false;

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

    public InputHandler() {
        setupDASCallbacks();
    }

    private void setupDASCallbacks() {
        dasManager.setOnLeftRepeat(() -> {
            if (callback != null) {
                callback.onMove(new MoveEvent(EventType.LEFT, EventSource.USER));
            }
        });

        dasManager.setOnRightRepeat(() -> {
            if (callback != null) {
                callback.onMove(new MoveEvent(EventType.RIGHT, EventSource.USER));
            }
        });

        dasManager.setOnDownRepeat(() -> {
            if (callback != null) {
                callback.onMove(new MoveEvent(EventType.DOWN, EventSource.USER));
            }
        });
    }

    public void setCallback(InputCallback callback) {
        this.callback = callback;
    }

    public void handleKeyPressed(KeyEvent keyEvent, boolean isPaused, boolean isGameOver) {
        KeyCode code = keyEvent.getCode();

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

        if (code == KeyCode.P && !pressedKeys.contains(code)) {
            if (!isGameOver && callback != null) {
                callback.onPause();
                pressedKeys.add(code);
            }
            keyEvent.consume();
            return;
        }

        if (!isPaused && !isGameOver) {
            handleGameControls(keyEvent, code);
        }

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
            return;
        }

        if ((code == KeyCode.UP || code == KeyCode.W) && callback != null) {
            callback.onRotate(new MoveEvent(EventType.ROTATE, EventSource.USER));
            pressedKeys.add(code);
            keyEvent.consume();
        }
        else if ((code == KeyCode.C || code == KeyCode.SHIFT) && callback != null) {
            callback.onHold(new MoveEvent(EventType.HOLD, EventSource.USER));
            pressedKeys.add(code);
            keyEvent.consume();
        }
        else if (code == KeyCode.LEFT || code == KeyCode.A) {
            if (callback != null) {
                callback.onMove(new MoveEvent(EventType.LEFT, EventSource.USER));
            }
            dasManager.startLeftDAS(code);
            pressedKeys.add(code);
            keyEvent.consume();
        }
        else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            if (callback != null) {
                callback.onMove(new MoveEvent(EventType.RIGHT, EventSource.USER));
            }
            dasManager.startRightDAS(code);
            pressedKeys.add(code);
            keyEvent.consume();
        }
        else if (code == KeyCode.DOWN || code == KeyCode.S) {
            if (callback != null) {
                callback.onMove(new MoveEvent(EventType.DOWN, EventSource.USER));
            }
            dasManager.startDownDAS(code);
            pressedKeys.add(code);
            keyEvent.consume();
        }
    }

    public void handleKeyReleased(KeyEvent keyEvent) {
        KeyCode code = keyEvent.getCode();

        if (code == KeyCode.SPACE) {
            spacePressed = false;
        }

        pressedKeys.remove(code);

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            dasManager.stopLeftDAS(code);
        } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
            dasManager.stopRightDAS(code);
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            dasManager.stopDownDAS(code);
        }

        keyEvent.consume();
    }

    public void stopAllTimers() {
        dasManager.stopAll();
    }

    public void reset() {
        dasManager.reset();
        pressedKeys.clear();
        spacePressed = false;
    }
}