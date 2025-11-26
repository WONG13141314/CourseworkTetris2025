package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class JBrickTest {
    private JBrick brick;

    @BeforeEach
    void setUp() {
        brick = new JBrick();
    }

    @Test
    void testGetShapeMatrix_HasFourRotations() {
        assertEquals(4, brick.getShapeMatrix().size(), "J-brick should have 4 rotations");
    }

    @Test
    void testAllRotations_UseCorrectBrickType() {
        List<int[][]> shapes = brick.getShapeMatrix();

        for (int[][] shape : shapes) {
            boolean hasTwo = false;
            for (int[] row : shape) {
                for (int val : row) {
                    if (val == 2) hasTwo = true;
                }
            }
            assertTrue(hasTwo, "Each rotation should contain brick type 2");
        }
    }

    @Test
    void testGetShapeMatrix_ReturnsDeepCopy() {
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1, shapes2, "Should return different list instances");
    }
}