package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int BLITZ_TIME_SECONDS = 120; // 2 minutes
    private GameMode gameMode;
    private int blitzTimeRemaining = BLITZ_TIME_SECONDS;
    private Timeline blitzTimer;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private Group pauseGroup;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private Rectangle[][] nextBrickRectangles;

    @FXML
    private Label scoreLabel;

    @FXML
    private GridPane nextBrickPanel;

    @FXML
    private GridPane holdBrickPanel;

    private Rectangle[][] holdBrickRectangles;

    @FXML
    private Label highScoreLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label gameModeLabel;

    private BlitzLevel blitzLevel;

    @FXML
    private Label blitzLevelLabel;

    @FXML
    private Label blitzProgressLabel;

    @FXML
    private VBox blitzLevelPanel;

    private GameTimer gameTimer;

    private GridPane shadowPanel;

    private Rectangle[][] shadowRectangles;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private boolean spacePressed = false;

    private final Set<KeyCode> pressedKeys = new HashSet<>();

    private Timeline leftRepeat;

    private Timeline rightRepeat;

    private Timeline downRepeat;

    private Timeline leftDelay;

    private Timeline rightDelay;

    private Timeline downDelay;

    private SoundManager soundManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic();
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gameTimer = new GameTimer();
        setupTimerLabel();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode code = keyEvent.getCode();

                if (code == KeyCode.SPACE) {
                    if (!spacePressed) {
                        spacePressed = true;
                        if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                            hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                        }
                        keyEvent.consume();
                    }
                    return;
                }

                if (code == KeyCode.P && !pressedKeys.contains(code)) {
                    if (isGameOver.getValue() == Boolean.FALSE) {
                        pauseGame(null);
                        pressedKeys.add(code);
                    }
                    gamePanel.requestFocus();
                    keyEvent.consume();
                    return;
                }

                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {

                    if ((code == KeyCode.UP || code == KeyCode.W) && !pressedKeys.contains(code)) {
                        ViewData viewData = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
                        refreshBrick(viewData);
                        pressedKeys.add(code);
                        gamePanel.requestFocus();
                        keyEvent.consume();
                    } else if ((code == KeyCode.C || code == KeyCode.SHIFT) && !pressedKeys.contains(code)) {
                        ViewData viewData = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
                        refreshBrick(viewData);
                        pressedKeys.add(code);
                        gamePanel.requestFocus();
                        keyEvent.consume();
                    }

                    else if ((code == KeyCode.LEFT || code == KeyCode.A) && !pressedKeys.contains(code)) {
                        ViewData viewData = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
                        refreshBrick(viewData);
                        pressedKeys.add(code);

                        if (leftRepeat != null) leftRepeat.stop();
                        if (leftDelay != null) leftDelay.stop();

                        leftDelay = new Timeline(new KeyFrame(Duration.millis(170), e -> {
                            if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A)) {
                                leftRepeat = new Timeline(new KeyFrame(Duration.millis(50), ev -> {
                                    if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A)) {
                                        ViewData vd = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
                                        refreshBrick(vd);
                                    } else {
                                        if (leftRepeat != null) leftRepeat.stop();
                                    }
                                }));
                                leftRepeat.setCycleCount(Timeline.INDEFINITE);
                                leftRepeat.play();
                            }
                        }));
                        leftDelay.setCycleCount(1);
                        leftDelay.play();

                        gamePanel.requestFocus();
                        keyEvent.consume();
                    }

                    else if ((code == KeyCode.RIGHT || code == KeyCode.D) && !pressedKeys.contains(code)) {
                        ViewData viewData = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
                        refreshBrick(viewData);
                        pressedKeys.add(code);

                        if (rightRepeat != null) rightRepeat.stop();
                        if (rightDelay != null) rightDelay.stop();

                        rightDelay = new Timeline(new KeyFrame(Duration.millis(170), e -> {
                            if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
                                rightRepeat = new Timeline(new KeyFrame(Duration.millis(50), ev -> {
                                    if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
                                        ViewData vd = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
                                        refreshBrick(vd);
                                    } else {
                                        if (rightRepeat != null) rightRepeat.stop();
                                    }
                                }));
                                rightRepeat.setCycleCount(Timeline.INDEFINITE);
                                rightRepeat.play();
                            }
                        }));
                        rightDelay.setCycleCount(1);
                        rightDelay.play();

                        gamePanel.requestFocus();
                        keyEvent.consume();
                    }

                    else if ((code == KeyCode.DOWN || code == KeyCode.S) && !pressedKeys.contains(code)) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        pressedKeys.add(code);

                        if (downRepeat != null) downRepeat.stop();
                        if (downDelay != null) downDelay.stop();

                        downDelay = new Timeline(new KeyFrame(Duration.millis(170), e -> {
                            if (pressedKeys.contains(KeyCode.DOWN) || pressedKeys.contains(KeyCode.S)) {
                                downRepeat = new Timeline(new KeyFrame(Duration.millis(50), ev -> {
                                    if (pressedKeys.contains(KeyCode.DOWN) || pressedKeys.contains(KeyCode.S)) {
                                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                                    } else {
                                        if (downRepeat != null) downRepeat.stop();
                                    }
                                }));
                                downRepeat.setCycleCount(Timeline.INDEFINITE);
                                downRepeat.play();
                            }
                        }));
                        downDelay.setCycleCount(1);
                        downDelay.play();

                        gamePanel.requestFocus();
                        keyEvent.consume();
                    }
                }

                if (code == KeyCode.ESCAPE) {
                    returnToMenu();
                    keyEvent.consume();
                }
                if (code == KeyCode.N) {
                    newGame(null);
                    keyEvent.consume();
                }
            }
        });
        gameOverPanel.setVisible(false);
        gamePanel.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode code = keyEvent.getCode();

                if (code == KeyCode.SPACE) {
                    spacePressed = false;
                }

                pressedKeys.remove(code);

                if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (leftRepeat != null) {
                        leftRepeat.stop();
                        leftRepeat = null;
                    }
                    if (leftDelay != null) {
                        leftDelay.stop();
                        leftDelay = null;
                    }
                } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    if (rightRepeat != null) {
                        rightRepeat.stop();
                        rightRepeat = null;
                    }
                    if (rightDelay != null) {
                        rightDelay.stop();
                        rightDelay = null;
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (downRepeat != null) {
                        downRepeat.stop();
                        downRepeat = null;
                    }
                    if (downDelay != null) {
                        downDelay.stop();
                        downDelay = null;
                    }
                }else if (code == KeyCode.P) {
                    pressedKeys.remove(code);
                }

                keyEvent.consume();
            }
        });
    }

    private void positionBrickPanel(ViewData brick) {
        double gamePanelX = 160;
        double gamePanelY = 120;

        double brickX = gamePanelX + 5 + brick.getxPosition() * (BRICK_SIZE + 1);
        double brickY = gamePanelY - 2 + (brick.getyPosition() - 2) * (BRICK_SIZE + 1);

        brickPanel.setLayoutX(brickX);
        brickPanel.setLayoutY(brickY);
    }



    public void setGameMode(GameMode mode) {
        this.gameMode = mode;

        if (mode == GameMode.BLITZ) {
            blitzLevel = new BlitzLevel();
            setupBlitzLevelDisplay();

            if (blitzLevelPanel != null) {
                blitzLevelPanel.setVisible(true);
                blitzLevelPanel.setManaged(true);
            }
        } else {
            if (blitzLevelPanel != null) {
                blitzLevelPanel.setVisible(false);
                blitzLevelPanel.setManaged(false);
            }
        }

        if (gameModeLabel != null) {
            gameModeLabel.setText(mode == GameMode.ZEN ? "ZEN MODE" : "BLITZ MODE");
            if (mode == GameMode.BLITZ) {
                gameModeLabel.setStyle("-fx-text-fill: #f7768e;");
            } else {
                gameModeLabel.setStyle("-fx-text-fill: #7aa2f7;");
            }
        }
    }

    private void setupBlitzLevelDisplay() {
        if (blitzLevelLabel != null && blitzProgressLabel != null) {
            blitzLevel.levelProperty().addListener((obs, oldVal, newVal) -> {
                blitzLevelLabel.setText("LEVEL " + newVal);
                updateGameSpeed();
            });

            blitzLevel.linesClearedProperty().addListener((obs, oldVal, newVal) -> {
                updateProgressLabel();
            });

            blitzLevel.linesNeededProperty().addListener((obs, oldVal, newVal) -> {
                updateProgressLabel();
            });

            updateProgressLabel();
            blitzLevelLabel.setText("LEVEL 1");
        }
    }

    private void updateProgressLabel() {
        if (blitzProgressLabel != null && blitzLevel != null) {
            blitzProgressLabel.setText(blitzLevel.getProgressText() + " LINES");
        }
    }

    private void updateGameSpeed() {
        if (gameMode == GameMode.BLITZ && blitzLevel != null) {
            if (timeLine != null) {
                timeLine.stop();
            }

            if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                timeLine = new Timeline(new KeyFrame(
                        Duration.millis(blitzLevel.getDropSpeed()),
                        ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
                ));
                timeLine.setCycleCount(Timeline.INDEFINITE);
                timeLine.play();
            }
        }
    }

    private void setupTimerLabel() {
        if (timerLabel != null) {
            gameTimer.elapsedSecondsProperty().addListener((observable, oldValue, newValue) -> {
                if (gameMode == GameMode.ZEN) {
                    timerLabel.setText(formatTime(newValue.intValue()));
                }
            });
            timerLabel.getStyleClass().add("timerClass");
            timerLabel.setText(formatTime(0));
        }
    }

    private void setupBlitzTimer() {
        if (blitzTimer != null) {
            blitzTimer.stop();
        }

        blitzTimeRemaining = BLITZ_TIME_SECONDS;

        if (timerLabel != null) {
            timerLabel.setText(formatTime(blitzTimeRemaining));
            timerLabel.setStyle("");
        }

        blitzTimer = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> {
            if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                blitzTimeRemaining--;

                if (timerLabel != null) {
                    timerLabel.setText(formatTime(blitzTimeRemaining));

                    if (blitzTimeRemaining <= 30) {
                        timerLabel.setStyle("-fx-text-fill: #f7768e;");
                    }
                }

                if (blitzTimeRemaining <= 0) {
                    blitzTimer.stop();
                    gameOver();
                }
            }
        }));
        blitzTimer.setCycleCount(Timeline.INDEFINITE);
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setArcWidth(5);
                rectangle.setArcHeight(5);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangle.setArcWidth(5);
                rectangle.setArcHeight(5);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }

        shadowPanel = new GridPane();
        shadowPanel.setVgap(1);
        shadowPanel.setHgap(1);

        shadowRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle shadow = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                shadow.setFill(getShadowColor(brick.getBrickData()[i][j]));
                shadow.setOpacity(0.3);
                shadow.setArcWidth(5);
                shadow.setArcHeight(5);
                shadowRectangles[i][j] = shadow;
                shadowPanel.add(shadow, j, i);
            }
        }

        if (gamePanel.getParent() instanceof Pane) {
            ((Pane) gamePanel.getParent()).getChildren().add(shadowPanel);
        }
        updateShadowPosition(brick);

        positionBrickPanel(brick);

        int[][] nextBrickData = brick.getNextBrickData();
        nextBrickRectangles = new Rectangle[nextBrickData.length][nextBrickData[0].length];
        if (nextBrickPanel != null) {
            nextBrickPanel.getChildren().clear();
            for (int i = 0; i < nextBrickData.length; i++) {
                for (int j = 0; j < nextBrickData[i].length; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(getFillColor(nextBrickData[i][j]));
                    nextBrickRectangles[i][j] = rectangle;
                    nextBrickPanel.add(rectangle, j, i);
                }
            }
        }
        centerNextBrick(nextBrickData);

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

        if (gameMode == GameMode.ZEN) {
            gameTimer.start();
        } else if (gameMode == GameMode.BLITZ) {
            setupBlitzTimer();
            blitzTimer.play();
            if (timerLabel != null) {
                timerLabel.setText(formatTime(BLITZ_TIME_SECONDS));
            }
        }
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    private Paint getShadowColor(int brickType) {
        switch (brickType) {
            case 1: return Color.rgb(0, 255, 255, 0.6);
            case 2: return Color.rgb(138, 43, 226, 0.6);
            case 3: return Color.rgb(0, 80, 0, 0.8);
            case 4: return Color.rgb(255, 255, 0, 0.6);
            case 5: return Color.rgb(255, 0, 0, 0.6);
            case 6: return Color.rgb(245, 245, 220, 0.6);
            case 7: return Color.rgb(222, 184, 135, 0.6);
            default: return Color.TRANSPARENT;
        }
    }

    private void centerNextBrick(int[][] nextBrickData) {
        if (nextBrickPanel != null) {
            nextBrickPanel.getChildren().clear();

            int rows = nextBrickData.length;
            int cols = nextBrickData[0].length;
            int maxSize = 4;

            int rowPadding = (maxSize - rows) / 2;
            int colPadding = (maxSize - cols) / 2;

            for (int i = 0; i < maxSize; i++) {
                for (int j = 0; j < maxSize; j++) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);

                    if (i >= rowPadding && i < rowPadding + rows &&
                            j >= colPadding && j < colPadding + cols) {
                        int brickValue = nextBrickData[i - rowPadding][j - colPadding];
                        rectangle.setFill(getFillColor(brickValue));
                    } else {
                        rectangle.setFill(Color.TRANSPARENT);
                    }

                    rectangle.setArcHeight(5);
                    rectangle.setArcWidth(5);
                    nextBrickPanel.add(rectangle, j, i);
                }
            }
        }
    }


    private void updateShadowPosition(ViewData brick) {
        for (int i = 0; i < shadowRectangles.length; i++) {
            for (int j = 0; j < shadowRectangles[i].length; j++) {
                shadowRectangles[i][j].setFill(Color.TRANSPARENT);
                gamePanel.getChildren().remove(shadowRectangles[i][j]);
            }
        }

        int[][] brickData = brick.getBrickData();
        if (shadowRectangles.length != brickData.length ||
                shadowRectangles[0].length != brickData[0].length) {
            shadowRectangles = new Rectangle[brickData.length][brickData[0].length];
            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    shadowRectangles[i][j] = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    shadowRectangles[i][j].setArcWidth(5);
                    shadowRectangles[i][j].setArcHeight(5);
                    shadowRectangles[i][j].setOpacity(0.3);
                }
            }
        }

        if (brick.getShadowYPosition() != brick.getyPosition()) {
            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    if (brickData[i][j] != 0) {
                        int gridX = brick.getxPosition() + j;
                        int gridY = brick.getShadowYPosition() + i;

                        if (gridY >= 2 && gridY < 25) {
                            shadowRectangles[i][j].setFill(getShadowColor(brickData[i][j]));
                            gamePanel.add(shadowRectangles[i][j], gridX, gridY - 2);
                        }
                    }
                }
            }
        }
    }

    private void initHoldBrickDisplay(int[][] holdBrickData) {
        if (holdBrickPanel != null) {
            holdBrickPanel.getChildren().clear();

            if (holdBrickData != null && holdBrickData.length > 0) {
                int rows = holdBrickData.length;
                int cols = holdBrickData[0].length;
                int maxSize = 4;

                int rowPadding = (maxSize - rows) / 2;
                int colPadding = (maxSize - cols) / 2;

                holdBrickRectangles = new Rectangle[maxSize][maxSize];

                for (int i = 0; i < maxSize; i++) {
                    for (int j = 0; j < maxSize; j++) {
                        Rectangle rectangle = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);

                        if (i >= rowPadding && i < rowPadding + rows &&
                                j >= colPadding && j < colPadding + cols) {
                            int brickValue = holdBrickData[i - rowPadding][j - colPadding];
                            rectangle.setFill(getFillColor(brickValue));
                        } else {
                            rectangle.setFill(Color.TRANSPARENT);
                        }

                        rectangle.setArcHeight(5);
                        rectangle.setArcWidth(5);
                        holdBrickRectangles[i][j] = rectangle;
                        holdBrickPanel.add(rectangle, j, i);
                    }
                }
            } else {
                holdBrickRectangles = null;
            }
        }
    }


    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            for (Rectangle[] row : rectangles) {
                for (Rectangle r : row) {
                    gamePanel.getChildren().remove(r);
                }
            }

            int[][] brickData = brick.getBrickData();
            if (rectangles.length != brickData.length ||
                    rectangles[0].length != brickData[0].length) {
                rectangles = new Rectangle[brickData.length][brickData[0].length];
                for (int i = 0; i < brickData.length; i++) {
                    for (int j = 0; j < brickData[i].length; j++) {
                        rectangles[i][j] = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                        rectangles[i][j].setArcWidth(5);
                        rectangles[i][j].setArcHeight(5);
                    }
                }
            }

            for (int i = 0; i < brickData.length; i++) {
                for (int j = 0; j < brickData[i].length; j++) {
                    if (brickData[i][j] != 0) {
                        int gridX = brick.getxPosition() + j;
                        int gridY = brick.getyPosition() + i;

                        if (gridY >= 2) {
                            rectangles[i][j].setFill(getFillColor(brickData[i][j]));
                            gamePanel.add(rectangles[i][j], gridX, gridY - 2);
                        }
                    } else {
                        rectangles[i][j].setFill(Color.TRANSPARENT);
                    }
                }
            }

            updateShadowPosition(brick);

            if (nextBrickRectangles != null) {
                int[][] nextBrickData = brick.getNextBrickData();
                centerNextBrick(nextBrickData);
            }

            int[][] holdBrickData = brick.getHoldBrickData();
            initHoldBrickDisplay(holdBrickData);
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.TRUE || isGameOver.getValue() == Boolean.TRUE) {
            return;
        }

        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);

            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                soundManager.playClearRow();
                if (gameMode == GameMode.BLITZ && blitzLevel != null) {
                    blitzLevel.addLines(downData.getClearRow().getLinesRemoved());
                }

                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }

            if (downData.isBoardCleared() && gameMode == GameMode.ZEN) {
                NotificationPanel notificationPanel = new NotificationPanel("Board Cleared!");
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }

            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    private void hardDrop(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onHardDropEvent(event);

            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                soundManager.playClearRow();
                if (gameMode == GameMode.BLITZ && blitzLevel != null) {
                    blitzLevel.addLines(downData.getClearRow().getLinesRemoved());
                }

                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }

            if (downData.isBoardCleared() && gameMode == GameMode.ZEN) {
                NotificationPanel notificationPanel = new NotificationPanel("Board Cleared!");
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }

            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(integerProperty.asString("Score: %d"));
        }
    }

    public void bindHighScore(IntegerProperty integerProperty) {
        if (highScoreLabel != null) {
            highScoreLabel.textProperty().bind(integerProperty.asString("Best: %d"));
        }
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (isPause.getValue() == Boolean.FALSE) {
            timeLine.stop();
            if (gameMode == GameMode.ZEN) {
                gameTimer.stop();
            } else if (gameMode == GameMode.BLITZ && blitzTimer != null) {
                blitzTimer.pause();
            }
            soundManager.pauseBackgroundMusic();
            isPause.setValue(Boolean.TRUE);

            if (pauseGroup != null) {
                pauseGroup.setVisible(true);
            }
        } else {
            timeLine.play();
            if (gameMode == GameMode.ZEN) {
                gameTimer.start();
            } else if (gameMode == GameMode.BLITZ && blitzTimer != null) {
                blitzTimer.play();
            }
            soundManager.resumeBackgroundMusic();
            isPause.setValue(Boolean.FALSE);

            if (pauseGroup != null) {
                pauseGroup.setVisible(false);
            }
        }
        gamePanel.requestFocus();
    }

    public void gameOver() {
        timeLine.stop();
        if (gameMode == GameMode.ZEN) {
            gameTimer.stop();
        } else if (gameMode == GameMode.BLITZ && blitzTimer != null) {
            blitzTimer.stop();
        }

        stopAllRepeatTimers();

        soundManager.stopBackgroundMusic();
        int currentScore = eventListener.getCurrentScore();
        int currentHighScore = eventListener.getCurrentHighScore();

        boolean showScores = (gameMode == GameMode.BLITZ);
        gameOverPanel.updateScores(currentScore, currentHighScore, showScores);

        if (pauseGroup != null) {
            pauseGroup.setVisible(false);
        }

        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        isPause.setValue(Boolean.FALSE);

        brickPanel.setVisible(false);
        if (shadowPanel != null) {
            shadowPanel.setVisible(false);
        }

        pressedKeys.clear();
        spacePressed = false;
    }

    private void stopAllRepeatTimers() {
        if (leftRepeat != null) {
            leftRepeat.stop();
            leftRepeat = null;
        }
        if (rightRepeat != null) {
            rightRepeat.stop();
            rightRepeat = null;
        }
        if (downRepeat != null) {
            downRepeat.stop();
            downRepeat = null;
        }
        if (leftDelay != null) {
            leftDelay.stop();
            leftDelay = null;
        }
        if (rightDelay != null) {
            rightDelay.stop();
            rightDelay = null;
        }
        if (downDelay != null) {
            downDelay.stop();
            downDelay = null;
        }
    }


    public void newGame(ActionEvent actionEvent) {
        if (timeLine != null) {
            timeLine.stop();
        }
        stopAllRepeatTimers();

        gameOverPanel.setVisible(false);
        if (pauseGroup != null) {
            pauseGroup.setVisible(false);
        }

        brickPanel.setVisible(true);
        if (shadowPanel != null) {
            shadowPanel.setVisible(true);
        }

        pressedKeys.clear();
        spacePressed = false;

        soundManager.stopBackgroundMusic();
        soundManager.playBackgroundMusic();

        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        if (gameMode == GameMode.ZEN) {
            if (blitzTimer != null) {
                blitzTimer.stop();
                blitzTimer = null;
            }

            gameTimer.reset();
            if (timerLabel != null) {
                timerLabel.setText(formatTime(0));
                timerLabel.setStyle("");
            }

            timeLine = new Timeline(new KeyFrame(
                    Duration.millis(400),
                    ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
            ));
            timeLine.setCycleCount(Timeline.INDEFINITE);
            timeLine.play();

            gameTimer.start();

        } else if (gameMode == GameMode.BLITZ) {
            if (gameTimer != null && gameTimer.isRunning()) {
                gameTimer.stop();
            }

            if (blitzLevel != null) {
                blitzLevel.reset();
                updateProgressLabel();
                blitzLevelLabel.setText("LEVEL 1");
            }

            setupBlitzTimer();
            blitzTimer.play();

            if (timerLabel != null) {
                timerLabel.setText(formatTime(BLITZ_TIME_SECONDS));
                timerLabel.setStyle("");
            }

            updateGameSpeed();
        }

        eventListener.createNewGame();
        gamePanel.requestFocus();

        gameOverPanel.updateScores(0, 0, false);
    }

    private void returnToMenu() {
        try {
            if (timeLine != null) {
                timeLine.stop();
            }
            if (gameTimer != null) {
                gameTimer.stop();
            }
            if (blitzTimer != null) {
                blitzTimer.stop();
            }
            soundManager.stopBackgroundMusic();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) gamePanel.getScene().getWindow();
            Scene scene = new Scene(root, 540, 720);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}