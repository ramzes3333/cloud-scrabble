package com.aryzko.scrabble.scrabbledictionary.adapters.api.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ResolveRequest {
    private Line line;
    private List<Character> availableLetters;
}
