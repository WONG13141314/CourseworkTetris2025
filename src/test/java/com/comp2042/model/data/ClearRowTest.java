package com.comp2042.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {
    @Test
    void testConstructor_StoresLinesRemoved() {
        ClearRow cr = new ClearRow(3, new int[10][10], 450);
        assertEquals(3, cr.getLinesRemoved());
    }

    @Test
    void testConstructor_StoresScoreBonus() {
        ClearRow cr = new ClearRow(2, new int[10][10], 200);
        assertEquals(200, cr.getScoreBonus());
    }

    @Test
    void testGetNewMatrix_ReturnsCopy() {
        int[][] original = new int[5][5];
        original[0][0] = 1;

        ClearRow cr = new ClearRow(1, original, 50);
        int[][] retrieved = cr.getNewMatrix();
        retrieved[0][0] = 999;

        assertNotEquals(999, cr.getNewMatrix()[0][0],
                "Should return copy of matrix");
    }

    @Test
    void testZeroLinesRemoved() {
        ClearRow cr = new ClearRow(0, new int[10][10], 0);
        assertEquals(0, cr.getLinesRemoved());
        assertEquals(0, cr.getScoreBonus());
    }

    @Test
    void testSingleLineCleared() {
        ClearRow cr = new ClearRow(1, new int[10][10], 50);
        assertEquals(1, cr.getLinesRemoved());
        assertEquals(50, cr.getScoreBonus());
    }

    @Test
    void testMultipleLineClears() {
        ClearRow cr2 = new ClearRow(2, new int[10][10], 200);
        ClearRow cr3 = new ClearRow(3, new int[10][10], 450);
        ClearRow cr4 = new ClearRow(4, new int[10][10], 800);

        assertEquals(2, cr2.getLinesRemoved());
        assertEquals(3, cr3.getLinesRemoved());
        assertEquals(4, cr4.getLinesRemoved());
    }

    @Test
    void testScoreBonusCalculation() {
        // Score = 50 * n^2 where n is lines cleared
        ClearRow cr1 = new ClearRow(1, new int[5][5], 50);
        ClearRow cr2 = new ClearRow(2, new int[5][5], 200);
        ClearRow cr3 = new ClearRow(3, new int[5][5], 450);
        ClearRow cr4 = new ClearRow(4, new int[5][5], 800);

        assertEquals(50, cr1.getScoreBonus());    // 50 * 1^2
        assertEquals(200, cr2.getScoreBonus());   // 50 * 2^2
        assertEquals(450, cr3.getScoreBonus());   // 50 * 3^2
        assertEquals(800, cr4.getScoreBonus());   // 50 * 4^2
    }
}