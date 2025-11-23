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

        setupHoverEffects();
        setupClickHandlers();
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
            zenModePanel.setStyle("");
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
            blitzModePanel.setStyle("");
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

            Stage stage = (Stage) zenModePanel.getScene().getWindow();
            Scene scene = new Scene(root, 540, 720);
            stage.setScene(scene);

            new GameController(guiController, mode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}