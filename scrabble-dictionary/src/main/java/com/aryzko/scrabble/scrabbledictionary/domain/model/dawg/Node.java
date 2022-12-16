package com.aryzko.scrabble.scrabbledictionary.domain.model.dawg;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Node(@JsonProperty("id") int id,
                @JsonProperty("terminal") boolean terminal,
                @JsonProperty("transitions") SortedMap<Character, Node> transitions) {
        this.id = id;
        this.terminal = terminal;
        this.transitions = transitions;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", terminal=" + terminal +
                ", transitions=" + transitions.keySet().stream().map(String::valueOf).collect(Collectors.joining(", ")) +
                '}';
    }
}
