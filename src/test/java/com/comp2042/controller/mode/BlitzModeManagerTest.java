package com.comp2042.controller.mode;

import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class BlitzModeManagerTest {
    private BlitzModeManager blitzManager;
    private Label levelLabel;
    private Label progressLabel;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already initialized
        }
        Thread.sleep(100);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            levelLabel = new Label();
            progressLabel = new Label();
            blitzManager = new BlitzModeManager(levelLabel, progressLabel);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialLevel_IsOne() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            assertEquals("LEVEL 1", levelLabel.getText());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testAddLines_UpdatesProgress() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            blitzManager.addLines(2);
            assertTrue(progressLabel.getText().contains("2/3"));
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testAddLines_TriggersLevelUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean leveledUp = new AtomicBoolean(false);

        Platform.runLater(() -> {
            blitzManager.setOnLevelUp(() -> leveledUp.set(true));
            blitzManager.addLines(3);

            assertTrue(leveledUp.get(), "Should trigger level up callback");
            assertEquals("LEVEL 2", levelLabel.getText());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testGetDropSpeed_DecreasesWithLevel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int speed1 = blitzManager.getDropSpeed();

            blitzManager.addLines(3); // Level up to 2
            int speed2 = blitzManager.getDropSpeed();

            assertTrue(speed2 < speed1, "Speed should decrease (get faster) with level");
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testReset_RestoresInitialState() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            blitzManager.addLines(10);
            blitzManager.reset();

            assertEquals("LEVEL 1", levelLabel.getText());
            assertTrue(progressLabel.getText().contains("0/3"));
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleLevelUps() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean levelUpCalled = new AtomicBoolean(false);

        Platform.runLater(() -> {
            blitzManager.setOnLevelUp(() -> levelUpCalled.set(true));

            blitzManager.addLines(3); // Level 2
            assertEquals("LEVEL 2", levelLabel.getText());

            blitzManager.addLines(5); // Level 3
            assertEquals("LEVEL 3", levelLabel.getText());

            assertTrue(levelUpCalled.get());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testProgressLabel_ShowsCorrectFormat() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            blitzManager.addLines(1);
            String progress = progressLabel.getText();

            assertTrue(progress.contains("LINES"), "Should contain 'LINES'");
            assertTrue(progress.contains("/"), "Should contain '/'");
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetOnLevelUp_CallbackWorks() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean callbackTriggered = new AtomicBoolean(false);

        Platform.runLater(() -> {
            blitzManager.setOnLevelUp(() -> callbackTriggered.set(true));
            blitzManager.addLines(3);

            assertTrue(callbackTriggered.get());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}