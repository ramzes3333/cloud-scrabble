package com.aryzko.scrabble.scrabbledictionary.domain.model.dawg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        List<String> words = List.of("gol", "rynna", "nity", "zew", "polska", "nit", "woka").stream()
                .sorted()
                .collect(Collectors.toList());

        //when
        Node node = builder.insert(words).build();

        //then
        assertNotNull(node);
    }

}