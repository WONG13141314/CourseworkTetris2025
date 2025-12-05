package com.comp2042.view.rendering;

import com.comp2042.model.data.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Coordinates all rendering operations for the game.
 * Manages board, brick, shadow, and preview renderers.
 */
public class GameRendererCoordinator {

    private final BoardRenderer boardRenderer;
    private final BrickRenderer brickRenderer;
    private final ShadowRenderer shadowRenderer;
    private final PreviewRenderer previewRenderer;

    /**
     * Constructs coordinator with all required panels.
     *
     * @param gamePanel main game board panel
     * @param brickPanel active brick overlay panel
     * @param nextBrickPanel next brick preview panel
     * @param holdBrickPanel hold brick preview panel
     */
    public GameRendererCoordinator(GridPane gamePanel, GridPane brickPanel,
                                   GridPane nextBrickPanel, GridPane holdBrickPanel) {
        this.boardRenderer = new BoardRenderer(gamePanel);
        this.brickRenderer = new BrickRenderer(brickPanel, gamePanel);
        this.shadowRenderer = new ShadowRenderer(gamePanel);
        this.previewRenderer = new PreviewRenderer(nextBrickPanel, holdBrickPanel);
    }

    /**
     * Initializes all renderers with initial game state.
     *
     * @param boardMatrix initial board state
     * @param brick initial brick view data
     * @param parentPane parent pane for shadow overlay
     */
    public void initialize(int[][] boardMatrix, ViewData brick, Pane parentPane) {
        boardRenderer.initialize(boardMatrix);
        brickRenderer.initialize(brick.getBrickData());
        brickRenderer.positionPanel(brick);
        shadowRenderer.initialize(brick.getBrickData());

        parentPane.getChildren().add(shadowRenderer.getShadowPanel());
        shadowRenderer.update(brick);
        previewRenderer.initializeNextBrick(brick.getNextBrickData());
    }

    /**
     * Refreshes brick, shadow, and preview displays.
     *
     * @param viewData updated view data
     */
    public void refreshBrick(ViewData viewData) {
        brickRenderer.refresh(viewData);
        shadowRenderer.update(viewData);
        previewRenderer.updateNextBrick(viewData.getNextBrickData());
        previewRenderer.updateHoldBrick(viewData.getHoldBrickData());
    }

    /**
     * Refreshes the board background display.
     *
     * @param board updated board matrix
     */
    public void refreshBoard(int[][] board) {
        boardRenderer.refresh(board);
    }

    /**
     * Sets visibility of brick and shadow displays.
     *
     * @param visible true to show, false to hide
     */
    public void setVisible(boolean visible) {
        brickRenderer.setVisible(visible);
        shadowRenderer.setVisible(visible);
    }
}
