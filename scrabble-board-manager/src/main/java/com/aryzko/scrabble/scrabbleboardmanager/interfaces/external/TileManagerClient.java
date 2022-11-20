package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("tile-manager-service")
public interface TileManagerClient {

    @GetMapping("boards/{uuid}/charset")
    List<Character> getCharset(@PathVariable("uuid") String uuid);
}
