package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class OBrickTest {
    private OBrick brick;

    @BeforeEach
    void setUp() {
        brick = new OBrick();
    }

    @Test
    void testGetShapeMatrix_HasOnlyOneRotation() {
        assertEquals(1, brick.getShapeMatrix().size(),
                "O-brick should have only 1 rotation (square shape)");
    }

    @Test
    void testShape_IsSquare() {
        int[][] shape = brick.getShapeMatrix().get(0);

        int count = 0;
        for (int[] row : shape) {
            for (int val : row) {
                if (val == 4) count++;
            }
        }
        assertEquals(4, count, "O-brick should have exactly 4 blocks");
    }

    @Test
    void testGetShapeMatrix_ReturnsDeepCopy() {
        int[][] shape1 = brick.getShapeMatrix().get(0);
        int[][] shape2 = brick.getShapeMatrix().get(0);

        assertNotSame(shape1, shape2, "Should return different instances");
    }
}