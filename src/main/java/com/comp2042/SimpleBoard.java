package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick holdBrick;
    private boolean canHold = true;
    private boolean hasHoldBrick = false;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    public int calculateShadowPosition() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        int shadowY = (int) currentOffset.getY();

        while (true) {
            shadowY++;
            boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(),
                    (int) currentOffset.getX(), shadowY);
            if (conflict) {
                shadowY--;
                break;
            }
        }
        return shadowY;
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(3, 0);
        canHold = true;
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int shadowY = calculateShadowPosition();
        return new ViewData(brickRotator.getCurrentShape(),
                (int) currentOffset.getX(), (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                shadowY,
                getHoldBrickData());
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }

    @Override
    public boolean holdBrick() {
        if (!canHold) {
            return false;
        }

        Brick currentBrick = brickRotator.getCurrentBrick();

        if (!hasHoldBrick) {
            holdBrick = currentBrick;
            brickGenerator.getBrick();
            Brick nextBrick = brickGenerator.getBrick();
            brickRotator.setBrick(nextBrick);
            currentOffset = new Point(4, 0);
            hasHoldBrick = true;
        } else {
            Brick temp = holdBrick;
            holdBrick = currentBrick;
            brickRotator.setBrick(temp);

            int[][] currentShape = brickRotator.getCurrentShape();
            boolean conflict = MatrixOperations.intersect(currentGameMatrix, currentShape,
                    (int) currentOffset.getX(), (int) currentOffset.getY());

            if (conflict) {
                int newY = (int) currentOffset.getY();
                while (newY > 0 && MatrixOperations.intersect(currentGameMatrix, currentShape,
                        (int) currentOffset.getX(), newY)) {
                    newY--;
                }

                if (newY >= 0 && !MatrixOperations.intersect(currentGameMatrix, currentShape,
                        (int) currentOffset.getX(), newY)) {
                    currentOffset = new Point((int) currentOffset.getX(), newY);
                } else {
                    holdBrick = currentBrick;
                    brickRotator.setBrick(temp);
                    return false;
                }
            }
        }

        canHold = false;
        return true;
    }

    @Override
    public int[][] getHoldBrickData() {
        if (holdBrick != null) {
            return holdBrick.getShapeMatrix().get(0);
        }
        return new int[0][0];
    }

    @Override
    public boolean canHold() {
        return canHold;
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        holdBrick = null;
        hasHoldBrick = false;
        canHold = true;
        createNewBrick();
    }
}
