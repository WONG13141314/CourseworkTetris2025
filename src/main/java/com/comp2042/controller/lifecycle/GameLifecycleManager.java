package com.comp2042.controller.lifecycle;

import com.comp2042.controller.game.GameEventHandler;
import com.comp2042.controller.game.GameLoopManager;
import com.comp2042.controller.game.GameStateManager;
import com.comp2042.controller.input.InputHandler;
import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.enums.GameMode;
import com.comp2042.util.SoundManager;
import com.comp2042.util.TimerManager;
import com.comp2042.view.rendering.GameRendererCoordinator;
import com.comp2042.view.game.UIManager;

/**
 * Manages game lifecycle (pause, resume, new game, game over)
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

    public GameLifecycleManager(GameMode gameMode, GameStateManager stateManager,
                                InputHandler inputHandler, UIManager uiManager,
                                SoundManager soundManager) {
        this.gameMode = gameMode;
        this.stateManager = stateManager;
        this.inputHandler = inputHandler;
        this.uiManager = uiManager;
        this.soundManager = soundManager;
    }

    public void setComponents(GameLoopManager loopManager, TimerManager timerManager,
                              GameRendererCoordinator rendererCoordinator,
                              GameEventHandler eventHandler, BlitzModeManager blitzModeManager) {
        this.loopManager = loopManager;
        this.timerManager = timerManager;
        this.rendererCoordinator = rendererCoordinator;
        this.eventHandler = eventHandler;
        this.blitzModeManager = blitzModeManager;
    }

    /**
     * Toggle pause state
     */
    public void togglePause() {
        if (!stateManager.isPaused()) {
            pause();
        } else {
            resume();
        }
    }

    /**
     * Pause the game
     */
    private void pause() {
        loopManager.stop();
        timerManager.pause();
        soundManager.pauseBackgroundMusic();
        stateManager.setPaused(true);
        uiManager.showPause(true);
    }

    /**
     * Resume the game
     */
    private void resume() {
        loopManager.start();
        timerManager.resume();
        soundManager.resumeBackgroundMusic();
        stateManager.setPaused(false);
        uiManager.showPause(false);
    }

    /**
     * Handle game over
     */
    public void handleGameOver() {
        loopManager.stop();
        timerManager.stop();
        inputHandler.stopAllTimers();
        soundManager.stopBackgroundMusic();

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
     * Start a new game
     */
    public void startNewGame() {
        loopManager.stop();
        inputHandler.stopAllTimers();
        uiManager.hideGameOver();
        rendererCoordinator.setVisible(true);
        inputHandler.reset();

        soundManager.stopBackgroundMusic();
        soundManager.playBackgroundMusic();

        stateManager.reset();

        if (blitzModeManager != null) {
            blitzModeManager.reset();
        }

        timerManager.reset();
        timerManager.start();
        loopManager.start();

        eventHandler.createNewGame();
    }
}