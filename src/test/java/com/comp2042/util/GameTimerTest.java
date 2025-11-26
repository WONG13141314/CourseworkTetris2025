package com.comp2042.util;

import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class GameTimerTest {
    private GameTimer timer;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        // Initialize JavaFX toolkit (only if not already initialized)
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized, ignore
        }

        // Give it a moment to initialize
        Thread.sleep(100);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timer = new GameTimer();
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialState_IsNotRunning() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertFalse(timer.isRunning(), "Timer should not be running initially");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialElapsedSeconds_IsZero() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertEquals(0, timer.elapsedSecondsProperty().get(),
                    "Initial elapsed time should be 0");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testGetFormattedTime_WithZeroSeconds() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            String formatted = timer.getFormattedTime();
            assertEquals("00:00", formatted, "Should format as 00:00");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testReset_StopsTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timer.start();
            timer.reset();

            assertFalse(timer.isRunning(), "Timer should stop after reset");
            assertEquals(0, timer.elapsedSecondsProperty().get(),
                    "Elapsed time should reset to 0");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testElapsedSecondsProperty_IsObservable() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertNotNull(timer.elapsedSecondsProperty(),
                    "Elapsed seconds should be observable property");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testStop_StopsRunningTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timer.start();
            timer.stop();
            assertFalse(timer.isRunning(), "Timer should stop");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleStartStop() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            timer.start();
            assertTrue(timer.isRunning());

            timer.stop();
            assertFalse(timer.isRunning());

            timer.start();
            assertTrue(timer.isRunning());

            timer.stop(); // Clean up
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testGetFormattedTime_WithSeconds() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // Manually set elapsed seconds for testing formatting
            timer.elapsedSecondsProperty().set(65);
            String formatted = timer.getFormattedTime();
            assertEquals("01:05", formatted, "Should format 65 seconds as 01:05");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }
}