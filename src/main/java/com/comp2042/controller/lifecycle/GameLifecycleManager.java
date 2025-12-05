package com.comp2042.controller.lifecycle;

import com.comp2042.controller.game.GameEventHandler;
import com.comp2042.controller.game.GameLoopManager;
import com.comp2042.controller.game.GameStateManager;
import com.comp2042.controller.input.InputHandler;
import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.controller.mode.ZenModeManager;
import com.comp2042.enums.GameMode;
import com.comp2042.util.SoundManager;
import com.comp2042.util.TimerManager;
import com.comp2042.view.rendering.GameRendererCoordinator;
import com.comp2042.view.game.UIManager;

/**
 * Manages game lifecycle events including pause, resume, new game, and game over.
 */
public class GameLifecycleManager {

    private final GameMode gameMode;
    private final GameStateManager stateManager;
    private final InputHandler inputHandler;
    private final UIManager uiManager;
    private final SoundManager soundManager;

    private GameLoopManager loopManager;
    private TimerManager timerManager;
    private GameRendererCoordinator rendererCoordinator;
    private GameEventHandler eventHandler;
    private BlitzModeManager blitzModeManager;
    private ZenModeManager zenModeManager;

    /**
     * Creates a new game lifecycle manager.
     * @param gameMode game mode
     * @param stateManager state manager
     * @param inputHandler input handler
     * @param uiManager UI manager
     * @param soundManager sound manager
     */
    public GameLifecycleManager(GameMode gameMode, GameStateManager stateManager,
                                InputHandler inputHandler, UIManager uiManager,
                                SoundManager soundManager) {
        this.gameMode = gameMode;
        this.stateManager = stateManager;
        this.inputHandler = inputHandler;
        this.uiManager = uiManager;
        this.soundManager = soundManager;
    }

    /**
     * Sets game components.
     * @param loopManager loop manager
     * @param timerManager timer manager
     * @param rendererCoordinator renderer coordinator
     * @param eventHandler event handler
     * @param blitzModeManager blitz mode manager
     * @param zenModeManager zen mode manager
     */
    public void setComponents(GameLoopManager loopManager, TimerManager timerManager,
                              GameRendererCoordinator rendererCoordinator,
                              GameEventHandler eventHandler, BlitzModeManager blitzModeManager,
                              ZenModeManager zenModeManager) {
        this.loopManager = loopManager;
        this.timerManager = timerManager;
        this.rendererCoordinator = rendererCoordinator;
        this.eventHandler = eventHandler;
        this.blitzModeManager = blitzModeManager;
        this.zenModeManager = zenModeManager;
    }

    /**
     * Toggles pause state.
     */
    public void togglePause() {
        if (!stateManager.isPaused()) {
            pause();
        } else {
            resume();
        }
    }

    /**
     * Pauses the game.
     */
    private void pause() {
        loopManager.stop();
        timerManager.pause();
        soundManager.pauseBackgroundMusic();
        stateManager.setPaused(true);
        uiManager.showPause(true);
    }

    /**
     * Resumes the game.
     */
    private void resume() {
        loopManager.start();
        timerManager.resume();
        soundManager.resumeBackgroundMusic();
        stateManager.setPaused(false);
        uiManager.showPause(false);
    }

    /**
     * Handles game over event.
     */
    public void handleGameOver() {
        if (stateManager.isGameOver()) {
            return;
        }

        loopManager.stop();
        timerManager.stop();
        timerManager.cleanup();
        inputHandler.stopAllTimers();
        soundManager.stopBackgroundMusic();
        soundManager.playGameOverMusic();

        uiManager.showGameOver(
                eventHandler.getCurrentScore(),
                eventHandler.getCurrentHighScore(),
                gameMode == GameMode.BLITZ
        );

        stateManager.setGameOver(true);
        stateManager.setPaused(false);
        rendererCoordinator.setVisible(false);
        inputHandler.reset();
    }

    /**
     * Starts a new game.
     */
    public void startNewGame() {
        loopManager.stop();
        inputHandler.stopAllTimers();

        timerManager.cleanup();

        uiManager.hideGameOver();
        rendererCoordinator.setVisible(true);
        inputHandler.reset();

        soundManager.stopGameOverMusic();
        soundManager.stopBackgroundMusic();
        soundManager.playBackgroundMusic();

        stateManager.reset();

        if (blitzModeManager != null) {
            blitzModeManager.reset();
        }

        if (zenModeManager != null) {
            zenModeManager.reset();
        }

        timerManager.reset();
        timerManager.start();
        loopManager.start();

        eventHandler.createNewGame();
    }
}
