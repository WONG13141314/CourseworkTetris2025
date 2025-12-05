package com.comp2042.view.rendering;

import com.comp2042.model.data.ViewData;
import com.comp2042.util.ColorPalette;
import com.comp2042.util.GameConstants;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handles rendering of the shadow (ghost piece) showing where brick will land.
 * Displays semi-transparent preview at drop position.
 */
public class ShadowRenderer {

    private final GridPane gamePanel;
    private final GridPane shadowPanel;
    private Rectangle[][] shadowRectangles;

    /**
     * Constructs shadow renderer.
     *
     * @param gamePanel main game panel
     */
    public ShadowRenderer(GridPane gamePanel) {
        this.gamePanel = gamePanel;
        this.shadowPanel = new GridPane();
        this.shadowPanel.setVgap(GameConstants.BRICK_SPACING);
        this.shadowPanel.setHgap(GameConstants.BRICK_SPACING);
    }

    /**
     * Initializes shadow with initial brick data.
     *
     * @param brickData initial brick shape
     */
    public void initialize(int[][] brickData) {
        shadowRectangles = new Rectangle[brickData.length][brickData[0].length];

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle shadow = new Rectangle(
                        GameConstants.BRICK_SIZE,
                        GameConstants.BRICK_SIZE
                );
                shadow.setFill(ColorPalette.getShadowColor(brickData[i][j]));
                shadow.setOpacity(GameConstants.SHADOW_OPACITY);
                shadow.setArcWidth(GameConstants.BRICK_ARC_SIZE);
                shadow.setArcHeight(GameConstants.BRICK_ARC_SIZE);
                shadowRectangles[i][j] = shadow;
                shadowPanel.add(shadow, j, i);
            }
        }
    }

    /**
     * Updates shadow position based on current brick state.
     * Only shows shadow if different from actual brick position.
     *
     * @param viewData updated view data
     */
    public void update(ViewData viewData) {
        // Clear existing shadow
        for (int i = 0; i < shadowRectangles.length; i++) {
            for (int j = 0; j < shadowRectangles[i].length; j++) {
                shadowRectangles[i][j].setFill(Color.TRANSPARENT);
                gamePanel.getChildren().remove(shadowRectangles[i][j]);
            }
        }

        int[][] brickData = viewData.getBrickData();

        // Recreate if size changed
        if (shadowRectangles.length != brickData.length ||
                shadowRectangles[0].length != brickData[0].length) {
            shadowRectangles = new Rectangle[brickData.length][brickData[0].length];
            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    shadowRectangles[i][j] = new Rectangle(
                            GameConstants.BRICK_SIZE,
                            GameConstants.BRICK_SIZE
                    );
                    shadowRectangles[i][j].setArcWidth(GameConstants.BRICK_ARC_SIZE);
                    shadowRectangles[i][j].setArcHeight(GameConstants.BRICK_ARC_SIZE);
                    shadowRectangles[i][j].setOpacity(GameConstants.SHADOW_OPACITY);
                }
            }
        }

        // Show shadow only if different from brick position
        if (viewData.getShadowYPosition() != viewData.getyPosition()) {
            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    if (brickData[i][j] != 0) {
                        int gridX = viewData.getxPosition() + j;
                        int gridY = viewData.getShadowYPosition() + i;

                        if (gridY >= GameConstants.VISIBLE_START_ROW && gridY < GameConstants.BOARD_WIDTH) {
                            shadowRectangles[i][j].setFill(ColorPalette.getShadowColor(brickData[i][j]));
                            gamePanel.add(shadowRectangles[i][j], gridX, gridY - GameConstants.VISIBLE_START_ROW);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the shadow panel.
     *
     * @return shadow GridPane
     */
    public GridPane getShadowPanel() {
        return shadowPanel;
    }

    /**
     * Sets shadow visibility.
     *
     * @param visible true to show, false to hide
     */
    public void setVisible(boolean visible) {
        if (shadowPanel != null) {
            shadowPanel.setVisible(visible);
        }
    }
}