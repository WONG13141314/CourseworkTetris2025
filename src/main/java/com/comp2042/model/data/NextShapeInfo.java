package com.comp2042.model.data;

import com.comp2042.util.MatrixOperations;

/**
 * Data class containing information about the next rotation state.
 * Used for brick rotation calculations.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs next shape information.
     *
     * @param shape the 2D array representing the rotated brick shape
     * @param position the rotation position index
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets the rotated shape matrix.
     *
     * @return copy of the shape matrix
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the rotation position index.
     *
     * @return rotation position
     */
    public int getPosition() {
        return position;
    }
}
