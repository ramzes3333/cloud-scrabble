package com.aryzko.scrabble.scrabbledictionary.domain.model.dawg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class DawgBuilderTest {

    private final Collator collator = Collator.getInstance(new Locale("pl", "PL"));

    private DawgBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new DawgBuilder();
    }

    @Test
    public void build() {
        //given
        List<String> words = Stream.of("ił", "iły", "iłżecku", "im", "ima", "imacie", "imać")
                .sorted(collator::compare)
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