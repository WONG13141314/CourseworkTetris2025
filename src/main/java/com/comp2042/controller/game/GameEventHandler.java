package com.comp2042.controller.game;

import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.controller.mode.ZenModeManager;
import com.comp2042.controller.input.InputEventListener;
import com.comp2042.enums.EventType;
import com.comp2042.enums.GameMode;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.util.SoundManager;

/**
 * Handles game events and coordinates responses between input and game logic.
 */
public class GameEventHandler {

    private final InputEventListener eventListener;
    private final SoundManager soundManager;
    private final GameMode gameMode;
    private BlitzModeManager blitzModeManager;
    private ZenModeManager zenModeManager;
    private NotificationCallback onNotification;

    /**
     * Callback interface for showing notifications.
     */
    public interface NotificationCallback {
        /**
         * Shows a notification with the given text.
         * @param text notification text
         */
        void showNotification(String text);
    }

    /**
     * Creates a new game event handler.
     * @param eventListener listener for game events
     * @param gameMode current game mode
     */
    public GameEventHandler(InputEventListener eventListener, GameMode gameMode) {
        this.eventListener = eventListener;
        this.gameMode = gameMode;
        this.soundManager = SoundManager.getInstance();
    }

    /**
     * Sets the Blitz mode manager.
     * @param manager Blitz mode manager
     */
    public void setBlitzModeManager(BlitzModeManager manager) {
        this.blitzModeManager = manager;
    }

    /**
     * Sets the Zen mode manager.
     * @param manager Zen mode manager
     */
    public void setZenModeManager(ZenModeManager manager) {
        this.zenModeManager = manager;
    }

    /**
     * Sets the notification callback.
     * @param callback notification callback
     */
    public void setOnNotification(NotificationCallback callback) {
        this.onNotification = callback;
    }

    /**
     * Handles horizontal movement events.
     * @param event move event
     * @return view data after move
     */
    public ViewData handleMove(MoveEvent event) {
        if (event.getEventType() == EventType.LEFT) {
            return eventListener.onLeftEvent(event);
        } else if (event.getEventType() == EventType.RIGHT) {
            return eventListener.onRightEvent(event);
        }
        return null;
    }

    /**
     * Handles rotation events.
     * @param event move event
     * @return view data after rotation
     */
    public ViewData handleRotate(MoveEvent event) {
        return eventListener.onRotateEvent(event);
    }

    /**
     * Handles hold events.
     * @param event move event
     * @return view data after hold
     */
    public ViewData handleHold(MoveEvent event) {
        return eventListener.onHoldEvent(event);
    }

    /**
     * Handles down movement events.
     * @param event move event
     * @return down data with results
     */
    public DownData handleDown(MoveEvent event) {
        DownData downData = eventListener.onDownEvent(event);
        processDownResult(downData);
        return downData;
    }

    /**
     * Handles hard drop events.
     * @param event move event
     * @return down data with results
     */
    public DownData handleHardDrop(MoveEvent event) {
        DownData downData = eventListener.onHardDropEvent(event);
        processDownResult(downData);
        return downData;
    }

    /**
     * Processes the result of a down movement.
     * @param downData down data to process
     */
    private void processDownResult(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            soundManager.playClearRow();

            if (gameMode == GameMode.BLITZ && blitzModeManager != null) {
                blitzModeManager.addLines(downData.getClearRow().getLinesRemoved());
            }

            if (onNotification != null) {
                onNotification.showNotification("+" + downData.getClearRow().getScoreBonus());
            }
        }

        if (downData.isBoardCleared() && gameMode == GameMode.ZEN && zenModeManager != null) {
            zenModeManager.notifyBoardCleared();
            if (onNotification != null) {
                onNotification.showNotification("Board Cleared!");
            }
        }
    }

    /**
     * Creates a new game.
     */
    public void createNewGame() {
        eventListener.createNewGame();
    }

    /**
     * Gets the current score.
     * @return current score
     */
    public int getCurrentScore() {
        return eventListener.getCurrentScore();
    }

    /**
     * Gets the current high score.
     * @return current high score
     */
    public int getCurrentHighScore() {
        return eventListener.getCurrentHighScore();
    }
}