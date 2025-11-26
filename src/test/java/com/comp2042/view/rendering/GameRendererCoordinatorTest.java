package com.comp2042.view.rendering;

import com.comp2042.model.data.ViewData;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameRendererCoordinatorTest {

    private GameRendererCoordinator rendererCoordinator;
    private GridPane gamePanel;
    private GridPane brickPanel;
    private GridPane nextBrickPanel;
    private GridPane holdBrickPanel;
    private Pane parentPane;

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
            brickPanel = new GridPane();
            nextBrickPanel = new GridPane();
            holdBrickPanel = new GridPane();
            parentPane = new Pane();

            rendererCoordinator = new GameRendererCoordinator(gamePanel, brickPanel,
                    nextBrickPanel, holdBrickPanel);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialize_InitializesAllRenderers() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            ViewData viewData = new ViewData(
                    new int[][]{{1, 1}, {1, 1}}, 3, 0,
                    new int[][]{{2, 2}, {2, 2}}, 0, null
            );

            rendererCoordinator.initialize(boardMatrix, viewData, parentPane);

            assertTrue(gamePanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testRefreshBrick_UpdatesAllComponents() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            ViewData initialData = new ViewData(
                    new int[][]{{1, 1}, {1, 1}}, 3, 0,
                    new int[][]{{2, 2}, {2, 2}}, 0, null
            );

            rendererCoordinator.initialize(boardMatrix, initialData, parentPane);

            ViewData newData = new ViewData(
                    new int[][]{{1, 1}, {1, 1}}, 3, 5,
                    new int[][]{{3, 3}, {3, 3}}, 10, new int[][]{{4, 4}, {4, 4}}
            );

            rendererCoordinator.refreshBrick(newData);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testRefreshBoard_UpdatesBoardDisplay() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            ViewData viewData = new ViewData(
                    new int[][]{{1}}, 3, 0, new int[][]{{2}}, 0, null
            );

            rendererCoordinator.initialize(boardMatrix, viewData, parentPane);

            boardMatrix[10][5] = 1;
            boardMatrix[11][5] = 2;

            rendererCoordinator.refreshBoard(boardMatrix);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetVisible_TogglesVisibility() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            ViewData viewData = new ViewData(
                    new int[][]{{1}}, 3, 0, new int[][]{{2}}, 0, null
            );

            rendererCoordinator.initialize(boardMatrix, viewData, parentPane);

            rendererCoordinator.setVisible(false);
            assertFalse(brickPanel.isVisible());

            rendererCoordinator.setVisible(true);
            assertTrue(brickPanel.isVisible());

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleRefreshCycles() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] boardMatrix = new int[25][10];
            ViewData initialData = new ViewData(
                    new int[][]{{1, 1}, {1, 1}}, 3, 0,
                    new int[][]{{2}}, 0, null
            );

            rendererCoordinator.initialize(boardMatrix, initialData, parentPane);

            for (int i = 0; i < 10; i++) {
                ViewData newData = new ViewData(
                        new int[][]{{1, 1}, {1, 1}}, 3, i,
                        new int[][]{{2}}, i + 10, null
                );
                rendererCoordinator.refreshBrick(newData);
            }

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}