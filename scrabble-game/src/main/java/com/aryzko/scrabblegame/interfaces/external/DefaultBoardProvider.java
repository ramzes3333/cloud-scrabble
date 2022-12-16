package com.aryzko.scrabblegame.interfaces.external;

import com.aryzko.scrabblegame.domain.provider.BoardProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultBoardProvider implements BoardProvider {

    private final BoardClient boardClient;

    @Override
    public String createBoard() {
        return boardClient.createBoard().getId();
    }
}
