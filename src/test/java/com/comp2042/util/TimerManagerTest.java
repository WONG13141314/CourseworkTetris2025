package com.comp2042.util;

import com.comp2042.enums.GameMode;
import javafx.application.Platform;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class TimerManagerTest {

    private TimerManager zenTimerManager;
    private TimerManager blitzTimerManager;
    private Label zenTimerLabel;
    private Label blitzTimerLabel;

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
            // Create SEPARATE labels for each timer mode
            zenTimerLabel = new Label();
            blitzTimerLabel = new Label();

            zenTimerManager = new TimerManager(GameMode.ZEN, zenTimerLabel);
            blitzTimerManager = new TimerManager(GameMode.BLITZ, blitzTimerLabel);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testZenTimer_InitializesWithZeroTime() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            assertEquals("00:00", zenTimerLabel.getText());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBlitzTimer_InitializesWithTwoMinutes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            assertEquals("02:00", blitzTimerLabel.getText());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testStart_StartsTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            zenTimerManager.start();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
        Thread.sleep(200);

        Platform.runLater(() -> zenTimerManager.stop());
    }

    @Test
    void testStop_StopsTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            zenTimerManager.start();
            zenTimerManager.stop();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testPause_PausesTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            zenTimerManager.start();
            zenTimerManager.pause();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testResume_ResumesTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            zenTimerManager.start();
            zenTimerManager.pause();
            zenTimerManager.resume();
            zenTimerManager.stop();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testReset_ResetsZenTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                zenTimerManager.start();
                Thread.sleep(100);
                zenTimerManager.reset();

                assertEquals("00:00", zenTimerLabel.getText());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testReset_ResetsBlitzTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            blitzTimerManager.start();
            blitzTimerManager.reset();

            assertEquals(120, blitzTimerManager.getBlitzTimeRemaining());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBlitzTimeUp_CallsCallback() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean callbackCalled = new AtomicBoolean(false);

        Platform.runLater(() -> {
            Label fastLabel = new Label();
            TimerManager fastTimer = new TimerManager(GameMode.BLITZ, fastLabel);

            fastTimer.setOnBlitzTimeUp(() -> callbackCalled.set(true));

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testGetBlitzTimeRemaining() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int remaining = blitzTimerManager.getBlitzTimeRemaining();
            assertEquals(120, remaining);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}