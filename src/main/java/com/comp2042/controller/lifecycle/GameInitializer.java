package com.comp2042.controller.lifecycle;

import com.comp2042.controller.game.GameEventHandler;
import com.comp2042.controller.game.GameLoopManager;
import com.comp2042.controller.game.GameStateManager;
import com.comp2042.controller.input.InputEventListener;
import com.comp2042.controller.input.InputHandler;
import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.controller.mode.ZenModeManager;
import com.comp2042.enums.GameMode;
import com.comp2042.util.SoundManager;
import com.comp2042.util.TimerManager;
import com.comp2042.view.rendering.GameRendererCoordinator;
import com.comp2042.view.game.UIManager;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Handles initialization of all game components at startup.
 */
public class GameInitializer {

    private final GameMode gameMode;
    private final GameStateManager stateManager;
    private final InputHandler inputHandler;
    private final UIManager uiManager;
    private final SoundManager soundManager;

    private TimerManager timerManager;
    private GameRendererCoordinator rendererCoordinator;
    private GameEventHandler eventHandler;
    private GameLoopManager loopManager;
    private BlitzModeManager blitzModeManager;
    private ZenModeManager zenModeManager;

    /**
     * Creates a new game initializer.
     * @param gameMode game mode
     * @param stateManager state manager
     * @param inputHandler input handler
     * @param uiManager UI manager
     * @param soundManager sound manager
     */
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
     * Initializes game mode specific components.
     * @param blitzLevelLabel label for Blitz level
     * @param blitzProgressLabel label for Blitz progress
     * @param timerLabel label for timer
     */
    public void initializeGameMode(Label blitzLevelLabel, Label blitzProgressLabel, Label timerLabel) {
        uiManager.setupGameMode(gameMode);

        if (gameMode == GameMode.BLITZ) {
            blitzModeManager = new BlitzModeManager(blitzLevelLabel, blitzProgressLabel);
        } else if (gameMode == GameMode.ZEN) {
            zenModeManager = new ZenModeManager(timerLabel);
        }
    }

    /**
     * Initializes rendering system.
     * @param gamePanel game panel
     * @param brickPanel brick panel
     * @param nextBrickPanel next brick panel
     * @param holdBrickPanel hold brick panel
     * @param parentPane parent pane
     * @param boardMatrix board matrix
     * @param brick brick view data
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
     * Initializes timer system.
     * @param timerLabel timer label
     * @param onBlitzTimeUp callback when Blitz time expires
     */
    public void initializeTimer(Label timerLabel, Runnable onBlitzTimeUp) {
        timerManager = new TimerManager(gameMode, timerLabel);
        timerManager.setOnBlitzTimeUp(onBlitzTimeUp);
        timerManager.start();
    }

    /**
     * Initializes game loop.
     * @param onDropTick callback for each drop tick
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
     * Initializes event handling.
     * @param listener input event listener
     */
    public void initializeEventHandler(InputEventListener listener) {
        eventHandler = new GameEventHandler(listener, gameMode);
        eventHandler.setBlitzModeManager(blitzModeManager);
        eventHandler.setZenModeManager(zenModeManager);
        eventHandler.setOnNotification(text -> uiManager.showNotification(text));
    }

    /**
     * Gets timer manager.
     * @return timer manager
     */
    public TimerManager getTimerManager() {
        return timerManager;
    }

    /**
     * Gets renderer coordinator.
     * @return renderer coordinator
     */
    public GameRendererCoordinator getRendererCoordinator() {
        return rendererCoordinator;
    }

    /**
     * Gets event handler.
     * @return event handler
     */
    public GameEventHandler getEventHandler() {
        return eventHandler;
    }

    /**
     * Gets loop manager.
     * @return loop manager
     */
    public GameLoopManager getLoopManager() {
        return loopManager;
    }

    /**
     * Gets Blitz mode manager.
     * @return Blitz mode manager
     */
    public BlitzModeManager getBlitzModeManager() {
        return blitzModeManager;
    }

    /**
     * Gets Zen mode manager.
     * @return Zen mode manager
     */
    public ZenModeManager getZenModeManager() {
        return zenModeManager;
    }
}
