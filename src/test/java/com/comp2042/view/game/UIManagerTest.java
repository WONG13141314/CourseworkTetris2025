package com.comp2042.view.game;

import com.comp2042.enums.GameMode;
import com.comp2042.view.components.GameOverPanel;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class UIManagerTest {

    private UIManager uiManager;
    private Label scoreLabel;
    private Label highScoreLabel;
    private Label gameModeLabel;
    private GameOverPanel gameOverPanel;
    private Group pauseGroup;
    private Group notificationGroup;
    private VBox blitzLevelPanel;

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
            scoreLabel = new Label();
            highScoreLabel = new Label();
            gameModeLabel = new Label();
            gameOverPanel = new GameOverPanel();
            pauseGroup = new Group();
            notificationGroup = new Group();
            blitzLevelPanel = new VBox();

            uiManager = new UIManager(scoreLabel, highScoreLabel, gameModeLabel,
                    gameOverPanel, pauseGroup, notificationGroup, blitzLevelPanel);

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testConstructor_InitializesComponents() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            assertNotNull(uiManager);
            assertFalse(gameOverPanel.isVisible());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetupGameMode_ZenMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            uiManager.setupGameMode(GameMode.ZEN);

            assertFalse(blitzLevelPanel.isVisible());
            assertEquals("ZEN MODE", gameModeLabel.getText());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testSetupGameMode_BlitzMode() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            uiManager.setupGameMode(GameMode.BLITZ);

            assertTrue(blitzLevelPanel.isVisible());
            assertEquals("BLITZ MODE", gameModeLabel.getText());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBindScore() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            IntegerProperty scoreProperty = new SimpleIntegerProperty(0);
            uiManager.bindScore(scoreProperty);

            scoreProperty.set(100);
            assertEquals("Score: 100", scoreLabel.getText());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testBindHighScore() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            IntegerProperty highScoreProperty = new SimpleIntegerProperty(0);
            uiManager.bindHighScore(highScoreProperty);

            highScoreProperty.set(500);
            assertEquals("Best: 500", highScoreLabel.getText());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testShowNotification() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            uiManager.showNotification("+100");
            assertEquals(1, notificationGroup.getChildren().size());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testShowPause() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            uiManager.showPause(true);
            assertTrue(pauseGroup.isVisible());

            uiManager.showPause(false);
            assertFalse(pauseGroup.isVisible());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testShowGameOver() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            uiManager.showGameOver(1000, 5000, true);

            assertTrue(gameOverPanel.isVisible());
            assertFalse(pauseGroup.isVisible());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testHideGameOver() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            uiManager.showGameOver(1000, 5000, true);
            uiManager.hideGameOver();

            assertFalse(gameOverPanel.isVisible());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }

    @Test
    void testMultipleNotifications() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            uiManager.showNotification("+50");
            uiManager.showNotification("+100");
            uiManager.showNotification("+200");

            assertEquals(3, notificationGroup.getChildren().size());
            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}