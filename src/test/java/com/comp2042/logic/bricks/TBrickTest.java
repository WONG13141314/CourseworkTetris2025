package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TBrickTest {
    private TBrick brick;

    @BeforeEach
    void setUp() {
        brick = new TBrick();
    }

    @Test
    void testGetShapeMatrix_HasFourRotations() {
        assertEquals(4, brick.getShapeMatrix().size(),
                "T-brick should have 4 rotations");
    }

    @Test
    void testAllRotations_UseCorrectBrickType() {
        List<int[][]> shapes = brick.getShapeMatrix();

        for (int[][] shape : shapes) {
            boolean hasSix = false;
            for (int[] row : shape) {
                for (int val : row) {
                    if (val == 6) hasSix = true;
                }
            }
            assertTrue(hasSix, "Each rotation should contain brick type 6");
        }
    }

    @Test
    void testShape_HasFourBlocks() {
        int[][] shape = brick.getShapeMatrix().get(0);
        int count = 0;

        for (int[] row : shape) {
            for (int val : row) {
                if (val == 6) count++;
            }
        }

        assertEquals(4, count, "T-brick should have exactly 4 blocks");
    }

    @Test
    void testGetShapeMatrix_ReturnsDeepCopy() {
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1, shapes2, "Should return different list instances");
        shapes1.get(0)[0][0] = 999;
        assertNotEquals(999, shapes2.get(0)[0][0], "Should be independent copy");
    }
}