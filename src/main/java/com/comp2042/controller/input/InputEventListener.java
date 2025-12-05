package com.comp2042.controller.input;

import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;

/**
 * Listener interface for handling game input events.
 * Defines callbacks for all player actions.
 */
public interface InputEventListener {
    /**
     * Handles downward movement event.
     * @param event the move event
     * @return data about the movement and any line clears
     */
    DownData onDownEvent(MoveEvent event);

    /**
     * Handles leftward movement event.
     * @param event the move event
     * @return updated view data for rendering
     */
    ViewData onLeftEvent(MoveEvent event);

    /**
     * Handles rightward movement event.
     * @param event the move event
     * @return updated view data for rendering
     */
    ViewData onRightEvent(MoveEvent event);

    /**
     * Handles rotation event.
     * @param event the move event
     * @return updated view data for rendering
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Handles hard drop event (instant drop to bottom).
     * @param event the move event
     * @return data about the drop and any line clears
     */
    DownData onHardDropEvent(MoveEvent event);

    /**
     * Handles hold piece event.
     * @param event the move event
     * @return updated view data for rendering
     */
    ViewData onHoldEvent(MoveEvent event);

    /**
     * Creates a new game session.
     */
    void createNewGame();

    /**
     * Gets the current score.
     * @return current score value
     */
    int getCurrentScore();

    /**
     * Gets the current high score.
     * @return current high score value
     */
    int getCurrentHighScore();
}