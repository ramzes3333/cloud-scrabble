package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import lombok.Data;
import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FeignClient("tile-manager-service")
public interface TileManagerClient {

    @GetMapping("api/boards/{uuid}/charset")
    List<Character> getCharset(@PathVariable("uuid") String uuid);

    @GetMapping("api/boards/{uuid}/tile-configuration")
    TileConfigurationResponse getTileConfiguration(@PathVariable("uuid") String uuid);

    @Getter
    class TileConfigurationResponse {
        private List<Tile> tiles;

        public record Tile(Character letter, Integer points, Integer number) { }
    }
}
