package com.aryzko.scrabble.scrabbledictionary.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;

/**
 * A DTO for the {@link com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryDb} entity
 */
@Data
public class Dictionary implements Serializable {
    private final Integer id;
    private final String language;
    private final boolean defaultDictionary;
    private final Collection<DictionaryEntry> entries;
}