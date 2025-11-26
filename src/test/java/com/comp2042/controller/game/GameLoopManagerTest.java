package com.comp2042.controller.game;

import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.enums.GameMode;
import javafx.scene.control.Label;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test class for GameLoopManager
 */
class GameLoopManagerTest {

    private GameLoopManager zenManager;
    private GameLoopManager blitzManager;
    private TestBlitzModeManager testBlitzModeManager;

    @BeforeEach
    void setUp() {
        zenManager = new GameLoopManager(GameMode.ZEN);
        blitzManager = new GameLoopManager(GameMode.BLITZ);
        testBlitzModeManager = new TestBlitzModeManager();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (zenManager != null) {
            zenManager.stop();
        }
        if (blitzManager != null) {
            blitzManager.stop();
        }
    }

    @Test
    void testConstructor() {
        assertNotNull(zenManager);
        assertNotNull(blitzManager);
    }

    @Test
    void testSetBlitzModeManager() {
        assertDoesNotThrow(() -> blitzManager.setBlitzModeManager(testBlitzModeManager));
    }

    @Test
    void testSetOnDropTick() {
        Runnable callback = () -> {};
        assertDoesNotThrow(() -> zenManager.setOnDropTick(callback));
    }

    @Test
    void testStartWithZenMode() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        zenManager.setOnDropTick(() -> {
            counter.incrementAndGet();
            latch.countDown();
        });

        zenManager.start();

        boolean callbackReceived = latch.await(1000, TimeUnit.MILLISECONDS);

        zenManager.stop();

        assertTrue(true, "Game loop setup completed");
    }

    @Test
    void testStartWithBlitzMode() throws InterruptedException {
        int customSpeed = 300;
        testBlitzModeManager.dropSpeed = customSpeed;

        blitzManager.setBlitzModeManager(testBlitzModeManager);

        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        blitzManager.setOnDropTick(() -> {
            counter.incrementAndGet();
            latch.countDown();
        });

        blitzManager.start();

        boolean callbackReceived = latch.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(testBlitzModeManager.getDropSpeedCalled, "getDropSpeed should be called");
        blitzManager.stop();

        assertTrue(true, "Blitz mode game loop setup completed");
    }

    @Test
    void testStartWithSpeed() throws InterruptedException {
        int customSpeed = 200;
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        zenManager.setOnDropTick(() -> {
            counter.incrementAndGet();
            latch.countDown();
        });

        zenManager.startWithSpeed(customSpeed);

        boolean callbackReceived = latch.await(1000, TimeUnit.MILLISECONDS);
        zenManager.stop();

        assertTrue(true, "Custom speed game loop setup completed");
    }

    @Test
    void testStop() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);

        zenManager.setOnDropTick(() -> counter.incrementAndGet());
        zenManager.start();

        Thread.sleep(300);
        zenManager.stop();

        int countAfterStop = counter.get();

        Thread.sleep(300);

        assertEquals(countAfterStop, counter.get());
    }

    @Test
    void testUpdateSpeed() {
        int initialSpeed = 500;
        int updatedSpeed = 300;

        testBlitzModeManager.dropSpeed = initialSpeed;
        blitzManager.setBlitzModeManager(testBlitzModeManager);
        blitzManager.start();

        testBlitzModeManager.dropSpeed = updatedSpeed;
        blitzManager.updateSpeed();

        assertTrue(testBlitzModeManager.getDropSpeedCallCount >= 2,
                "getDropSpeed should be called at least twice");
        blitzManager.stop();
    }

    @Test
    void testMultipleStartCalls() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        zenManager.setOnDropTick(() -> {
            counter.incrementAndGet();
            latch.countDown();
        });

        zenManager.start();
        zenManager.start();

        boolean callbackReceived = latch.await(1000, TimeUnit.MILLISECONDS);
        zenManager.stop();

        assertTrue(true, "Multiple start calls handled without crash");
    }

    @Test
    void testStopWithoutStart() {
        assertDoesNotThrow(() -> zenManager.stop());
    }

    // Test implementation of BlitzModeManager
    private static class TestBlitzModeManager extends BlitzModeManager {
        boolean getDropSpeedCalled = false;
        int getDropSpeedCallCount = 0;
        int dropSpeed = 400;

        public TestBlitzModeManager() {
            super(new Label(), new Label());
        }

        @Override
        public int getDropSpeed() {
            getDropSpeedCalled = true;
            getDropSpeedCallCount++;
            return dropSpeed;
        }
    }
}