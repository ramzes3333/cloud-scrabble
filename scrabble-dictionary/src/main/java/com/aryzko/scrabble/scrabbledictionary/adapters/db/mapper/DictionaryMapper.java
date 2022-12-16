package com.aryzko.scrabble.scrabbledictionary.adapters.db.mapper;

import com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryDb;
import com.aryzko.scrabble.scrabbledictionary.domain.model.Dictionary;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DictionaryMapper {
    DictionaryDb dictionaryToDictionaryDb(Dictionary dictionary);

    Dictionary dictionaryDbToDictionary(DictionaryDb dictionaryDb);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DictionaryDb updateDictionaryDbFromDictionary(Dictionary dictionary, @MappingTarget DictionaryDb dictionaryDb);

    @AfterMapping
    default void linkEntries(@MappingTarget DictionaryDb dictionaryDb) {
        dictionaryDb.getEntries().forEach(entry -> entry.setDictionary(dictionaryDb));
    }
}
