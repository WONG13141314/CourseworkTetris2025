package com.comp2042.model.game;

import com.comp2042.logic.bricks.Brick;
import java.awt.Point;

/**
 * Manages the hold brick feature
 */
public class HoldBrickManager {

    private Brick holdBrick;
    private boolean canHold;
    private boolean hasHoldBrick;

    public HoldBrickManager() {
        this.canHold = true;
        this.hasHoldBrick = false;
        this.holdBrick = null;
    }

    /**
     * Check if player can currently hold
     */
    public boolean canHold() {
        return canHold;
    }

    /**
     * Get the currently held brick
     */
    public Brick getHoldBrick() {
        return holdBrick;
    }

    /**
     * Get hold brick display data
     */
    public int[][] getHoldBrickData() {
        if (holdBrick != null) {
            return holdBrick.getShapeMatrix().get(0);
        }
        return new int[0][0];
    }

    /**
     * Check if there's a brick in hold
     */
    public boolean hasHoldBrick() {
        return hasHoldBrick;
    }

    /**
     * Set the hold brick (internal use)
     */
    public void setHoldBrick(Brick brick) {
        this.holdBrick = brick;
        this.hasHoldBrick = (brick != null);
    }

    /**
     * Disable hold for this turn (called after using hold)
     */
    public void disableHold() {
        this.canHold = false;
    }

    /**
     * Enable hold for new brick
     */
    public void enableHold() {
        this.canHold = true;
    }

    /**
     * Reset hold system
     */
    public void reset() {
        this.holdBrick = null;
        this.canHold = true;
        this.hasHoldBrick = false;
    }
}