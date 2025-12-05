package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Interface for Tetris brick types.
 * Each implementation defines the shape matrices for all rotation states.
 */
public interface Brick {

    /**
     * Gets all rotation states of this brick.
     *
     * @return list of 2D arrays, each representing a rotation state
     */
    List<int[][]> getShapeMatrix();
}