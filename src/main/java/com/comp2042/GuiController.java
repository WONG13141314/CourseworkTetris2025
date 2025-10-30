package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;
    private double getGamePanelOffsetX() {
        double screenShift = 100;
        return gamePanel.getLayoutX() + screenShift;
    }

    private double getGamePanelOffsetY() {
        return gamePanel.getLayoutY();
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
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
                if (keyEvent.getCode() == KeyCode.C || keyEvent.getCode() == KeyCode.SHIFT) {
                    if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                        refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                        keyEvent.consume();
                    }
                }
            }
        });
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
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

        brickPanel.setLayoutX(getGamePanelOffsetX() + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE));
        brickPanel.setLayoutY(getGamePanelOffsetY() + brick.getyPosition() * (brickPanel.getHgap() + BRICK_SIZE) - 46);

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
                int maxSize = 4; // Fixed size for hold panel

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
            brickPanel.setLayoutX(getGamePanelOffsetX() + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE));
            brickPanel.setLayoutY(getGamePanelOffsetY() + brick.getyPosition() * (brickPanel.getHgap() + BRICK_SIZE) - 46);

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
            highScoreLabel.textProperty().bind(integerProperty.asString("High Score: %d"));
        }
    }

    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        brickPanel.setVisible(false);
        if (shadowPanel != null) {
            shadowPanel.setVisible(false);
        }
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        brickPanel.setVisible(true);
        if (shadowPanel != null) {
            shadowPanel.setVisible(true);
        }
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }
}