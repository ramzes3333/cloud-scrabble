package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("dictionary-service")
public interface DictionaryClient {

    @GetMapping("/api/dictionary/lookup")
    Map<String, Boolean> lookupEntries(@RequestParam("entries") List<String> entries);

    @GetMapping("/api/dawg/fill-gap/{pattern}")
    List<Character> fillGap(@PathVariable("pattern") String pattern);

    @PostMapping("/api/line/resolve")
    Solution resolve(@RequestBody ResolveRequest resolveRequest);

    @Getter
    @AllArgsConstructor
    class ResolveRequest {
        private PreparedLine line;
        private List<Character> availableLetters;

        @Value
        static class PreparedLine {
            List<LineField> fields;

            @Value
            static class LineField {
                int x;
                int y;
                Character letter;
                boolean anchor;
                boolean anyLetter;
                Integer leftLimit;
                List<Character> availableLetters;
            }
        }
    }

    @Getter
    class Solution {

        private List<Word> words;

        @Getter
        static class Word {
            private List<Element> elements;

            @Getter
            static class Element {
                private int x;
                private int y;
                private char letter;
                private boolean blank;
                private boolean unmodifiableLetter;
            }
        }
    }
}
