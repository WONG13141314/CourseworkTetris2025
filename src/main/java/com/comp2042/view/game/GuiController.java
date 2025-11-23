package com.comp2042.view.game;

import com.comp2042.controller.game.GameStateManager;
import com.comp2042.controller.input.InputCoordinator;
import com.comp2042.controller.input.InputEventListener;
import com.comp2042.controller.input.InputHandler;
import com.comp2042.controller.lifecycle.GameInitializer;
import com.comp2042.controller.lifecycle.GameLifecycleManager;
import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.enums.GameMode;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.util.SoundManager;
import com.comp2042.view.components.GameOverPanel;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main GUI Controller - coordinates all game components
 */
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
    private UIManager uiManager;
    private SoundManager soundManager;

    private GameInitializer gameInitializer;
    private GameLifecycleManager lifecycleManager;
    private InputCoordinator inputCoordinator;
    private MenuNavigator menuNavigator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic();
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        initializeManagers();
        setupInputSystem();
    }

    private void initializeManagers() {
        stateManager = new GameStateManager();
        inputHandler = new InputHandler();
        uiManager = new UIManager(scoreLabel, highScoreLabel, gameModeLabel,
                gameOverPanel, pauseGroup, groupNotification, blitzLevelPanel);
        menuNavigator = new MenuNavigator(soundManager);
    }

    private void setupInputSystem() {
        inputCoordinator = new InputCoordinator(stateManager, inputHandler, gamePanel);

        gamePanel.setOnKeyPressed(e ->
                inputHandler.handleKeyPressed(e, stateManager.isPaused(), stateManager.isGameOver())
        );
        gamePanel.setOnKeyReleased(e ->
                inputHandler.handleKeyReleased(e)
        );
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;

        gameInitializer = new GameInitializer(mode, stateManager, inputHandler,
                uiManager, soundManager);
        lifecycleManager = new GameLifecycleManager(mode, stateManager, inputHandler,
                uiManager, soundManager);

        gameInitializer.initializeGameMode(blitzLevelLabel, blitzProgressLabel, timerLabel);
    }

    public void initGameView(int[][] boardMatrix, com.comp2042.model.data.ViewData brick) {
        gameInitializer.initializeRendering(gamePanel, brickPanel, nextBrickPanel,
                holdBrickPanel, (Pane) gamePanel.getParent(), boardMatrix, brick);

        gameInitializer.initializeTimer(timerLabel, this::gameOver);

        gameInitializer.initializeGameLoop(() -> {
            if (stateManager.canProcessInput()) {
                DownData downData = gameInitializer.getEventHandler()
                        .handleDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                if (downData != null && downData.getViewData() != null) {
                    gameInitializer.getRendererCoordinator()
                            .refreshBrick(downData.getViewData());
                }
            }
        });

        lifecycleManager.setComponents(
                gameInitializer.getLoopManager(),
                gameInitializer.getTimerManager(),
                gameInitializer.getRendererCoordinator(),
                gameInitializer.getEventHandler(),
                gameInitializer.getBlitzModeManager(),
                gameInitializer.getZenModeManager()
        );

        inputCoordinator.setComponents(
                gameInitializer.getEventHandler(),
                gameInitializer.getRendererCoordinator()
        );

        inputCoordinator.setupCallbacks(
                lifecycleManager::togglePause,
                this::newGame,
                this::returnToMenu
        );
    }

    public void setEventListener(InputEventListener listener) {
        gameInitializer.initializeEventHandler(listener);
    }

    public void bindScore(IntegerProperty score) {
        uiManager.bindScore(score);
    }

    public void bindHighScore(IntegerProperty highScore) {
        uiManager.bindHighScore(highScore);
    }

    public void refreshGameBackground(int[][] board) {
        gameInitializer.getRendererCoordinator().refreshBoard(board);
    }

    public void gameOver() {
        lifecycleManager.handleGameOver();
    }

    public void newGame() {
        lifecycleManager.startNewGame();
        gamePanel.requestFocus();
    }

    private void returnToMenu() {
        Stage stage = (Stage) gamePanel.getScene().getWindow();
        menuNavigator.returnToMainMenu(stage);
    }
}