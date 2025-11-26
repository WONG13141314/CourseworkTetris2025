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

class ShadowRendererTest {

    private ShadowRenderer shadowRenderer;
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
            shadowRenderer = new ShadowRenderer(gamePanel);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitialize_CreatesShadowPanel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1, 1}, {1, 1}};
            shadowRenderer.initialize(brickData);

            assertNotNull(shadowRenderer.getShadowPanel());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testUpdate_ShowsShadowWhenDifferentFromBrick() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1, 1}, {1, 1}};
            shadowRenderer.initialize(brickData);

            ViewData viewData = new ViewData(
                    brickData, 5, 5, new int[][]{{2}}, 15, null
            );

            shadowRenderer.update(viewData);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testUpdate_HidesShadowWhenSameAsBrick() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1, 1}, {1, 1}};
            shadowRenderer.initialize(brickData);

            // Shadow at same position as brick
            ViewData viewData = new ViewData(
                    brickData, 5, 10, new int[][]{{2}}, 10, null
            );

            shadowRenderer.update(viewData);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testUpdate_WithRotation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData1 = {{1, 1}, {1, 1}};
            shadowRenderer.initialize(brickData1);

            // Rotated brick
            int[][] brickData2 = {{1, 1, 1}, {0, 1, 0}};
            ViewData viewData = new ViewData(
                    brickData2, 3, 5, new int[][]{{2}}, 15, null
            );

            shadowRenderer.update(viewData);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetVisible_TogglesVisibility() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1}};
            shadowRenderer.initialize(brickData);

            shadowRenderer.setVisible(true);
            assertTrue(shadowRenderer.getShadowPanel().isVisible());

            shadowRenderer.setVisible(false);
            assertFalse(shadowRenderer.getShadowPanel().isVisible());

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleUpdates() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1, 1}, {1, 1}};
            shadowRenderer.initialize(brickData);

            for (int i = 0; i < 10; i++) {
                ViewData viewData = new ViewData(
                        brickData, 3, i, new int[][]{{2}}, 15, null
                );
                shadowRenderer.update(viewData);
            }

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testUpdate_WithDifferentBrickTypes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            int[][] brickData = {{1, 1}, {1, 1}};
            shadowRenderer.initialize(brickData);

            int[][] brick2 = {{2, 2, 2, 2}};
            ViewData viewData1 = new ViewData(
                    brick2, 0, 0, new int[][]{{3}}, 10, null
            );
            shadowRenderer.update(viewData1);

            int[][] brick3 = {{3, 0}, {3, 3}, {0, 3}};
            ViewData viewData2 = new ViewData(
                    brick3, 3, 5, new int[][]{{4}}, 15, null
            );
            shadowRenderer.update(viewData2);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}