package com.comp2042.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {
    @Test
    void testConstructor_StoresAllFields() {
        int[][] brickData = {{1, 1}, {1, 1}};
        int[][] nextBrick = {{2, 2}, {2, 2}};
        int[][] holdBrick = {{3, 3}, {3, 3}};

        ViewData vd = new ViewData(brickData, 5, 10, nextBrick, 15, holdBrick);

        assertEquals(5, vd.getxPosition());
        assertEquals(10, vd.getyPosition());
        assertEquals(15, vd.getShadowYPosition());
    }

    @Test
    void testGetBrickData_ReturnsCopy() {
        int[][] original = {{1, 1}, {1, 1}};
        ViewData vd = new ViewData(original, 0, 0, original, 0, original);

        int[][] retrieved = vd.getBrickData();
        retrieved[0][0] = 999;

        assertNotEquals(999, vd.getBrickData()[0][0],
                "Should return copy to prevent external modification");
    }

    @Test
    void testGetNextBrickData_ReturnsCopy() {
        int[][] original = {{1, 1}, {1, 1}};
        ViewData vd = new ViewData(original, 0, 0, original, 0, original);

        int[][] retrieved = vd.getNextBrickData();
        retrieved[0][0] = 999;

        assertNotEquals(999, vd.getNextBrickData()[0][0],
                "Should return copy to prevent external modification");
    }

    @Test
    void testGetHoldBrickData_WithNullHoldBrick() {
        ViewData vd = new ViewData(new int[2][2], 0, 0, new int[2][2], 0, null);

        int[][] hold = vd.getHoldBrickData();
        assertNotNull(hold, "Should return empty array, not null");
        assertEquals(0, hold.length, "Should be empty array");
    }

    @Test
    void testGetHoldBrickData_ReturnsCopy() {
        int[][] original = {{1, 1}, {1, 1}};
        ViewData vd = new ViewData(original, 0, 0, original, 0, original);

        int[][] retrieved = vd.getHoldBrickData();
        retrieved[0][0] = 999;

        assertNotEquals(999, vd.getHoldBrickData()[0][0],
                "Should return copy to prevent external modification");
    }

    @Test
    void testPositions_CanBeNegative() {
        ViewData vd = new ViewData(new int[2][2], -1, -5, new int[2][2], 0, null);

        assertEquals(-1, vd.getxPosition());
        assertEquals(-5, vd.getyPosition());
    }

    @Test
    void testShadowPosition_CanBeDifferentFromYPosition() {
        ViewData vd = new ViewData(new int[2][2], 5, 10, new int[2][2], 20, null);

        assertEquals(10, vd.getyPosition());
        assertEquals(20, vd.getShadowYPosition());
        assertNotEquals(vd.getyPosition(), vd.getShadowYPosition());
    }
}