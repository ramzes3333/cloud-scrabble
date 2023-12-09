package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver;

import com.aryzko.scrabble.scrabbleboardmanager.domain.ports.ResolveCommandSender;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.AmqpSender;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.Command;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.EventMetadata;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.Metadata;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.mapper.ResolverMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AmqpResolveCommandSender implements ResolveCommandSender {

    private final ResolverMapper resolverMapper;
    private final AmqpSender sender;

    public void sendResolveCommand(com.aryzko.scrabble.scrabbleboardmanager.domain.ports.ResolveCommandSender.ResolveCommand resolveCommand) {
        sender.sendCommand(resolverMapper.convert(resolveCommand));
    }

    @Value
    @Builder
    @EventMetadata(type = "ResolveCommand", routingKey = "dictionary.command.resolve")
    public static class ResolveCommand implements Command {
        @Metadata(name = "board-id")
        String boardId;
        @Metadata(name = "transposed")
        String transposed;
        @Metadata(name = "login")
        String login;

        List<ResolveCommandSender.LineField> fields;
        List<Character> availableLetters;
    }

    @Value
    @Builder
    public static class LineField {
        int x;
        int y;
        Character letter;
        boolean anchor;
        boolean anyLetter;
        Integer leftLimit;
        List<Character> availableLetters;
    }
}
