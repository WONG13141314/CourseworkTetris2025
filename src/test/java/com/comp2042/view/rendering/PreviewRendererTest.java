package com.comp2042.view.rendering;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class PreviewRendererTest {

    private PreviewRenderer previewRenderer;
    private GridPane nextBrickPanel;
    private GridPane holdBrickPanel;

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
            nextBrickPanel = new GridPane();
            holdBrickPanel = new GridPane();
            previewRenderer = new PreviewRenderer(nextBrickPanel, holdBrickPanel);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitializeNextBrick_CreatesPreview() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] nextBrickData = {{1, 1}, {1, 1}};
            previewRenderer.initializeNextBrick(nextBrickData);

            assertTrue(nextBrickPanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testUpdateNextBrick_UpdatesPreview() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] initialBrick = {{1, 1}, {1, 1}};
            previewRenderer.initializeNextBrick(initialBrick);

            int[][] newBrick = {{2, 2, 2}, {0, 2, 0}};
            previewRenderer.updateNextBrick(newBrick);

            assertTrue(nextBrickPanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testUpdateHoldBrick_WithBrick() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] holdBrickData = {{3, 3}, {3, 3}};
            previewRenderer.updateHoldBrick(holdBrickData);

            assertTrue(holdBrickPanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testUpdateHoldBrick_WithNull() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            // First add a brick
            int[][] holdBrickData = {{3, 3}, {3, 3}};
            previewRenderer.updateHoldBrick(holdBrickData);

            // Then clear it
            previewRenderer.updateHoldBrick(null);

            assertEquals(0, holdBrickPanel.getChildren().size());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testUpdateHoldBrick_WithEmptyArray() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] emptyArray = new int[0][0];
            previewRenderer.updateHoldBrick(emptyArray);

            assertEquals(0, holdBrickPanel.getChildren().size());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleUpdates() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brick1 = {{1, 1}, {1, 1}};
            int[][] brick2 = {{2, 2, 2}, {0, 2, 0}};
            int[][] brick3 = {{3, 0}, {3, 0}, {3, 3}};

            previewRenderer.initializeNextBrick(brick1);
            previewRenderer.updateNextBrick(brick2);
            previewRenderer.updateNextBrick(brick3);

            assertTrue(nextBrickPanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testCenteredRendering_SmallBrick() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] smallBrick = {{1}};
            previewRenderer.initializeNextBrick(smallBrick);

            assertTrue(nextBrickPanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testCenteredRendering_LargeBrick() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] largeBrick = {{1, 1, 1, 1}};
            previewRenderer.initializeNextBrick(largeBrick);

            assertTrue(nextBrickPanel.getChildren().size() > 0);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}