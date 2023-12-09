package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class Solution {
    private List<Word> words;

    public Solution transpose() {
        return Solution.builder()
                .words(words.stream()
                        .map(Word::transpose)
                        .collect(Collectors.toList()))
                .build();
    }
}
