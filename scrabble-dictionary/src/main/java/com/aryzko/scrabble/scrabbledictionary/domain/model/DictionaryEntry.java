package com.aryzko.scrabble.scrabbledictionary.domain.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DictionaryEntry implements Serializable {
    private final Long id;
    private final String entry;
}