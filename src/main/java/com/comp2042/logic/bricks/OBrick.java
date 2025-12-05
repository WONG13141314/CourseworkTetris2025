package com.comp2042.logic.bricks;

import com.comp2042.util.MatrixOperations;
import java.util.ArrayList;
import java.util.List;

/**
 * O-shaped Tetris brick (square).
 * Has only 1 rotation state. Uses color value 4 (yellow).
 */
public final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    /**
     * Constructs O-brick with single rotation state.
     */
    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }

    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}