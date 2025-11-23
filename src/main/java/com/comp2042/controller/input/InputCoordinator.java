package com.comp2042.controller.input;

import com.comp2042.controller.game.GameEventHandler;
import com.comp2042.controller.game.GameStateManager;
import com.comp2042.enums.EventType;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.view.rendering.GameRendererCoordinator;
import javafx.scene.layout.GridPane;

/**
 * Coordinates input handling with game events and rendering
 */
public class InputCoordinator {

    private final GameStateManager stateManager;
    private final InputHandler inputHandler;
    private final GridPane gamePanel;

    private GameEventHandler eventHandler;
    private GameRendererCoordinator rendererCoordinator;

    public InputCoordinator(GameStateManager stateManager, InputHandler inputHandler,
                            GridPane gamePanel) {
        this.stateManager = stateManager;
        this.inputHandler = inputHandler;
        this.gamePanel = gamePanel;
    }

    public void setComponents(GameEventHandler eventHandler,
                              GameRendererCoordinator rendererCoordinator) {
        this.eventHandler = eventHandler;
        this.rendererCoordinator = rendererCoordinator;
    }

    /**
     * Setup input callback handlers
     */
    public void setupCallbacks(Runnable onPause, Runnable onNewGame, Runnable onReturnToMenu) {
        inputHandler.setCallback(new InputHandler.InputCallback() {
            @Override
            public void onMove(MoveEvent event) {
                if (stateManager.canProcessInput()) {
                    handleMove(event);
                }
                gamePanel.requestFocus();
            }

            @Override
            public void onRotate(MoveEvent event) {
                if (stateManager.canProcessInput()) {
                    handleRotate(event);
                }
                gamePanel.requestFocus();
            }

            @Override
            public void onHardDrop(MoveEvent event) {
                if (stateManager.canProcessInput()) {
                    handleHardDrop(event);
                }
                gamePanel.requestFocus();
            }

            @Override
            public void onHold(MoveEvent event) {
                if (stateManager.canProcessInput()) {
                    handleHold(event);
                }
                gamePanel.requestFocus();
            }

            @Override
            public void onPause() {
                onPause.run();
            }

            @Override
            public void onNewGame() {
                onNewGame.run();
            }

            @Override
            public void onReturnToMenu() {
                onReturnToMenu.run();
            }
        });
    }

    private void handleMove(MoveEvent event) {
        if (event.getEventType() == EventType.DOWN) {
            DownData downData = eventHandler.handleDown(event);
            if (downData != null && downData.getViewData() != null) {
                rendererCoordinator.refreshBrick(downData.getViewData());
            }
        } else {
            ViewData vd = eventHandler.handleMove(event);
            if (vd != null) {
                rendererCoordinator.refreshBrick(vd);
            }
        }
    }

    private void handleRotate(MoveEvent event) {
        ViewData vd = eventHandler.handleRotate(event);
        if (vd != null) {
            rendererCoordinator.refreshBrick(vd);
        }
    }

    private void handleHardDrop(MoveEvent event) {
        DownData downData = eventHandler.handleHardDrop(event);
        if (downData != null && downData.getViewData() != null) {
            rendererCoordinator.refreshBrick(downData.getViewData());
        }
    }

    private void handleHold(MoveEvent event) {
        ViewData vd = eventHandler.handleHold(event);
        if (vd != null) {
            rendererCoordinator.refreshBrick(vd);
        }
    }
}