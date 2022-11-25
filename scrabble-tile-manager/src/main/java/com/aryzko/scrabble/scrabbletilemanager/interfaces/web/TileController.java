package com.aryzko.scrabble.scrabbletilemanager.interfaces.web;

import com.aryzko.scrabble.scrabbletilemanager.application.mapper.TileMapper;
import com.aryzko.scrabble.scrabbletilemanager.application.response.TileConfigurationResponse;
import com.aryzko.scrabble.scrabbletilemanager.application.response.TileResponse;
import com.aryzko.scrabble.scrabbletilemanager.domain.service.TileService;
import com.aryzko.scrabble.scrabbletilemanager.domain.service.exception.NoBoardWithUUIDException;
import com.aryzko.scrabble.scrabbletilemanager.interfaces.web.error.RestErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class TileController {

    private static final String NO_BOARD_WITH_UUID = "E0000";

    private final TileService tileService;
    private final TileMapper tileMapper;

    @PostMapping("boards/{uuid}/tiles/{numberOfItems}")
    public List<TileResponse> getTiles(@PathVariable String uuid, @PathVariable Integer numberOfItems) {
        return tileService.get(UUID.fromString(uuid), numberOfItems).stream()
                .map(tileMapper::tileToTileResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("boards/{uuid}/charset")
    public List<Character> getCharset(@PathVariable String uuid,
                                      @RequestParam(required = false, defaultValue = "false") Boolean withoutBlank) {
        return tileService.getCharset(UUID.fromString(uuid), withoutBlank);
    }

    @GetMapping("boards/{uuid}/tile-configuration")
    public TileConfigurationResponse getCharset(@PathVariable String uuid) {
        return TileConfigurationResponse.of(tileService.getTileConfigurations(UUID.fromString(uuid)));
    }

    @ExceptionHandler(NoBoardWithUUIDException.class)
    public ResponseEntity<RestErrorResponse> handleException(NoBoardWithUUIDException e) {

        return ResponseEntity.status(SERVICE_UNAVAILABLE)
                .contentType(APPLICATION_JSON)
                .body(RestErrorResponse.builder()
                        .error(RestErrorResponse.RestError.of(NO_BOARD_WITH_UUID, e.getMessage()))
                        .build());
    }
}
