package com.aryzko.scrabble.scrabbledictionary.adapters.db.mapper;

import com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryEntryDb;
import com.aryzko.scrabble.scrabbledictionary.domain.model.DictionaryEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictionaryEntryMapper {
    DictionaryEntryDb dictionaryEntryToDictionaryEntryDb(DictionaryEntry dictionary);

    DictionaryEntry dictionaryEntryDbToDictionaryEntry(DictionaryEntryDb dictionaryDb);
}
