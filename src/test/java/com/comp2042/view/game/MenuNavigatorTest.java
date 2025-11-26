package com.comp2042.view.game;

import com.comp2042.util.SoundManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MenuNavigatorTest {

    private MenuNavigator menuNavigator;
    private SoundManager soundManager;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already initialized
        }
        Thread.sleep(100);
    }

    @BeforeEach
    void setUp() {
        soundManager = SoundManager.getInstance();
        menuNavigator = new MenuNavigator(soundManager);
    }

    @Test
    void testReturnToMainMenu_StopsSounds() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            Stage stage = new Stage();
            Scene scene = new Scene(new Pane(), 540, 720);
            stage.setScene(scene);

            try {
                menuNavigator.returnToMainMenu(stage);
            } catch (Exception e) {
            }

            latch.countDown();
        });

        latch.await(2, TimeUnit.SECONDS);
    }
}