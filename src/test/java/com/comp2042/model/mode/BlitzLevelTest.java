package com.comp2042.model.mode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BlitzLevelTest {

    private BlitzLevel blitzLevel;

    @BeforeEach
    void setUp() {
        blitzLevel = new BlitzLevel();
    }

    @Test
    void testGetDropSpeed_Level1_Is400ms() {
        assertEquals(1, blitzLevel.getLevel());
        assertEquals(400, blitzLevel.getDropSpeed());
    }

    @Test
    void testGetDropSpeed_Level2_Is350ms() {
        blitzLevel.addLines(3);
        assertEquals(2, blitzLevel.getLevel());
        assertEquals(350, blitzLevel.getDropSpeed());
    }

    @Test
    void testLevelUp_Exactly3Lines_GoesToLevel2() {
        blitzLevel.addLines(3);
        assertEquals(2, blitzLevel.getLevel());
        assertEquals(0, blitzLevel.getLinesCleared());
        assertEquals(5, blitzLevel.getLinesNeeded());
    }

    @Test
    void testLevelUp_MoreThanNeeded_CarriesOverflow() {
        blitzLevel.addLines(5);
        assertEquals(2, blitzLevel.getLevel());
        assertEquals(2, blitzLevel.getLinesCleared());
    }

    @Test
    void testGetProgressText_ShowsCorrectFormat() {
        assertEquals("0/3", blitzLevel.getProgressText());
        blitzLevel.addLines(1);
        assertEquals("1/3", blitzLevel.getProgressText());
    }

    @Test
    void testReset_ResetsToInitialState() {
        blitzLevel.addLines(10);
        blitzLevel.reset();
        assertEquals(1, blitzLevel.getLevel());
        assertEquals(0, blitzLevel.getLinesCleared());
        assertEquals(3, blitzLevel.getLinesNeeded());
        assertEquals(400, blitzLevel.getDropSpeed());
    }

    @Test
    void testLevelProperty_UpdatesOnLevelChange() {
        assertEquals(1, blitzLevel.levelProperty().get());
        blitzLevel.addLines(3);
        assertEquals(2, blitzLevel.levelProperty().get());
    }

    @Test
    void testLinesClearedProperty_UpdatesCorrectly() {
        assertEquals(0, blitzLevel.linesClearedProperty().get());

        blitzLevel.addLines(1);
        assertEquals(1, blitzLevel.linesClearedProperty().get());

        blitzLevel.addLines(2);
        // After adding 2 more lines (total 3), should level up and reset to 0
        assertEquals(2, blitzLevel.getLevel());
        assertEquals(0, blitzLevel.linesClearedProperty().get());
    }

    @Test
    void testAddLines_Zero_NoChange() {
        blitzLevel.addLines(0);
        assertEquals(1, blitzLevel.getLevel());
        assertEquals(0, blitzLevel.getLinesCleared());
    }

    @Test
    void testAddLines_Negative_HandledGracefully() {
        int initialLevel = blitzLevel.getLevel();
        int initialLines = blitzLevel.getLinesCleared();

        blitzLevel.addLines(-5);

        // After adding negative lines, level should not decrease
        assertEquals(1, blitzLevel.getLevel());
        // Lines cleared should either stay the same or be set to 0 (not negative)
        assertTrue(blitzLevel.getLinesCleared() >= 0, "Lines cleared should not be negative");
        assertEquals(initialLines, blitzLevel.getLinesCleared(), "Lines cleared should remain unchanged");
    }

    @Test
    void testGetLevel() {
        assertEquals(1, blitzLevel.getLevel());
        blitzLevel.addLines(3);
        assertEquals(2, blitzLevel.getLevel());
    }

    @Test
    void testGetLinesCleared() {
        assertEquals(0, blitzLevel.getLinesCleared());
        blitzLevel.addLines(2);
        assertEquals(2, blitzLevel.getLinesCleared());
    }

    @Test
    void testGetLinesNeeded() {
        assertEquals(3, blitzLevel.getLinesNeeded());
        blitzLevel.addLines(3);
        assertEquals(5, blitzLevel.getLinesNeeded());
    }

    @Test
    void testMultipleLevelUps() {
        blitzLevel.addLines(3);  // Level 2
        assertEquals(2, blitzLevel.getLevel());

        blitzLevel.addLines(5);  // Level 3
        assertEquals(3, blitzLevel.getLevel());

        blitzLevel.addLines(7);  // Level 4
        assertEquals(4, blitzLevel.getLevel());
    }

    @Test
    void testSpeedDecreasesWithLevel() {
        int speed1 = blitzLevel.getDropSpeed();

        blitzLevel.addLines(3);
        int speed2 = blitzLevel.getDropSpeed();

        blitzLevel.addLines(5);
        int speed3 = blitzLevel.getDropSpeed();

        assertTrue(speed2 < speed1, "Speed should decrease at level 2");
        assertTrue(speed3 < speed2, "Speed should decrease at level 3");
    }
}