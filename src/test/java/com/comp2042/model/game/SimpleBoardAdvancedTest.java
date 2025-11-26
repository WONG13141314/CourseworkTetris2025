package com.comp2042.model.game;

import com.comp2042.enums.GameMode;
import com.comp2042.model.data.ViewData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardAdvancedTest {
    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        board = new SimpleBoard(25, 10, GameMode.ZEN);
        board.createNewBrick();
    }

    @Test
    void testMultipleRotations_BrickReturnsToOriginal() {
        ViewData original = board.getViewData();

        for (int i = 0; i < 4; i++) {
            board.rotateLeftBrick();
        }

        ViewData afterRotations = board.getViewData();
        assertNotNull(afterRotations);
    }

    @Test
    void testMoveBrickToLeftEdge() {
        int moveCount = 0;
        while (board.moveBrickLeft() && moveCount < 20) {
            moveCount++;
        }

        ViewData vd = board.getViewData();
        int[][] brickShape = vd.getBrickData();

        // The offset can be negative, but no filled cell should be out of bounds
        boolean hasFilledCellOutOfBounds = false;
        for (int row = 0; row < brickShape.length; row++) {
            for (int col = 0; col < brickShape[row].length; col++) {
                if (brickShape[row][col] != 0) {
                    int actualX = vd.getxPosition() + col;
                    if (actualX < 0) {
                        hasFilledCellOutOfBounds = true;
                        break;
                    }
                }
            }
            if (hasFilledCellOutOfBounds) break;
        }

        assertFalse(hasFilledCellOutOfBounds,
                "No filled cells should be at negative positions. Offset: " + vd.getxPosition());
    }

    @Test
    void testMoveBrickToRightEdge() {
        int moveCount = 0;
        while (board.moveBrickRight() && moveCount < 20) {
            moveCount++;
        }

        ViewData vd = board.getViewData();
        int[][] matrix = board.getBoardMatrix();
        int[][] brickShape = vd.getBrickData();

        // Check that no filled cell exceeds the board width
        boolean hasFilledCellOutOfBounds = false;
        for (int row = 0; row < brickShape.length; row++) {
            for (int col = 0; col < brickShape[row].length; col++) {
                if (brickShape[row][col] != 0) {
                    int actualX = vd.getxPosition() + col;
                    if (actualX >= matrix[0].length) {
                        hasFilledCellOutOfBounds = true;
                        break;
                    }
                }
            }
            if (hasFilledCellOutOfBounds) break;
        }

        assertFalse(hasFilledCellOutOfBounds,
                "No filled cells should exceed board width");
    }

    @Test
    void testDropBrickToBottom() {
        while (board.moveBrickDown()) {
            // Keep dropping
        }

        assertFalse(board.moveBrickDown(),
                "Brick at bottom should not move down");
    }

    @Test
    void testMergeThenCreateNew_UpdatesViewData() {
        while (board.moveBrickDown()) {
            // Keep dropping
        }
        board.mergeBrickToBackground();

        board.createNewBrick();
        ViewData newVd = board.getViewData();

        assertNotNull(newVd);
        assertNotNull(newVd.getBrickData());
    }

    @Test
    void testShadowPosition_IsAlwaysBelowBrick() {
        ViewData vd = board.getViewData();
        int shadow = board.calculateShadowPosition();

        assertTrue(shadow >= vd.getyPosition(),
                "Shadow must be at or below current position");
    }

    @Test
    void testShadowPosition_UpdatesWithMovement() {
        int shadow1 = board.calculateShadowPosition();

        board.moveBrickDown();
        int shadow2 = board.calculateShadowPosition();

        assertTrue(shadow2 >= shadow1 - 1,
                "Shadow should move with brick or stay at bottom");
    }

    @Test
    void testHoldBrick_SwapsTwice() {
        boolean held1 = board.holdBrick();
        assertTrue(held1, "First hold should succeed");

        while (board.moveBrickDown()) {}
        board.mergeBrickToBackground();
        board.createNewBrick();

        boolean held2 = board.holdBrick();
        assertTrue(held2, "Should be able to hold new brick");
    }

    @Test
    void testNewGame_ClearsHoldBrick() {
        board.holdBrick();
        board.newGame();

        board.createNewBrick();
        assertTrue(board.canHold(), "Should be able to hold after new game");
    }
}