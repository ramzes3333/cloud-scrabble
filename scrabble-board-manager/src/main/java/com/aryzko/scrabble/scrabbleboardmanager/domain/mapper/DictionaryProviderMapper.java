package com.aryzko.scrabble.scrabbleboardmanager.domain.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DictionaryProviderMapper {
    DictionaryProvider.PreparedLine convert(PreparedLine preparedLine);

    Word convert(DictionaryProvider.Solution.Word word);

    @Mapping(target = "onBoard", source = "unmodifiableLetter")
    Word.Element convert(DictionaryProvider.Solution.Word.Element element);
}
