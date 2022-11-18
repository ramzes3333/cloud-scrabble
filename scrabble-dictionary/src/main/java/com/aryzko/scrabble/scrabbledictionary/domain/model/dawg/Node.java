package com.aryzko.scrabble.scrabbledictionary.domain.model.dawg;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Node {
    private final int id;
    private boolean terminal;
    private SortedMap<Character, Node> transitions = new TreeMap<>();

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", terminal=" + terminal +
                ", transitions=" + transitions.keySet().stream().map(String::valueOf).collect(Collectors.joining(", ")) +
                '}';
    }
}
