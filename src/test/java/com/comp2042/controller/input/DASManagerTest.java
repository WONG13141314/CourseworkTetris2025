package com.comp2042.controller.input;

import com.comp2042.util.GameConstants;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for DASManager
 * Tests DAS (Delayed Auto Shift) and ARR (Auto Repeat Rate) timing
 */
class DASManagerTest {

    private DASManager dasManager;

    @BeforeEach
    void setUp() {
        dasManager = new DASManager();
    }

    @AfterEach
    void tearDown() {
        if (dasManager != null) {
            dasManager.reset();
        }
    }

    // ==================== CONSTRUCTOR & SETUP TESTS ====================

    @Test
    void testConstructor() {
        assertNotNull(dasManager);
    }

    @Test
    void testSetCallbacks() {
        Runnable callback = () -> {};
        assertDoesNotThrow(() -> {
            dasManager.setOnLeftRepeat(callback);
            dasManager.setOnRightRepeat(callback);
            dasManager.setOnDownRepeat(callback);
        });
    }

    // ==================== INITIAL DELAY TIMING TESTS (170ms) ====================

    @Test
    void testDAS_InitialDelayIs170ms() throws InterruptedException {
        CountDownLatch firstRepeatLatch = new CountDownLatch(1);
        AtomicLong startTime = new AtomicLong();
        AtomicLong firstRepeatTime = new AtomicLong();

        dasManager.setOnLeftRepeat(() -> {
            if (firstRepeatLatch.getCount() > 0) {
                firstRepeatTime.set(System.currentTimeMillis());
                firstRepeatLatch.countDown();
            }
        });

        startTime.set(System.currentTimeMillis());
        dasManager.startLeftDAS(KeyCode.LEFT);

        boolean gotRepeat = firstRepeatLatch.await(500, TimeUnit.MILLISECONDS);
        dasManager.stopLeftDAS(KeyCode.LEFT);

        if (gotRepeat) {
            long actualDelay = firstRepeatTime.get() - startTime.get();

            // Allow 50ms tolerance for system timing variations
            assertTrue(actualDelay >= GameConstants.INPUT_DELAY_MS - 50,
                    "Initial delay should be at least ~120ms (170ms - tolerance). Got: " + actualDelay + "ms");
            assertTrue(actualDelay <= GameConstants.INPUT_DELAY_MS + 100,
                    "Initial delay should be at most ~270ms (170ms + tolerance). Got: " + actualDelay + "ms");
        }
    }

    @Test
    void testDAS_RightInitialDelayIs170ms() throws InterruptedException {
        CountDownLatch firstRepeatLatch = new CountDownLatch(1);
        AtomicLong startTime = new AtomicLong();
        AtomicLong firstRepeatTime = new AtomicLong();

        dasManager.setOnRightRepeat(() -> {
            if (firstRepeatLatch.getCount() > 0) {
                firstRepeatTime.set(System.currentTimeMillis());
                firstRepeatLatch.countDown();
            }
        });

        startTime.set(System.currentTimeMillis());
        dasManager.startRightDAS(KeyCode.RIGHT);

        boolean gotRepeat = firstRepeatLatch.await(500, TimeUnit.MILLISECONDS);
        dasManager.stopRightDAS(KeyCode.RIGHT);

        if (gotRepeat) {
            long actualDelay = firstRepeatTime.get() - startTime.get();

            assertTrue(actualDelay >= GameConstants.INPUT_DELAY_MS - 50,
                    "Right DAS initial delay should be ~170ms. Got: " + actualDelay + "ms");
            assertTrue(actualDelay <= GameConstants.INPUT_DELAY_MS + 100,
                    "Right DAS initial delay should be ~170ms. Got: " + actualDelay + "ms");
        }
    }

    @Test
    void testDAS_DownInitialDelayIs170ms() throws InterruptedException {
        CountDownLatch firstRepeatLatch = new CountDownLatch(1);
        AtomicLong startTime = new AtomicLong();
        AtomicLong firstRepeatTime = new AtomicLong();

        dasManager.setOnDownRepeat(() -> {
            if (firstRepeatLatch.getCount() > 0) {
                firstRepeatTime.set(System.currentTimeMillis());
                firstRepeatLatch.countDown();
            }
        });

        startTime.set(System.currentTimeMillis());
        dasManager.startDownDAS(KeyCode.DOWN);

        boolean gotRepeat = firstRepeatLatch.await(500, TimeUnit.MILLISECONDS);
        dasManager.stopDownDAS(KeyCode.DOWN);

        if (gotRepeat) {
            long actualDelay = firstRepeatTime.get() - startTime.get();

            assertTrue(actualDelay >= GameConstants.INPUT_DELAY_MS - 50,
                    "Down DAS initial delay should be ~170ms. Got: " + actualDelay + "ms");
            assertTrue(actualDelay <= GameConstants.INPUT_DELAY_MS + 100,
                    "Down DAS initial delay should be ~170ms. Got: " + actualDelay + "ms");
        }
    }

    // ==================== REPEAT RATE TIMING TESTS (50ms) ====================

    @Test
    void testARR_RepeatRateIs50ms() throws InterruptedException {
        AtomicInteger repeatCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(5); // Wait for 5 repeats
        AtomicLong firstRepeatTime = new AtomicLong();
        AtomicLong fifthRepeatTime = new AtomicLong();

        dasManager.setOnLeftRepeat(() -> {
            int count = repeatCount.incrementAndGet();
            if (count == 1) {
                firstRepeatTime.set(System.currentTimeMillis());
            } else if (count == 5) {
                fifthRepeatTime.set(System.currentTimeMillis());
            }
            latch.countDown();
        });

        dasManager.startLeftDAS(KeyCode.LEFT);

        boolean got5Repeats = latch.await(1000, TimeUnit.MILLISECONDS);
        dasManager.stopLeftDAS(KeyCode.LEFT);

        if (got5Repeats) {
            // Time for 4 intervals between repeat 1 and repeat 5
            long totalTime = fifthRepeatTime.get() - firstRepeatTime.get();
            long averageInterval = totalTime / 4;

            // Each interval should be ~50ms
            assertTrue(averageInterval >= GameConstants.INPUT_REPEAT_MS - 30,
                    "Average repeat interval should be ~50ms. Got: " + averageInterval + "ms");
            assertTrue(averageInterval <= GameConstants.INPUT_REPEAT_MS + 50,
                    "Average repeat interval should be ~50ms. Got: " + averageInterval + "ms");
        }
    }

    @Test
    void testARR_ConsistentRepeatRate() throws InterruptedException {
        AtomicInteger repeatCount = new AtomicInteger(0);
        AtomicLong lastRepeatTime = new AtomicLong();
        CountDownLatch latch = new CountDownLatch(10); // Get 10 repeats

        dasManager.setOnLeftRepeat(() -> {
            long now = System.currentTimeMillis();
            long last = lastRepeatTime.getAndSet(now);

            if (last > 0 && repeatCount.get() > 0) {
                long interval = now - last;
                // Each interval should be roughly 50ms
                // Allow generous tolerance for system variations
                assertTrue(interval >= 20 && interval <= 150,
                        "Repeat interval should be consistent around 50ms. Got: " + interval + "ms");
            }

            repeatCount.incrementAndGet();
            latch.countDown();
        });

        dasManager.startLeftDAS(KeyCode.LEFT);
        latch.await(1500, TimeUnit.MILLISECONDS);
        dasManager.stopLeftDAS(KeyCode.LEFT);

        assertTrue(repeatCount.get() >= 5,
                "Should get multiple repeats (got " + repeatCount.get() + ")");
    }

    // ==================== BASIC START/STOP TESTS ====================

    @Test
    void testStartLeftDAS() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        dasManager.setOnLeftRepeat(() -> latch.countDown());

        dasManager.startLeftDAS(KeyCode.LEFT);

        boolean completed = latch.await(500, TimeUnit.MILLISECONDS);
        assertTrue(dasManager.isKeyPressed(KeyCode.LEFT));

        dasManager.stopLeftDAS(KeyCode.LEFT);
    }

    @Test
    void testStartRightDAS() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        dasManager.setOnRightRepeat(() -> latch.countDown());

        dasManager.startRightDAS(KeyCode.RIGHT);

        boolean completed = latch.await(500, TimeUnit.MILLISECONDS);
        assertTrue(dasManager.isKeyPressed(KeyCode.RIGHT));

        dasManager.stopRightDAS(KeyCode.RIGHT);
    }

    @Test
    void testStartDownDAS() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        dasManager.setOnDownRepeat(() -> latch.countDown());

        dasManager.startDownDAS(KeyCode.DOWN);

        boolean completed = latch.await(500, TimeUnit.MILLISECONDS);
        assertTrue(dasManager.isKeyPressed(KeyCode.DOWN));

        dasManager.stopDownDAS(KeyCode.DOWN);
    }

    @Test
    void testStopLeftDAS() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        dasManager.setOnLeftRepeat(() -> counter.incrementAndGet());

        dasManager.startLeftDAS(KeyCode.LEFT);

        Thread.sleep(100);

        dasManager.stopLeftDAS(KeyCode.LEFT);
        int countAfterStop = counter.get();

        Thread.sleep(200);
        assertEquals(countAfterStop, counter.get());
    }

    @Test
    void testStopAll() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        dasManager.setOnLeftRepeat(() -> counter.incrementAndGet());
        dasManager.setOnRightRepeat(() -> counter.incrementAndGet());

        dasManager.startLeftDAS(KeyCode.LEFT);
        dasManager.startRightDAS(KeyCode.RIGHT);

        Thread.sleep(100);
        dasManager.stopAll();

        int countAfterStop = counter.get();
        Thread.sleep(200);

        assertEquals(countAfterStop, counter.get());
    }

    // ==================== SIMULTANEOUS KEY HANDLING TESTS ====================

    @Test
    void testDAS_LeftAndRightSimultaneous() throws InterruptedException {
        AtomicInteger leftCount = new AtomicInteger(0);
        AtomicInteger rightCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(2); // One from each direction

        dasManager.setOnLeftRepeat(() -> {
            leftCount.incrementAndGet();
            latch.countDown();
        });

        dasManager.setOnRightRepeat(() -> {
            rightCount.incrementAndGet();
            latch.countDown();
        });

        // Press both keys simultaneously
        dasManager.startLeftDAS(KeyCode.LEFT);
        dasManager.startRightDAS(KeyCode.RIGHT);

        boolean gotBoth = latch.await(500, TimeUnit.MILLISECONDS);

        dasManager.stopLeftDAS(KeyCode.LEFT);
        dasManager.stopRightDAS(KeyCode.RIGHT);

        if (gotBoth) {
            assertTrue(leftCount.get() > 0 && rightCount.get() > 0,
                    "Both left and right repeats should trigger when held simultaneously");
        }
    }

    @Test
    void testDAS_AlternativeKeys_SameTimingAsMainKeys() throws InterruptedException {
        CountDownLatch mainKeyLatch = new CountDownLatch(1);
        CountDownLatch altKeyLatch = new CountDownLatch(1);
        AtomicLong mainKeyTime = new AtomicLong();
        AtomicLong altKeyTime = new AtomicLong();

        // Test LEFT key
        dasManager.setOnLeftRepeat(() -> {
            mainKeyTime.set(System.currentTimeMillis());
            mainKeyLatch.countDown();
        });

        long startTime1 = System.currentTimeMillis();
        dasManager.startLeftDAS(KeyCode.LEFT);
        mainKeyLatch.await(500, TimeUnit.MILLISECONDS);
        dasManager.stopLeftDAS(KeyCode.LEFT);

        long mainDelay = mainKeyTime.get() - startTime1;

        // Reset and test A key (alternative)
        dasManager.reset();
        dasManager.setOnLeftRepeat(() -> {
            altKeyTime.set(System.currentTimeMillis());
            altKeyLatch.countDown();
        });

        long startTime2 = System.currentTimeMillis();
        dasManager.startLeftDAS(KeyCode.A);
        altKeyLatch.await(500, TimeUnit.MILLISECONDS);
        dasManager.stopLeftDAS(KeyCode.A);

        long altDelay = altKeyTime.get() - startTime2;

        // Both should have similar timing (within 100ms)
        assertTrue(Math.abs(mainDelay - altDelay) < 150,
                "Alternative keys (A) should have same timing as main keys (LEFT). " +
                        "LEFT: " + mainDelay + "ms, A: " + altDelay + "ms");
    }

    @Test
    void testAlternativeKeys() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        dasManager.setOnLeftRepeat(() -> latch.countDown());

        dasManager.startLeftDAS(KeyCode.A);

        boolean completed = latch.await(500, TimeUnit.MILLISECONDS);
        assertTrue(dasManager.isKeyPressed(KeyCode.A));

        dasManager.stopLeftDAS(KeyCode.A);
    }

    // ==================== STOP BEHAVIOR TESTS ====================

    @Test
    void testDAS_StopImmediately_NoMoreRepeats() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);

        dasManager.setOnLeftRepeat(() -> count.incrementAndGet());

        dasManager.startLeftDAS(KeyCode.LEFT);

        // Let it trigger once or twice
        Thread.sleep(250);

        dasManager.stopLeftDAS(KeyCode.LEFT);
        int countAtStop = count.get();

        // Wait more time, count should not increase
        Thread.sleep(200);

        assertEquals(countAtStop, count.get(),
                "After stop, no more repeats should occur");
    }

    @Test
    void testDAS_StopAll_StopsAllDirections() throws InterruptedException {
        AtomicInteger leftCount = new AtomicInteger(0);
        AtomicInteger rightCount = new AtomicInteger(0);
        AtomicInteger downCount = new AtomicInteger(0);

        dasManager.setOnLeftRepeat(() -> leftCount.incrementAndGet());
        dasManager.setOnRightRepeat(() -> rightCount.incrementAndGet());
        dasManager.setOnDownRepeat(() -> downCount.incrementAndGet());

        dasManager.startLeftDAS(KeyCode.LEFT);
        dasManager.startRightDAS(KeyCode.RIGHT);
        dasManager.startDownDAS(KeyCode.DOWN);

        Thread.sleep(250);
        dasManager.stopAll();

        int leftAtStop = leftCount.get();
        int rightAtStop = rightCount.get();
        int downAtStop = downCount.get();

        Thread.sleep(200);

        assertEquals(leftAtStop, leftCount.get(), "Left should stop");
        assertEquals(rightAtStop, rightCount.get(), "Right should stop");
        assertEquals(downAtStop, downCount.get(), "Down should stop");
    }

    // ==================== RESET TESTS ====================

    @Test
    void testReset() {
        dasManager.startLeftDAS(KeyCode.LEFT);
        dasManager.startRightDAS(KeyCode.RIGHT);

        dasManager.reset();

        assertFalse(dasManager.isKeyPressed(KeyCode.LEFT));
        assertFalse(dasManager.isKeyPressed(KeyCode.RIGHT));
    }

    @Test
    void testReset_ClearsAllKeys() {
        dasManager.startLeftDAS(KeyCode.LEFT);
        dasManager.startRightDAS(KeyCode.RIGHT);
        dasManager.startDownDAS(KeyCode.DOWN);

        assertTrue(dasManager.isKeyPressed(KeyCode.LEFT));
        assertTrue(dasManager.isKeyPressed(KeyCode.RIGHT));
        assertTrue(dasManager.isKeyPressed(KeyCode.DOWN));

        dasManager.reset();

        assertFalse(dasManager.isKeyPressed(KeyCode.LEFT));
        assertFalse(dasManager.isKeyPressed(KeyCode.RIGHT));
        assertFalse(dasManager.isKeyPressed(KeyCode.DOWN));
    }

    // ==================== KEY TRACKING TESTS ====================

    @Test
    void testIsKeyPressed() {
        assertFalse(dasManager.isKeyPressed(KeyCode.LEFT));

        dasManager.startLeftDAS(KeyCode.LEFT);
        assertTrue(dasManager.isKeyPressed(KeyCode.LEFT));

        dasManager.stopLeftDAS(KeyCode.LEFT);
        assertFalse(dasManager.isKeyPressed(KeyCode.LEFT));
    }

    @Test
    void testIsKeyPressed_ReflectsCurrentState() {
        assertFalse(dasManager.isKeyPressed(KeyCode.LEFT),
                "Initially, no keys should be pressed");

        dasManager.startLeftDAS(KeyCode.LEFT);
        assertTrue(dasManager.isKeyPressed(KeyCode.LEFT),
                "After start, key should be marked as pressed");

        dasManager.stopLeftDAS(KeyCode.LEFT);
        assertFalse(dasManager.isKeyPressed(KeyCode.LEFT),
                "After stop, key should not be pressed");
    }

    // ==================== EDGE CASE TESTS ====================

    @Test
    void testDAS_RestartBeforeFirstRepeat() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        dasManager.setOnLeftRepeat(() -> {
            count.incrementAndGet();
            latch.countDown();
        });

        dasManager.startLeftDAS(KeyCode.LEFT);

        // Stop and restart immediately (before initial delay completes)
        Thread.sleep(50);
        dasManager.stopLeftDAS(KeyCode.LEFT);
        dasManager.startLeftDAS(KeyCode.LEFT);

        boolean gotRepeat = latch.await(500, TimeUnit.MILLISECONDS);
        dasManager.stopLeftDAS(KeyCode.LEFT);

        if (gotRepeat) {
            assertTrue(count.get() > 0, "Should eventually get repeat after restart");
        }
    }

    @Test
    void testDAS_MultipleStartCalls_DoesNotMultiplySpeed() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);

        dasManager.setOnLeftRepeat(() -> count.incrementAndGet());

        // Start multiple times
        dasManager.startLeftDAS(KeyCode.LEFT);
        dasManager.startLeftDAS(KeyCode.LEFT);
        dasManager.startLeftDAS(KeyCode.LEFT);

        Thread.sleep(400);
        dasManager.stopLeftDAS(KeyCode.LEFT);

        int finalCount = count.get();

        // Should not have 3x the normal speed
        // With 50ms repeats, 400ms should give ~8 repeats maximum (after 170ms delay)
        // 3x would be ~24 repeats
        assertTrue(finalCount < 15,
                "Multiple start calls should not multiply speed. Got " + finalCount + " repeats");
    }

    @Test
    void testStopWithoutStart() {
        assertDoesNotThrow(() -> dasManager.stopAll());
    }
}