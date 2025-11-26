package com.comp2042.controller.input;

import com.comp2042.enums.EventType;
import com.comp2042.model.data.MoveEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {
    private InputHandler inputHandler;
    private boolean moveCallbackTriggered;
    private boolean rotateCallbackTriggered;
    private boolean pauseCallbackTriggered;
    private EventType lastEventType;

    @BeforeEach
    void setUp() {
        inputHandler = new InputHandler();
        moveCallbackTriggered = false;
        rotateCallbackTriggered = false;
        pauseCallbackTriggered = false;
        lastEventType = null;

        inputHandler.setCallback(new InputHandler.InputCallback() {
            @Override
            public void onMove(MoveEvent event) {
                moveCallbackTriggered = true;
                lastEventType = event.getEventType();
            }

            @Override
            public void onRotate(MoveEvent event) {
                rotateCallbackTriggered = true;
            }

            @Override
            public void onHardDrop(MoveEvent event) {
                // Track hard drop
            }

            @Override
            public void onHold(MoveEvent event) {
                // Track hold
            }

            @Override
            public void onPause() {
                pauseCallbackTriggered = true;
            }

            @Override
            public void onNewGame() {
                // Track new game
            }

            @Override
            public void onReturnToMenu() {
                // Track menu return
            }
        });
    }
}