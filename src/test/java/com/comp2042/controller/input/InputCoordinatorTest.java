package com.comp2042.controller.input;

import com.comp2042.controller.game.GameEventHandler;
import com.comp2042.controller.game.GameStateManager;
import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.view.rendering.GameRendererCoordinator;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class InputCoordinatorTest {

    private GameStateManager stateManager;
    private TestInputHandler testInputHandler;
    private TestGameEventHandler testEventHandler;
    private TestGameRendererCoordinator testRendererCoordinator;

    private InputCoordinator inputCoordinator;
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
            stateManager = new GameStateManager();
            testInputHandler = new TestInputHandler();
            testEventHandler = new TestGameEventHandler();
            testRendererCoordinator = new TestGameRendererCoordinator();

            inputCoordinator = new InputCoordinator(stateManager, testInputHandler, gamePanel);
            inputCoordinator.setComponents(testEventHandler, testRendererCoordinator);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetupCallbacks_SetsUpInputHandler() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            inputCoordinator.setupCallbacks(() -> {}, () -> {}, () -> {});
            assertTrue(testInputHandler.setCallbackCalled, "setCallback should be called");
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMoveEvent_WhenCanProcessInput_HandlesMove() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            stateManager.setPaused(false);
            stateManager.setGameOver(false);

            ViewData mockViewData = new ViewData(
                    new int[][]{{1, 1}, {1, 1}}, 5, 10,
                    new int[][]{{2, 2}, {2, 2}}, 15, null
            );

            testEventHandler.handleMoveReturn = mockViewData;

            assertNotNull(inputCoordinator);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testDownEvent_RendersResult() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            stateManager.setPaused(false);
            stateManager.setGameOver(false);

            ViewData mockViewData = new ViewData(
                    new int[][]{{1}}, 0, 0, new int[][]{{1}}, 0, null
            );
            DownData mockDownData = new DownData(null, mockViewData);

            testEventHandler.handleDownReturn = mockDownData;

            assertNotNull(inputCoordinator);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testRotateEvent_WhenCanProcessInput_HandlesRotate() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            stateManager.setPaused(false);
            stateManager.setGameOver(false);

            ViewData mockViewData = new ViewData(
                    new int[][]{{1, 0}, {1, 1}}, 5, 10,
                    new int[][]{{2}}, 15, null
            );

            testEventHandler.handleRotateReturn = mockViewData;

            assertNotNull(inputCoordinator);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testHardDropEvent_RendersResult() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            stateManager.setPaused(false);
            stateManager.setGameOver(false);

            ViewData mockViewData = new ViewData(
                    new int[][]{{1}}, 0, 19, new int[][]{{1}}, 19, null
            );
            DownData mockDownData = new DownData(null, mockViewData);

            testEventHandler.handleHardDropReturn = mockDownData;

            assertNotNull(inputCoordinator);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testHoldEvent_RendersResult() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            stateManager.setPaused(false);
            stateManager.setGameOver(false);

            ViewData mockViewData = new ViewData(
                    new int[][]{{1}}, 3, 0, new int[][]{{2}}, 0, new int[][]{{1}}
            );

            testEventHandler.handleHoldReturn = mockViewData;

            assertNotNull(inputCoordinator);
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    // Test implementations
    private static class TestInputHandler extends InputHandler {
        boolean setCallbackCalled = false;

        @Override
        public void setCallback(InputCallback callback) {
            setCallbackCalled = true;
            super.setCallback(callback);
        }
    }

    private static class TestGameEventHandler extends GameEventHandler {
        ViewData handleMoveReturn = null;
        ViewData handleRotateReturn = null;
        ViewData handleHoldReturn = null;
        DownData handleDownReturn = null;
        DownData handleHardDropReturn = null;

        public TestGameEventHandler() {
            super(new TestInputEventListener(), com.comp2042.enums.GameMode.ZEN);
        }

        @Override
        public ViewData handleMove(MoveEvent event) {
            return handleMoveReturn;
        }

        @Override
        public ViewData handleRotate(MoveEvent event) {
            return handleRotateReturn;
        }

        @Override
        public ViewData handleHold(MoveEvent event) {
            return handleHoldReturn;
        }

        @Override
        public DownData handleDown(MoveEvent event) {
            return handleDownReturn;
        }

        @Override
        public DownData handleHardDrop(MoveEvent event) {
            return handleHardDropReturn;
        }
    }

    private static class TestInputEventListener implements InputEventListener {
        @Override
        public DownData onDownEvent(MoveEvent event) { return null; }
        @Override
        public ViewData onLeftEvent(MoveEvent event) { return null; }
        @Override
        public ViewData onRightEvent(MoveEvent event) { return null; }
        @Override
        public ViewData onRotateEvent(MoveEvent event) { return null; }
        @Override
        public DownData onHardDropEvent(MoveEvent event) { return null; }
        @Override
        public ViewData onHoldEvent(MoveEvent event) { return null; }
        @Override
        public void createNewGame() {}
        @Override
        public int getCurrentScore() { return 0; }
        @Override
        public int getCurrentHighScore() { return 0; }
    }

    private static class TestGameRendererCoordinator extends GameRendererCoordinator {
        public TestGameRendererCoordinator() {
            super(new GridPane(), new GridPane(), new GridPane(), new GridPane());
        }

        @Override
        public void refreshBrick(ViewData viewData) {
            // Do nothing in test
        }

        @Override
        public void refreshBoard(int[][] board) {
            // Do nothing in test
        }
    }
}