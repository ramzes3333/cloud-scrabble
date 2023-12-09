package com.aryzko.scrabble.scrabbledictionary.adapters.amqp.resolver;

import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common.AmqpSender;
import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common.Result;
import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common.EventMetadata;
import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.mapper.ResolverMapper;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;
import com.aryzko.scrabble.scrabbledictionary.domain.ports.ResolveResultSender;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AmqpResolveResultSender implements ResolveResultSender {

    private final AmqpSender sender;
    private final ResolverMapper resolverMapper;

    @Override
    public void sendResolvedWord(Solution.Word word) {
        sender.sendResult(resolverMapper.convert(word));
    }

    @Data
    @Builder
    @EventMetadata(type = "Word", routingKey = "dictionary.result.resolve")
    public static class Word implements Result {
        List<Element> elements;
    }

    @Data
    @Builder
    public static class Element {
        int x;
        int y;
        char letter;
        boolean blank;
        boolean unmodifiableLetter;
    }
}
