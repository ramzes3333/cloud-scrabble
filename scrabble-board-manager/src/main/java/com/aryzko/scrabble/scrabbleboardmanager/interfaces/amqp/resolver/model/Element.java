package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.model;

public record Element(int x, int y, char letter, int points, boolean blank, boolean onBoard) {
}
