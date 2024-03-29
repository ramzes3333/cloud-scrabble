package com.aryzko.scrabblegame.application.service.botstrategies;

import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.domain.model.Level;
import org.springframework.stereotype.Component;

@Component
public class NewbieBotWordSearcherStrategy extends BotWordSearcherStrategy {

    public NewbieBotWordSearcherStrategy(BoardProvider boardProvider) {
        super(boardProvider);
    }

    @Override
    public BoardProvider.Word search(Board board, String playerId) {
        return resolve(board, playerId);
    }

    @Override
    public Level level() {
        return Level.NEWBIE;
    }
}
