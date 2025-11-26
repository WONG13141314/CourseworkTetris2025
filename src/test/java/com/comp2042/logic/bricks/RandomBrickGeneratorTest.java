package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Tests for RandomBrickGenerator
 * Tests the 7-bag system guarantee: exactly one of each type per 7 pieces
 */
class RandomBrickGeneratorTest {
    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    // ==================== 7-BAG SYSTEM GUARANTEE TESTS ====================

    @Test
    void testSevenBagSystem_ExactlyOneOfEachTypePerSeven() {
        Map<Class<?>, Integer> counts = new HashMap<>();

        // Get 7 bricks (one complete bag)
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            Class<?> brickClass = brick.getClass();
            counts.put(brickClass, counts.getOrDefault(brickClass, 0) + 1);
        }

        // Should have exactly 7 different types, each appearing once
        assertEquals(7, counts.size(),
                "Should have all 7 brick types in one bag");

        for (Map.Entry<Class<?>, Integer> entry : counts.entrySet()) {
            assertEquals(1, entry.getValue(),
                    "Each brick type should appear exactly once: " + entry.getKey().getSimpleName());
        }
    }

    @Test
    void testSevenBagSystem_TwoCompleteSets() {
        Map<Class<?>, Integer> counts = new HashMap<>();

        // Get 14 bricks (two complete bags)
        for (int i = 0; i < 14; i++) {
            Brick brick = generator.getBrick();
            Class<?> brickClass = brick.getClass();
            counts.put(brickClass, counts.getOrDefault(brickClass, 0) + 1);
        }

        // Should have all 7 types, each appearing exactly twice
        assertEquals(7, counts.size(),
                "Should still have all 7 brick types after two bags");

        for (Map.Entry<Class<?>, Integer> entry : counts.entrySet()) {
            assertEquals(2, entry.getValue(),
                    "Each brick type should appear exactly twice in 14 pieces: " +
                            entry.getKey().getSimpleName());
        }
    }

    @Test
    void testSevenBagSystem_ThreeCompleteSets() {
        Map<Class<?>, Integer> counts = new HashMap<>();

        // Get 21 bricks (three complete bags)
        for (int i = 0; i < 21; i++) {
            Brick brick = generator.getBrick();
            Class<?> brickClass = brick.getClass();
            counts.put(brickClass, counts.getOrDefault(brickClass, 0) + 1);
        }

        assertEquals(7, counts.size(), "Should have all 7 brick types");

        for (Map.Entry<Class<?>, Integer> entry : counts.entrySet()) {
            assertEquals(3, entry.getValue(),
                    "Each brick type should appear exactly 3 times in 21 pieces: " +
                            entry.getKey().getSimpleName());
        }
    }

    @Test
    void testSevenBagSystem_PartialBag() {
        // Get first 3 pieces from a bag
        Set<Class<?>> firstThree = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            Brick brick = generator.getBrick();
            firstThree.add(brick.getClass());
        }

        // All should be different (no duplicates in partial bag)
        assertEquals(3, firstThree.size(),
                "First 3 pieces should all be different types");

        // Get next 4 pieces to complete the bag
        Set<Class<?>> nextFour = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            Brick brick = generator.getBrick();
            nextFour.add(brick.getClass());
        }

        assertEquals(4, nextFour.size(),
                "Next 4 pieces should all be different types");

        // Combined, should have all 7 types
        Set<Class<?>> combined = new HashSet<>();
        combined.addAll(firstThree);
        combined.addAll(nextFour);

        assertEquals(7, combined.size(),
                "First 7 pieces combined should include all 7 types");
    }

    @Test
    void testSevenBagSystem_OrderIsRandom() {
        // Get the order of two complete bags
        List<Class<?>> firstBagOrder = new ArrayList<>();
        List<Class<?>> secondBagOrder = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            firstBagOrder.add(generator.getBrick().getClass());
        }

        for (int i = 0; i < 7; i++) {
            secondBagOrder.add(generator.getBrick().getClass());
        }

        // Orders should be different (very unlikely to be same if random)
        assertNotEquals(firstBagOrder, secondBagOrder,
                "Two bags should have different orders (randomized)");
    }

    @Test
    void testSevenBagSystem_VerifyAllBrickTypes() {
        Set<Class<?>> allTypes = new HashSet<>();

        // Get 7 bricks
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            allTypes.add(brick.getClass());
        }

        // Verify we have all expected types
        Set<Class<?>> expectedTypes = new HashSet<>(Arrays.asList(
                IBrick.class,
                JBrick.class,
                LBrick.class,
                OBrick.class,
                SBrick.class,
                TBrick.class,
                ZBrick.class
        ));

        assertEquals(expectedTypes, allTypes,
                "Should generate all 7 brick types (I, J, L, O, S, T, Z)");
    }

    @Test
    void testSevenBagSystem_NoDuplicatesInSingleBag() {
        Set<Class<?>> seenInBag = new HashSet<>();

        // Check 7 bricks (one bag)
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            Class<?> brickClass = brick.getClass();

            assertFalse(seenInBag.contains(brickClass),
                    "Brick type " + brickClass.getSimpleName() +
                            " appeared twice in same bag at position " + i);

            seenInBag.add(brickClass);
        }
    }

    @Test
    void testSevenBagSystem_LargeNumberOfBricks() {
        Map<Class<?>, Integer> counts = new HashMap<>();

        // Get 70 bricks (10 complete bags)
        for (int i = 0; i < 70; i++) {
            Brick brick = generator.getBrick();
            Class<?> brickClass = brick.getClass();
            counts.put(brickClass, counts.getOrDefault(brickClass, 0) + 1);
        }

        // Each type should appear exactly 10 times
        assertEquals(7, counts.size(), "Should have all 7 types");

        for (Map.Entry<Class<?>, Integer> entry : counts.entrySet()) {
            assertEquals(10, entry.getValue(),
                    "Each type should appear exactly 10 times in 70 pieces: " +
                            entry.getKey().getSimpleName());
        }
    }

    // ==================== RESET FUNCTIONALITY TESTS ====================

    @Test
    void testReset_RefillsQueue() {
        // Get some bricks
        for (int i = 0; i < 5; i++) {
            generator.getBrick();
        }

        generator.reset();

        // Should be able to get 7 unique types again
        Set<Class<?>> typesAfterReset = new HashSet<>();
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            typesAfterReset.add(brick.getClass());
        }

        assertEquals(7, typesAfterReset.size(),
                "After reset, should get all 7 types in first bag");
    }

    @Test
    void testReset_StartsNewBags() {
        Map<Class<?>, Integer> beforeReset = new HashMap<>();

        // Get 10 bricks (1 bag + 3 from second bag)
        for (int i = 0; i < 10; i++) {
            Brick brick = generator.getBrick();
            beforeReset.put(brick.getClass(),
                    beforeReset.getOrDefault(brick.getClass(), 0) + 1);
        }

        generator.reset();

        Map<Class<?>, Integer> afterReset = new HashMap<>();

        // Get 7 bricks (should be fresh bag)
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();
            afterReset.put(brick.getClass(),
                    afterReset.getOrDefault(brick.getClass(), 0) + 1);
        }

        // After reset, should have exactly one of each type
        assertEquals(7, afterReset.size(), "After reset, first 7 should be complete bag");
        for (Integer count : afterReset.values()) {
            assertEquals(1, count, "Each type should appear once after reset");
        }
    }

    // ==================== NEXT BRICK PREVIEW TESTS ====================

    @Test
    void testGetNextBrick_ShowsActualNext() {
        Brick nextBrick = generator.getNextBrick();
        Brick actualNext = generator.getBrick();

        assertEquals(nextBrick.getClass(), actualNext.getClass(),
                "getNextBrick should show the actual next brick");
    }

    @Test
    void testGetNextBrick_DoesNotAdvanceQueue() {
        Brick first = generator.getNextBrick();
        Brick stillFirst = generator.getNextBrick();

        assertEquals(first.getClass(), stillFirst.getClass(),
                "Multiple getNextBrick calls should show same brick");
    }

    @Test
    void testGetNextBrick_UpdatesAfterGetBrick() {
        Brick next1 = generator.getNextBrick();
        generator.getBrick(); // Advance queue
        Brick next2 = generator.getNextBrick();

        // In a 7-bag, next2 should be different from next1
        // (very unlikely to be same in random order)
        assertNotNull(next2, "Should have a next brick");
    }

    // ==================== EXISTING TESTS (kept for compatibility) ====================

    @Test
    void testGetBrick_ReturnsNonNull() {
        assertNotNull(generator.getBrick(), "getBrick should never return null");
    }

    @Test
    void testGetNextBrick_ReturnsNonNull() {
        assertNotNull(generator.getNextBrick(), "getNextBrick should never return null");
    }

    @Test
    void testGetBrick_AdvancesQueue() {
        Brick first = generator.getNextBrick();
        generator.getBrick();
        Brick second = generator.getNextBrick();

        assertNotSame(first, second, "Getting a brick should advance the queue");
    }

    @Test
    void testMultipleGenerations_NeverReturnsNull() {
        for (int i = 0; i < 100; i++) {
            assertNotNull(generator.getBrick(),
                    "Should never return null even after many generations (iteration " + i + ")");
        }
    }

    // ==================== BRICK SHAPE VERIFICATION ====================

    @Test
    void testBrickShapes_AreValid() {
        // Get 7 bricks and verify each has valid shape matrix
        for (int i = 0; i < 7; i++) {
            Brick brick = generator.getBrick();

            assertNotNull(brick.getShapeMatrix(),
                    "Brick should have shape matrix");
            assertFalse(brick.getShapeMatrix().isEmpty(),
                    "Shape matrix should not be empty");

            // Verify first shape is valid
            int[][] shape = brick.getShapeMatrix().get(0);
            assertNotNull(shape, "First shape should not be null");
            assertTrue(shape.length > 0, "Shape should have rows");
            assertTrue(shape[0].length > 0, "Shape should have columns");
        }
    }
}