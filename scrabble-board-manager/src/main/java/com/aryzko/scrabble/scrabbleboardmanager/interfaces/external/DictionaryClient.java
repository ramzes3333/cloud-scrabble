package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("dictionary-service")
public interface DictionaryClient {

    @GetMapping("/dictionary/api/dictionary/lookup")
    Map<String, Boolean> lookupValues(@RequestParam List<String> values);
}
