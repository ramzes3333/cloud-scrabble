package com.aryzko.scrabble.scrabbletilemanager.infrastracture.web;

import com.aryzko.scrabble.scrabbletilemanager.application.mapper.TileMapper;
import com.aryzko.scrabble.scrabbletilemanager.application.response.TileResponse;
import com.aryzko.scrabble.scrabbletilemanager.domain.service.TileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class TileController {

    private final TileService tileService;
    private final TileMapper tileMapper;

    @PostMapping("boards/{uuid}/tiles/{numberOfItems}")
    public List<TileResponse> getTiles(@PathVariable String uuid, @PathVariable Integer numberOfItems) {
        return tileService.get(UUID.fromString(uuid), numberOfItems).stream()
                .map(tileMapper::tileToTileResponse)
                .collect(Collectors.toList());
    }
}
