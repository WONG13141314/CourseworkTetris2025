package com.comp2042.controller.lifecycle;

import com.comp2042.controller.game.GameStateManager;
import com.comp2042.controller.input.InputEventListener;
import com.comp2042.controller.input.InputHandler;
import com.comp2042.enums.GameMode;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.util.SoundManager;
import com.comp2042.view.components.GameOverPanel;
import com.comp2042.view.game.UIManager;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameInitializerTest {

    private GameInitializer zenInitializer;
    private GameInitializer blitzInitializer;
    private GameStateManager stateManager;
    private InputHandler inputHandler;
    private UIManager uiManager;
    private SoundManager soundManager;
    private TestInputEventListener testEventListener;

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
            stateManager = new GameStateManager();
            inputHandler = new InputHandler();
            soundManager = SoundManager.getInstance();

            Label scoreLabel = new Label();
            Label highScoreLabel = new Label();
            Label gameModeLabel = new Label();
            GameOverPanel gameOverPanel = new GameOverPanel();
            Group pauseGroup = new Group();
            Group notificationGroup = new Group();
            VBox blitzLevelPanel = new VBox();

            uiManager = new UIManager(scoreLabel, highScoreLabel, gameModeLabel,
                    gameOverPanel, pauseGroup, notificationGroup, blitzLevelPanel);

            zenInitializer = new GameInitializer(GameMode.ZEN, stateManager,
                    inputHandler, uiManager, soundManager);
            blitzInitializer = new GameInitializer(GameMode.BLITZ, stateManager,
                    inputHandler, uiManager, soundManager);

            testEventListener = new TestInputEventListener();

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitializeGameMode_ZenMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Label timerLabel = new Label();
            zenInitializer.initializeGameMode(null, null, timerLabel);

            assertNotNull(zenInitializer.getZenModeManager());
            assertNull(zenInitializer.getBlitzModeManager());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitializeGameMode_BlitzMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Label levelLabel = new Label();
            Label progressLabel = new Label();
            blitzInitializer.initializeGameMode(levelLabel, progressLabel, null);

            assertNotNull(blitzInitializer.getBlitzModeManager());
            assertNull(blitzInitializer.getZenModeManager());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitializeRendering() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            GridPane gamePanel = new GridPane();
            GridPane brickPanel = new GridPane();
            GridPane nextBrickPanel = new GridPane();
            GridPane holdBrickPanel = new GridPane();
            Pane parentPane = new Pane();

            int[][] boardMatrix = new int[25][10];
            ViewData viewData = new ViewData(
                    new int[][]{{1, 1}, {1, 1}}, 3, 0,
                    new int[][]{{2, 2}, {2, 2}}, 0, null
            );

            zenInitializer.initializeRendering(gamePanel, brickPanel, nextBrickPanel,
                    holdBrickPanel, parentPane, boardMatrix, viewData);

            assertNotNull(zenInitializer.getRendererCoordinator());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitializeTimer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Label timerLabel = new Label();
            zenInitializer.initializeTimer(timerLabel, () -> {});

            assertNotNull(zenInitializer.getTimerManager());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitializeGameLoop() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            zenInitializer.initializeGameLoop(() -> {});

            assertNotNull(zenInitializer.getLoopManager());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testInitializeEventHandler() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            testEventListener.downEventReturn = new DownData(null, new ViewData(
                    new int[][]{{1}}, 0, 0, new int[][]{{1}}, 0, null
            ));

            zenInitializer.initializeEventHandler(testEventListener);

            assertNotNull(zenInitializer.getEventHandler());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testGetters_ReturnNullBeforeInitialization() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            GameInitializer newInitializer = new GameInitializer(
                    GameMode.ZEN, stateManager, inputHandler, uiManager, soundManager
            );

            assertNull(newInitializer.getTimerManager());
            assertNull(newInitializer.getRendererCoordinator());
            assertNull(newInitializer.getEventHandler());
            assertNull(newInitializer.getLoopManager());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    // Test implementation of InputEventListener
    private static class TestInputEventListener implements InputEventListener {
        DownData downEventReturn = null;
        ViewData leftEventReturn = null;
        ViewData rightEventReturn = null;
        ViewData rotateEventReturn = null;
        DownData hardDropEventReturn = null;
        ViewData holdEventReturn = null;

        @Override
        public DownData onDownEvent(MoveEvent event) {
            return downEventReturn;
        }

        @Override
        public ViewData onLeftEvent(MoveEvent event) {
            return leftEventReturn;
        }

        @Override
        public ViewData onRightEvent(MoveEvent event) {
            return rightEventReturn;
        }

        @Override
        public ViewData onRotateEvent(MoveEvent event) {
            return rotateEventReturn;
        }

        @Override
        public DownData onHardDropEvent(MoveEvent event) {
            return hardDropEventReturn;
        }

        @Override
        public ViewData onHoldEvent(MoveEvent event) {
            return holdEventReturn;
        }

        @Override
        public void createNewGame() {
            // Do nothing
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