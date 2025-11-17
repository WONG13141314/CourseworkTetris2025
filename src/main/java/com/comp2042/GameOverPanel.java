package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class GameOverPanel extends BorderPane {

    private final Label scoreLabel;
    private final Label highScoreLabel;

    public GameOverPanel() {
        getStyleClass().add("game-over-container");

        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        scoreLabel = new Label();
        scoreLabel.getStyleClass().add("game-over-score");

        highScoreLabel = new Label();
        highScoreLabel.getStyleClass().add("game-over-highscore");

        final Label restartLabel = new Label("Press 'N' to Restart  |  Press 'ESC' for Main Menu");
        restartLabel.setStyle("-fx-text-fill: #a9b1d6; -fx-font-size: 12px; -fx-font-family: 'Segoe UI';");

        VBox container = new VBox(8);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(gameOverLabel, scoreLabel, highScoreLabel, restartLabel);

        setCenter(container);
    }

    public void updateScores(int score, int highScore, boolean showScores) {
        if (showScores) {
            scoreLabel.setText("SCORE: " + score);
            highScoreLabel.setText("BEST: " + highScore);
            scoreLabel.setVisible(true);
            highScoreLabel.setVisible(true);
        } else {
            scoreLabel.setVisible(false);
            highScoreLabel.setVisible(false);
        }
    }
}