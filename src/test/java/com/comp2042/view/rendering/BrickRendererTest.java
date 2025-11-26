package com.comp2042.view.rendering;

import com.comp2042.model.data.ViewData;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BrickRendererTest {

    private BrickRenderer brickRenderer;
    private GridPane brickPanel;
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
            brickPanel = new GridPane();
            gamePanel = new GridPane();
            brickRenderer = new BrickRenderer(brickPanel, gamePanel);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialize_CreatesBrickRectangles() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1, 1}, {1, 1}};
            brickRenderer.initialize(brickData);

            assertTrue(brickPanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testRefresh_UpdatesBrickPosition() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1, 1}, {1, 1}};
            brickRenderer.initialize(brickData);

            ViewData viewData = new ViewData(
                    brickData, 5, 10, new int[][]{{2}}, 15, null
            );

            brickRenderer.refresh(viewData);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testRefresh_WithRotation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData1 = {{1, 1}, {1, 1}};
            brickRenderer.initialize(brickData1);

            // Rotate brick (different shape)
            int[][] brickData2 = {{1, 1, 1}, {0, 1, 0}};
            ViewData viewData = new ViewData(
                    brickData2, 3, 5, new int[][]{{2}}, 10, null
            );

            brickRenderer.refresh(viewData);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testPositionPanel_SetsCorrectPosition() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1}};
            brickRenderer.initialize(brickData);

            ViewData viewData = new ViewData(
                    brickData, 5, 10, new int[][]{{2}}, 15, null
            );

            brickRenderer.positionPanel(viewData);

            assertTrue(brickPanel.getLayoutX() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetVisible_TogglesVisibility() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            brickRenderer.setVisible(true);
            assertTrue(brickPanel.isVisible());

            brickRenderer.setVisible(false);
            assertFalse(brickPanel.isVisible());

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testRefresh_MultipleTimes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1, 1}, {1, 1}};
            brickRenderer.initialize(brickData);

            for (int i = 0; i < 5; i++) {
                ViewData viewData = new ViewData(
                        brickData, 3, i, new int[][]{{2}}, i + 10, null
                );
                brickRenderer.refresh(viewData);
            }

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}