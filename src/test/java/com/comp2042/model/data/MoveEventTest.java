package com.comp2042.model.data;

import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MoveEventTest {
    @Test
    void testConstructor_StoresEventType() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        assertEquals(EventType.LEFT, event.getEventType());
    }

    @Test
    void testConstructor_StoresEventSource() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        assertEquals(EventSource.THREAD, event.getEventSource());
    }

    @Test
    void testMultipleEventTypes() {
        MoveEvent rotate = new MoveEvent(EventType.ROTATE, EventSource.USER);
        MoveEvent hardDrop = new MoveEvent(EventType.HARD_DROP, EventSource.USER);

        assertNotEquals(rotate.getEventType(), hardDrop.getEventType());
    }

    @Test
    void testUserEventSource() {
        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
        assertEquals(EventSource.USER, event.getEventSource());
    }

    @Test
    void testThreadEventSource() {
        MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        assertEquals(EventSource.THREAD, event.getEventSource());
    }

    @Test
    void testAllEventTypes() {
        assertNotNull(new MoveEvent(EventType.DOWN, EventSource.USER));
        assertNotNull(new MoveEvent(EventType.LEFT, EventSource.USER));
        assertNotNull(new MoveEvent(EventType.RIGHT, EventSource.USER));
        assertNotNull(new MoveEvent(EventType.ROTATE, EventSource.USER));
        assertNotNull(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
        assertNotNull(new MoveEvent(EventType.HOLD, EventSource.USER));
    }
}