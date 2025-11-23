package com.comp2042.view.game;

import com.comp2042.enums.GameMode;
import com.comp2042.view.components.GameOverPanel;
import com.comp2042.view.components.NotificationPanel;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Manages UI elements (labels, panels, notifications)
 */
public class UIManager {

    private final Label scoreLabel;
    private final Label highScoreLabel;
    private final Label gameModeLabel;
    private final GameOverPanel gameOverPanel;
    private final Group pauseGroup;
    private final Group notificationGroup;
    private final VBox blitzLevelPanel;

    public UIManager(Label scoreLabel, Label highScoreLabel, Label gameModeLabel,
                     GameOverPanel gameOverPanel, Group pauseGroup,
                     Group notificationGroup, VBox blitzLevelPanel) {
        this.scoreLabel = scoreLabel;
        this.highScoreLabel = highScoreLabel;
        this.gameModeLabel = gameModeLabel;
        this.gameOverPanel = gameOverPanel;
        this.pauseGroup = pauseGroup;
        this.notificationGroup = notificationGroup;
        this.blitzLevelPanel = blitzLevelPanel;

        gameOverPanel.setVisible(false);
    }

    public void setupGameMode(GameMode mode) {
        if (mode == GameMode.BLITZ) {
            if (blitzLevelPanel != null) {
                blitzLevelPanel.setVisible(true);
                blitzLevelPanel.setManaged(true);
            }
        } else {
            if (blitzLevelPanel != null) {
                blitzLevelPanel.setVisible(false);
                blitzLevelPanel.setManaged(false);
            }
        }

        updateGameModeLabel(mode);
    }

    private void updateGameModeLabel(GameMode mode) {
        if (gameModeLabel != null) {
            gameModeLabel.setText(mode == GameMode.ZEN ? "ZEN MODE" : "BLITZ MODE");
            if (mode == GameMode.BLITZ) {
                gameModeLabel.setStyle("-fx-text-fill: #f7768e;");
            } else {
                gameModeLabel.setStyle("-fx-text-fill: #7aa2f7;");
            }
        }
    }

    public void bindScore(IntegerProperty scoreProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
        }
    }

    public void bindHighScore(IntegerProperty highScoreProperty) {
        if (highScoreLabel != null) {
            highScoreLabel.textProperty().bind(highScoreProperty.asString("Best: %d"));
        }
    }

    public void showNotification(String text) {
        NotificationPanel panel = new NotificationPanel(text);
        notificationGroup.getChildren().add(panel);
        panel.showScore(notificationGroup.getChildren());
    }

    public void showPause(boolean show) {
        if (pauseGroup != null) {
            pauseGroup.setVisible(show);
        }
    }

    public void showGameOver(int score, int highScore, boolean showScores) {
        gameOverPanel.updateScores(score, highScore, showScores);
        gameOverPanel.setVisible(true);
        if (pauseGroup != null) {
            pauseGroup.setVisible(false);
        }
    }

    public void hideGameOver() {
        gameOverPanel.setVisible(false);
        if (pauseGroup != null) {
            pauseGroup.setVisible(false);
        }
    }
}