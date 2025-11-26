package com.comp2042.controller.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class GameStateManagerTest {
    private GameStateManager stateManager;

    @BeforeEach
    void setUp() {
        stateManager = new GameStateManager();
    }

    @Test
    void testInitialState_NotPaused() {
        assertFalse(stateManager.isPaused(), "Game should not be paused initially");
    }

    @Test
    void testInitialState_NotGameOver() {
        assertFalse(stateManager.isGameOver(), "Game should not be over initially");
    }

    @Test
    void testSetPaused_UpdatesState() {
        stateManager.setPaused(true);
        assertTrue(stateManager.isPaused());

        stateManager.setPaused(false);
        assertFalse(stateManager.isPaused());
    }

    @Test
    void testSetGameOver_UpdatesState() {
        stateManager.setGameOver(true);
        assertTrue(stateManager.isGameOver());

        stateManager.setGameOver(false);
        assertFalse(stateManager.isGameOver());
    }

    @Test
    void testReset_RestoresInitialState() {
        stateManager.setPaused(true);
        stateManager.setGameOver(true);

        stateManager.reset();

        assertFalse(stateManager.isPaused(), "Should not be paused after reset");
        assertFalse(stateManager.isGameOver(), "Should not be game over after reset");
    }

    @Test
    void testCanProcessInput_WhenNotPausedOrGameOver() {
        assertTrue(stateManager.canProcessInput(),
                "Should be able to process input in normal state");
    }

    @Test
    void testCanProcessInput_WhenPaused_ReturnsFalse() {
        stateManager.setPaused(true);
        assertFalse(stateManager.canProcessInput(),
                "Should not process input when paused");
    }

    @Test
    void testCanProcessInput_WhenGameOver_ReturnsFalse() {
        stateManager.setGameOver(true);
        assertFalse(stateManager.canProcessInput(),
                "Should not process input when game over");
    }

    @Test
    void testCanProcessInput_WhenBothPausedAndGameOver() {
        stateManager.setPaused(true);
        stateManager.setGameOver(true);
        assertFalse(stateManager.canProcessInput());
    }

    @Test
    void testPausedProperty_IsObservable() {
        assertNotNull(stateManager.pausedProperty(),
                "Paused should be observable property");
    }

    @Test
    void testGameOverProperty_IsObservable() {
        assertNotNull(stateManager.gameOverProperty(),
                "Game over should be observable property");
    }
}