package com.comp2042.view.rendering;

import com.comp2042.model.data.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Coordinates all rendering operations
 */
public class GameRendererCoordinator {

    private final BoardRenderer boardRenderer;
    private final BrickRenderer brickRenderer;
    private final ShadowRenderer shadowRenderer;
    private final PreviewRenderer previewRenderer;

    public GameRendererCoordinator(GridPane gamePanel, GridPane brickPanel,
                                   GridPane nextBrickPanel, GridPane holdBrickPanel) {
        this.boardRenderer = new BoardRenderer(gamePanel);
        this.brickRenderer = new BrickRenderer(brickPanel, gamePanel);
        this.shadowRenderer = new ShadowRenderer(gamePanel);
        this.previewRenderer = new PreviewRenderer(nextBrickPanel, holdBrickPanel);
    }

    public void initialize(int[][] boardMatrix, ViewData brick, Pane parentPane) {
        boardRenderer.initialize(boardMatrix);
        brickRenderer.initialize(brick.getBrickData());
        brickRenderer.positionPanel(brick);
        shadowRenderer.initialize(brick.getBrickData());

        parentPane.getChildren().add(shadowRenderer.getShadowPanel());
        shadowRenderer.update(brick);
        previewRenderer.initializeNextBrick(brick.getNextBrickData());
    }

    public void refreshBrick(ViewData viewData) {
        brickRenderer.refresh(viewData);
        shadowRenderer.update(viewData);
        previewRenderer.updateNextBrick(viewData.getNextBrickData());
        previewRenderer.updateHoldBrick(viewData.getHoldBrickData());
    }

    public void refreshBoard(int[][] board) {
        boardRenderer.refresh(board);
    }

    public void setVisible(boolean visible) {
        brickRenderer.setVisible(visible);
        shadowRenderer.setVisible(visible);
    }
}