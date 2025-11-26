package com.comp2042.util;

import com.comp2042.model.data.ClearRow;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testCopy_CreatesDeepCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        int[][] copy = MatrixOperations.copy(original);

        copy[0][0] = 999;

        assertEquals(1, original[0][0], "Original should not be modified");
        assertEquals(999, copy[0][0], "Copy should be modified");
    }

    @Test
    void testIntersect_WithEmptyMatrix_ReturnsFalse() {
        int[][] matrix = new int[10][10];
        int[][] brick = {{1, 1}, {1, 1}};

        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 0),
                "Should not intersect with empty matrix");
    }

    @Test
    void testIntersect_OutOfBounds_ReturnsTrue() {
        int[][] matrix = new int[10][10];
        int[][] brick = {{1}};

        assertTrue(MatrixOperations.intersect(matrix, brick, -1, 0),
                "Should intersect when out of bounds");
        assertTrue(MatrixOperations.intersect(matrix, brick, 10, 0),
                "Should intersect when out of bounds");
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 10),
                "Should intersect when out of bounds");
    }

    @Test
    void testIntersect_WithFilledCell_ReturnsTrue() {
        int[][] matrix = new int[10][10];
        matrix[5][5] = 1;
        int[][] brick = {{1}};

        assertTrue(MatrixOperations.intersect(matrix, brick, 5, 5),
                "Should intersect with filled cell");
    }

    @Test
    void testMerge_CombinesBrickAndMatrix() {
        int[][] matrix = new int[5][5];
        int[][] brick = {{1, 1}, {1, 1}};

        int[][] merged = MatrixOperations.merge(matrix, brick, 0, 0);

        assertEquals(1, merged[0][0]);
        assertEquals(1, merged[0][1]);
        assertEquals(1, merged[1][0]);
        assertEquals(1, merged[1][1]);
    }

    @Test
    void testMerge_DoesNotModifyOriginal() {
        int[][] matrix = new int[5][5];
        int[][] brick = {{1}};

        MatrixOperations.merge(matrix, brick, 0, 0);

        assertEquals(0, matrix[0][0], "Original matrix should not be modified");
    }

    @Test
    void testCheckRemoving_NoCompleteRows() {
        int[][] matrix = new int[5][10];
        matrix[0][0] = 1;

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(0, result.getLinesRemoved(), "Should not remove partial rows");
        assertEquals(0, result.getScoreBonus(), "No bonus for no clears");
    }

    @Test
    void testCheckRemoving_OneCompleteRow() {
        int[][] matrix = new int[5][10];
        for (int i = 0; i < 10; i++) {
            matrix[4][i] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(1, result.getLinesRemoved());
        assertEquals(50, result.getScoreBonus(), "Score should be 50 * 1^2");
    }

    @Test
    void testCheckRemoving_MultipleRows() {
        int[][] matrix = new int[5][10];
        for (int j = 0; j < 10; j++) {
            matrix[3][j] = 1;
            matrix[4][j] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(2, result.getLinesRemoved());
        assertEquals(200, result.getScoreBonus(), "Score should be 50 * 2^2");
    }

    @Test
    void testCheckRemoving_ScoreIncreasesBySquare() {
        int[][] matrix1 = new int[5][10];
        for (int i = 0; i < 10; i++) matrix1[4][i] = 1;
        ClearRow one = MatrixOperations.checkRemoving(matrix1);

        int[][] matrix2 = new int[5][10];
        for (int j = 0; j < 10; j++) {
            matrix2[3][j] = 1;
            matrix2[4][j] = 1;
        }
        ClearRow two = MatrixOperations.checkRemoving(matrix2);

        assertEquals(50, one.getScoreBonus());
        assertEquals(200, two.getScoreBonus(), "Double clear should give 4x points");
    }

    @Test
    void testCheckRemoving_RowsCollapse() {
        int[][] matrix = new int[5][10];
        matrix[0][0] = 7;  // Top row has value 7
        for (int i = 0; i < 10; i++) {
            matrix[4][i] = 1;  // Bottom row (row 4) is complete and will be cleared
        }

        ClearRow result = MatrixOperations.checkRemoving(matrix);
        int[][] newMatrix = result.getNewMatrix();

        // After clearing row 4, rows collapse down:
        // Old row 0 (with 7) becomes new row 1
        // New row 0 is empty
        assertEquals(0, newMatrix[0][0], "Top should be empty after collapse");
        assertEquals(7, newMatrix[1][0], "Row 0 should move to row 1");
    }

    @Test
    void testDeepCopyList_CreatesIndependentCopies() {
        java.util.List<int[][]> original = new java.util.ArrayList<>();
        original.add(new int[][]{{1, 2}, {3, 4}});

        java.util.List<int[][]> copy = MatrixOperations.deepCopyList(original);
        copy.get(0)[0][0] = 999;

        assertEquals(1, original.get(0)[0][0], "Original should not be modified");
    }

    @Test
    void testCheckRemoving_FourRowClear() {
        int[][] matrix = new int[6][10];
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(4, result.getLinesRemoved());
        assertEquals(800, result.getScoreBonus(), "Tetris clear: 50 * 4^2 = 800");
    }
}