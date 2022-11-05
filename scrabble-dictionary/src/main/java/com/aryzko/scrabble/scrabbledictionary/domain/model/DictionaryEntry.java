package com.aryzko.scrabble.scrabbledictionary.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.aryzko.scrabble.scrabbledictionary.adapters.db.model.DictionaryEntryDb} entity
 */
@Data
public class DictionaryEntry implements Serializable {
    private final Long id;
    private final String value;
}