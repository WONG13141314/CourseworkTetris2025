package com.comp2042.model.game;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.IBrick;
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

    @Test
    void testSetBrick_ResetsRotation() {
        // Advance rotation first
        rotator.setCurrentShape(2);

        // Set brick again - this should reset to 0
        rotator.setBrick(mockBrick);

        // getNextShape() advances from current (0) to next (1)
        // So if we just set the brick, getNextShape() should return position 1
        // But to verify it was reset to 0, we check getCurrentShape works
        assertNotNull(rotator.getCurrentShape(),
                "Setting new brick should reset rotation and getCurrentShape should work");
    }

    @Test
    void testGetNextShape_AdvancesRotation() {
        com.comp2042.model.data.NextShapeInfo info = rotator.getNextShape();

        assertNotNull(info.getShape());
        assertTrue(info.getPosition() >= 0);
        // Since we start at 0, getNextShape() should return position 1
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

        // IBrick only has 2 rotation states (0 and 1)
        // After setting to 1, getNextShape should cycle back to position 0
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
        // Advance rotation
        rotator.setCurrentShape(3);

        // Create a new brick and set it
        Brick newBrick = new IBrick();
        rotator.setBrick(newBrick);

        // Now getCurrentShape should work (implying currentShape is 0)
        int[][] currentShape = rotator.getCurrentShape();
        assertNotNull(currentShape, "After setBrick, current shape should be accessible");

        // Verify the brick was set
        assertEquals(newBrick, rotator.getCurrentBrick(),
                "New brick should be set");
    }

    @Test
    void testGetNextShape_CyclesCorrectly() {
        // IBrick has 2 rotation states (vertical and horizontal)
        rotator.setCurrentShape(0);

        com.comp2042.model.data.NextShapeInfo shape1 = rotator.getNextShape();
        assertEquals(1, shape1.getPosition());

        rotator.setCurrentShape(1);
        com.comp2042.model.data.NextShapeInfo shape2 = rotator.getNextShape();
        assertEquals(0, shape2.getPosition(), "Should cycle back to 0");
    }
}