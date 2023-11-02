package com.aryzko.scrabble.scrabbleboardmanager.domain.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictionaryProviderMapper {
    DictionaryProvider.PreparedLine convert(PreparedLine preparedLine);

    Solution.Word convert(DictionaryProvider.Solution.Word word);
    Solution.Word.Element convert(DictionaryProvider.Solution.Word.Element element);
}
