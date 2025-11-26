package com.comp2042.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NextShapeInfoTest {
    @Test
    void testConstructor_StoresShape() {
        int[][] shape = {{1, 1}, {1, 1}};
        NextShapeInfo info = new NextShapeInfo(shape, 0);

        assertNotNull(info.getShape());
        assertEquals(2, info.getShape().length);
    }

    @Test
    void testConstructor_StoresPosition() {
        int[][] shape = {{1, 1}, {1, 1}};
        NextShapeInfo info = new NextShapeInfo(shape, 2);

        assertEquals(2, info.getPosition());
    }

    @Test
    void testGetShape_ReturnsCopy() {
        int[][] original = {{1, 1}, {1, 1}};
        NextShapeInfo info = new NextShapeInfo(original, 0);

        int[][] retrieved = info.getShape();
        retrieved[0][0] = 999;

        assertEquals(1, info.getShape()[0][0],
                "Should return copy to prevent modification");
    }

    @Test
    void testWithDifferentPositions() {
        int[][] shape = {{1}};

        NextShapeInfo info0 = new NextShapeInfo(shape, 0);
        NextShapeInfo info1 = new NextShapeInfo(shape, 1);
        NextShapeInfo info3 = new NextShapeInfo(shape, 3);

        assertEquals(0, info0.getPosition());
        assertEquals(1, info1.getPosition());
        assertEquals(3, info3.getPosition());
    }

    @Test
    void testWithComplexShape() {
        int[][] tShape = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        NextShapeInfo info = new NextShapeInfo(tShape, 1);

        assertEquals(1, info.getPosition());
        assertEquals(3, info.getShape().length);
        assertEquals(3, info.getShape()[0].length);
    }

    @Test
    void testPositionCanBeAnyInteger() {
        int[][] shape = {{1}};

        NextShapeInfo negativePos = new NextShapeInfo(shape, -1);
        NextShapeInfo largePos = new NextShapeInfo(shape, 100);

        assertEquals(-1, negativePos.getPosition());
        assertEquals(100, largePos.getPosition());
    }
}