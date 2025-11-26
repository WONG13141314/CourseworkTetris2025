package com.comp2042.model.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DownDataTest {
    @Test
    void testConstructor_WithTwoParameters() {
        ClearRow cr = new ClearRow(1, new int[5][5], 50);
        ViewData vd = new ViewData(new int[2][2], 0, 0, new int[2][2], 0, null);

        DownData dd = new DownData(cr, vd);

        assertFalse(dd.isBoardCleared(), "Default boardCleared should be false");
    }

    @Test
    void testConstructor_WithThreeParameters() {
        ClearRow cr = new ClearRow(1, new int[5][5], 50);
        ViewData vd = new ViewData(new int[2][2], 0, 0, new int[2][2], 0, null);

        DownData dd = new DownData(cr, vd, true);

        assertTrue(dd.isBoardCleared());
    }

    @Test
    void testGetters_ReturnCorrectData() {
        ClearRow cr = new ClearRow(2, new int[5][5], 200);
        ViewData vd = new ViewData(new int[2][2], 3, 4, new int[2][2], 5, null);

        DownData dd = new DownData(cr, vd, false);

        assertEquals(cr, dd.getClearRow());
        assertEquals(vd, dd.getViewData());
        assertFalse(dd.isBoardCleared());
    }

    @Test
    void testBoardCleared_True() {
        ClearRow cr = new ClearRow(0, new int[5][5], 0);
        ViewData vd = new ViewData(new int[2][2], 0, 0, new int[2][2], 0, null);

        DownData dd = new DownData(cr, vd, true);

        assertTrue(dd.isBoardCleared());
    }

    @Test
    void testBoardCleared_False() {
        ClearRow cr = new ClearRow(0, new int[5][5], 0);
        ViewData vd = new ViewData(new int[2][2], 0, 0, new int[2][2], 0, null);

        DownData dd = new DownData(cr, vd, false);

        assertFalse(dd.isBoardCleared());
    }

    @Test
    void testWithNullClearRow() {
        ViewData vd = new ViewData(new int[2][2], 0, 0, new int[2][2], 0, null);
        DownData dd = new DownData(null, vd, false);

        assertNull(dd.getClearRow());
        assertNotNull(dd.getViewData());
    }

    @Test
    void testWithNullViewData() {
        ClearRow cr = new ClearRow(1, new int[5][5], 50);
        DownData dd = new DownData(cr, null, false);

        assertNotNull(dd.getClearRow());
        assertNull(dd.getViewData());
    }
}