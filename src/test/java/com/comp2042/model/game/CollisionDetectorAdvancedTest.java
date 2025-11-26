package com.comp2042.model.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;

class CollisionDetectorAdvancedTest {
    private int[][] matrix;
    private CollisionDetector detector;

    @BeforeEach
    void setUp() {
        matrix = new int[20][10];
        detector = new CollisionDetector(matrix);
    }

    @Test
    void testWouldCollide_WithComplexShape() {
        int[][] tShape = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        assertFalse(detector.wouldCollide(tShape, 3, 0),
                "T-shape should fit in middle");
    }

    @Test
    void testWouldCollide_NearBottomEdge() {
        int[][] brick = {{1}};

        assertFalse(detector.wouldCollide(brick, 0, 19),
                "Single block at bottom row should not collide");
        assertTrue(detector.wouldCollide(brick, 0, 20),
                "Single block below board should collide");
    }

    @Test
    void testCanMoveLeft_WithObstacle() {
        matrix[5][0] = 1;
        int[][] brick = {{1}};
        Point pos = new Point(1, 5);

        assertFalse(detector.canMoveLeft(brick, pos),
                "Cannot move left into obstacle");
    }

    @Test
    void testCanMoveRight_WithObstacle() {
        matrix[5][9] = 1;
        int[][] brick = {{1}};
        Point pos = new Point(8, 5);

        assertFalse(detector.canMoveRight(brick, pos),
                "Cannot move right into obstacle");
    }

    @Test
    void testFindValidRotationPosition_WithWallKick() {
        int[][] newShape = {{1, 1}, {1, 1}};
        Point pos = new Point(9, 5);

        Point validPos = detector.findValidRotationPosition(newShape, pos);

        if (validPos != null) {
            assertFalse(detector.wouldCollide(newShape,
                            (int)validPos.getX(), (int)validPos.getY()),
                    "Returned position should be valid");
        }
    }

    @Test
    void testCalculateShadowY_WithObstacles() {
        for (int j = 0; j < 10; j++) {
            matrix[19][j] = 1;
        }

        int[][] brick = {{1}};
        Point pos = new Point(5, 0);

        int shadow = detector.calculateShadowY(brick, pos);

        assertEquals(18, shadow, "Shadow should stop above obstacle");
    }

    @Test
    void testCalculateShadowY_OnTopOfOtherBricks() {
        matrix[19][5] = 1;
        matrix[18][5] = 1;
        matrix[17][5] = 1;

        int[][] brick = {{1}};
        Point pos = new Point(5, 0);

        int shadow = detector.calculateShadowY(brick, pos);

        assertEquals(16, shadow, "Shadow should be on top of stack");
    }

    @Test
    void testCanRotate_InOpenSpace() {
        int[][] newShape = {{1, 1}, {1, 1}};
        Point pos = new Point(4, 4);

        assertTrue(detector.canRotate(newShape, pos),
                "Should be able to rotate in open space");
    }
}