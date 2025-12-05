package com.comp2042.model.game;

import com.comp2042.enums.GameMode;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import com.comp2042.model.scoring.Score;
import com.comp2042.model.data.ClearRow;
import com.comp2042.model.data.NextShapeInfo;
import com.comp2042.model.data.ViewData;
import com.comp2042.util.MatrixOperations;
import java.awt.*;

/**
 * Main game board implementation managing game state and piece movement.
 */
public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final HoldBrickManager holdBrickManager;
    private final Score score;
    private final GameMode gameMode;

    private int[][] currentGameMatrix;
    private Point currentOffset;
    private boolean boardCleared = false;

    /**
     * Creates a new game board.
     * @param width board width in cells
     * @param height board height in cells
     * @param gameMode the game mode (ZEN or BLITZ)
     */
    public SimpleBoard(int width, int height, GameMode gameMode) {
        this.width = width;
        this.height = height;
        this.gameMode = gameMode;
        this.currentGameMatrix = new int[width][height];
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.holdBrickManager = new HoldBrickManager();
        this.score = new Score(gameMode);
    }

    @Override
    public boolean moveBrickDown() {
        CollisionDetector detector = new CollisionDetector(currentGameMatrix);

        if (detector.canMoveDown(brickRotator.getCurrentShape(), currentOffset)) {
            currentOffset.translate(0, 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean moveBrickLeft() {
        CollisionDetector detector = new CollisionDetector(currentGameMatrix);

        if (detector.canMoveLeft(brickRotator.getCurrentShape(), currentOffset)) {
            currentOffset.translate(-1, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean moveBrickRight() {
        CollisionDetector detector = new CollisionDetector(currentGameMatrix);

        if (detector.canMoveRight(brickRotator.getCurrentShape(), currentOffset)) {
            currentOffset.translate(1, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean rotateLeftBrick() {
        CollisionDetector detector = new CollisionDetector(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();

        Point validPosition = detector.findValidRotationPosition(
                nextShape.getShape(), currentOffset);

        if (validPosition != null) {
            currentOffset = validPosition;
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
        return false;
    }

    @Override
    public int calculateShadowPosition() {
        CollisionDetector detector = new CollisionDetector(currentGameMatrix);
        return detector.calculateShadowY(brickRotator.getCurrentShape(), currentOffset);
    }

    @Override
    public boolean createNewBrick() {
        boardCleared = false;
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(3, 0);
        holdBrickManager.enableHold();

        CollisionDetector detector = new CollisionDetector(currentGameMatrix);
        boolean gameOver = detector.wouldCollide(brickRotator.getCurrentShape(),
                (int) currentOffset.getX(), (int) currentOffset.getY());

        if (gameOver && gameMode == GameMode.ZEN) {
            clearEntireBoard();
            boardCleared = true;

            detector = new CollisionDetector(currentGameMatrix);
            gameOver = detector.wouldCollide(brickRotator.getCurrentShape(),
                    (int) currentOffset.getX(), (int) currentOffset.getY());
        }

        return gameOver;
    }

    /**
     * Clears the entire board (used in Zen mode on game over).
     */
    private void clearEntireBoard() {
        currentGameMatrix = new int[width][height];
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        int shadowY = calculateShadowPosition();
        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                shadowY,
                holdBrickManager.getHoldBrickData()
        );
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY()
        );
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
        if (!holdBrickManager.canHold()) {
            return false;
        }

        Brick currentBrick = brickRotator.getCurrentBrick();

        if (!holdBrickManager.hasHoldBrick()) {
            holdBrickManager.setHoldBrick(currentBrick);
            Brick nextBrick = brickGenerator.getBrick();
            brickRotator.setBrick(nextBrick);
            currentOffset = new Point(3, 0);
        } else {
            Brick heldBrick = holdBrickManager.getHoldBrick();
            holdBrickManager.setHoldBrick(currentBrick);
            brickRotator.setBrick(heldBrick);

            CollisionDetector detector = new CollisionDetector(currentGameMatrix);
            int[][] currentShape = brickRotator.getCurrentShape();

            if (detector.wouldCollide(currentShape, (int) currentOffset.getX(), (int) currentOffset.getY())) {
                int newY = (int) currentOffset.getY();
                while (newY > 0 && detector.wouldCollide(currentShape, (int) currentOffset.getX(), newY)) {
                    newY--;
                }

                if (newY >= 0 && !detector.wouldCollide(currentShape, (int) currentOffset.getX(), newY)) {
                    currentOffset = new Point((int) currentOffset.getX(), newY);
                } else {
                    holdBrickManager.setHoldBrick(heldBrick);
                    brickRotator.setBrick(currentBrick);
                    return false;
                }
            }
        }

        holdBrickManager.disableHold();
        return true;
    }

    @Override
    public int[][] getHoldBrickData() {
        return holdBrickManager.getHoldBrickData();
    }

    @Override
    public boolean canHold() {
        return holdBrickManager.canHold();
    }

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        holdBrickManager.reset();
        boardCleared = false;
        brickGenerator.reset();
        createNewBrick();
    }

    @Override
    public boolean wasBoardCleared() {
        return boardCleared;
    }

    /**
     * Gets the game mode.
     * @return the game mode
     */
    public GameMode getGameMode() {
        return gameMode;
    }
}
