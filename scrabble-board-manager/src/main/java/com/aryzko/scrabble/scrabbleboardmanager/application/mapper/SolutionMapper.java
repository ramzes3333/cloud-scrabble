package com.aryzko.scrabble.scrabbleboardmanager.application.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.external.DictionaryClient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolutionMapper {

    com.aryzko.scrabble.scrabbleboardmanager.application.response.Solution.Element convert(Solution.Word.Element element);
    com.aryzko.scrabble.scrabbleboardmanager.application.response.Solution.Word convert(Solution.Word word);
    com.aryzko.scrabble.scrabbleboardmanager.application.response.Solution convert(Solution solution);
}
