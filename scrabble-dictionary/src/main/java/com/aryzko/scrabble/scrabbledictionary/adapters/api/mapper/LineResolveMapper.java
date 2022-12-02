package com.aryzko.scrabble.scrabbledictionary.adapters.api.mapper;

import com.aryzko.scrabble.scrabbledictionary.adapters.api.request.Line;
import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LineResolveMapper {
    com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Line convert(Line line);

    com.aryzko.scrabble.scrabbledictionary.adapters.api.response.Solution.Word.Element convert(Solution.Word.Element element);
    com.aryzko.scrabble.scrabbledictionary.adapters.api.response.Solution.Word convert(Solution.Word word);
    com.aryzko.scrabble.scrabbledictionary.adapters.api.response.Solution convert(Solution line);
}
