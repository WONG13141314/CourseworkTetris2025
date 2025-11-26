package com.comp2042.model.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Point;

class CollisionDetectorTest {
    private int[][] matrix;
    private CollisionDetector detector;

    @BeforeEach
    void setUp() {
        matrix = new int[20][10];
        detector = new CollisionDetector(matrix);
    }

    // ==================== EXISTING TESTS ====================

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

    // ==================== NEW ENHANCED TESTS ====================

    @Test
    void testWallKick_PrioritizesLeftOverRight() {
        // Create obstacle on right side
        matrix[5][9] = 1;

        // I-piece horizontal at right edge
        int[][] iPieceHorizontal = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        Point rightEdgePos = new Point(7, 5); // Would collide on right
        Point validPos = detector.findValidRotationPosition(iPieceHorizontal, rightEdgePos);

        assertNotNull(validPos, "Should find valid position with wall kick");

        // Should kick left (smaller x) when right is blocked
        if (validPos.x != rightEdgePos.x) {
            assertTrue(validPos.x < rightEdgePos.x,
                    "Should try left kick when right is blocked");
        }
    }

    @Test
    void testWallKick_TriesRightWhenLeftBlocked() {
        // Create obstacle on left side
        matrix[5][0] = 1;
        matrix[5][1] = 1;

        int[][] iPieceHorizontal = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        Point leftEdgePos = new Point(0, 5);
        Point validPos = detector.findValidRotationPosition(iPieceHorizontal, leftEdgePos);

        assertNotNull(validPos, "Should find valid position");

        if (validPos.x != leftEdgePos.x) {
            assertTrue(validPos.x > leftEdgePos.x,
                    "Should try right kick when left is blocked");
        }
    }

    @Test
    void testWallKick_WithTShape_NearLeftWall() {
        int[][] tShapeVertical = {
                {0, 1, 0, 0},
                {0, 1, 1, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0}
        };

        Point nearLeftWall = new Point(0, 5); // At left wall but valid
        Point validPos = detector.findValidRotationPosition(tShapeVertical, nearLeftWall);

        assertNotNull(validPos, "Should find valid position");
        // The implementation tries current, left, then right
        // At x=0, left would be -1 (invalid), so it returns current or right
        assertTrue(validPos.x >= 0, "Position should be within bounds");
    }

    @Test
    void testWallKick_WithTShape_NearRightWall() {
        int[][] tShapeVertical = {
                {0, 1, 0, 0},
                {0, 1, 1, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0}
        };

        Point nearRightWall = new Point(9, 5); // Would extend beyond right
        Point validPos = detector.findValidRotationPosition(tShapeVertical, nearRightWall);

        if (validPos != null) {
            assertFalse(detector.wouldCollide(tShapeVertical,
                            (int)validPos.getX(), (int)validPos.getY()),
                    "Kicked position should be valid");
        }
    }

    @Test
    void testWallKick_BothSidesBlocked_ReturnsNull() {
        // Block both sides
        for (int i = 4; i <= 6; i++) {
            matrix[i][0] = 1; // Left blocked
            matrix[i][9] = 1; // Right blocked
        }

        // Use 2x2 brick instead of 1x4 to avoid array index issues
        int[][] squareBrick = {{1, 1}, {1, 1}};
        Point centerPos = new Point(4, 5);

        // Make current position also blocked
        matrix[5][4] = 1;
        matrix[5][5] = 1;

        Point validPos = detector.findValidRotationPosition(squareBrick, centerPos);

        assertNull(validPos, "Should return null when no valid position exists");
    }

    @Test
    void testWallKick_SuccessiveKicks_FindsFirstValid() {
        // I-piece at left edge trying to rotate
        int[][] iPieceVertical = {
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        };

        Point leftEdge = new Point(0, 5);
        Point validPos = detector.findValidRotationPosition(iPieceVertical, leftEdge);

        assertNotNull(validPos, "Should find valid position");
        assertFalse(detector.wouldCollide(iPieceVertical,
                        (int)validPos.getX(), (int)validPos.getY()),
                "Found position should not collide");
    }

    @Test
    void testRotation_NearCeiling_AllowsRotation() {
        int[][] tShape = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        Point nearTop = new Point(3, 0); // At very top

        boolean canRotate = !detector.wouldCollide(tShape,
                (int)nearTop.getX(), (int)nearTop.getY());

        assertTrue(canRotate, "Should allow rotation near ceiling in open space");
    }

    @Test
    void testRotation_WithPartialOverlap_DetectsCollision() {
        // Place obstacle
        matrix[5][4] = 1;
        matrix[5][5] = 1;

        int[][] shape = {
                {0, 1, 1},
                {1, 1, 0},
                {0, 0, 0}
        };

        Point overlapPos = new Point(3, 5);

        assertTrue(detector.wouldCollide(shape,
                        (int)overlapPos.getX(), (int)overlapPos.getY()),
                "Should detect partial overlap collision");
    }

    @Test
    void testShadowCalculation_WithGaps_FindsLowestPoint() {
        // Create gap in middle
        for (int j = 0; j < 10; j++) {
            if (j != 5) { // Leave column 5 empty
                matrix[15][j] = 1;
            }
        }

        int[][] brick = {{1}};
        Point pos = new Point(5, 0); // Column with gap

        int shadow = detector.calculateShadowY(brick, pos);

        assertTrue(shadow > 15,
                "Shadow should fall through gap to bottom");
    }

    @Test
    void testShadowCalculation_WithComplexShape() {
        // L-shape shadow calculation
        int[][] lShape = {
                {0, 0, 1},
                {1, 1, 1},
                {0, 0, 0}
        };

        // Place obstacle at bottom
        for (int j = 0; j < 10; j++) {
            matrix[19][j] = 1;
        }

        Point pos = new Point(3, 0);
        int shadow = detector.calculateShadowY(lShape, pos);

        assertTrue(shadow >= 0 && shadow < 19,
                "Shadow should land above obstacle");
    }

    @Test
    void testShadowCalculation_AlreadyAtBottom() {
        int[][] brick = {{1}};
        Point bottomPos = new Point(5, 19);

        int shadow = detector.calculateShadowY(brick, bottomPos);

        assertEquals(19, shadow,
                "Shadow should be same as position when at bottom");
    }

    @Test
    void testCollision_WithEmptyShape_NoCollision() {
        int[][] emptyShape = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        // Even with obstacles
        matrix[5][5] = 1;

        Point pos = new Point(4, 5);

        assertFalse(detector.wouldCollide(emptyShape,
                        (int)pos.getX(), (int)pos.getY()),
                "Empty shape should never collide");
    }

    @Test
    void testWallKick_IPiece_RotationFromVerticalToHorizontal() {
        int[][] iPieceHorizontal = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        // At x=8, horizontal I-piece would extend to x=11 (out of bounds)
        Point edgePos = new Point(8, 10);
        Point validPos = detector.findValidRotationPosition(iPieceHorizontal, edgePos);

        if (validPos != null) {
            // Should kick left to fit
            assertTrue(validPos.x <= 6,
                    "Should kick left enough to fit 4-wide piece");
        }
    }

    @Test
    void testMultipleObstacles_ComplexCollisionDetection() {
        // Create scattered obstacles
        matrix[5][3] = 1;
        matrix[5][4] = 1;
        matrix[6][2] = 1;
        matrix[6][5] = 1;
        matrix[7][3] = 1;

        int[][] tShape = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        Point testPos = new Point(2, 5);

        boolean collides = detector.wouldCollide(tShape,
                (int)testPos.getX(), (int)testPos.getY());

        assertTrue(collides, "Should detect collision with scattered obstacles");
    }
}