package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DictionaryMapper {

    @Mapping(target = "onBoard", source = "unmodifiableLetter")
    Solution.Word.Element convert(DictionaryClient.Solution.Word.Element element);

    Solution.Word convert(DictionaryClient.Solution.Word word);

    Solution convert(DictionaryClient.Solution solution);
}
