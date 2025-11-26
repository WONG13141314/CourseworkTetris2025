package com.comp2042.controller.game;

import com.comp2042.controller.input.InputEventListener;
import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.enums.GameMode;
import com.comp2042.model.data.ClearRow;
import com.comp2042.model.data.DownData;
import com.comp2042.model.data.MoveEvent;
import com.comp2042.model.data.ViewData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class GameEventHandlerTest {
    private GameEventHandler eventHandler;
    private TestInputEventListener testListener;

    private boolean notificationShown;
    private String notificationText;

    @BeforeEach
    void setUp() {
        testListener = new TestInputEventListener();
        eventHandler = new GameEventHandler(testListener, GameMode.ZEN);

        notificationShown = false;
        notificationText = null;

        eventHandler.setOnNotification(text -> {
            notificationShown = true;
            notificationText = text;
        });
    }

    @Test
    void testHandleMove_Left_CallsListener() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        ViewData mockViewData = createMockViewData();
        testListener.leftEventReturn = mockViewData;

        ViewData result = eventHandler.handleMove(event);

        assertNotNull(result);
        assertTrue(testListener.onLeftEventCalled, "onLeftEvent should be called");
    }

    @Test
    void testHandleMove_Right_CallsListener() {
        MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
        ViewData mockViewData = createMockViewData();
        testListener.rightEventReturn = mockViewData;

        ViewData result = eventHandler.handleMove(event);

        assertNotNull(result);
        assertTrue(testListener.onRightEventCalled, "onRightEvent should be called");
    }

    @Test
    void testHandleRotate_CallsListener() {
        MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
        ViewData mockViewData = createMockViewData();
        testListener.rotateEventReturn = mockViewData;

        ViewData result = eventHandler.handleRotate(event);

        assertNotNull(result);
        assertTrue(testListener.onRotateEventCalled, "onRotateEvent should be called");
    }

    @Test
    void testHandleHold_CallsListener() {
        MoveEvent event = new MoveEvent(EventType.HOLD, EventSource.USER);
        ViewData mockViewData = createMockViewData();
        testListener.holdEventReturn = mockViewData;

        ViewData result = eventHandler.handleHold(event);

        assertNotNull(result);
        assertTrue(testListener.onHoldEventCalled, "onHoldEvent should be called");
    }

    @Test
    void testGetCurrentScore_CallsListener() {
        testListener.currentScore = 100;

        int score = eventHandler.getCurrentScore();

        assertEquals(100, score);
        assertTrue(testListener.getCurrentScoreCalled, "getCurrentScore should be called");
    }

    @Test
    void testGetCurrentHighScore_CallsListener() {
        testListener.currentHighScore = 500;

        int highScore = eventHandler.getCurrentHighScore();

        assertEquals(500, highScore);
        assertTrue(testListener.getCurrentHighScoreCalled, "getCurrentHighScore should be called");
    }

    @Test
    void testCreateNewGame_CallsListener() {
        eventHandler.createNewGame();
        assertTrue(testListener.createNewGameCalled, "createNewGame should be called");
    }

    @Test
    void testHandleDown_WithLineClear_ShowsNotification() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        ClearRow clearRow = new ClearRow(2, new int[20][10], 200);
        DownData downData = new DownData(clearRow, createMockViewData());
        testListener.downEventReturn = downData;

        eventHandler.handleDown(event);

        assertTrue(notificationShown, "Notification should be shown");
        assertEquals("+200", notificationText);
    }

    @Test
    void testHandleDown_WithoutLineClear_NoNotification() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        DownData downData = new DownData(null, createMockViewData());
        testListener.downEventReturn = downData;

        eventHandler.handleDown(event);

        assertFalse(notificationShown, "No notification should be shown");
    }

    @Test
    void testHandleHardDrop_CallsListener() {
        MoveEvent event = new MoveEvent(EventType.HARD_DROP, EventSource.USER);
        DownData downData = new DownData(null, createMockViewData());
        testListener.hardDropEventReturn = downData;

        DownData result = eventHandler.handleHardDrop(event);

        assertNotNull(result);
        assertTrue(testListener.onHardDropEventCalled, "onHardDropEvent should be called");
    }

    @Test
    void testHandleDown_WithBoardCleared_ShowsNotification() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
        ClearRow clearRow = new ClearRow(1, new int[20][10], 50);
        DownData downData = new DownData(clearRow, createMockViewData(), true);
        testListener.downEventReturn = downData;

        eventHandler.handleDown(event);

        assertTrue(notificationShown, "Notification should be shown for board clear");
    }

    private ViewData createMockViewData() {
        return new ViewData(
                new int[][]{{1, 1}, {1, 1}},
                5, 10,
                new int[][]{{2, 2}, {2, 2}},
                15,
                new int[][]{{3, 3}, {3, 3}}
        );
    }

    // Test implementation of InputEventListener
    private static class TestInputEventListener implements InputEventListener {
        boolean onDownEventCalled = false;
        boolean onLeftEventCalled = false;
        boolean onRightEventCalled = false;
        boolean onRotateEventCalled = false;
        boolean onHardDropEventCalled = false;
        boolean onHoldEventCalled = false;
        boolean createNewGameCalled = false;
        boolean getCurrentScoreCalled = false;
        boolean getCurrentHighScoreCalled = false;

        DownData downEventReturn = null;
        ViewData leftEventReturn = null;
        ViewData rightEventReturn = null;
        ViewData rotateEventReturn = null;
        DownData hardDropEventReturn = null;
        ViewData holdEventReturn = null;
        int currentScore = 0;
        int currentHighScore = 0;

        @Override
        public DownData onDownEvent(MoveEvent event) {
            onDownEventCalled = true;
            return downEventReturn;
        }

        @Override
        public ViewData onLeftEvent(MoveEvent event) {
            onLeftEventCalled = true;
            return leftEventReturn;
        }

        @Override
        public ViewData onRightEvent(MoveEvent event) {
            onRightEventCalled = true;
            return rightEventReturn;
        }

        @Override
        public ViewData onRotateEvent(MoveEvent event) {
            onRotateEventCalled = true;
            return rotateEventReturn;
        }

        @Override
        public DownData onHardDropEvent(MoveEvent event) {
            onHardDropEventCalled = true;
            return hardDropEventReturn;
        }

        @Override
        public ViewData onHoldEvent(MoveEvent event) {
            onHoldEventCalled = true;
            return holdEventReturn;
        }

        @Override
        public void createNewGame() {
            createNewGameCalled = true;
        }

        @Override
        public int getCurrentScore() {
            getCurrentScoreCalled = true;
            return currentScore;
        }

        @Override
        public int getCurrentHighScore() {
            getCurrentHighScoreCalled = true;
            return currentHighScore;
        }
    }
}