package com.aryzko.scrabble.scrabbledictionary.domain.ports;

import com.aryzko.scrabble.scrabbledictionary.domain.model.resolver.Solution;

public interface ResolveResultSender {

    void sendResolvedWord(Solution.Word word);
}
