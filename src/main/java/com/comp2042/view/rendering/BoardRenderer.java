package com.comp2042.view.rendering;

import com.comp2042.util.ColorPalette;
import com.comp2042.util.GameConstants;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handles rendering of the game board (placed bricks background).
 * Manages the grid of rectangles representing the board state.
 */
public class BoardRenderer {

    private final GridPane gamePanel;
    private Rectangle[][] displayMatrix;

    /**
     * Constructs board renderer for given panel.
     *
     * @param gamePanel the GridPane to render board on
     */
    public BoardRenderer(GridPane gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Initializes the board display with empty cells.
     * Creates rectangle grid starting from visible row.
     *
     * @param boardMatrix initial board state
     */
    public void initialize(int[][] boardMatrix) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        gamePanel.getChildren().clear();

        for (int i = GameConstants.VISIBLE_START_ROW; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(
                        GameConstants.BRICK_SIZE,
                        GameConstants.BRICK_SIZE
                );
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setArcWidth(GameConstants.BRICK_ARC_SIZE);
                rectangle.setArcHeight(GameConstants.BRICK_ARC_SIZE);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - GameConstants.VISIBLE_START_ROW);
            }
        }
    }

    /**
     * Refreshes the board display with current state.
     * Updates colors of all visible cells.
     *
     * @param boardMatrix updated board state
     */
    public void refresh(int[][] boardMatrix) {
        for (int i = GameConstants.VISIBLE_START_ROW; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                displayMatrix[i][j].setFill(ColorPalette.getBrickColor(boardMatrix[i][j]));
            }
        }
    }
}
