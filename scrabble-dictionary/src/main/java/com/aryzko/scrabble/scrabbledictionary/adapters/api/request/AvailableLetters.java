package com.aryzko.scrabble.scrabbledictionary.adapters.api.request;

import lombok.Getter;

import java.util.List;

@Getter
public class AvailableLetters {
    private List<Character> characters;
}
