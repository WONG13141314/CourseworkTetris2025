package com.comp2042.controller.game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Manages game state (paused, game over, etc.)
 */
public class GameStateManager {

    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    public boolean isPaused() {
        return isPaused.get();
    }

    public void setPaused(boolean paused) {
        isPaused.set(paused);
    }

    public BooleanProperty pausedProperty() {
        return isPaused;
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }

    public void setGameOver(boolean gameOver) {
        isGameOver.set(gameOver);
    }

    public BooleanProperty gameOverProperty() {
        return isGameOver;
    }

    public void reset() {
        isPaused.set(false);
        isGameOver.set(false);
    }

    public boolean canProcessInput() {
        return !isPaused.get() && !isGameOver.get();
    }
}