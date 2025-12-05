package com.comp2042.view.game;

import com.comp2042.util.SoundManager;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Handles navigation between game scenes.
 */
public class MenuNavigator {

    private final SoundManager soundManager;
    private Runnable onReturnToMenuCleanup;

    /**
     * Creates a new menu navigator.
     * @param soundManager sound manager
     */
    public MenuNavigator(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    /**
     * Sets cleanup callback for returning to menu.
     * @param callback cleanup callback
     */
    public void setOnReturnToMenuCleanup(Runnable callback) {
        this.onReturnToMenuCleanup = callback;
    }

    /**
     * Returns to main menu.
     * @param stage current stage
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

            StackPane centeredRoot = new StackPane();
            centeredRoot.setAlignment(Pos.CENTER);
            centeredRoot.setStyle("-fx-background-color: #1a1b26;");

            root.setStyle("-fx-pref-width: 540px; -fx-pref-height: 720px; -fx-max-width: 540px; -fx-max-height: 720px;");

            centeredRoot.getChildren().add(root);

            Scene currentScene = stage.getScene();

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

    /**
     * Updates scaling for window resize.
     * @param centeredRoot centered root pane
     * @param scene scene
     */
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