package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.model;

import java.util.List;

public record Word(int points, List<Element> elements, List<Word> relatedWords, List<Bonus> bonuses) {
}
