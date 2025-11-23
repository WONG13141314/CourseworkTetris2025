package com.comp2042.controller.game;

import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.controller.input.InputEventListener;
import com.comp2042.enums.EventType;
import com.comp2042.enums.GameMode;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.util.SoundManager;

/**
 * Handles game events and coordinates responses
 */
public class GameEventHandler {

    private final InputEventListener eventListener;
    private final SoundManager soundManager;
    private final GameMode gameMode;
    private BlitzModeManager blitzModeManager;
    private NotificationCallback onNotification;

    public interface NotificationCallback {
        void showNotification(String text);
    }

    public GameEventHandler(InputEventListener eventListener, GameMode gameMode) {
        this.eventListener = eventListener;
        this.gameMode = gameMode;
        this.soundManager = SoundManager.getInstance();
    }

    public void setBlitzModeManager(BlitzModeManager manager) {
        this.blitzModeManager = manager;
    }

    public void setOnNotification(NotificationCallback callback) {
        this.onNotification = callback;
    }

    public ViewData handleMove(MoveEvent event) {
        if (event.getEventType() == EventType.LEFT) {
            return eventListener.onLeftEvent(event);
        } else if (event.getEventType() == EventType.RIGHT) {
            return eventListener.onRightEvent(event);
        }
        return null;
    }

    public ViewData handleRotate(MoveEvent event) {
        return eventListener.onRotateEvent(event);
    }

    public ViewData handleHold(MoveEvent event) {
        return eventListener.onHoldEvent(event);
    }

    public DownData handleDown(MoveEvent event) {
        DownData downData = eventListener.onDownEvent(event);
        processDownResult(downData);
        return downData;
    }

    public DownData handleHardDrop(MoveEvent event) {
        DownData downData = eventListener.onHardDropEvent(event);
        processDownResult(downData);
        return downData;
    }

    private void processDownResult(DownData downData) {
        // Handle line clears
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            soundManager.playClearRow();

            // Update Blitz level progress
            if (gameMode == GameMode.BLITZ && blitzModeManager != null) {
                blitzModeManager.addLines(downData.getClearRow().getLinesRemoved());
            }

            // Show score notification
            if (onNotification != null) {
                onNotification.showNotification("+" + downData.getClearRow().getScoreBonus());
            }
        }

        // Handle board cleared achievement (Zen mode)
        if (downData.isBoardCleared() && gameMode == GameMode.ZEN) {
            if (onNotification != null) {
                onNotification.showNotification("Board Cleared!");
            }
        }
    }

    public void createNewGame() {
        eventListener.createNewGame();
    }

    public int getCurrentScore() {
        return eventListener.getCurrentScore();
    }

    public int getCurrentHighScore() {
        return eventListener.getCurrentHighScore();
    }
}