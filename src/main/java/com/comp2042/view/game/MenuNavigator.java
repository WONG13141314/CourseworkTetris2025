package com.comp2042.view.game;

import com.comp2042.util.SoundManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Handles navigation between scenes
 */
public class MenuNavigator {

    private final SoundManager soundManager;
    private Runnable onReturnToMenuCleanup;

    public MenuNavigator(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    /**
     * Set cleanup callback to be called before returning to menu
     */
    public void setOnReturnToMenuCleanup(Runnable callback) {
        this.onReturnToMenuCleanup = callback;
    }

    /**
     * Return to main menu
     */
    public void returnToMainMenu(Stage stage) {
        try {
            if (onReturnToMenuCleanup != null) {
                onReturnToMenuCleanup.run();
            }

            soundManager.stopBackgroundMusic();
            soundManager.stopGameOverMusic();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("mainMenu.fxml")
            );
            Parent root = loader.load();

            // Create a properly constrained StackPane
            StackPane centeredRoot = new StackPane();
            centeredRoot.setAlignment(Pos.CENTER);
            centeredRoot.setStyle("-fx-background-color: #1a1b26;");

            // Force the root to maintain its preferred size
            root.setStyle("-fx-pref-width: 540px; -fx-pref-height: 720px; -fx-max-width: 540px; -fx-max-height: 720px;");

            centeredRoot.getChildren().add(root);

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateScaling(StackPane centeredRoot, Scene scene) {
        double baseWidth = 540;
        double baseHeight = 720;

        double scaleX = scene.getWidth() / baseWidth;
        double scaleY = scene.getHeight() / baseHeight;

        // Use the smaller scale to maintain aspect ratio
        double scale = Math.min(scaleX, scaleY);

        centeredRoot.setScaleX(scale);
        centeredRoot.setScaleY(scale);
        centeredRoot.setPrefSize(scene.getWidth(), scene.getHeight());
    }
}