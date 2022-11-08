package com.aryzko.scrabble.scrabbledictionary.adapters.api.web;

import com.aryzko.scrabble.scrabbledictionary.domain.service.DictionaryService;
import com.aryzko.scrabble.scrabbledictionary.infrastracture.aspect.PerformanceLog;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @PerformanceLog
    @GetMapping("{language}/lookup/{entry}")
    public Boolean lookup(@PathVariable @NotBlank String language, @PathVariable @NotBlank String entry) {
        return dictionaryService.lookup(language, entry);
    }

    @PerformanceLog
    @GetMapping("lookup/{entry}")
    public Boolean lookupEntry(@PathVariable @NotBlank String entry) {
        return dictionaryService.lookup(entry);
    }

    @PerformanceLog
    @GetMapping("lookup")
    public Map<String, Boolean> lookupEntries(@RequestParam List<String> entries) {
        return dictionaryService.lookup(entries);
    }
}
