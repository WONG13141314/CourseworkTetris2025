package com.comp2042.logic.bricks;

/**
 * Interface for generating bricks in sequence.
 * Implementations can use different algorithms (random, 7-bag, etc.).
 */
public interface BrickGenerator {

    /**
     * Gets the next brick in sequence.
     *
     * @return the next Brick to use
     */
    Brick getBrick();

    /**
     * Peeks at the next brick without removing it.
     *
     * @return the upcoming Brick
     */
    Brick getNextBrick();

    /**
     * Resets the generator to initial state.
     */
    void reset();
}