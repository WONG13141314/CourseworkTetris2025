package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class LBrickTest {
    private LBrick brick;

    @BeforeEach
    void setUp() {
        brick = new LBrick();
    }

    @Test
    void testGetShapeMatrix_HasFourRotations() {
        assertEquals(4, brick.getShapeMatrix().size(), "L-brick should have 4 rotations");
    }

    @Test
    void testAllRotations_UseCorrectBrickType() {
        List<int[][]> shapes = brick.getShapeMatrix();

        for (int[][] shape : shapes) {
            boolean hasThree = false;
            for (int[] row : shape) {
                for (int val : row) {
                    if (val == 3) hasThree = true;
                }
            }
            assertTrue(hasThree, "Each rotation should contain brick type 3");
        }
    }

    @Test
    void testGetShapeMatrix_ReturnsDeepCopy() {
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1, shapes2, "Should return different list instances");
    }

    @Test
    void testRotations_AreDistinct() {
        List<int[][]> shapes = brick.getShapeMatrix();

        assertNotEquals(shapes.get(0), shapes.get(1));
        assertNotEquals(shapes.get(1), shapes.get(2));
        assertNotEquals(shapes.get(2), shapes.get(3));
    }
}