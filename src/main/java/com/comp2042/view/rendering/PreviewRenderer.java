package com.comp2042.view.rendering;

import com.comp2042.util.ColorPalette;
import com.comp2042.util.GameConstants;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handles rendering of next brick and hold brick preview panels.
 * Centers bricks within fixed-size preview areas.
 */
public class PreviewRenderer {

    private final GridPane nextBrickPanel;
    private final GridPane holdBrickPanel;
    private Rectangle[][] nextBrickRectangles;
    private Rectangle[][] holdBrickRectangles;

    /**
     * Constructs preview renderer.
     *
     * @param nextBrickPanel panel for next brick
     * @param holdBrickPanel panel for hold brick
     */
    public PreviewRenderer(GridPane nextBrickPanel, GridPane holdBrickPanel) {
        this.nextBrickPanel = nextBrickPanel;
        this.holdBrickPanel = holdBrickPanel;
    }

    /**
     * Initializes next brick preview.
     *
     * @param nextBrickData initial next brick data
     */
    public void initializeNextBrick(int[][] nextBrickData) {
        if (nextBrickPanel != null) {
            renderCenteredBrick(nextBrickData, nextBrickPanel, true);
        }
    }

    /**
     * Updates next brick preview.
     *
     * @param nextBrickData updated next brick data
     */
    public void updateNextBrick(int[][] nextBrickData) {
        if (nextBrickPanel != null) {
            renderCenteredBrick(nextBrickData, nextBrickPanel, true);
        }
    }

    /**
     * Updates hold brick preview.
     *
     * @param holdBrickData updated hold brick data, or null/empty to clear
     */
    public void updateHoldBrick(int[][] holdBrickData) {
        if (holdBrickPanel != null) {
            if (holdBrickData != null && holdBrickData.length > 0) {
                renderCenteredBrick(holdBrickData, holdBrickPanel, false);
            } else {
                holdBrickPanel.getChildren().clear();
                holdBrickRectangles = null;
            }
        }
    }

    /**
     * Renders brick centered in preview panel.
     *
     * @param brickData brick shape matrix
     * @param panel target GridPane
     * @param isNextBrick true if next brick, false if hold brick
     */
    private void renderCenteredBrick(int[][] brickData, GridPane panel, boolean isNextBrick) {
        panel.getChildren().clear();

        int rows = brickData.length;
        int cols = brickData[0].length;
        int maxSize = GameConstants.PREVIEW_PANEL_SIZE;

        int rowPadding = (maxSize - rows) / 2;
        int colPadding = (maxSize - cols) / 2;

        Rectangle[][] rectangles = new Rectangle[maxSize][maxSize];

        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                Rectangle rectangle = new Rectangle(
                        GameConstants.BRICK_SIZE - 2,
                        GameConstants.BRICK_SIZE - 2
                );

                if (i >= rowPadding && i < rowPadding + rows &&
                        j >= colPadding && j < colPadding + cols) {
                    int brickValue = brickData[i - rowPadding][j - colPadding];
                    rectangle.setFill(ColorPalette.getBrickColor(brickValue));
                } else {
                    rectangle.setFill(Color.TRANSPARENT);
                }

                rectangle.setArcHeight(GameConstants.BRICK_ARC_SIZE);
                rectangle.setArcWidth(GameConstants.BRICK_ARC_SIZE);
                rectangles[i][j] = rectangle;
                panel.add(rectangle, j, i);
            }
        }

        if (isNextBrick) {
            nextBrickRectangles = rectangles;
        } else {
            holdBrickRectangles = rectangles;
        }
    }
}