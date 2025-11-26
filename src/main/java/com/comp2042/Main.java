package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL location = getClass().getClassLoader().getResource("mainMenu.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();

        // Create main container with background
        StackPane mainContainer = new StackPane();
        mainContainer.setStyle("-fx-background-color: #1a1b26;");

        // Create centered content container
        StackPane centeredRoot = new StackPane();
        centeredRoot.setAlignment(Pos.CENTER);

        // Force the root to maintain its preferred size
        root.setStyle("-fx-pref-width: 540px; -fx-pref-height: 720px; -fx-max-width: 540px; -fx-max-height: 720px;");

        centeredRoot.getChildren().add(root);
        mainContainer.getChildren().add(centeredRoot);

        Scene scene = new Scene(mainContainer, 540, 720); // Set initial window size

        // Scale the centered content when window is resized
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            updateScaling(centeredRoot, scene);
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            updateScaling(centeredRoot, scene);
        });

        primaryStage.setTitle("TetrisJFX");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true); // Allow resizing but don't start maximized

        // Set minimum window size to prevent the window from being too small
        primaryStage.setMinWidth(540);
        primaryStage.setMinHeight(700);

        primaryStage.show();

        updateScaling(centeredRoot, scene);
    }

    private void updateScaling(StackPane centeredRoot, Scene scene) {
        double baseWidth = 540;
        double baseHeight = 720;

        double scaleX = scene.getWidth() / baseWidth;
        double scaleY = scene.getHeight() / baseHeight;

        // Use the smaller scale to maintain aspect ratio
        double scale = Math.min(scaleX, scaleY);

        // Apply scaling to the centered content only
        centeredRoot.setScaleX(scale);
        centeredRoot.setScaleY(scale);
    }

    public static void main(String[] args) {
        launch(args);
    }
}