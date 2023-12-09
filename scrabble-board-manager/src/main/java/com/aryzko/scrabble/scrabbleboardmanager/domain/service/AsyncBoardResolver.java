package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.mapper.ResolveCommandMapper;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Letter;
import com.aryzko.scrabble.scrabbleboardmanager.domain.ports.ResolveCommandSender;
import com.aryzko.scrabble.scrabbleboardmanager.infrastructure.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AsyncBoardResolver {

    private final BoardService boardService;
    private final LinePreparationService linePreparationService;
    private final ResolveCommandSender resolveCommandSender;
    private final ResolveCommandMapper resolverMapper;
    private final AuthenticationFacade authenticationFacade;

    public void asyncResolve(Board board, String playerId) {
        Board boardFromDb = boardService.getBoard(board.getId());
        board.setBoardParameters(boardFromDb.getBoardParameters());

        if(board.isEmpty()) {
            asyncEmptyBoardResolve(playerId, board);
        } else {
            asyncResolve(playerId, board, false);
            asyncResolve(playerId, board, true);
        }
    }

    private void asyncEmptyBoardResolve(final String playerId, final Board board) {
        resolveCommandSender.sendResolveCommand(ResolveCommandSender.ResolveCommand.builder()
                .boardId(board.getId().toString())
                .transposed("false")
                .login(authenticationFacade.getName())
                .fields(resolverMapper.convert(linePreparationService.prepareStartingLine(board).getFields()))
                .availableLetters(prepareAvailableLetters(playerId, board))
                .build());
    }

    private void asyncResolve(final String playerId, final Board originalBoard, boolean transpose) {
        Board board;
        if (transpose) {
            board = originalBoard.transpose();
        } else {
            board = originalBoard;
        }

        linePreparationService.prepareLines(board).getLines()
            .forEach(line -> {
                resolveCommandSender.sendResolveCommand(ResolveCommandSender.ResolveCommand.builder()
                        .boardId(board.getId().toString())
                        .transposed(String.valueOf(transpose))
                        .login(authenticationFacade.getName())
                        .fields(resolverMapper.convert(line.getFields()))
                        .availableLetters(prepareAvailableLetters(playerId, board))
                        .build());
            });
    }

    private List<Character> prepareAvailableLetters(final String playerId, Board board) {
        return board.getRacks().stream()
                .filter(r -> r.getPlayerId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No rack for player!"))
                .getLetters().stream()
                .map(Letter::getLetter)
                .collect(Collectors.toList());
    }
}
