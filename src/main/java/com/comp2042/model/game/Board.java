package com.comp2042.model.game;

import com.comp2042.model.scoring.Score;
import com.comp2042.model.data.ClearRow;
import com.comp2042.model.data.ViewData;

/**
 * Interface defining core game board operations.
 * Handles brick movement, rotation, collision detection, and scoring.
 */
public interface Board {

    /**
     * Moves the current brick down by one unit.
     *
     * @return true if movement was successful, false if brick reached bottom
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick left by one unit.
     *
     * @return true if movement was successful, false if blocked
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick right by one unit.
     *
     * @return true if movement was successful, false if blocked
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick counter-clockwise.
     * Includes wall kick logic for valid rotation placement.
     *
     * @return true if rotation was successful, false if blocked
     */
    boolean rotateLeftBrick();

    /**
     * Creates a new brick at the spawn position.
     *
     * @return true if game over (brick collides immediately), false otherwise
     */
    boolean createNewBrick();

    /**
     * Gets the current game board matrix.
     *
     * @return 2D array representing the board state
     */
    int[][] getBoardMatrix();

    /**
     * Calculates where the current brick will land (shadow position).
     *
     * @return y-coordinate of the shadow position
     */
    int calculateShadowPosition();

    /**
     * Gets current view data for rendering.
     *
     * @return ViewData containing brick, position, and preview information
     */
    ViewData getViewData();

    /**
     * Merges current brick into the background board.
     * Called when brick reaches final position.
     */
    void mergeBrickToBackground();

    /**
     * Checks and removes completed rows.
     *
     * @return ClearRow containing cleared lines and score information
     */
    ClearRow clearRows();

    /**
     * Gets the score manager.
     *
     * @return Score object for this game
     */
    Score getScore();

    /**
     * Starts a new game, resetting all state.
     */
    void newGame();

    /**
     * Swaps current brick with held brick.
     *
     * @return true if hold was successful, false if hold disabled
     */
    boolean holdBrick();

    /**
     * Gets the held brick display data.
     *
     * @return 2D array of held brick, or empty if none
     */
    int[][] getHoldBrickData();

    /**
     * Checks if player can use hold feature.
     *
     * @return true if hold is available
     */
    boolean canHold();

    /**
     * Checks if the board was cleared (Zen mode).
     *
     * @return true if board was just cleared
     */
    boolean wasBoardCleared();
}