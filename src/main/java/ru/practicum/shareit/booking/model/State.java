package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.StateException;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static State parseState(String state) {
        try {
            return State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new StateException(String.format("Unknown state: %s", state));
        }
    }
}
