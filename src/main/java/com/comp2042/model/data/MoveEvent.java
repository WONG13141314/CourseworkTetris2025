package com.comp2042.model.data;

import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;

/**
 * Data class representing a game movement event.
 * Contains the type of movement and its source.
 */
public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a movement event.
     *
     * @param eventType the type of movement action
     * @param eventSource the source of the event (user or thread)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the event type.
     *
     * @return the type of movement
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the event source.
     *
     * @return the source of the event
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}