package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * S-shaped Tetris brick.
 * Has 2 rotation states. Uses color value 5 (red).
 */
public final class SBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs S-brick with 2 rotation states.
     */
    public SBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}