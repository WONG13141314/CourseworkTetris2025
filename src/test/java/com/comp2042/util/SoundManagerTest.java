package com.comp2042.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

class SoundManagerTest {

    private SoundManager soundManager;

    @BeforeEach
    void setUp() throws InterruptedException {
        soundManager = SoundManager.getInstance();
        // Give time for any previous async operations to complete first
        Thread.sleep(200);
        soundManager.stopBackgroundMusic();
        soundManager.stopGameOverMusic();
        Thread.sleep(200);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        // Give time for test's async operations to complete
        Thread.sleep(200);
        // Clean up after each test
        soundManager.stopBackgroundMusic();
        soundManager.stopGameOverMusic();
        Thread.sleep(200);
    }

    @Test
    void testGetInstance_ReturnsSingleton() {
        SoundManager instance1 = SoundManager.getInstance();
        SoundManager instance2 = SoundManager.getInstance();

        assertSame(instance1, instance2, "Should return same instance");
    }

    @Test
    void testPlayBackgroundMusic_DoesNotThrow() throws InterruptedException {
        assertDoesNotThrow(() -> soundManager.playBackgroundMusic());
        Thread.sleep(300); // Allow async operation to complete
    }

    @Test
    void testStopBackgroundMusic_DoesNotThrow() throws InterruptedException {
        assertDoesNotThrow(() -> soundManager.stopBackgroundMusic());
        Thread.sleep(100);
    }

    @Test
    void testPauseBackgroundMusic_DoesNotThrow() throws InterruptedException {
        assertDoesNotThrow(() -> soundManager.pauseBackgroundMusic());
        Thread.sleep(100);
    }

    @Test
    void testResumeBackgroundMusic_DoesNotThrow() throws InterruptedException {
        assertDoesNotThrow(() -> soundManager.resumeBackgroundMusic());
        Thread.sleep(300); // Allow async operation to complete
    }

    @Test
    void testPlayClearRow_DoesNotThrow() throws InterruptedException {
        assertDoesNotThrow(() -> soundManager.playClearRow());
        Thread.sleep(200); // Allow async operation to complete
    }

    @Test
    void testPlayGameOverMusic_DoesNotThrow() throws InterruptedException {
        assertDoesNotThrow(() -> soundManager.playGameOverMusic());
        Thread.sleep(300); // Allow async operation to complete
    }

    @Test
    void testStopGameOverMusic_DoesNotThrow() throws InterruptedException {
        assertDoesNotThrow(() -> soundManager.stopGameOverMusic());
        Thread.sleep(100);
    }

    @Test
    void testMultipleStopCalls_DoesNotThrow() throws InterruptedException {
        soundManager.stopBackgroundMusic();
        Thread.sleep(100);
        assertDoesNotThrow(() -> soundManager.stopBackgroundMusic());
        Thread.sleep(100);
    }

    @Test
    void testMultiplePauseCalls_DoesNotThrow() throws InterruptedException {
        soundManager.pauseBackgroundMusic();
        Thread.sleep(100);
        assertDoesNotThrow(() -> soundManager.pauseBackgroundMusic());
        Thread.sleep(100);
    }
}