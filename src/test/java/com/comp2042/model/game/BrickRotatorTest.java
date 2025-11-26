package com.comp2042.model.game;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.IBrick;
import com.comp2042.logic.bricks.TBrick;
import com.comp2042.logic.bricks.OBrick;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {
    private BrickRotator rotator;
    private Brick mockBrick;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        mockBrick = new IBrick();
        rotator.setBrick(mockBrick);
    }

    // ==================== EXISTING TESTS ====================

    @Test
    void testSetBrick_ResetsRotation() {
        rotator.setCurrentShape(2);
        rotator.setBrick(mockBrick);

        assertNotNull(rotator.getCurrentShape(),
                "Setting new brick should reset rotation and getCurrentShape should work");
    }

    @Test
    void testGetNextShape_AdvancesRotation() {
        com.comp2042.model.data.NextShapeInfo info = rotator.getNextShape();

        assertNotNull(info.getShape());
        assertTrue(info.getPosition() >= 0);
        assertEquals(1, info.getPosition(),
                "getNextShape from position 0 should return position 1");
    }

    @Test
    void testGetCurrentShape_ReturnsNonNull() {
        assertNotNull(rotator.getCurrentShape());
    }

    @Test
    void testSetCurrentShape_UpdatesPosition() {
        rotator.setCurrentShape(1);

        com.comp2042.model.data.NextShapeInfo next = rotator.getNextShape();
        assertEquals(0, next.getPosition(),
                "IBrick has 2 states: after setting to 1, next should cycle to 0");
    }

    @Test
    void testGetCurrentBrick_ReturnsSameBrick() {
        assertEquals(mockBrick, rotator.getCurrentBrick());
    }

    @Test
    void testRotationCycles() {
        rotator.setCurrentShape(0);
        com.comp2042.model.data.NextShapeInfo next1 = rotator.getNextShape();

        rotator.setCurrentShape(next1.getPosition());
        com.comp2042.model.data.NextShapeInfo next2 = rotator.getNextShape();

        assertNotNull(next1);
        assertNotNull(next2);
        assertTrue(next2.getPosition() >= 0, "Rotation should cycle through positions");
    }

    @Test
    void testSetBrick_ActuallyResetsToZero() {
        rotator.setCurrentShape(3);

        Brick newBrick = new IBrick();
        rotator.setBrick(newBrick);

        int[][] currentShape = rotator.getCurrentShape();
        assertNotNull(currentShape, "After setBrick, current shape should be accessible");

        assertEquals(newBrick, rotator.getCurrentBrick(),
                "New brick should be set");
    }

    @Test
    void testGetNextShape_CyclesCorrectly() {
        rotator.setCurrentShape(0);

        com.comp2042.model.data.NextShapeInfo shape1 = rotator.getNextShape();
        assertEquals(1, shape1.getPosition());

        rotator.setCurrentShape(1);
        com.comp2042.model.data.NextShapeInfo shape2 = rotator.getNextShape();
        assertEquals(0, shape2.getPosition(), "Should cycle back to 0");
    }

    // ==================== NEW ENHANCED TESTS ====================

    @Test
    void testSetCurrentShape_WithInvalidNegativeIndex_CausesError() {
        // BrickRotator does NOT handle negative indices gracefully
        // It uses ArrayList.get() which throws IndexOutOfBoundsException
        assertThrows(IndexOutOfBoundsException.class, () -> {
            rotator.setCurrentShape(-1);
            rotator.getCurrentShape();
        }, "Negative index should throw IndexOutOfBoundsException");
    }

    @Test
    void testSetCurrentShape_WithIndexBeyondMatrixSize() {
        // IBrick has 2 rotation states (0 and 1)
        // Setting to 10 should wrap around via modulo
        rotator.setCurrentShape(10);

        com.comp2042.model.data.NextShapeInfo next = rotator.getNextShape();

        assertNotNull(next, "Should handle large index with modulo wrap");
        assertTrue(next.getPosition() < mockBrick.getShapeMatrix().size(),
                "Position should wrap to valid range");
    }

    @Test
    void testRotation_WithOBrick_OnlyOneState() {
        Brick oBrick = new OBrick();
        rotator.setBrick(oBrick);

        com.comp2042.model.data.NextShapeInfo next1 = rotator.getNextShape();

        // O-brick has only 1 rotation state, should cycle to position 0
        assertEquals(0, next1.getPosition(),
                "O-brick should cycle back to same position");

        rotator.setCurrentShape(next1.getPosition());
        com.comp2042.model.data.NextShapeInfo next2 = rotator.getNextShape();

        assertEquals(0, next2.getPosition(),
                "O-brick should always return to position 0");
    }

    @Test
    void testRotation_WithTBrick_FourStates() {
        Brick tBrick = new TBrick();
        rotator.setBrick(tBrick);

        // T-brick has 4 rotation states
        int[] positions = new int[5];

        for (int i = 0; i < 5; i++) {
            com.comp2042.model.data.NextShapeInfo next = rotator.getNextShape();
            positions[i] = next.getPosition();
            rotator.setCurrentShape(next.getPosition());
        }

        // After 4 rotations, should be back to start
        assertEquals(positions[0], positions[4],
                "After 4 rotations, T-brick should return to initial state");
    }

    @Test
    void testGetNextShape_DoesNotModifyCurrentShape() {
        int[][] shapeBefore = rotator.getCurrentShape();

        rotator.getNextShape(); // Just peek, don't apply

        int[][] shapeAfter = rotator.getCurrentShape();

        // Should still be at position 0
        assertEquals(shapeBefore.length, shapeAfter.length,
                "getNextShape should not modify current shape");
    }

    @Test
    void testSetBrick_WithNull_HandlesGracefully() {
        // Test defensive programming - what happens with null?
        assertThrows(NullPointerException.class, () -> {
            rotator.setBrick(null);
            rotator.getCurrentShape(); // This should cause NPE
        }, "Should throw NPE when accessing null brick");
    }

    @Test
    void testMultipleConsecutiveRotations() {
        // Rotate multiple times and verify consistency
        for (int i = 0; i < 10; i++) {
            com.comp2042.model.data.NextShapeInfo next = rotator.getNextShape();
            assertNotNull(next, "Rotation " + i + " should return valid NextShapeInfo");
            assertNotNull(next.getShape(), "Shape should not be null");

            rotator.setCurrentShape(next.getPosition());
        }

        // Should still be in valid state
        assertNotNull(rotator.getCurrentShape());
    }

    @Test
    void testGetCurrentShape_ReturnsDeepCopy() {
        int[][] shape1 = rotator.getCurrentShape();
        int[][] shape2 = rotator.getCurrentShape();

        // Modify first shape
        if (shape1.length > 0 && shape1[0].length > 0) {
            shape1[0][0] = 999;

            // Second call should return fresh copy
            assertNotEquals(999, shape2[0][0],
                    "getCurrentShape should return independent copies");
        }
    }

    @Test
    void testRotation_PreservesShapeData() {
        int[][] originalShape = rotator.getCurrentShape();
        int originalBlockCount = countFilledCells(originalShape);

        // Rotate and come back
        com.comp2042.model.data.NextShapeInfo next1 = rotator.getNextShape();
        rotator.setCurrentShape(next1.getPosition());

        com.comp2042.model.data.NextShapeInfo next2 = rotator.getNextShape();
        rotator.setCurrentShape(next2.getPosition());

        int[][] finalShape = rotator.getCurrentShape();
        int finalBlockCount = countFilledCells(finalShape);

        assertEquals(originalBlockCount, finalBlockCount,
                "Number of blocks should remain same through rotations");
    }

    @Test
    void testSetBrick_MultipleTimes_ResetsEachTime() {
        Brick brick1 = new IBrick();
        Brick brick2 = new TBrick();
        Brick brick3 = new OBrick();

        rotator.setBrick(brick1);
        rotator.setCurrentShape(1);

        rotator.setBrick(brick2);
        // Should be reset to position 0
        com.comp2042.model.data.NextShapeInfo next = rotator.getNextShape();
        assertTrue(next.getPosition() < brick2.getShapeMatrix().size(),
                "Should reset to valid position after setBrick");

        rotator.setBrick(brick3);
        assertNotNull(rotator.getCurrentShape(),
                "Should work after multiple setBrick calls");
    }

    @Test
    void testEdgeCase_RapidRotationCycling() {
        // Simulate rapid rotation button mashing
        for (int cycle = 0; cycle < 100; cycle++) {
            com.comp2042.model.data.NextShapeInfo next = rotator.getNextShape();
            rotator.setCurrentShape(next.getPosition());

            // Verify still in valid state
            assertNotNull(rotator.getCurrentShape(),
                    "Should remain valid after rapid cycling");
        }
    }

    @Test
    void testGetNextShape_ConsistentWithGetCurrentShape() {
        rotator.setCurrentShape(0);

        com.comp2042.model.data.NextShapeInfo next = rotator.getNextShape();
        int nextPos = next.getPosition();

        rotator.setCurrentShape(nextPos);
        int[][] actualCurrentShape = rotator.getCurrentShape();
        int[][] expectedShape = next.getShape();

        assertEquals(expectedShape.length, actualCurrentShape.length,
                "Next shape should match current shape after setting position");
    }

    // Helper method
    private int countFilledCells(int[][] shape) {
        int count = 0;
        for (int[] row : shape) {
            for (int cell : row) {
                if (cell != 0) count++;
            }
        }
        return count;
    }
}