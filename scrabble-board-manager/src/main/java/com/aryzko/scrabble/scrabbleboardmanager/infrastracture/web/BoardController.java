package com.aryzko.scrabble.scrabbleboardmanager.infrastracture.web;

import com.aryzko.scrabble.scrabbleboardmanager.application.mapper.BoardMapper;
import com.aryzko.scrabble.scrabbleboardmanager.application.request.BoardRequest;
import com.aryzko.scrabble.scrabbleboardmanager.application.response.BoardResponse;
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
    private final BoardMapper boardMapper;

    @PostMapping
    public BoardResponse createBoard() {
        return boardMapper.boardToBoardResponse(boardService.createEmptyBoard());
    }

    @PutMapping
    public BoardResponse updateBoard(@RequestBody @Valid BoardRequest board) {
        return boardMapper.boardToBoardResponse(boardService.updateBoard(boardMapper.boardRequestToBoard(board)));
    }

    @GetMapping("{uuid}")
    public BoardResponse getBoard(@PathVariable String uuid) {
        return boardMapper.boardToBoardResponse(boardService.getBoard(UUID.fromString(uuid)));
    }

    @GetMapping
    public List<BoardResponse> getBoards() {
        return boardService.getBoards().stream()
                        .map(boardMapper::boardToBoardResponse)
                        .collect(Collectors.toList());
    }
}
