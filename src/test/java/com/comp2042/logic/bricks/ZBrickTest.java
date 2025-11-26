package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class ZBrickTest {
    private ZBrick brick;

    @BeforeEach
    void setUp() {
        brick = new ZBrick();
    }

    @Test
    void testGetShapeMatrix_HasTwoRotations() {
        assertEquals(2, brick.getShapeMatrix().size(),
                "Z-brick should have 2 rotations");
    }

    @Test
    void testAllRotations_UseCorrectBrickType() {
        List<int[][]> shapes = brick.getShapeMatrix();

        for (int[][] shape : shapes) {
            boolean hasSeven = false;
            for (int[] row : shape) {
                for (int val : row) {
                    if (val == 7) hasSeven = true;
                }
            }
            assertTrue(hasSeven, "Each rotation should contain brick type 7");
        }
    }

    @Test
    void testShape_HasFourBlocks() {
        int[][] shape = brick.getShapeMatrix().get(0);
        int count = 0;

        for (int[] row : shape) {
            for (int val : row) {
                if (val == 7) count++;
            }
        }

        assertEquals(4, count, "Z-brick should have exactly 4 blocks");
    }

    @Test
    void testGetShapeMatrix_ReturnsDeepCopy() {
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1, shapes2, "Should return different list instances");
    }
}