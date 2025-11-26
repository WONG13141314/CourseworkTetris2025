package com.comp2042.model.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;

class CollisionDetectorTest {
    private int[][] emptyMatrix;
    private CollisionDetector detector;

    @BeforeEach
    void setUp() {
        emptyMatrix = new int[20][10];
        detector = new CollisionDetector(emptyMatrix);
    }

    @Test
    void testWouldCollide_WithEmptyBoard_ReturnsFalse() {
        int[][] brick = {{1, 1}, {1, 1}};
        assertFalse(detector.wouldCollide(brick, 0, 0),
                "Should not collide with empty board at valid position");
    }

    @Test
    void testWouldCollide_OutOfBounds_ReturnsTrue() {
        int[][] brick = {{1}};
        assertTrue(detector.wouldCollide(brick, -1, 0),
                "Should collide when out of bounds (left)");
        assertTrue(detector.wouldCollide(brick, 10, 0),
                "Should collide when out of bounds (right)");
    }

    @Test
    void testCanMoveDown_WithEmptyBelow_ReturnsTrue() {
        // Create a proper 2D brick array
        int[][] brick = {{1}};
        Point pos = new Point(0, 0);

        assertTrue(detector.canMoveDown(brick, pos),
                "Should be able to move down when space is empty");
    }

    @Test
    void testCanMoveLeft_AtLeftEdge_ReturnsFalse() {
        int[][] brick = {{1}};
        Point pos = new Point(0, 5);

        assertFalse(detector.canMoveLeft(brick, pos),
                "Should not be able to move left at left edge");
    }

    @Test
    void testCanMoveRight_WithSpace_ReturnsTrue() {
        int[][] brick = {{1}};
        Point pos = new Point(0, 0);

        assertTrue(detector.canMoveRight(brick, pos),
                "Should be able to move right when space is available");
    }

    @Test
    void testCalculateShadowY_FindsLowestPosition() {
        int[][] brick = {{1}};
        Point pos = new Point(0, 0);

        int shadowY = detector.calculateShadowY(brick, pos);

        assertTrue(shadowY >= pos.y, "Shadow should be at or below current position");
        assertEquals(19, shadowY, "Shadow should reach bottom of 20-row board");
    }

    @Test
    void testWouldCollide_WithFilledCell() {
        emptyMatrix[5][5] = 1;
        int[][] brick = {{1}};

        assertTrue(detector.wouldCollide(brick, 5, 5),
                "Should collide with filled cell");
    }

    @Test
    void testCanMoveDown_AtBottom() {
        int[][] brick = {{1}};
        Point pos = new Point(5, 19);

        assertFalse(detector.canMoveDown(brick, pos),
                "Cannot move down from bottom row");
    }

    @Test
    void testCanMoveRight_AtRightEdge() {
        int[][] brick = {{1}};
        Point pos = new Point(9, 5);

        assertFalse(detector.canMoveRight(brick, pos),
                "Should not be able to move right at right edge");
    }

    @Test
    void testFindValidRotationPosition_CurrentPositionValid() {
        int[][] brick = {{1}};
        Point pos = new Point(5, 5);

        Point result = detector.findValidRotationPosition(brick, pos);

        assertNotNull(result, "Should find valid position");
        assertEquals(pos, result, "Should return current position when valid");
    }

    @Test
    void testCalculateShadowY_WithObstacle() {
        // Place obstacle at row 10
        for (int i = 0; i < 10; i++) {
            emptyMatrix[10][i] = 1;
        }

        int[][] brick = {{1}};
        Point pos = new Point(0, 0);

        int shadowY = detector.calculateShadowY(brick, pos);

        assertEquals(9, shadowY, "Shadow should stop above obstacle");
    }
}