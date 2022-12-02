package com.aryzko.scrabble.scrabbledictionary.domain.model.dawg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DawgBuilderTest {

    private DawgBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new DawgBuilder();
    }

    @Test
    public void build() {
        //given
        List<String> words = List.of("ił", "iły", "iłżecku", "im", "ima", "imacie", "imać").stream()
                .sorted()
                .collect(Collectors.toList());

        //when
        Node node = builder.insert(words).build();

        //then
        assertNotNull(node);
    }

    @Test
    public void build_incorrectOrder() {
        //given
        List<String> words = List.of("iły", "iłżecku", "im", "ima", "imacie", "imać", "ił");

        //when
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder.insert(words).build());

        //then
    }

}