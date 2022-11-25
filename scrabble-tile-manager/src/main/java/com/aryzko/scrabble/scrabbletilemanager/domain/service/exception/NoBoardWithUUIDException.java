package com.aryzko.scrabble.scrabbletilemanager.domain.service.exception;

import java.util.UUID;

import static java.lang.String.format;

public class NoBoardWithUUIDException extends RuntimeException {
    public NoBoardWithUUIDException(UUID boardId) {
        super(format("No board with uuid: %s", boardId.toString()));
    }
}
