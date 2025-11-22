package com.comp2042.controller;

import com.comp2042.controller.input.InputEventListener;
import com.comp2042.enums.EventSource;
import com.comp2042.enums.GameMode;
import com.comp2042.model.game.Board;
import com.comp2042.model.game.SimpleBoard;
import com.comp2042.model.data.ClearRow;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.view.GuiController;

public class GameController implements InputEventListener {

    private Board board;
    private final GuiController viewGuiController;
    private final GameMode gameMode;

    public GameController(GuiController c, GameMode gameMode) {
        this.gameMode = gameMode;
        this.viewGuiController = c;
        this.board = new SimpleBoard(25, 10, gameMode);

        board.createNewBrick();
        viewGuiController.setGameMode(gameMode);
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindHighScore(board.getScore().highScoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        boolean boardCleared = false;

        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }

            boolean gameOver = board.createNewBrick();

            if (board.wasBoardCleared()) {
                boardCleared = true;
                viewGuiController.refreshGameBackground(board.getBoardMatrix());
            } else if (gameOver) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData(), boardCleared);
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public DownData onHardDropEvent(MoveEvent event) {
        ClearRow clearRow = null;
        int dropDistance = 0;
        boolean boardCleared = false;

        while (board.moveBrickDown()) {
            dropDistance++;
        }

        if (dropDistance > 0) {
            board.getScore().add(dropDistance * 2);
        }

        board.mergeBrickToBackground();
        clearRow = board.clearRows();

        if (clearRow != null && clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
        }

        boolean gameOver = board.createNewBrick();

        if (board.wasBoardCleared()) {
            boardCleared = true;
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        } else if (gameOver) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());

        return new DownData(clearRow, board.getViewData(), boardCleared);
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        if (board.holdBrick()) {
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        }
        return board.getViewData();
    }

    @Override
    public int getCurrentScore() {
        return board.getScore().scoreProperty().get();
    }

    @Override
    public int getCurrentHighScore() {
        return board.getScore().highScoreProperty().get();
    }
}