package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.exception.InvalidStateException;

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
            throw new InvalidStateException(String.format("Unknown state: %s", state));
        }
    }
}
