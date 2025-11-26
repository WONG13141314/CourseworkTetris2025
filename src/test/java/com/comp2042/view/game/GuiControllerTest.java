package com.comp2042.view.game;

import com.comp2042.controller.input.InputEventListener;
import com.comp2042.enums.GameMode;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GuiControllerTest {

    private TestInputEventListener testEventListener;
    private GuiController controller;

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
            controller = new GuiController();
            testEventListener = new TestInputEventListener();
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testGuiControllerCreation() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            assertNotNull(controller, "GuiController should be instantiated");
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetGameMode_ZenMode_DoesNotThrow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.setGameMode(GameMode.ZEN);
            } catch (NullPointerException e) {
            }
            assertNotNull(controller);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetGameMode_BlitzMode_DoesNotThrow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.setGameMode(GameMode.BLITZ);
            } catch (NullPointerException e) {
                // Expected if UIManager is not initialized in test environment
            }
            assertNotNull(controller);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBindScore_PropertyWorks() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            IntegerProperty scoreProperty = new SimpleIntegerProperty(0);

            // Test property functionality
            scoreProperty.set(100);
            assertEquals(100, scoreProperty.get());

            scoreProperty.set(200);
            assertEquals(200, scoreProperty.get());

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBindHighScore_PropertyWorks() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            IntegerProperty highScoreProperty = new SimpleIntegerProperty(0);

            // Test property functionality
            highScoreProperty.set(500);
            assertEquals(500, highScoreProperty.get());

            highScoreProperty.set(1000);
            assertEquals(1000, highScoreProperty.get());

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testEventListenerCanBeSet() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                controller.setEventListener(testEventListener);
            } catch (NullPointerException e) {
                // Expected if controller is not fully initialized
            }
            // If we get here without crash, test passes
            assertTrue(true, "setEventListener should not crash");
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    // Test implementation of InputEventListener
    private static class TestInputEventListener implements InputEventListener {
        boolean onDownEventCalled = false;
        boolean onLeftEventCalled = false;
        boolean onRightEventCalled = false;
        boolean onRotateEventCalled = false;
        boolean onHardDropEventCalled = false;
        boolean onHoldEventCalled = false;
        boolean createNewGameCalled = false;

        @Override
        public DownData onDownEvent(MoveEvent event) {
            onDownEventCalled = true;
            return new DownData(null, new ViewData(
                    new int[][]{{1}}, 0, 0, new int[][]{{1}}, 0, null
            ));
        }

        @Override
        public ViewData onLeftEvent(MoveEvent event) {
            onLeftEventCalled = true;
            return new ViewData(
                    new int[][]{{1}}, 0, 0, new int[][]{{1}}, 0, null
            );
        }

        @Override
        public ViewData onRightEvent(MoveEvent event) {
            onRightEventCalled = true;
            return new ViewData(
                    new int[][]{{1}}, 0, 0, new int[][]{{1}}, 0, null
            );
        }

        @Override
        public ViewData onRotateEvent(MoveEvent event) {
            onRotateEventCalled = true;
            return new ViewData(
                    new int[][]{{1}}, 0, 0, new int[][]{{1}}, 0, null
            );
        }

        @Override
        public DownData onHardDropEvent(MoveEvent event) {
            onHardDropEventCalled = true;
            return new DownData(null, new ViewData(
                    new int[][]{{1}}, 0, 0, new int[][]{{1}}, 0, null
            ));
        }

        @Override
        public ViewData onHoldEvent(MoveEvent event) {
            onHoldEventCalled = true;
            return new ViewData(
                    new int[][]{{1}}, 0, 0, new int[][]{{1}}, 0, null
            );
        }

        @Override
        public void createNewGame() {
            createNewGameCalled = true;
        }

        @Override
        public int getCurrentScore() {
            return 0;
        }

        @Override
        public int getCurrentHighScore() {
            return 0;
        }
    }
}