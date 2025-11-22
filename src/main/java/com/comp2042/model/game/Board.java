package com.comp2042.model.game;

import com.comp2042.model.scoring.Score;
import com.comp2042.model.data.ClearRow;
import com.comp2042.model.data.ViewData;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    int calculateShadowPosition();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    boolean holdBrick();

    int[][] getHoldBrickData();

    boolean canHold();

    boolean wasBoardCleared();
}
