package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * Z-shaped Tetris brick.
 * Has 2 rotation states. Uses color value 7 (brown).
 */
public final class ZBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs Z-brick with 2 rotation states.
     */
    public ZBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}