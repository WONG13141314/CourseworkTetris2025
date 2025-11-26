package com.comp2042.util;

import com.comp2042.model.data.ClearRow;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    // ==================== EXISTING TESTS ====================

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

    // ==================== NEW ENHANCED TESTS ====================

    @Test
    void testCheckRemoving_NonContiguousRows() {
        int[][] matrix = new int[15][10];

        // Fill row 5 and row 10 (non-contiguous)
        for (int j = 0; j < 10; j++) {
            matrix[5][j] = 1;
            matrix[10][j] = 1;
        }

        // Add some blocks in between
        matrix[7][3] = 2;
        matrix[8][4] = 3;

        ClearRow result = MatrixOperations.checkRemoving(matrix);
        int[][] newMatrix = result.getNewMatrix();

        assertEquals(2, result.getLinesRemoved(),
                "Should clear 2 non-contiguous rows");
        assertEquals(200, result.getScoreBonus(),
                "Score should be 50 * 2^2 = 200");

        // Verify blocks between cleared rows moved down
        // Original row 7 (with value 2) should move to a lower row
        boolean foundValue2 = false;
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix[i].length; j++) {
                if (newMatrix[i][j] == 2) {
                    foundValue2 = true;
                    assertTrue(i > 7,
                            "Block should have moved down after clearing rows above");
                }
            }
        }
        assertTrue(foundValue2, "Should find the block that was between cleared rows");
    }

    @Test
    void testCheckRemoving_AllRowsFilled() {
        int[][] matrix = new int[5][10];

        // Fill all rows
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(matrix);
        int[][] newMatrix = result.getNewMatrix();

        assertEquals(5, result.getLinesRemoved(), "Should clear all 5 rows");
        assertEquals(1250, result.getScoreBonus(), "Score: 50 * 5^2 = 1250");

        // All cells should be empty
        for (int i = 0; i < newMatrix.length; i++) {
            for (int j = 0; j < newMatrix[i].length; j++) {
                assertEquals(0, newMatrix[i][j],
                        "All cells should be empty after clearing all rows");
            }
        }
    }

    @Test
    void testCheckRemoving_PreservesBlocksAboveClearedRows() {
        int[][] matrix = new int[10][10];

        // Place blocks at top (rows 0-2)
        matrix[0][5] = 7;
        matrix[1][5] = 7;
        matrix[2][5] = 7;

        // Fill row 8 (will be cleared)
        for (int j = 0; j < 10; j++) {
            matrix[8][j] = 1;
        }

        ClearRow result = MatrixOperations.checkRemoving(matrix);
        int[][] newMatrix = result.getNewMatrix();

        assertEquals(1, result.getLinesRemoved());

        // The blocks with value 7 should have moved down by 1 row
        assertEquals(7, newMatrix[1][5], "Row 0 block should move to row 1");
        assertEquals(7, newMatrix[2][5], "Row 1 block should move to row 2");
        assertEquals(7, newMatrix[3][5], "Row 2 block should move to row 3");
    }

    @Test
    void testCheckRemoving_AlternatingFilledRows() {
        int[][] matrix = new int[10][10];

        // Fill rows 2, 4, 6, 8 (alternating)
        for (int i = 2; i <= 8; i += 2) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = 1;
            }
        }

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(4, result.getLinesRemoved());
        assertEquals(800, result.getScoreBonus());
    }

    @Test
    void testIntersect_ComplexBrickShape() {
        int[][] matrix = new int[10][10];
        matrix[5][5] = 1; // Single obstacle

        // T-shape brick
        int[][] tShape = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        // Should intersect when center piece overlaps obstacle
        assertTrue(MatrixOperations.intersect(matrix, tShape, 4, 5),
                "T-shape should intersect with obstacle at center");

        // Should not intersect when placed elsewhere
        assertFalse(MatrixOperations.intersect(matrix, tShape, 0, 0),
                "T-shape should not intersect in empty area");
    }

    @Test
    void testIntersect_WithRotatedBrick() {
        int[][] matrix = new int[10][10];

        // I-piece vertical
        int[][] iPieceVertical = {
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        };

        // Place obstacles in column 1
        matrix[5][1] = 1;
        matrix[6][1] = 1;

        assertTrue(MatrixOperations.intersect(matrix, iPieceVertical, 0, 5),
                "Vertical I-piece should intersect with column obstacles");
    }

    @Test
    void testMerge_PreservesExistingBlocks() {
        int[][] matrix = new int[10][10];
        matrix[0][0] = 5; // Existing block
        matrix[5][5] = 3; // Another existing block

        int[][] brick = {{1, 1}, {1, 1}};

        int[][] merged = MatrixOperations.merge(matrix, brick, 2, 2);

        // Verify existing blocks are preserved
        assertEquals(5, merged[0][0], "Should preserve existing block at [0][0]");
        assertEquals(3, merged[5][5], "Should preserve existing block at [5][5]");

        // Verify new brick is added
        assertEquals(1, merged[2][2]);
        assertEquals(1, merged[2][3]);
        assertEquals(1, merged[3][2]);
        assertEquals(1, merged[3][3]);
    }

    @Test
    void testMerge_WithComplexShape_CorrectPlacement() {
        int[][] matrix = new int[10][10];

        // L-shape - need to understand the coordinate system
        // The brick array is [row][col], where row maps to Y and col maps to X
        int[][] lShape = {
                {0, 0, 1},  // Row 0
                {1, 1, 1},  // Row 1
                {0, 0, 0}   // Row 2
        };

        // Merge at position (3, 5) means x=3, y=5
        // So brick[row][col] goes to matrix[y+row][x+col]
        int[][] merged = MatrixOperations.merge(matrix, lShape, 3, 5);

        // Verify L-shape placement
        // brick[0][2] = 1 -> matrix[5+0][3+2] = matrix[5][5]
        assertEquals(1, merged[5][5], "Top right of L");
        // brick[1][0] = 1 -> matrix[5+1][3+0] = matrix[6][3]
        assertEquals(1, merged[6][3], "Bottom left of L");
        // brick[1][1] = 1 -> matrix[5+1][3+1] = matrix[6][4]
        assertEquals(1, merged[6][4], "Bottom middle of L");
        // brick[1][2] = 1 -> matrix[5+1][3+2] = matrix[6][5]
        assertEquals(1, merged[6][5], "Bottom right of L");
    }

    @Test
    void testCopy_LargeMatrix() {
        int[][] large = new int[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                large[i][j] = i * 100 + j;
            }
        }

        int[][] copy = MatrixOperations.copy(large);

        // Modify copy
        copy[50][50] = -1;

        assertEquals(5050, large[50][50], "Original should not be modified");
        assertEquals(-1, copy[50][50], "Copy should be modified");
    }

    @Test
    void testDeepCopyList_MultipleMatrices() {
        java.util.List<int[][]> original = new java.util.ArrayList<>();
        original.add(new int[][]{{1, 2}, {3, 4}});
        original.add(new int[][]{{5, 6}, {7, 8}});
        original.add(new int[][]{{9, 10}, {11, 12}});

        java.util.List<int[][]> copy = MatrixOperations.deepCopyList(original);

        // Modify all matrices in copy
        copy.get(0)[0][0] = 999;
        copy.get(1)[0][0] = 999;
        copy.get(2)[0][0] = 999;

        // Verify originals are not modified
        assertEquals(1, original.get(0)[0][0]);
        assertEquals(5, original.get(1)[0][0]);
        assertEquals(9, original.get(2)[0][0]);
    }

    @Test
    void testCheckRemoving_WithDifferentBlockTypes() {
        int[][] matrix = new int[5][10];

        // Fill bottom row with different block types
        for (int j = 0; j < 10; j++) {
            matrix[4][j] = (j % 7) + 1; // Different brick types
        }

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(1, result.getLinesRemoved(),
                "Should clear row regardless of block types");
    }

    @Test
    void testIntersect_BrickPartiallyOutOfBounds() {
        int[][] matrix = new int[10][10];
        int[][] brick = {{1, 1}, {1, 1}};

        // Partially out of bounds on right
        assertTrue(MatrixOperations.intersect(matrix, brick, 9, 5),
                "Should intersect when brick extends beyond right edge");

        // Partially out of bounds on bottom
        assertTrue(MatrixOperations.intersect(matrix, brick, 5, 9),
                "Should intersect when brick extends beyond bottom edge");
    }

    @Test
    void testCheckRemoving_EmptyMatrix() {
        int[][] matrix = new int[10][10];

        ClearRow result = MatrixOperations.checkRemoving(matrix);

        assertEquals(0, result.getLinesRemoved());
        assertEquals(0, result.getScoreBonus());

        // Matrix should remain unchanged
        int[][] newMatrix = result.getNewMatrix();
        assertEquals(10, newMatrix.length);
        assertEquals(10, newMatrix[0].length);
    }
}