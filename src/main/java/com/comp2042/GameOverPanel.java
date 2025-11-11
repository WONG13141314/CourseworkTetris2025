package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class GameOverPanel extends BorderPane {

    public GameOverPanel() {
        getStyleClass().add("game-over-container");

        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");

        final Label restartLabel = new Label("Press 'N' for New Game");
        restartLabel.setStyle("-fx-text-fill: #a9b1d6; -fx-font-size: 12px; -fx-font-family: 'Segoe UI';");

        VBox container = new VBox(8);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(gameOverLabel, restartLabel);

        setCenter(container);
    }
}