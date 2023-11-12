package com.aryzko.scrabble.scrabbleboardmanager.interfaces.external;

import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient("tile-manager-service")
public interface TileManagerClient {

    @GetMapping("api/boards/{uuid}/charset")
    List<Character> getCharset(@PathVariable("uuid") String uuid);

    @PostMapping("api/boards/{uuid}/tiles/{numberOfItems}")
    List<TileResponse> getTiles(@PathVariable("uuid") String uuid, @PathVariable("numberOfItems") Integer numberOfItems);

    @GetMapping("api/boards/{uuid}/tile-configuration")
    TileConfigurationResponse getTileConfiguration(@PathVariable("uuid") String uuid);

    @Getter
    class TileConfigurationResponse {
        private List<Tile> tiles;

        public record Tile(Character letter, Integer points, Integer number) { }
    }

    @Getter
    class TileResponse {
        private Character letter;
        private Integer points;
    }
}
