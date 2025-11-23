package com.comp2042.controller.lifecycle;

import com.comp2042.controller.game.GameEventHandler;
import com.comp2042.controller.game.GameLoopManager;
import com.comp2042.controller.game.GameStateManager;
import com.comp2042.controller.input.InputEventListener;
import com.comp2042.controller.input.InputHandler;
import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.enums.GameMode;
import com.comp2042.util.SoundManager;
import com.comp2042.util.TimerManager;
import com.comp2042.view.rendering.GameRendererCoordinator;
import com.comp2042.view.game.UIManager;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Handles initialization of all game components
 */
public class GameInitializer {

    private final GameMode gameMode;
    private final GameStateManager stateManager;
    private final InputHandler inputHandler;
    private final UIManager uiManager;
    private final SoundManager soundManager;

    // Components to initialize
    private TimerManager timerManager;
    private GameRendererCoordinator rendererCoordinator;
    private GameEventHandler eventHandler;
    private GameLoopManager loopManager;
    private BlitzModeManager blitzModeManager;

    public GameInitializer(GameMode gameMode, GameStateManager stateManager,
                           InputHandler inputHandler, UIManager uiManager,
                           SoundManager soundManager) {
        this.gameMode = gameMode;
        this.stateManager = stateManager;
        this.inputHandler = inputHandler;
        this.uiManager = uiManager;
        this.soundManager = soundManager;
    }

    /**
     * Initialize game mode specific components
     */
    public void initializeGameMode(Label blitzLevelLabel, Label blitzProgressLabel) {
        uiManager.setupGameMode(gameMode);

        if (gameMode == GameMode.BLITZ) {
            blitzModeManager = new BlitzModeManager(blitzLevelLabel, blitzProgressLabel);
        }
    }

    /**
     * Initialize rendering system
     */
    public void initializeRendering(GridPane gamePanel, GridPane brickPanel,
                                    GridPane nextBrickPanel, GridPane holdBrickPanel,
                                    Pane parentPane, int[][] boardMatrix,
                                    com.comp2042.model.data.ViewData brick) {
        rendererCoordinator = new GameRendererCoordinator(gamePanel, brickPanel,
                nextBrickPanel, holdBrickPanel);
        rendererCoordinator.initialize(boardMatrix, brick, parentPane);
    }

    /**
     * Initialize timer system
     */
    public void initializeTimer(Label timerLabel, Runnable onBlitzTimeUp) {
        timerManager = new TimerManager(gameMode, timerLabel);
        timerManager.setOnBlitzTimeUp(onBlitzTimeUp);
        timerManager.start();
    }

    /**
     * Initialize game loop
     */
    public void initializeGameLoop(Runnable onDropTick) {
        loopManager = new GameLoopManager(gameMode);
        loopManager.setBlitzModeManager(blitzModeManager);
        loopManager.setOnDropTick(onDropTick);

        if (blitzModeManager != null) {
            blitzModeManager.setOnLevelUp(() -> loopManager.updateSpeed());
        }

        loopManager.start();
    }

    /**
     * Initialize event handling
     */
    public void initializeEventHandler(InputEventListener listener) {
        eventHandler = new GameEventHandler(listener, gameMode);
        eventHandler.setBlitzModeManager(blitzModeManager);
        eventHandler.setOnNotification(text -> uiManager.showNotification(text));
    }

    // Getters
    public TimerManager getTimerManager() { return timerManager; }
    public GameRendererCoordinator getRendererCoordinator() { return rendererCoordinator; }
    public GameEventHandler getEventHandler() { return eventHandler; }
    public GameLoopManager getLoopManager() { return loopManager; }
    public BlitzModeManager getBlitzModeManager() { return blitzModeManager; }
}