package com.aryzko.scrabble.scrabbledictionary.adapters.amqp.mapper;

import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.resolver.AmqpResolveListener;
import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.resolver.AmqpResolveResultSender;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Line;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResolverMapper {
    List<Line.LineField> convert(List<AmqpResolveListener.LineField> field);
    Line.LineField convert(AmqpResolveListener.LineField field);
    AmqpResolveResultSender.Word convert(Solution.Word word);
}
