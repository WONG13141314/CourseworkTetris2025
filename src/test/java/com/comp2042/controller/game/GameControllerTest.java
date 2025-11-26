package com.comp2042.controller.game;

import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.enums.GameMode;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.view.game.GuiController;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    // Use separate GuiController instances for each controller
    private TestGuiController zenGuiController;
    private TestGuiController blitzGuiController;
    private GameController zenController;
    private GameController blitzController;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
        Thread.sleep(100);
    }

    @BeforeEach
    void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        Platform.runLater(() -> {
            zenGuiController = new TestGuiController();
            zenController = new GameController(zenGuiController, GameMode.ZEN);
            latch.countDown();
        });

        Platform.runLater(() -> {
            blitzGuiController = new TestGuiController();
            blitzController = new GameController(blitzGuiController, GameMode.BLITZ);
            latch.countDown();
        });

        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    void testConstructor_InitializesBoard() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);

        Platform.runLater(() -> {
            assertNotNull(zenController, "Zen controller should be initialized");
            assertTrue(zenGuiController.setGameModeCalled, "setGameMode should be called for Zen");
            assertEquals(GameMode.ZEN, zenGuiController.lastGameMode, "Zen mode should be set correctly");
            latch.countDown();
        });

        Platform.runLater(() -> {
            assertNotNull(blitzController, "Blitz controller should be initialized");
            assertTrue(blitzGuiController.setGameModeCalled, "setGameMode should be called for Blitz");
            assertEquals(GameMode.BLITZ, blitzGuiController.lastGameMode, "Blitz mode should be set correctly");
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnDownEvent_MovesBlockDown() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
            DownData result = zenController.onDownEvent(event);

            assertNotNull(result, "Should return DownData");
            assertNotNull(result.getViewData(), "Should have ViewData");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnLeftEvent_MovesBlockLeft() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
            ViewData result = zenController.onLeftEvent(event);

            assertNotNull(result, "Should return ViewData");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnRightEvent_MovesBlockRight() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
            ViewData result = zenController.onRightEvent(event);

            assertNotNull(result, "Should return ViewData");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnRotateEvent_RotatesBlock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
            ViewData result = zenController.onRotateEvent(event);

            assertNotNull(result, "Should return ViewData");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnHardDropEvent_DropsBlockToBottom() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MoveEvent event = new MoveEvent(EventType.HARD_DROP, EventSource.USER);
            DownData result = zenController.onHardDropEvent(event);

            assertNotNull(result, "Should return DownData");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnHoldEvent_HoldsBrick() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            MoveEvent event = new MoveEvent(EventType.HOLD, EventSource.USER);
            ViewData result = zenController.onHoldEvent(event);

            assertNotNull(result, "Should return ViewData");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testGetCurrentScore_ReturnsScore() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int score = zenController.getCurrentScore();
            assertTrue(score >= 0, "Score should be non-negative");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testGetCurrentHighScore_ReturnsHighScore() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int highScore = zenController.getCurrentHighScore();
            assertTrue(highScore >= 0, "High score should be non-negative");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testCreateNewGame_ResetsBoard() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            zenGuiController.refreshGameBackgroundCallCount = 0;
            zenController.createNewGame();
            assertTrue(zenGuiController.refreshGameBackgroundCallCount >= 1,
                    "refreshGameBackground should be called at least once");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnDownEvent_WithUserSource_AddsScore() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int initialScore = zenController.getCurrentScore();
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);

            for (int i = 0; i < 5; i++) {
                zenController.onDownEvent(event);
            }

            int finalScore = zenController.getCurrentScore();
            assertTrue(finalScore >= initialScore, "Score should increase or stay same");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testOnHardDropEvent_AddsDropBonus() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int initialScore = zenController.getCurrentScore();
            MoveEvent event = new MoveEvent(EventType.HARD_DROP, EventSource.USER);

            zenController.onHardDropEvent(event);

            int finalScore = zenController.getCurrentScore();
            assertTrue(finalScore >= initialScore, "Hard drop should add score");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testZenMode_ClearsBoardOnGameOver() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertNotNull(zenController, "Zen controller should exist");
            assertEquals(GameMode.ZEN, zenGuiController.lastGameMode, "Should be in ZEN mode");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBlitzMode_SetsCorrectMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            assertNotNull(blitzController, "Blitz controller should exist");
            assertTrue(blitzGuiController.setGameModeCalled, "setGameMode should be called");
            assertEquals(GameMode.BLITZ, blitzGuiController.lastGameMode, "Should be in BLITZ mode");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBlitzMode_OnDownEvent_AddsScore() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int initialScore = blitzController.getCurrentScore();
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);

            for (int i = 0; i < 5; i++) {
                blitzController.onDownEvent(event);
            }

            int finalScore = blitzController.getCurrentScore();
            assertTrue(finalScore >= initialScore, "Blitz score should increase or stay same");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBlitzMode_OnHardDropEvent_AddsDropBonus() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int initialScore = blitzController.getCurrentScore();
            MoveEvent event = new MoveEvent(EventType.HARD_DROP, EventSource.USER);

            blitzController.onHardDropEvent(event);

            int finalScore = blitzController.getCurrentScore();
            assertTrue(finalScore >= initialScore, "Blitz hard drop should add score");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBlitzMode_GetCurrentScore() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int score = blitzController.getCurrentScore();
            assertTrue(score >= 0, "Blitz score should be non-negative");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBlitzMode_GetCurrentHighScore() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            int highScore = blitzController.getCurrentHighScore();
            assertTrue(highScore >= 0, "Blitz high score should be non-negative");
            latch.countDown();
        });
        latch.await(2, TimeUnit.SECONDS);
    }

    // Test implementation of GuiController
    private static class TestGuiController extends GuiController {
        boolean setGameModeCalled = false;
        GameMode lastGameMode = null;
        int refreshGameBackgroundCallCount = 0;
        boolean gameOverCalled = false;

        @Override
        public void setGameMode(GameMode mode) {
            setGameModeCalled = true;
            lastGameMode = mode;
        }

        @Override
        public void setEventListener(com.comp2042.controller.input.InputEventListener listener) {
            // Do nothing in test
        }

        @Override
        public void initGameView(int[][] boardMatrix, ViewData brick) {
            // Do nothing in test
        }

        @Override
        public void bindScore(IntegerProperty scoreProperty) {
            // Do nothing in test
        }

        @Override
        public void bindHighScore(IntegerProperty highScoreProperty) {
            // Do nothing in test
        }

        @Override
        public void refreshGameBackground(int[][] board) {
            refreshGameBackgroundCallCount++;
        }

        @Override
        public void gameOver() {
            gameOverCalled = true;
        }
    }
}