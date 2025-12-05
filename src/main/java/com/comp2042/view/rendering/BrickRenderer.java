package com.comp2042.view.rendering;

import com.comp2042.model.data.ViewData;
import com.comp2042.util.ColorPalette;
import com.comp2042.util.GameConstants;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handles rendering of the active falling brick.
 * Manages brick positioning, rotation, and color updates.
 */
public class BrickRenderer {

    private final GridPane brickPanel;
    private final GridPane gamePanel;
    private Rectangle[][] rectangles;

    /**
     * Constructs brick renderer.
     *
     * @param brickPanel overlay panel for brick
     * @param gamePanel main game panel for positioning
     */
    public BrickRenderer(GridPane brickPanel, GridPane gamePanel) {
        this.brickPanel = brickPanel;
        this.gamePanel = gamePanel;
    }

    /**
     * Initializes brick rectangles based on initial brick data.
     *
     * @param brickData initial brick shape matrix
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
     * Refreshes brick display with new position and rotation.
     * Handles brick size changes from rotation.
     *
     * @param viewData updated view data
     */
    public void refresh(ViewData viewData) {
        // Remove old rectangles
        for (Rectangle[] row : rectangles) {
            for (Rectangle r : row) {
                gamePanel.getChildren().remove(r);
            }
        }

        int[][] brickData = viewData.getBrickData();

        // Recreate if size changed
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

        // Update and position rectangles
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
     * Positions the brick panel overlay.
     *
     * @param viewData view data with position information
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

    /**
     * Sets brick visibility.
     *
     * @param visible true to show, false to hide
     */
    public void setVisible(boolean visible) {
        brickPanel.setVisible(visible);
    }
}