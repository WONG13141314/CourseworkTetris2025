package com.comp2042.model.data;

/**
 * Data class representing the result of a downward movement.
 * Contains information about cleared rows, updated view data, and board clear status.
 */
public class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    private final boolean boardCleared;

    /**
     * Constructs DownData with clear row and view data.
     *
     * @param clearRow information about cleared rows, or null if none
     * @param viewData updated view data after movement
     */
    public DownData(ClearRow clearRow, ViewData viewData) {
        this(clearRow, viewData, false);
    }

    /**
     * Constructs DownData with all parameters.
     *
     * @param clearRow information about cleared rows, or null if none
     * @param viewData updated view data after movement
     * @param boardCleared true if the entire board was cleared (Zen mode)
     */
    public DownData(ClearRow clearRow, ViewData viewData, boolean boardCleared) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.boardCleared = boardCleared;
    }

    /**
     * Gets clear row information.
     *
     * @return ClearRow object or null if no rows were cleared
     */
    public ClearRow getClearRow() {
        return clearRow;
    }

    /**
     * Gets updated view data.
     *
     * @return ViewData object with current game state
     */
    public ViewData getViewData() {
        return viewData;
    }

    /**
     * Checks if the entire board was cleared.
     *
     * @return true if board was cleared (Zen mode feature)
     */
    public boolean isBoardCleared() {
        return boardCleared;
    }
}