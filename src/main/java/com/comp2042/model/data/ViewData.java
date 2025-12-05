package com.comp2042.model.data;

import com.comp2042.util.MatrixOperations;

/**
 * Immutable data class containing all information needed to render the game state.
 * Includes current brick, position, preview bricks, and shadow position.
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int shadowYPosition;
    private final int[][] holdBrickData;

    /**
     * Constructs view data with all game state information.
     *
     * @param brickData matrix representing current brick
     * @param xPosition x-coordinate of brick
     * @param yPosition y-coordinate of brick
     * @param nextBrickData matrix representing next brick
     * @param shadowYPosition y-coordinate where brick will land
     * @param holdBrickData matrix representing held brick, or null
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition,
                    int[][] nextBrickData, int shadowYPosition, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.shadowYPosition = shadowYPosition;
        this.holdBrickData = holdBrickData;
    }

    /**
     * Gets the current brick matrix.
     *
     * @return copy of brick data
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Gets the x-position of the brick.
     *
     * @return x-coordinate
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Gets the y-position of the brick.
     *
     * @return y-coordinate
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Gets the next brick matrix.
     *
     * @return copy of next brick data
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Gets the shadow y-position (where brick will land).
     *
     * @return shadow y-coordinate
     */
    public int getShadowYPosition() {
        return shadowYPosition;
    }

    /**
     * Gets the held brick matrix.
     *
     * @return copy of hold brick data, or empty array if no brick held
     */
    public int[][] getHoldBrickData() {
        return MatrixOperations.copy(holdBrickData != null ? holdBrickData : new int[0][0]);
    }
}