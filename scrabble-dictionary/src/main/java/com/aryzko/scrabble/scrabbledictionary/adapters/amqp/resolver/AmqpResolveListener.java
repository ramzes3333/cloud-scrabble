package com.aryzko.scrabble.scrabbledictionary.adapters.amqp.resolver;

import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common.Metadata;
import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common.EventMetadata;
import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.mapper.ResolverMapper;
import com.aryzko.scrabble.scrabbledictionary.domain.exception.DawgIsNotReady;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.AvailableLetters;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Line;
import com.aryzko.scrabble.scrabbledictionary.domain.service.LineResolver;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.aryzko.scrabble.scrabbledictionary.adapters.amqp.resolver.ResolverConfiguration.RESOLVE_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmqpResolveListener {

    private final LineResolver lineResolver;
    private final ResolverMapper resolverMapper;

    @RabbitListener(queues = RESOLVE_QUEUE)
    public void receiveMessage(ResolveCommand resolveCommand) {
        try {
            lineResolver.asyncResolve(
                    new Line(resolverMapper.convert(resolveCommand.getFields())),
                    AvailableLetters.builder()
                    .characters(resolveCommand.getAvailableLetters())
                    .build());
        } catch (DawgIsNotReady e) {
            log.error("DawgIsNotReady");
        }
    }

    @Data
    @Builder
    @EventMetadata(type = "ResolveCommand")
    public static class ResolveCommand {
        private List<LineField> fields;
        private List<Character> availableLetters;
    }

    @Data
    @Builder
    public static class LineField {
        private int x;
        private int y;
        private Character letter;
        private boolean anchor;
        private boolean anyLetter;
        private Integer leftLimit;
        private List<Character> availableLetters;
    }
}
