package com.comp2042.view.game;

import com.comp2042.util.SoundManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Handles navigation between scenes
 */
public class MenuNavigator {

    private final SoundManager soundManager;

    public MenuNavigator(SoundManager soundManager) {
        this.soundManager = soundManager;
    }

    /**
     * Return to main menu
     */
    public void returnToMainMenu(Stage stage) {
        try {
            soundManager.stopBackgroundMusic();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getClassLoader().getResource("mainMenu.fxml")
            );
            Parent root = loader.load();
            stage.setScene(new Scene(root, 540, 720));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}