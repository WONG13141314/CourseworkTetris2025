package com.comp2042.model.data;

import com.comp2042.util.MatrixOperations;

/**
 * Data class representing the result of clearing rows.
 * Contains information about cleared lines, the updated board matrix, and score bonus.
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Constructs a ClearRow result.
     *
     * @param linesRemoved number of lines that were cleared
     * @param newMatrix the updated board matrix after clearing
     * @param scoreBonus score points earned from clearing
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Gets the number of lines removed.
     *
     * @return number of cleared lines
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Gets the updated board matrix after line clearing.
     *
     * @return copy of the new board matrix
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Gets the score bonus earned from clearing lines.
     *
     * @return score bonus points
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
