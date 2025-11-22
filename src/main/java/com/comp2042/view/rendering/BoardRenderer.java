package com.comp2042.view.rendering;

import com.comp2042.util.ColorPalette;
import com.comp2042.util.GameConstants;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handles rendering of the game board (placed bricks background)
 */
public class BoardRenderer {

    private final GridPane gamePanel;
    private Rectangle[][] displayMatrix;

    public BoardRenderer(GridPane gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Initialize the board display with empty cells
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
     * Refresh the board display with current state
     */
    public void refresh(int[][] boardMatrix) {
        for (int i = GameConstants.VISIBLE_START_ROW; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                displayMatrix[i][j].setFill(ColorPalette.getBrickColor(boardMatrix[i][j]));
            }
        }
    }
}