package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web;

import com.aryzko.scrabble.scrabbleboardmanager.application.mapper.BoardMapper;
import com.aryzko.scrabble.scrabbleboardmanager.application.mapper.BoardValidationMapper;
import com.aryzko.scrabble.scrabbleboardmanager.application.mapper.SolutionMapper;
import com.aryzko.scrabble.scrabbleboardmanager.application.request.BoardRequest;
import com.aryzko.scrabble.scrabbleboardmanager.application.response.BoardResponse;
import com.aryzko.scrabble.scrabbleboardmanager.application.response.BoardValidationResultResponse;
import com.aryzko.scrabble.scrabbleboardmanager.application.response.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardResolver;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @PostMapping
    public BoardResponse createBoard() {
        return boardMapper.toBoardResponse(boardService.createEmptyBoard());
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
    public BoardValidationResultResponse validate(@RequestBody @Valid BoardRequest board) {
        return boardValidationMapper.toBoardValidationResultResponse(
                boardService.validate(boardMapper.toBoard(board)));
    }

    @PostMapping(value = "resolve")
    public Solution resolve(@RequestBody @Valid BoardRequest board) {
        return solutionMapper.convert(resolver.resolve(boardMapper.toBoard(board)));
    }
}
