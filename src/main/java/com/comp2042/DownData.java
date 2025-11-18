package com.comp2042;

public class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    private final boolean boardCleared;

    public DownData(ClearRow clearRow, ViewData viewData) {
        this(clearRow, viewData, false);
    }

    public DownData(ClearRow clearRow, ViewData viewData, boolean boardCleared) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.boardCleared = boardCleared;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }

    public boolean isBoardCleared() {
        return boardCleared;
    }
}
