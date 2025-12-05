package com.comp2042.model.game;

import com.comp2042.logic.bricks.Brick;
import java.awt.Point;

/**
 * Manages the hold piece feature allowing players to swap current piece.
 */
public class HoldBrickManager {

    private Brick holdBrick;
    private boolean canHold;
    private boolean hasHoldBrick;

    /**
     * Creates a new hold brick manager.
     */
    public HoldBrickManager() {
        this.canHold = true;
        this.hasHoldBrick = false;
        this.holdBrick = null;
    }

    /**
     * Checks if player can currently use hold.
     * @return true if hold is available
     */
    public boolean canHold() {
        return canHold;
    }

    /**
     * Gets the currently held brick.
     * @return held brick or null
     */
    public Brick getHoldBrick() {
        return holdBrick;
    }

    /**
     * Gets the hold brick display data.
     * @return hold brick matrix for rendering
     */
    public int[][] getHoldBrickData() {
        if (holdBrick != null) {
            return holdBrick.getShapeMatrix().get(0);
        }
        return new int[0][0];
    }

    /**
     * Checks if there is a brick in hold.
     * @return true if hold slot is occupied
     */
    public boolean hasHoldBrick() {
        return hasHoldBrick;
    }

    /**
     * Sets the held brick.
     * @param brick brick to hold
     */
    public void setHoldBrick(Brick brick) {
        this.holdBrick = brick;
        this.hasHoldBrick = (brick != null);
    }

    /**
     * Disables hold for current piece (called after using hold).
     */
    public void disableHold() {
        this.canHold = false;
    }

    /**
     * Enables hold for new piece.
     */
    public void enableHold() {
        this.canHold = true;
    }

    /**
     * Resets hold system to initial state.
     */
    public void reset() {
        this.holdBrick = null;
        this.canHold = true;
        this.hasHoldBrick = false;
    }
}