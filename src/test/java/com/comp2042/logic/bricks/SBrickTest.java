package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class SBrickTest {
    private SBrick brick;

    @BeforeEach
    void setUp() {
        brick = new SBrick();
    }

    @Test
    void testGetShapeMatrix_HasTwoRotations() {
        assertEquals(2, brick.getShapeMatrix().size(),
                "S-brick should have 2 rotations");
    }

    @Test
    void testAllRotations_UseCorrectBrickType() {
        List<int[][]> shapes = brick.getShapeMatrix();

        for (int[][] shape : shapes) {
            boolean hasFive = false;
            for (int[] row : shape) {
                for (int val : row) {
                    if (val == 5) hasFive = true;
                }
            }
            assertTrue(hasFive, "Each rotation should contain brick type 5");
        }
    }

    @Test
    void testShape_HasFourBlocks() {
        int[][] shape = brick.getShapeMatrix().get(0);
        int count = 0;

        for (int[] row : shape) {
            for (int val : row) {
                if (val == 5) count++;
            }
        }

        assertEquals(4, count, "S-brick should have exactly 4 blocks");
    }

    @Test
    void testGetShapeMatrix_ReturnsDeepCopy() {
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1, shapes2, "Should return different list instances");
    }
}