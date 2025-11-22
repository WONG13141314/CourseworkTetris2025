package com.comp2042.view.rendering;

import com.comp2042.model.data.ViewData;
import com.comp2042.util.ColorPalette;
import com.comp2042.util.GameConstants;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handles rendering of the active falling brick
 */
public class BrickRenderer {

    private final GridPane brickPanel;
    private final GridPane gamePanel;
    private Rectangle[][] rectangles;

    public BrickRenderer(GridPane brickPanel, GridPane gamePanel) {
        this.brickPanel = brickPanel;
        this.gamePanel = gamePanel;
    }

    /**
     * Initialize the brick rectangles based on initial brick data
     */
    public void initialize(int[][] brickData) {
        rectangles = new Rectangle[brickData.length][brickData[0].length];
        brickPanel.getChildren().clear();

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(
                        GameConstants.BRICK_SIZE,
                        GameConstants.BRICK_SIZE
                );
                rectangle.setFill(ColorPalette.getBrickColor(brickData[i][j]));
                rectangle.setArcWidth(GameConstants.BRICK_ARC_SIZE);
                rectangle.setArcHeight(GameConstants.BRICK_ARC_SIZE);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
    }

    /**
     * Refresh the brick display with new position and rotation
     */
    public void refresh(ViewData viewData) {
        // Remove old rectangles from game panel
        for (Rectangle[] row : rectangles) {
            for (Rectangle r : row) {
                gamePanel.getChildren().remove(r);
            }
        }

        int[][] brickData = viewData.getBrickData();

        // Recreate rectangles if size changed (rotation)
        if (rectangles.length != brickData.length ||
                rectangles[0].length != brickData[0].length) {
            rectangles = new Rectangle[brickData.length][brickData[0].length];
            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    rectangles[i][j] = new Rectangle(
                            GameConstants.BRICK_SIZE,
                            GameConstants.BRICK_SIZE
                    );
                    rectangles[i][j].setArcWidth(GameConstants.BRICK_ARC_SIZE);
                    rectangles[i][j].setArcHeight(GameConstants.BRICK_ARC_SIZE);
                }
            }
        }

        // Update rectangles and add to game panel
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (brickData[i][j] != 0) {
                    int gridX = viewData.getxPosition() + j;
                    int gridY = viewData.getyPosition() + i;

                    if (gridY >= GameConstants.VISIBLE_START_ROW) {
                        rectangles[i][j].setFill(ColorPalette.getBrickColor(brickData[i][j]));
                        gamePanel.add(rectangles[i][j], gridX, gridY - GameConstants.VISIBLE_START_ROW);
                    }
                } else {
                    rectangles[i][j].setFill(Color.TRANSPARENT);
                }
            }
        }
    }

    /**
     * Position the brick panel overlay (used for initial brick placement)
     */
    public void positionPanel(ViewData viewData) {
        double brickX = GameConstants.GAME_PANEL_X +
                GameConstants.BRICK_PANEL_X_OFFSET +
                viewData.getxPosition() * (GameConstants.BRICK_SIZE + 1);
        double brickY = GameConstants.GAME_PANEL_Y +
                GameConstants.BRICK_PANEL_Y_OFFSET +
                (viewData.getyPosition() - 2) * (GameConstants.BRICK_SIZE + 1);

        brickPanel.setLayoutX(brickX);
        brickPanel.setLayoutY(brickY);
    }

    public void setVisible(boolean visible) {
        brickPanel.setVisible(visible);
    }
}