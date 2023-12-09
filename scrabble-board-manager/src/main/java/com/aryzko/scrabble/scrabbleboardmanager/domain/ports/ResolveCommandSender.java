package com.aryzko.scrabble.scrabbleboardmanager.domain.ports;

import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.Command;
import lombok.Builder;
import lombok.Value;

import java.util.List;

public interface ResolveCommandSender {

    void sendResolveCommand(ResolveCommand resolveCommand);

    @Value
    @Builder
    class ResolveCommand implements Command {
        String boardId;
        String transposed;
        String login;

        List<LineField> fields;
        List<Character> availableLetters;
    }

    @Value
    @Builder
    class LineField {
        int x;
        int y;
        Character letter;
        boolean anchor;
        boolean anyLetter;
        Integer leftLimit;
        List<Character> availableLetters;
    }
}
