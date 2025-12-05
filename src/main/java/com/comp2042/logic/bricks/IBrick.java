package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * I-shaped Tetris brick (straight line piece).
 * Has 2 rotation states (horizontal and vertical).
 */
public final class IBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs I-brick with horizontal and vertical orientations.
     * Uses color value 1 (cyan).
     */
    public IBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}