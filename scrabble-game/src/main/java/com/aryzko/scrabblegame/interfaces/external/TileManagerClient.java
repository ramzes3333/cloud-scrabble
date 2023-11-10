package com.aryzko.scrabblegame.interfaces.external;

import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient("tile-manager-service")
public interface TileManagerClient {

    @PostMapping("api/boards/{uuid}/tiles/{numberOfItems}")
    List<TileResponse> getTiles(@PathVariable("uuid") String uuid, @PathVariable("numberOfItems") Integer numberOfItems);

    @Getter
    class TileResponse {
        private Character letter;
        private Integer points;
    }
}
