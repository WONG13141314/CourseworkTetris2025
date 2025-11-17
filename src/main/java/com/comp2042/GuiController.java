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
import javafx.scene.effect.Reflection;
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

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int BLITZ_TIME_SECONDS = 120; // 2 minutes

    private GameMode gameMode;
    private int blitzTimeRemaining = BLITZ_TIME_SECONDS;
    private Timeline blitzTimer;

    private double getAbsoluteX(javafx.scene.Node node) {
        double x = 0;
        while (node != null) {
            x += node.getLayoutX();
            if (node.getParent() == null) break;
            node = node.getParent();
        }
        return x;
    }

    private double getAbsoluteY(javafx.scene.Node node) {
        double y = 0;
        while (node != null) {
            y += node.getLayoutY();
            if (node.getParent() == null) break;
            node = node.getParent();
        }
        return y;
    }

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

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

    private GameTimer gameTimer;

    private GridPane shadowPanel;

    private Rectangle[][] shadowRectangles;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gameTimer = new GameTimer();
        setupTimerLabel();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE) {
                    if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                        hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                        keyEvent.consume();
                    } else {
                        pauseGame(null);
                        keyEvent.consume();
                    }
                    return;
                }

                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    boolean moved = false;

                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        ViewData viewData = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
                        refreshBrick(viewData);
                        moved = true;
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        ViewData viewData = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
                        refreshBrick(viewData);
                        moved = true;
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        ViewData viewData = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
                        refreshBrick(viewData);
                        moved = true;
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        moved = true;
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == KeyCode.C || keyEvent.getCode() == KeyCode.SHIFT) {
                        ViewData viewData = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
                        refreshBrick(viewData);
                        moved = true;
                        keyEvent.consume();
                    }

                    if (moved) {
                        gamePanel.requestFocus();
                    }
                }
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    returnToMenu();
                    keyEvent.consume();
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                    keyEvent.consume();
                }
            }
        });
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
        if (gameModeLabel != null) {
            gameModeLabel.setText(mode == GameMode.ZEN ? "ZEN MODE" : "BLITZ MODE");
            if (mode == GameMode.BLITZ) {
                gameModeLabel.setStyle("-fx-text-fill: #f7768e;");
            } else {
                gameModeLabel.setStyle("-fx-text-fill: #7aa2f7;");
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
        blitzTimeRemaining = BLITZ_TIME_SECONDS;
        blitzTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
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
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
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
                shadow.setArcHeight(9);
                shadow.setArcWidth(9);
                shadowRectangles[i][j] = shadow;
                shadowPanel.add(shadow, j, i);
            }
        }

        if (gamePanel.getParent() instanceof Pane) {
            ((Pane) gamePanel.getParent()).getChildren().add(shadowPanel);
        }
        updateShadowPosition(brick);

        double gamePanelAbsX = getAbsoluteX(gamePanel);
        double gamePanelAbsY = getAbsoluteY(gamePanel);
        double brickX = gamePanelAbsX + brick.getxPosition() * (BRICK_SIZE + 1);
        double brickY = gamePanelAbsY + (brick.getyPosition() - 2) * (BRICK_SIZE + 1);
        brickPanel.setLayoutX(brickX);
        brickPanel.setLayoutY(brickY);

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
        if (shadowPanel != null && brick != null && shadowRectangles != null) {
            shadowPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * (shadowPanel.getVgap() + BRICK_SIZE));
            shadowPanel.setLayoutY(gamePanel.getLayoutY() + brick.getShadowYPosition() * (shadowPanel.getHgap() + BRICK_SIZE) - 46);

            int[][] currentBrickData = brick.getBrickData();
            for (int i = 0; i < shadowRectangles.length; i++) {
                for (int j = 0; j < shadowRectangles[i].length; j++) {
                    if (shadowRectangles[i][j] != null) {
                        boolean shouldBeVisible = currentBrickData[i][j] != 0;
                        shadowRectangles[i][j].setVisible(shouldBeVisible);

                        if (shouldBeVisible) {
                            shadowRectangles[i][j].setFill(getShadowColor(currentBrickData[i][j]));
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
            updateShadowPosition(brick);
            double gamePanelAbsX = getAbsoluteX(gamePanel);
            double gamePanelAbsY = getAbsoluteY(gamePanel);
            double brickX = gamePanelAbsX + brick.getxPosition() * (BRICK_SIZE + 1);
            double brickY = gamePanelAbsY + (brick.getyPosition() - 2) * (BRICK_SIZE + 1);
            brickPanel.setLayoutX(brickX);
            brickPanel.setLayoutY(brickY);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
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
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
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
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
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

    public void gameOver() {
        timeLine.stop();
        if (gameMode == GameMode.ZEN) {
            gameTimer.stop();
        } else if (gameMode == GameMode.BLITZ && blitzTimer != null) {
            blitzTimer.stop();
        }
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        brickPanel.setVisible(false);
        if (shadowPanel != null) {
            shadowPanel.setVisible(false);
        }
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        if (gameMode == GameMode.ZEN) {
            gameTimer.reset();
        } else if (gameMode == GameMode.BLITZ) {
            if (blitzTimer != null) {
                blitzTimer.stop();
            }
            setupBlitzTimer();
            blitzTimer.play();
            if (timerLabel != null) {
                timerLabel.setText(formatTime(BLITZ_TIME_SECONDS));
                timerLabel.setStyle("");
            }
        }
        gameOverPanel.setVisible(false);
        brickPanel.setVisible(true);
        if (shadowPanel != null) {
            shadowPanel.setVisible(true);
        }
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        if (gameMode == GameMode.ZEN) {
            gameTimer.start();
        }
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (isPause.getValue() == Boolean.FALSE) {
            timeLine.stop();
            if (gameMode == GameMode.ZEN) {
                gameTimer.stop();
            } else if (gameMode == GameMode.BLITZ && blitzTimer != null) {
                blitzTimer.pause();
            }
            isPause.setValue(Boolean.TRUE);
        } else {
            timeLine.play();
            if (gameMode == GameMode.ZEN) {
                gameTimer.start();
            } else if (gameMode == GameMode.BLITZ && blitzTimer != null) {
                blitzTimer.play();
            }
            isPause.setValue(Boolean.FALSE);
        }
        gamePanel.requestFocus();
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

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) gamePanel.getScene().getWindow();
            Scene scene = new Scene(root, 540, 720);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }
}