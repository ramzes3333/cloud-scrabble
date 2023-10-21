package com.aryzko.scrabblegame.interfaces.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@FeignClient("board-manager-service")
public interface BoardClient {

    @PostMapping("/api/boards")
    BoardResponse createBoard(CreateBoardRequest createBoardRequest);

    @GetMapping("/api/boards")
    BoardResponse getBoard(UUID id);

    @GetMapping("/api/boards/validate")
    BoardValidationResultResponse validateBoard(BoardRequest boardRequest);

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

    @Getter
    class Field {
        private Integer x;
        private Integer y;
        private Bonus bonus;
        private Letter letter;
    }

    @Getter
    class Rack {
        private String playerId;
        private List<Letter> letters;
    }

    @Getter
    class Letter {
        private Character letter;
        private Integer points;
        private boolean blank;
    }

    @Getter
    class BoardParameters {
        private Integer horizontalSize;
        private Integer verticalSize;
    }

    enum Bonus {
        DoubleWordScore,
        TripleWordScore,
        DoubleLetterScore,
        TripleLetterScore,
        None,
    }
}