package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web;

import com.aryzko.scrabble.scrabbleboardmanager.domain.command.CreateBoardCommand;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardResolver;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardService;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardResolver resolver;
    private final BoardMapper boardMapper;
    private final SolutionMapper solutionMapper;
    private final BoardValidationMapper boardValidationMapper;

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

    @PostMapping(value = "resolve/{playerId}")
    public Solution resolve(@PathVariable("playerId") String playerId, @RequestBody @Valid BoardRequest board) {
        return solutionMapper.convert(resolver.resolve(playerId, boardMapper.toBoard(board)));
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
}
