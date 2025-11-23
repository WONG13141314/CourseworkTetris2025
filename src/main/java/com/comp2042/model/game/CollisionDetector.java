package com.comp2042.model.game;

import com.comp2042.util.MatrixOperations;
import java.awt.Point;

/**
 * Handles all collision detection logic for the game board
 */
public class CollisionDetector {

    private final int[][] gameMatrix;

    public CollisionDetector(int[][] gameMatrix) {
        this.gameMatrix = gameMatrix;
    }

    /**
     * Check if brick would collide at given position
     */
    public boolean wouldCollide(int[][] brickShape, int x, int y) {
        return MatrixOperations.intersect(gameMatrix, brickShape, x, y);
    }

    /**
     * Check if brick can move down
     */
    public boolean canMoveDown(int[][] brickShape, Point currentOffset) {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(0, 1);
        return !wouldCollide(brickShape, (int) newPosition.getX(), (int) newPosition.getY());
    }

    /**
     * Check if brick can move left
     */
    public boolean canMoveLeft(int[][] brickShape, Point currentOffset) {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(-1, 0);
        return !wouldCollide(brickShape, (int) newPosition.getX(), (int) newPosition.getY());
    }

    /**
     * Check if brick can move right
     */
    public boolean canMoveRight(int[][] brickShape, Point currentOffset) {
        Point newPosition = new Point(currentOffset);
        newPosition.translate(1, 0);
        return !wouldCollide(brickShape, (int) newPosition.getX(), (int) newPosition.getY());
    }

    /**
     * Check if brick can rotate
     */
    public boolean canRotate(int[][] newShape, Point currentOffset) {
        return !wouldCollide(newShape, (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Try wall kick for rotation (check left/right positions)
     */
    public Point findValidRotationPosition(int[][] newShape, Point currentOffset) {
        // Try current position
        if (!wouldCollide(newShape, (int) currentOffset.getX(), (int) currentOffset.getY())) {
            return currentOffset;
        }

        // Try left
        Point leftPos = new Point(currentOffset);
        leftPos.translate(-1, 0);
        if (!wouldCollide(newShape, (int) leftPos.getX(), (int) leftPos.getY())) {
            return leftPos;
        }

        // Try right
        Point rightPos = new Point(currentOffset);
        rightPos.translate(1, 0);
        if (!wouldCollide(newShape, (int) rightPos.getX(), (int) rightPos.getY())) {
            return rightPos;
        }

        return null; // No valid position found
    }

    /**
     * Calculate shadow (drop) position
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