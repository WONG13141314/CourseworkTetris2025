package com.comp2042.controller.game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Manages game state including pause and game over states.
 */
public class GameStateManager {

    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    /**
     * Checks if game is paused.
     * @return true if paused
     */
    public boolean isPaused() {
        return isPaused.get();
    }

    /**
     * Sets pause state.
     * @param paused pause state
     */
    public void setPaused(boolean paused) {
        isPaused.set(paused);
    }

    /**
     * Gets pause property.
     * @return pause property
     */
    public BooleanProperty pausedProperty() {
        return isPaused;
    }

    /**
     * Checks if game is over.
     * @return true if game over
     */
    public boolean isGameOver() {
        return isGameOver.get();
    }

    /**
     * Sets game over state.
     * @param gameOver game over state
     */
    public void setGameOver(boolean gameOver) {
        isGameOver.set(gameOver);
    }

    /**
     * Gets game over property.
     * @return game over property
     */
    public BooleanProperty gameOverProperty() {
        return isGameOver;
    }

    /**
     * Resets all states.
     */
    public void reset() {
        isPaused.set(false);
        isGameOver.set(false);
    }

    /**
     * Checks if input can be processed.
     * @return true if not paused and not game over
     */
    public boolean canProcessInput() {
        return !isPaused.get() && !isGameOver.get();
    }
}