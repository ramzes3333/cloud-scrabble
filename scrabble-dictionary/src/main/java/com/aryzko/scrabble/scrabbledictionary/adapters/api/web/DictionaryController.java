package com.aryzko.scrabble.scrabbledictionary.adapters.api.web;

import com.aryzko.scrabble.scrabbledictionary.domain.service.DictionaryService;
import com.aryzko.scrabble.scrabbledictionary.infrastracture.aspect.PerformanceLog;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping(value = "/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @PerformanceLog
    @GetMapping("{language}/lookup/{value}")
    public Boolean lookup(@PathVariable @NotBlank String language, @PathVariable @NotBlank String value) {
        return dictionaryService.lookup(language, value);
    }

    @PerformanceLog
    @GetMapping("lookup/{value}")
    public Boolean lookup(@PathVariable @NotBlank String value) {
        return dictionaryService.lookup(value);
    }
}
