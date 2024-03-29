package com.aryzko.scrabblegame.interfaces.external;

import com.aryzko.scrabblegame.application.provider.BoardProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.OffsetDateTime;
import java.util.List;

@FeignClient("board-manager-service")
public interface BoardClient {

    @PostMapping("/api/boards")
    BoardResponse createBoard(CreateBoardRequest createBoardRequest);

    @PutMapping("/api/boards")
    BoardResponse updateBoard(BoardRequest boardRequest);

    @GetMapping("/api/boards/{uuid}")
    BoardResponse getBoard(@PathVariable("uuid") String id);

    @GetMapping("/api/boards/validate")
    BoardValidationResultResponse validateBoard(BoardRequest boardRequest);

    @PostMapping("/api/boards/{uuid}/score")
    ScoreResult score(@PathVariable("uuid") String id, Tiles tiles);

    @PostMapping("/api/boards/score")
    ScoreResult score(ScoreRequest scoreRequest);

    @PostMapping("/api/boards/resolve/{playerId}")
    Solution resolve(@PathVariable("playerId") String playerId, BoardRequest boardRequest);

    record Tiles (List<Tile> tiles) { }
    record Tile (int x, int y, char letter, boolean blank) { }

    record ScoreResult (String word, String tiles, Integer points) { }

    @Value
    class BoardValidationResultResponse {

        private final List<CharacterSequence> incorrectWords;
        private final List<CharacterWithPosition> orphans;

        public record CharacterSequence(List<CharacterWithPosition> characters) {
        }

        public record CharacterWithPosition(Integer x, Integer y, Character character) {
        }
    }

    @Data
    @AllArgsConstructor
    class CreateBoardRequest {
        private List<String> playerIds;
    }

    @Data
    @AllArgsConstructor
    class BoardRequest {
        private String id;
        private List<Field> fields;
        private List<Rack> racks;
    }

    @Getter
    class BoardResponse {
        private String id;
        private OffsetDateTime creationDate;
        private List<Field> fields;
        private List<Rack> racks;
        private BoardParameters boardParameters;
    }

    @Data
    class Field {
        private Integer x;
        private Integer y;
        private Bonus bonus;
        private Letter letter;
    }

    @Data
    class Rack {
        private String playerId;
        private List<Letter> letters;
    }

    @Data
    class Letter {
        private Character letter;
        private Integer points;
        private boolean blank;
    }

    @Data
    class BoardParameters {
        private Integer horizontalSize;
        private Integer verticalSize;
        private Integer rackSize;
    }

    record Solution(List<Word> words) { }
    record Word (int points, List<Element> elements, List<Word> relatedWords, List<Bonus> bonuses) { }
    record Element (int x, int y, char letter, int points, boolean blank, boolean onBoard) { }

    enum Bonus {
        DoubleWordScore,
        TripleWordScore,
        DoubleLetterScore,
        TripleLetterScore,
        None,
    }

    record ScoreRequest(BoardRequest board, Tiles tiles) { }
}