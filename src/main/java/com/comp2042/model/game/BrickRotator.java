package com.comp2042.model.game;

import com.comp2042.model.data.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;

/**
 * Manages brick rotation states and shape transitions.
 * Handles cycling through different rotation states of bricks.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Gets information about the next rotation state.
     *
     * @return NextShapeInfo containing next shape and position
     */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Gets the current rotation shape matrix.
     *
     * @return 2D array representing current brick shape
     */
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Gets the current brick object.
     *
     * @return current Brick
     */
    public Brick getCurrentBrick() {
        return brick;
    }

    /**
     * Sets the current rotation position.
     *
     * @param currentShape rotation index to set
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets a new brick and resets rotation to initial state.
     *
     * @param brick the new Brick to manage
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }
}