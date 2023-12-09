package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.domain.ports.ResolveCommandSender;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.AmqpResolveCommandSender;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.ResolveResultListener;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.model.Bonus;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.model.Element;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.model.Word;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResolverMapper {

    @Mapping(target = "onBoard", source = "unmodifiableLetter")
    com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word.Element convert(ResolveResultListener.Element element);
    com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word convert(ResolveResultListener.Word word);

    Bonus convert(com.aryzko.scrabble.scrabbleboardmanager.domain.model.Bonus bonus);
    Element convert(com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word.Element element);
    Word convert(com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word word);

    AmqpResolveCommandSender.ResolveCommand  convert(com.aryzko.scrabble.scrabbleboardmanager.domain.ports.ResolveCommandSender.ResolveCommand resolveCommand);
    AmqpResolveCommandSender.LineField  convert(com.aryzko.scrabble.scrabbleboardmanager.domain.ports.ResolveCommandSender.LineField resolveCommand);
}
