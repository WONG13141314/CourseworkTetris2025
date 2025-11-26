package com.comp2042.model.game;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.OBrick;
import com.comp2042.logic.bricks.TBrick;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class HoldBrickManagerTest {
    private HoldBrickManager manager;

    @BeforeEach
    void setUp() {
        manager = new HoldBrickManager();
    }

    @Test
    void testInitialState_CanHold() {
        assertTrue(manager.canHold(), "Should be able to hold initially");
    }

    @Test
    void testInitialState_NoHoldBrick() {
        assertFalse(manager.hasHoldBrick(), "Should not have hold brick initially");
        assertNull(manager.getHoldBrick());
    }

    @Test
    void testSetHoldBrick_UpdatesState() {
        Brick brick = new OBrick();
        manager.setHoldBrick(brick);

        assertTrue(manager.hasHoldBrick());
        assertEquals(brick, manager.getHoldBrick());
    }

    @Test
    void testDisableHold_PreventsHolding() {
        manager.disableHold();
        assertFalse(manager.canHold());
    }

    @Test
    void testEnableHold_AllowsHolding() {
        manager.disableHold();
        manager.enableHold();
        assertTrue(manager.canHold());
    }

    @Test
    void testReset_RestoresInitialState() {
        Brick brick = new TBrick();
        manager.setHoldBrick(brick);
        manager.disableHold();

        manager.reset();

        assertNull(manager.getHoldBrick());
        assertTrue(manager.canHold());
        assertFalse(manager.hasHoldBrick());
    }

    @Test
    void testGetHoldBrickData_WithNoBrick_ReturnsEmpty() {
        int[][] data = manager.getHoldBrickData();
        assertEquals(0, data.length, "Should return empty array when no hold brick");
    }

    @Test
    void testGetHoldBrickData_WithBrick_ReturnsData() {
        Brick brick = new OBrick();
        manager.setHoldBrick(brick);

        int[][] data = manager.getHoldBrickData();
        assertTrue(data.length > 0, "Should return brick data");
    }

    @Test
    void testSetHoldBrick_WithNull_ClearsHold() {
        Brick brick = new OBrick();
        manager.setHoldBrick(brick);
        manager.setHoldBrick(null);

        assertFalse(manager.hasHoldBrick());
        assertNull(manager.getHoldBrick());
    }

    @Test
    void testMultipleEnableDisableCycles() {
        manager.disableHold();
        assertFalse(manager.canHold());

        manager.enableHold();
        assertTrue(manager.canHold());

        manager.disableHold();
        assertFalse(manager.canHold());
    }
}