package com.comp2042.view;

import com.comp2042.controller.*;
import com.comp2042.controller.input.InputEventListener;
import com.comp2042.controller.input.InputHandler;
import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.enums.GameMode;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import com.comp2042.util.SoundManager;
import com.comp2042.util.TimerManager;
import com.comp2042.view.components.GameOverPanel;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    @FXML private GridPane gamePanel;
    @FXML private Group groupNotification;
    @FXML private Group pauseGroup;
    @FXML private GridPane brickPanel;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Label scoreLabel;
    @FXML private GridPane nextBrickPanel;
    @FXML private GridPane holdBrickPanel;
    @FXML private Label highScoreLabel;
    @FXML private Label timerLabel;
    @FXML private Label gameModeLabel;
    @FXML private Label blitzLevelLabel;
    @FXML private Label blitzProgressLabel;
    @FXML private VBox blitzLevelPanel;

    private GameMode gameMode;
    private GameStateManager stateManager;
    private InputHandler inputHandler;
    private TimerManager timerManager;
    private GameRendererCoordinator rendererCoordinator;
    private UIManager uiManager;
    private GameEventHandler eventHandler;
    private GameLoopManager loopManager;
    private BlitzModeManager blitzModeManager;
    private SoundManager soundManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic();
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        stateManager = new GameStateManager();
        inputHandler = new InputHandler();
        uiManager = new UIManager(scoreLabel, highScoreLabel, gameModeLabel,
                gameOverPanel, pauseGroup, groupNotification, blitzLevelPanel);

        setupInputHandling();
    }

    private void setupInputHandling() {
        inputHandler.setCallback(new InputHandler.InputCallback() {
            @Override
            public void onMove(MoveEvent event) {
                if (stateManager.canProcessInput()) {
                    if (event.getEventType() == EventType.DOWN) {
                        DownData downData = eventHandler.handleDown(event);
                        if (downData != null && downData.getViewData() != null) {
                            rendererCoordinator.refreshBrick(downData.getViewData());
                        }
                    } else {
                        ViewData vd = eventHandler.handleMove(event);
                        if (vd != null) {
                            rendererCoordinator.refreshBrick(vd);
                        }
                    }
                }
                gamePanel.requestFocus();
            }

            @Override
            public void onRotate(MoveEvent event) {
                if (stateManager.canProcessInput()) {
                    ViewData vd = eventHandler.handleRotate(event);
                    if (vd != null) {
                        rendererCoordinator.refreshBrick(vd);
                    }
                }
                gamePanel.requestFocus();
            }

            @Override
            public void onHardDrop(MoveEvent event) {
                if (stateManager.canProcessInput()) {
                    DownData downData = eventHandler.handleHardDrop(event);
                    if (downData != null && downData.getViewData() != null) {
                        rendererCoordinator.refreshBrick(downData.getViewData());
                    }
                }
                gamePanel.requestFocus();
            }

            @Override
            public void onHold(MoveEvent event) {
                if (stateManager.canProcessInput()) {
                    ViewData vd = eventHandler.handleHold(event);
                    if (vd != null) {
                        rendererCoordinator.refreshBrick(vd);
                    }
                }
                gamePanel.requestFocus();
            }

            @Override
            public void onPause() {
                pauseGame();
            }

            @Override
            public void onNewGame() {
                newGame();
            }

            @Override
            public void onReturnToMenu() {
                returnToMenu();
            }
        });

        gamePanel.setOnKeyPressed(e -> inputHandler.handleKeyPressed(e, stateManager.isPaused(), stateManager.isGameOver()));
        gamePanel.setOnKeyReleased(e -> inputHandler.handleKeyReleased(e));
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
        uiManager.setupGameMode(mode);

        if (mode == GameMode.BLITZ) {
            blitzModeManager = new BlitzModeManager(blitzLevelLabel, blitzProgressLabel);
        }
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        rendererCoordinator = new GameRendererCoordinator(gamePanel, brickPanel, nextBrickPanel, holdBrickPanel);
        rendererCoordinator.initialize(boardMatrix, brick, (Pane) gamePanel.getParent());

        timerManager = new TimerManager(gameMode, timerLabel);
        timerManager.setOnBlitzTimeUp(this::gameOver);
        timerManager.start();

        loopManager = new GameLoopManager(gameMode);
        loopManager.setBlitzModeManager(blitzModeManager);
        loopManager.setOnDropTick(() -> {
            if (stateManager.canProcessInput()) {
                DownData downData = eventHandler.handleDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                if (downData != null && downData.getViewData() != null) {
                    rendererCoordinator.refreshBrick(downData.getViewData());
                }
            }
        });

        if (blitzModeManager != null) {
            blitzModeManager.setOnLevelUp(() -> loopManager.updateSpeed());
        }

        loopManager.start();
    }

    public void setEventListener(InputEventListener listener) {
        eventHandler = new GameEventHandler(listener, gameMode);
        eventHandler.setBlitzModeManager(blitzModeManager);
        eventHandler.setOnNotification(text -> uiManager.showNotification(text));
    }

    public void bindScore(IntegerProperty score) {
        uiManager.bindScore(score);
    }

    public void bindHighScore(IntegerProperty highScore) {
        uiManager.bindHighScore(highScore);
    }

    public void refreshGameBackground(int[][] board) {
        rendererCoordinator.refreshBoard(board);
    }

    private void pauseGame() {
        if (!stateManager.isPaused()) {
            loopManager.stop();
            timerManager.pause();
            soundManager.pauseBackgroundMusic();
            stateManager.setPaused(true);
            uiManager.showPause(true);
        } else {
            loopManager.start();
            timerManager.resume();
            soundManager.resumeBackgroundMusic();
            stateManager.setPaused(false);
            uiManager.showPause(false);
        }
        gamePanel.requestFocus();
    }

    public void gameOver() {
        loopManager.stop();
        timerManager.stop();
        inputHandler.stopAllTimers();
        soundManager.stopBackgroundMusic();

        uiManager.showGameOver(eventHandler.getCurrentScore(),
                eventHandler.getCurrentHighScore(),
                gameMode == GameMode.BLITZ);

        stateManager.setGameOver(true);
        stateManager.setPaused(false);
        rendererCoordinator.setVisible(false);
        inputHandler.reset();
    }

    public void newGame() {
        loopManager.stop();
        inputHandler.stopAllTimers();
        uiManager.hideGameOver();
        rendererCoordinator.setVisible(true);
        inputHandler.reset();

        soundManager.stopBackgroundMusic();
        soundManager.playBackgroundMusic();

        stateManager.reset();

        if (blitzModeManager != null) {
            blitzModeManager.reset();
        }

        timerManager.reset();
        timerManager.start();
        loopManager.start();

        eventHandler.createNewGame();
        gamePanel.requestFocus();
    }

    private void returnToMenu() {
        try {
            loopManager.stop();
            timerManager.stop();
            soundManager.stopBackgroundMusic();

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gamePanel.getScene().getWindow();
            stage.setScene(new Scene(root, 540, 720));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}