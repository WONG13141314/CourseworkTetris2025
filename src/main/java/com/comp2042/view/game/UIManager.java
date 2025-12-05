package com.comp2042.view.game;

import com.comp2042.enums.GameMode;
import com.comp2042.view.components.GameOverPanel;
import com.comp2042.view.components.NotificationPanel;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Manages UI elements including labels, panels, and notifications.
 */
public class UIManager {

    private final Label scoreLabel;
    private final Label highScoreLabel;
    private final Label gameModeLabel;
    private final GameOverPanel gameOverPanel;
    private final Group pauseGroup;
    private final Group notificationGroup;
    private final VBox blitzLevelPanel;

    /**
     * Creates a new UI manager.
     * @param scoreLabel score label
     * @param highScoreLabel high score label
     * @param gameModeLabel game mode label
     * @param gameOverPanel game over panel
     * @param pauseGroup pause group
     * @param notificationGroup notification group
     * @param blitzLevelPanel blitz level panel
     */
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

    /**
     * Sets up game mode display.
     * @param mode game mode
     */
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

    /**
     * Updates game mode label.
     * @param mode game mode
     */
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

    /**
     * Binds score property to label.
     * @param scoreProperty score property
     */
    public void bindScore(IntegerProperty scoreProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(scoreProperty.asString("Score: %d"));
        }
    }

    /**
     * Binds high score property to label.
     * @param highScoreProperty high score property
     */
    public void bindHighScore(IntegerProperty highScoreProperty) {
        if (highScoreLabel != null) {
            highScoreLabel.textProperty().bind(highScoreProperty.asString("Best: %d"));
        }
    }

    /**
     * Shows notification.
     * @param text notification text
     */
    public void showNotification(String text) {
        NotificationPanel panel = new NotificationPanel(text);
        notificationGroup.getChildren().add(panel);
        panel.showScore(notificationGroup.getChildren());
    }

    /**
     * Shows or hides pause overlay.
     * @param show whether to show
     */
    public void showPause(boolean show) {
        if (pauseGroup != null) {
            pauseGroup.setVisible(show);
        }
    }

    /**
     * Shows game over screen.
     * @param score final score
     * @param highScore high score
     * @param showScores whether to show scores
     */
    public void showGameOver(int score, int highScore, boolean showScores) {
        gameOverPanel.updateScores(score, highScore, showScores);
        gameOverPanel.setVisible(true);
        if (pauseGroup != null) {
            pauseGroup.setVisible(false);
        }
    }

    /**
     * Hides game over screen.
     */
    public void hideGameOver() {
        gameOverPanel.setVisible(false);
        if (pauseGroup != null) {
            pauseGroup.setVisible(false);
        }
    }
}
