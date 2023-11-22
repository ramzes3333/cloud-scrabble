package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web;

import com.aryzko.scrabble.scrabbleboardmanager.domain.command.CreateBoardCommand;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardResolver;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardService;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.TilesScoring;
import com.aryzko.scrabble.scrabbleboardmanager.domain.validator.BoardValidationResult;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.common.Bonus;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.mapper.BoardMapper;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.mapper.BoardValidationMapper;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.mapper.SolutionMapper;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.request.BoardRequest;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.BoardResponse;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.BoardValidationResultResponse;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.Solution;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping(value = "/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardResolver resolver;
    private final BoardMapper boardMapper;
    private final SolutionMapper solutionMapper;
    private final BoardValidationMapper boardValidationMapper;
    private final TilesScoring tilesScoring;

    @GetMapping("preview")
    public BoardPreviewResponse preview() {
        return boardMapper.convert(boardService.prepareEmptyBoardPreview());
    }

    @PostMapping
    public BoardResponse createBoard(@RequestBody CreateBoardRequest request) {
        return boardMapper.toBoardResponse(boardService.createEmptyBoard(request.toDomainCommand()));
    }

    @PutMapping
    public BoardResponse updateBoard(@RequestBody @Valid BoardRequest board) {
        return boardMapper.toBoardResponse(boardService.updateBoard(boardMapper.toBoard(board)));
    }

    @GetMapping("{uuid}")
    public BoardResponse getBoard(@PathVariable String uuid) {
        return boardMapper.toBoardResponse(boardService.getBoard(UUID.fromString(uuid)));
    }

    @GetMapping
    public List<BoardResponse> getBoards() {
        return boardService.getBoards().stream()
                        .map(boardMapper::toBoardResponse)
                        .collect(Collectors.toList());
    }

    @PostMapping(value = "validate")
    public ResponseEntity<BoardValidationResultResponse> validate(@RequestBody @Valid BoardRequest board) {
        BoardValidationResult validationResult = boardService.validate(boardMapper.toBoard(board));

        if(validationResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(boardValidationMapper.toBoardValidationResultResponse(validationResult));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(boardValidationMapper.toBoardValidationResultResponse(BoardValidationResult.builder().build()));
        }
    }

    @GetMapping(value = "{uuid}/resolve/{playerId}")
    public Solution resolve(@PathVariable("uuid") String uuid, @PathVariable("playerId") String playerId) {
        return solutionMapper.convert(resolver.resolve(uuid, playerId));
    }

    @PostMapping(value = "resolve/{playerId}")
    public Solution resolve(@PathVariable("playerId") String playerId, @RequestBody @Valid BoardRequest board) {
        return solutionMapper.convert(resolver.resolve(boardMapper.toBoard(board), playerId));
    }

    @PostMapping("{uuid}/score")
    public ScoreResult scoreWord(@PathVariable("uuid") String boardId, @RequestBody Tiles tiles) {
        return ScoreResult.of(tilesScoring.scoreTiles(boardId, tiles.toDomainTiles()), tiles);
    }

    @PostMapping("score")
    public ScoreResult scoreWord(@RequestBody @Valid ScoreRequest scoreRequest) {
        return ScoreResult.of(tilesScoring.scoreTiles(
                boardMapper.toBoard(scoreRequest.board()),
                scoreRequest.tiles().toDomainTiles()), scoreRequest.tiles());
    }

    @Data
    public static class ScoreResult {
        private String word;
        private String tiles;
        private Integer points;

        public static ScoreResult of(Word word, Tiles tiles) {
            ScoreResult result = new ScoreResult();
            result.setWord(ofNullable(word).map(Word::getWordAsString).orElse(""));
            result.setTiles(tiles.getTilesAsString());
            result.setPoints(ofNullable(word).map(Word::getPoints).orElse(0));
            return result;
        }
    }

    @Data
    public static class CreateBoardRequest {
        List<String> playerIds;

        public CreateBoardCommand toDomainCommand() {
            return new CreateBoardCommand(playerIds.stream().toList());
        }
    }

    @Data
    public static class BoardPreviewResponse {
        private List<Field> fields;
        private BoardParameters boardParameters;

        @Data
        public static class Field {
            private Integer x;
            private Integer y;
            private Bonus bonus;
        }

        @Data
        public static class BoardParameters {
            private Integer horizontalSize;
            private Integer verticalSize;
        }
    }

    public record Tiles(List<Tile> tiles) {
        com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tiles toDomainTiles() {
            return new com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tiles(
                    tiles.stream()
                            .map(e -> new com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tile(
                                    e.x(), e.y(), e.letter(), e.blank()))
                            .toList());
        }

        public String getTilesAsString() {
            return ofNullable(tiles).orElse(Collections.emptyList()).stream()
                    .map(Tile::letter)
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        }
    }

    public record Tile(int x, int y, char letter, boolean blank) { }

    public record ScoreRequest(BoardRequest board, Tiles tiles) { }
}
