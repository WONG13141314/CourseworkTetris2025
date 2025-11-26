package com.comp2042.controller.lifecycle;

import com.comp2042.controller.game.GameEventHandler;
import com.comp2042.controller.game.GameLoopManager;
import com.comp2042.controller.game.GameStateManager;
import com.comp2042.controller.input.InputHandler;
import com.comp2042.controller.mode.BlitzModeManager;
import com.comp2042.controller.mode.ZenModeManager;
import com.comp2042.enums.GameMode;
import com.comp2042.util.SoundManager;
import com.comp2042.util.TimerManager;
import com.comp2042.view.game.UIManager;
import com.comp2042.view.rendering.GameRendererCoordinator;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameLifecycleManagerTest {

    private GameLifecycleManager zenLifecycleManager;
    private GameLifecycleManager blitzLifecycleManager;
    private GameStateManager stateManager;
    private InputHandler inputHandler;
    private UIManager uiManager;
    private TestSoundManager testSoundManager;
    private TestGameLoopManager testLoopManager;
    private TestTimerManager testTimerManager;
    private TestGameRendererCoordinator testRendererCoordinator;
    private TestGameEventHandler testEventHandler;

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
            testSoundManager = new TestSoundManager();

            // Create UI components
            Label scoreLabel = new Label();
            Label highScoreLabel = new Label();
            Label gameModeLabel = new Label();
            com.comp2042.view.components.GameOverPanel gameOverPanel =
                    new com.comp2042.view.components.GameOverPanel();
            Group pauseGroup = new Group();
            Group notificationGroup = new Group();
            VBox blitzLevelPanel = new VBox();

            uiManager = new UIManager(scoreLabel, highScoreLabel, gameModeLabel,
                    gameOverPanel, pauseGroup, notificationGroup, blitzLevelPanel);

            zenLifecycleManager = new GameLifecycleManager(GameMode.ZEN, stateManager,
                    inputHandler, uiManager, testSoundManager);

            blitzLifecycleManager = new GameLifecycleManager(GameMode.BLITZ, stateManager,
                    inputHandler, uiManager, testSoundManager);

            // Create test doubles
            testLoopManager = new TestGameLoopManager();
            testTimerManager = new TestTimerManager();
            testRendererCoordinator = new TestGameRendererCoordinator();
            testEventHandler = new TestGameEventHandler();

            // Set mock components
            zenLifecycleManager.setComponents(testLoopManager, testTimerManager,
                    testRendererCoordinator, testEventHandler, null, null);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testTogglePause_WhenNotPaused_Pauses() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            zenLifecycleManager.togglePause();

            assertTrue(stateManager.isPaused(), "Should be paused after toggle");
            assertTrue(testLoopManager.stopCalled, "Loop manager stop should be called");
            assertTrue(testTimerManager.pauseCalled, "Timer manager pause should be called");
            assertTrue(testSoundManager.pauseBackgroundMusicCalled,
                    "Pause background music should be called");

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testTogglePause_WhenPaused_Resumes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            // First pause
            zenLifecycleManager.togglePause();
            // Then resume
            zenLifecycleManager.togglePause();

            assertFalse(stateManager.isPaused(), "Should be resumed after second toggle");
            assertTrue(testLoopManager.startCalled, "Loop manager start should be called");
            assertTrue(testTimerManager.resumeCalled, "Timer manager resume should be called");
            assertTrue(testSoundManager.resumeBackgroundMusicCalled,
                    "Resume background music should be called");

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testHandleGameOver_StopsAllSystems() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            testEventHandler.currentScore = 1000;
            testEventHandler.currentHighScore = 2000;

            zenLifecycleManager.handleGameOver();

            assertTrue(stateManager.isGameOver(), "Should be game over");
            assertTrue(testLoopManager.stopCalled, "Loop manager stop should be called");
            assertTrue(testTimerManager.stopCalled, "Timer manager stop should be called");
            assertTrue(testRendererCoordinator.setVisibleCalled,
                    "Renderer setVisible should be called");
            assertFalse(testRendererCoordinator.lastVisibleState,
                    "Renderer should be set to invisible");
            assertTrue(testSoundManager.stopBackgroundMusicCalled,
                    "Stop background music should be called");

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testStartNewGame_ResetsAllSystems() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            // Set game over state first
            stateManager.setGameOver(true);

            zenLifecycleManager.startNewGame();

            assertFalse(stateManager.isGameOver(), "Should not be game over");
            assertFalse(stateManager.isPaused(), "Should not be paused");

            assertTrue(testLoopManager.stopCalled, "Loop manager stop should be called");
            assertTrue(testLoopManager.startCalled, "Loop manager start should be called");
            assertTrue(testTimerManager.resetCalled, "Timer manager reset should be called");
            assertTrue(testTimerManager.startCalled, "Timer manager start should be called");
            assertTrue(testRendererCoordinator.setVisibleCalled,
                    "Renderer setVisible should be called");
            assertTrue(testEventHandler.createNewGameCalled,
                    "Event handler createNewGame should be called");
            assertTrue(testSoundManager.playBackgroundMusicCalled,
                    "Play background music should be called");

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testStartNewGame_WithBlitzMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Label levelLabel = new Label();
            Label progressLabel = new Label();
            BlitzModeManager blitzManager = new BlitzModeManager(levelLabel, progressLabel);

            TestGameLoopManager newLoopManager = new TestGameLoopManager();
            TestTimerManager newTimerManager = new TestTimerManager();
            TestGameRendererCoordinator newRendererCoordinator = new TestGameRendererCoordinator();
            TestGameEventHandler newEventHandler = new TestGameEventHandler();

            blitzLifecycleManager.setComponents(newLoopManager, newTimerManager,
                    newRendererCoordinator, newEventHandler, blitzManager, null);

            blitzLifecycleManager.startNewGame();

            assertTrue(newLoopManager.startCalled, "Loop manager start should be called");
            assertTrue(testSoundManager.playBackgroundMusicCalled,
                    "Play background music should be called");

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testStartNewGame_WithZenMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Label timerLabel = new Label();
            ZenModeManager zenManager = new ZenModeManager(timerLabel);

            TestGameLoopManager newLoopManager = new TestGameLoopManager();
            TestTimerManager newTimerManager = new TestTimerManager();
            TestGameRendererCoordinator newRendererCoordinator = new TestGameRendererCoordinator();
            TestGameEventHandler newEventHandler = new TestGameEventHandler();

            zenLifecycleManager.setComponents(newLoopManager, newTimerManager,
                    newRendererCoordinator, newEventHandler, null, zenManager);

            zenLifecycleManager.startNewGame();

            assertTrue(newEventHandler.createNewGameCalled,
                    "Event handler createNewGame should be called");
            assertTrue(testSoundManager.playBackgroundMusicCalled,
                    "Play background music should be called");

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testHandleGameOver_DisplaysScores() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            testEventHandler.currentScore = 5000;
            testEventHandler.currentHighScore = 10000;

            zenLifecycleManager.handleGameOver();

            assertTrue(testEventHandler.getCurrentScoreCalled,
                    "getCurrentScore should be called");
            assertTrue(testEventHandler.getCurrentHighScoreCalled,
                    "getCurrentHighScore should be called");
            assertTrue(testSoundManager.stopBackgroundMusicCalled,
                    "Stop background music should be called");

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultiplePauseResumeCycles() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            // Pause
            zenLifecycleManager.togglePause();
            assertTrue(stateManager.isPaused());

            // Resume
            zenLifecycleManager.togglePause();
            assertFalse(stateManager.isPaused());

            // Pause again
            zenLifecycleManager.togglePause();
            assertTrue(stateManager.isPaused());

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    // Test implementations
    private static class TestSoundManager extends SoundManager {
        boolean playBackgroundMusicCalled = false;
        boolean stopBackgroundMusicCalled = false;
        boolean pauseBackgroundMusicCalled = false;
        boolean resumeBackgroundMusicCalled = false;
        boolean playGameOverMusicCalled = false;
        boolean stopGameOverMusicCalled = false;

        @Override
        public void playBackgroundMusic() {
            playBackgroundMusicCalled = true;
        }

        @Override
        public void stopBackgroundMusic() {
            stopBackgroundMusicCalled = true;
        }

        @Override
        public void pauseBackgroundMusic() {
            pauseBackgroundMusicCalled = true;
        }

        @Override
        public void resumeBackgroundMusic() {
            resumeBackgroundMusicCalled = true;
        }

        @Override
        public void playGameOverMusic() {
            playGameOverMusicCalled = true;
        }

        @Override
        public void stopGameOverMusic() {
            stopGameOverMusicCalled = true;
        }
    }

    private static class TestGameLoopManager extends GameLoopManager {
        boolean startCalled = false;
        boolean stopCalled = false;

        public TestGameLoopManager() {
            super(GameMode.ZEN);
        }

        @Override
        public void start() {
            startCalled = true;
        }

        @Override
        public void stop() {
            stopCalled = true;
        }
    }

    private static class TestTimerManager extends TimerManager {
        boolean startCalled = false;
        boolean stopCalled = false;
        boolean pauseCalled = false;
        boolean resumeCalled = false;
        boolean resetCalled = false;

        public TestTimerManager() {
            super(GameMode.ZEN, new Label());
        }

        @Override
        public void start() {
            startCalled = true;
        }

        @Override
        public void stop() {
            stopCalled = true;
        }

        @Override
        public void pause() {
            pauseCalled = true;
        }

        @Override
        public void resume() {
            resumeCalled = true;
        }

        @Override
        public void reset() {
            resetCalled = true;
        }
    }

    private static class TestGameRendererCoordinator extends GameRendererCoordinator {
        boolean setVisibleCalled = false;
        boolean lastVisibleState = false;

        public TestGameRendererCoordinator() {
            super(new GridPane(), new GridPane(), new GridPane(), new GridPane());
        }

        @Override
        public void setVisible(boolean visible) {
            setVisibleCalled = true;
            lastVisibleState = visible;
        }
    }

    private static class TestGameEventHandler extends GameEventHandler {
        int currentScore = 0;
        int currentHighScore = 0;
        boolean getCurrentScoreCalled = false;
        boolean getCurrentHighScoreCalled = false;
        boolean createNewGameCalled = false;

        public TestGameEventHandler() {
            super(new TestInputEventListener(), GameMode.ZEN);
        }

        @Override
        public int getCurrentScore() {
            getCurrentScoreCalled = true;
            return currentScore;
        }

        @Override
        public int getCurrentHighScore() {
            getCurrentHighScoreCalled = true;
            return currentHighScore;
        }

        @Override
        public void createNewGame() {
            createNewGameCalled = true;
        }
    }

    private static class TestInputEventListener implements com.comp2042.controller.input.InputEventListener {
        @Override
        public com.comp2042.model.data.DownData onDownEvent(com.comp2042.model.data.MoveEvent event) { return null; }
        @Override
        public com.comp2042.model.data.ViewData onLeftEvent(com.comp2042.model.data.MoveEvent event) { return null; }
        @Override
        public com.comp2042.model.data.ViewData onRightEvent(com.comp2042.model.data.MoveEvent event) { return null; }
        @Override
        public com.comp2042.model.data.ViewData onRotateEvent(com.comp2042.model.data.MoveEvent event) { return null; }
        @Override
        public com.comp2042.model.data.DownData onHardDropEvent(com.comp2042.model.data.MoveEvent event) { return null; }
        @Override
        public com.comp2042.model.data.ViewData onHoldEvent(com.comp2042.model.data.MoveEvent event) { return null; }
        @Override
        public void createNewGame() {}
        @Override
        public int getCurrentScore() { return 0; }
        @Override
        public int getCurrentHighScore() { return 0; }
    }
}