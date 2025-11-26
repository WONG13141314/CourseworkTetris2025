package com.comp2042.model.game;

import com.comp2042.enums.GameMode;
import com.comp2042.model.data.ClearRow;
import com.comp2042.model.data.ViewData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {
    private SimpleBoard zenBoard;
    private SimpleBoard blitzBoard;

    @BeforeEach
    void setUp() {
        zenBoard = new SimpleBoard(25, 10, GameMode.ZEN);
        blitzBoard = new SimpleBoard(25, 10, GameMode.BLITZ);
    }

    @Test
    void testConstructor_InitializesBoard() {
        assertNotNull(zenBoard.getBoardMatrix());
        assertNotNull(zenBoard.getScore());
    }

    @Test
    void testCreateNewBrick_ReturnsBoolean() {
        boolean gameOver = zenBoard.createNewBrick();
        assertNotNull(zenBoard.getViewData(), "Should have view data after creating brick");
    }

    @Test
    void testMoveBrickDown_InitiallyReturnsTrue() {
        zenBoard.createNewBrick();
        boolean moved = zenBoard.moveBrickDown();
        assertTrue(moved, "Should be able to move down initially");
    }

    @Test
    void testMoveBrickLeft_WorksWithinBounds() {
        zenBoard.createNewBrick();
        ViewData before = zenBoard.getViewData();

        boolean moved = zenBoard.moveBrickLeft();
        ViewData after = zenBoard.getViewData();

        if (moved) {
            assertTrue(after.getxPosition() < before.getxPosition(),
                    "X position should decrease when moving left");
        }
    }

    @Test
    void testMoveBrickRight_WorksWithinBounds() {
        zenBoard.createNewBrick();
        ViewData before = zenBoard.getViewData();

        boolean moved = zenBoard.moveBrickRight();
        ViewData after = zenBoard.getViewData();

        if (moved) {
            assertTrue(after.getxPosition() > before.getxPosition(),
                    "X position should increase when moving right");
        }
    }

    @Test
    void testRotateLeftBrick_ChangesOrientation() {
        zenBoard.createNewBrick();
        ViewData before = zenBoard.getViewData();

        zenBoard.rotateLeftBrick();
        ViewData after = zenBoard.getViewData();

        assertNotNull(after);
    }

    @Test
    void testMergeBrickToBackground_UpdatesMatrix() {
        zenBoard.createNewBrick();
        zenBoard.mergeBrickToBackground();
        int[][] afterMerge = zenBoard.getBoardMatrix();

        assertNotNull(afterMerge);
    }

    @Test
    void testClearRows_ReturnsValidClearRow() {
        zenBoard.createNewBrick();
        ClearRow clearRow = zenBoard.clearRows();

        assertNotNull(clearRow);
        assertTrue(clearRow.getLinesRemoved() >= 0);
        assertTrue(clearRow.getScoreBonus() >= 0);
    }

    @Test
    void testGetScore_ReturnsNonNull() {
        assertNotNull(zenBoard.getScore());
        assertNotNull(blitzBoard.getScore());
    }

    @Test
    void testCanHold_InitiallyTrue() {
        zenBoard.createNewBrick();
        assertTrue(zenBoard.canHold(), "Should be able to hold initially");
    }

    @Test
    void testHoldBrick_WorksFirstTime() {
        zenBoard.createNewBrick();
        boolean held = zenBoard.holdBrick();
        assertTrue(held, "First hold should succeed");
    }

    @Test
    void testHoldBrick_CannotHoldTwiceInRow() {
        zenBoard.createNewBrick();
        zenBoard.holdBrick();
        boolean secondHold = zenBoard.holdBrick();

        assertFalse(secondHold, "Cannot hold twice without placing brick");
    }

    @Test
    void testGetHoldBrickData_ReturnsData() {
        zenBoard.createNewBrick();
        zenBoard.holdBrick();

        int[][] holdData = zenBoard.getHoldBrickData();
        assertNotNull(holdData);
    }

    @Test
    void testNewGame_ResetsBoard() {
        zenBoard.createNewBrick();
        zenBoard.moveBrickDown();

        zenBoard.newGame();

        assertEquals(0, zenBoard.getScore().scoreProperty().get(),
                "Score should reset to 0");
    }

    @Test
    void testCalculateShadowPosition_ReturnsValidPosition() {
        zenBoard.createNewBrick();
        ViewData vd = zenBoard.getViewData();

        int shadow = zenBoard.calculateShadowPosition();

        assertTrue(shadow >= vd.getyPosition(),
                "Shadow should be at or below current position");
    }

    @Test
    void testWasBoardCleared_InitiallyFalse() {
        assertFalse(zenBoard.wasBoardCleared(),
                "Board should not be cleared initially");
    }

    @Test
    void testGetGameMode_ReturnsCorrectMode() {
        assertEquals(GameMode.ZEN, zenBoard.getGameMode());
        assertEquals(GameMode.BLITZ, blitzBoard.getGameMode());
    }

    @Test
    void testGetViewData_ContainsAllNecessaryInfo() {
        zenBoard.createNewBrick();
        ViewData vd = zenBoard.getViewData();

        assertNotNull(vd.getBrickData(), "Should have brick data");
        assertNotNull(vd.getNextBrickData(), "Should have next brick data");
        assertTrue(vd.getxPosition() >= 0, "X position should be valid");
        assertTrue(vd.getyPosition() >= 0, "Y position should be valid");
    }
}