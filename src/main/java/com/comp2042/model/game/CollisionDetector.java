package com.comp2042.model.game;

import com.comp2042.util.MatrixOperations;
import java.awt.Point;

/**
 * Handles collision detection between pieces and the game board.
 */
public class CollisionDetector {

    private final int[][] gameMatrix;

    /**
     * Creates a collision detector for the given board state.
     * @param gameMatrix the current game board matrix
     */
    public CollisionDetector(int[][] gameMatrix) {
        this.gameMatrix = gameMatrix;
    }

    /**
     * Checks if a brick would collide at the given position.
     * @param brickShape the brick shape matrix
     * @param x x position
     * @param y y position
     * @return true if collision would occur
     */
    public boolean wouldCollide(int[][] brickShape, int x, int y) {
        return MatrixOperations.intersect(gameMatrix, brickShape, x, y);
    }

    /**
     * Checks if brick can move down from current position.
     * @param brickShape the brick shape matrix
     * @param currentOffset current position
     * @return true if can move down
     */
    public boolean canMoveDown(int[][] brickShape, Point currentOffset) {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(0, 1);
        return !wouldCollide(brickShape, (int) newPosition.getX(), (int) newPosition.getY());
    }

    /**
     * Checks if brick can move left from current position.
     * @param brickShape the brick shape matrix
     * @param currentOffset current position
     * @return true if can move left
     */
    public boolean canMoveLeft(int[][] brickShape, Point currentOffset) {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(-1, 0);
        return !wouldCollide(brickShape, (int) newPosition.getX(), (int) newPosition.getY());
    }

    /**
     * Checks if brick can move right from current position.
     * @param brickShape the brick shape matrix
     * @param currentOffset current position
     * @return true if can move right
     */
    public boolean canMoveRight(int[][] brickShape, Point currentOffset) {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(1, 0);
        return !wouldCollide(brickShape, (int) newPosition.getX(), (int) newPosition.getY());
    }

    /**
     * Checks if brick can rotate at current position.
     * @param newShape the rotated brick shape
     * @param currentOffset current position
     * @return true if can rotate
     */
    public boolean canRotate(int[][] newShape, Point currentOffset) {
        return !wouldCollide(newShape, (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Finds a valid position for rotation using wall kick system.
     * @param newShape the rotated brick shape
     * @param currentOffset current position
     * @return valid position or null if rotation impossible
     */
    public Point findValidRotationPosition(int[][] newShape, Point currentOffset) {
        if (!wouldCollide(newShape, (int) currentOffset.getX(), (int) currentOffset.getY())) {
            return currentOffset;
        }

        Point leftPos = new Point(currentOffset);
        leftPos.translate(-1, 0);
        if (!wouldCollide(newShape, (int) leftPos.getX(), (int) leftPos.getY())) {
            return leftPos;
        }

        Point rightPos = new Point(currentOffset);
        rightPos.translate(1, 0);
        if (!wouldCollide(newShape, (int) rightPos.getX(), (int) rightPos.getY())) {
            return rightPos;
        }

        return null;
    }

    /**
     * Calculates the shadow (drop preview) Y position.
     * @param brickShape the brick shape matrix
     * @param currentOffset current position
     * @return the Y position where piece would land
     */
    public int calculateShadowY(int[][] brickShape, Point currentOffset) {
        int shadowY = (int) currentOffset.getY();

        while (true) {
            shadowY++;
            if (wouldCollide(brickShape, (int) currentOffset.getX(), shadowY)) {
                shadowY--;
                break;
            }
        }
        return shadowY;
    }
}