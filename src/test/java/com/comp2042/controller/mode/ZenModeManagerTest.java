package com.comp2042.controller.mode;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class ZenModeManagerTest {
    private ZenModeManager zenManager;
    private Label timerLabel;

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
            timerLabel = new Label();
            zenManager = new ZenModeManager(timerLabel);
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialBoardClears_IsZero() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertEquals(0, zenManager.getTotalBoardClears(),
                    "Initial board clears should be 0");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testNotifyBoardCleared_IncreasesCount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            zenManager.notifyBoardCleared();
            assertEquals(1, zenManager.getTotalBoardClears());

            zenManager.notifyBoardCleared();
            assertEquals(2, zenManager.getTotalBoardClears());
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testReset_ClearsBoardClearsCount() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            zenManager.notifyBoardCleared();
            zenManager.notifyBoardCleared();

            zenManager.reset();

            assertEquals(0, zenManager.getTotalBoardClears(),
                    "Reset should clear board clears count");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testShouldClearBoard_AlwaysTrue() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertTrue(zenManager.shouldClearBoard(),
                    "Zen mode should always clear board on game over");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetOnBoardCleared_TriggersCallback() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            final boolean[] callbackTriggered = {false};

            zenManager.setOnBoardCleared(() -> callbackTriggered[0] = true);
            zenManager.notifyBoardCleared();

            assertTrue(callbackTriggered[0], "Callback should be triggered");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleBoardClears() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            for (int i = 1; i <= 5; i++) {
                zenManager.notifyBoardCleared();
                assertEquals(i, zenManager.getTotalBoardClears());
            }
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }
}