package com.comp2042.controller.menu;

import com.comp2042.controller.game.GameController;
import com.comp2042.enums.GameMode;
import com.comp2042.model.scoring.Score;
import com.comp2042.util.SoundManager;
import com.comp2042.view.game.GuiController;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MainMenuController implements Initializable {

    @FXML
    private VBox zenModePanel;

    @FXML
    private VBox blitzModePanel;

    @FXML
    private Label zenHighScoreLabel;

    @FXML
    private Label blitzHighScoreLabel;

    private Preferences prefs;

    private SoundManager soundManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic();

        prefs = Preferences.userNodeForPackage(Score.class);
        loadHighScores();

        setInitialStyles();
        setupHoverEffects();
        setupClickHandlers();
    }

    private void setInitialStyles() {
        // Set zen mode initial border
        zenModePanel.setStyle(
                "-fx-border-color: rgba(122, 162, 247, 0.3); " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-background-radius: 12px;"
        );

        // Set blitz mode initial border
        blitzModePanel.setStyle(
                "-fx-border-color: rgba(247, 118, 142, 0.3); " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-background-radius: 12px;"
        );
    }

    private void loadHighScores() {
        int zenHighScore = prefs.getInt("zen_high_score", 0);
        int blitzHighScore = prefs.getInt("blitz_high_score", 0);

        zenHighScoreLabel.setText(String.format("Best: %d", zenHighScore));
        blitzHighScoreLabel.setText(String.format("Best: %d", blitzHighScore));
    }

    private void setupHoverEffects() {
        zenModePanel.setOnMouseEntered(e -> {
            zenModePanel.setStyle(
                    "-fx-background-color: rgba(122, 162, 247, 0.15); " +
                            "-fx-border-color: #7aa2f7; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 12px; " +
                            "-fx-background-radius: 12px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(122, 162, 247, 0.5), 15, 0.5, 0, 0);"
            );
            animateScale(zenModePanel, 1.0, 1.05);
        });

        zenModePanel.setOnMouseExited(e -> {
            // Keep default border visible when not hovering
            zenModePanel.setStyle(
                    "-fx-border-color: rgba(122, 162, 247, 0.3); " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 12px; " +
                            "-fx-background-radius: 12px;"
            );
            animateScale(zenModePanel, 1.05, 1.0);
        });

        blitzModePanel.setOnMouseEntered(e -> {
            blitzModePanel.setStyle(
                    "-fx-background-color: rgba(247, 118, 142, 0.15); " +
                            "-fx-border-color: #f7768e; " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 12px; " +
                            "-fx-background-radius: 12px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(247, 118, 142, 0.5), 15, 0.5, 0, 0);"
            );
            animateScale(blitzModePanel, 1.0, 1.05);
        });

        blitzModePanel.setOnMouseExited(e -> {
            // Keep default border visible when not hovering
            blitzModePanel.setStyle(
                    "-fx-border-color: rgba(247, 118, 142, 0.3); " +
                            "-fx-border-width: 2px; " +
                            "-fx-border-radius: 12px; " +
                            "-fx-background-radius: 12px;"
            );
            animateScale(blitzModePanel, 1.05, 1.0);
        });
    }

    private void animateScale(VBox panel, double fromScale, double toScale) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), panel);
        scaleTransition.setFromX(fromScale);
        scaleTransition.setFromY(fromScale);
        scaleTransition.setToX(toScale);
        scaleTransition.setToY(toScale);
        scaleTransition.play();
    }

    private void setupClickHandlers() {
        zenModePanel.setOnMouseClicked(e -> startGame(GameMode.ZEN));
        blitzModePanel.setOnMouseClicked(e -> startGame(GameMode.BLITZ));
    }

    private void startGame(GameMode mode) {
        try {
            soundManager.stopBackgroundMusic();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("gameLayout.fxml"));
            Parent root = loader.load();
            GuiController guiController = loader.getController();

            // Create a properly constrained StackPane
            StackPane centeredRoot = new StackPane();
            centeredRoot.setAlignment(javafx.geometry.Pos.CENTER);
            centeredRoot.setStyle("-fx-background-color: #1a1b26;");

            // Force the root to maintain its preferred size
            root.setStyle("-fx-pref-width: 540px; -fx-pref-height: 720px; -fx-max-width: 540px; -fx-max-height: 720px;");

            centeredRoot.getChildren().add(root);

            Stage stage = (Stage) zenModePanel.getScene().getWindow();
            Scene currentScene = stage.getScene();

            // Apply scaling
            currentScene.widthProperty().addListener((obs, oldVal, newVal) -> {
                updateScaling(centeredRoot, currentScene);
            });

            currentScene.heightProperty().addListener((obs, oldVal, newVal) -> {
                updateScaling(centeredRoot, currentScene);
            });

            currentScene.setRoot(centeredRoot);
            updateScaling(centeredRoot, currentScene);

            new GameController(guiController, mode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateScaling(StackPane centeredRoot, Scene scene) {
        double baseWidth = 540;
        double baseHeight = 720;

        double scaleX = scene.getWidth() / baseWidth;
        double scaleY = scene.getHeight() / baseHeight;

        double scale = Math.min(scaleX, scaleY);

        centeredRoot.setScaleX(scale);
        centeredRoot.setScaleY(scale);
        centeredRoot.setPrefSize(scene.getWidth(), scene.getHeight());
    }
}