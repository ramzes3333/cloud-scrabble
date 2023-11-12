package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface DictionaryMapper {

    DictionaryProvider.Solution.Word.Element convert(DictionaryClient.Solution.Word.Element element);

    DictionaryProvider.Solution.Word convert(DictionaryClient.Solution.Word word);

    DictionaryProvider.Solution convert(DictionaryClient.Solution solution);

    DictionaryClient.ResolveRequest.PreparedLine convert(DictionaryProvider.PreparedLine preparedLine);
    DictionaryClient.ResolveRequest.PreparedLine.LineField convert(DictionaryProvider.PreparedLine.LineField lineField);
}
