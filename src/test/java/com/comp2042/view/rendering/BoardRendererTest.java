package com.comp2042.view.rendering;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BoardRendererTest {

    private BoardRenderer boardRenderer;
    private GridPane gamePanel;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already initialized
        }
        Thread.sleep(100);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            gamePanel = new GridPane();
            boardRenderer = new BoardRenderer(gamePanel);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialize_CreatesDisplayMatrix() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            boardRenderer.initialize(boardMatrix);

            // Check that rectangles were added to the panel
            assertTrue(gamePanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testRefresh_UpdatesBoardDisplay() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            boardRenderer.initialize(boardMatrix);

            // Fill some cells
            boardMatrix[10][5] = 1;
            boardMatrix[15][3] = 2;

            boardRenderer.refresh(boardMatrix);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleRefreshes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            boardRenderer.initialize(boardMatrix);

            boardMatrix[10][5] = 1;
            boardRenderer.refresh(boardMatrix);

            boardMatrix[11][5] = 2;
            boardRenderer.refresh(boardMatrix);

            boardMatrix[12][5] = 3;
            boardRenderer.refresh(boardMatrix);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialize_WithEmptyBoard() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] emptyBoard = new int[25][10];
            boardRenderer.initialize(emptyBoard);

            assertTrue(gamePanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testRefresh_WithFullBoard() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            boardRenderer.initialize(boardMatrix);

            // Fill entire board
            for (int i = 0; i < boardMatrix.length; i++) {
                for (int j = 0; j < boardMatrix[i].length; j++) {
                    boardMatrix[i][j] = (i + j) % 7 + 1;
                }
            }

            boardRenderer.refresh(boardMatrix);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}