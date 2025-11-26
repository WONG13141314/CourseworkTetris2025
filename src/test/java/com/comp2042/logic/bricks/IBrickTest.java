package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class IBrickTest {
    private IBrick brick;

    @BeforeEach
    void setUp() {
        brick = new IBrick();
    }

    @Test
    void testGetShapeMatrix_ReturnsCorrectNumberOfRotations() {
        List<int[][]> shapes = brick.getShapeMatrix();
        assertEquals(2, shapes.size(), "I-brick should have 2 rotations");
    }

    @Test
    void testGetShapeMatrix_ReturnsDeepCopy() {
        List<int[][]> shapes1 = brick.getShapeMatrix();
        List<int[][]> shapes2 = brick.getShapeMatrix();

        assertNotSame(shapes1, shapes2, "Should return different list instances");
        assertNotSame(shapes1.get(0), shapes2.get(0), "Should return deep copy of matrices");
    }

    @Test
    void testHorizontalRotation_HasCorrectShape() {
        List<int[][]> shapes = brick.getShapeMatrix();
        int[][] horizontal = shapes.get(0);

        assertEquals(4, horizontal.length);
        assertEquals(4, horizontal[0].length);

        int count = 0;
        for (int val : horizontal[1]) {
            if (val == 1) count++;
        }
        assertEquals(4, count, "Horizontal I-brick should have 4 blocks in a row");
    }

    @Test
    void testVerticalRotation_HasCorrectShape() {
        List<int[][]> shapes = brick.getShapeMatrix();
        int[][] vertical = shapes.get(1);

        int count = 0;
        for (int i = 0; i < vertical.length; i++) {
            if (vertical[i][1] == 1) count++;
        }
        assertEquals(4, count, "Vertical I-brick should have 4 blocks in a column");
    }
}