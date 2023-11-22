package com.aryzko.scrabblegame.application.service.moveperformer;

import com.aryzko.scrabblegame.application.model.BoardTile;
import com.aryzko.scrabblegame.application.model.MoveResult;
import com.aryzko.scrabblegame.application.model.RackTile;
import com.aryzko.scrabblegame.application.model.board.Board;
import com.aryzko.scrabblegame.application.model.board.BoardValidationResultResponse;
import com.aryzko.scrabblegame.application.model.board.Field;
import com.aryzko.scrabblegame.application.model.board.Letter;
import com.aryzko.scrabblegame.application.model.board.Rack;
import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.application.provider.TileProvider;
import com.aryzko.scrabblegame.application.provider.model.Tile;
import com.aryzko.scrabblegame.application.response.GameFailure;
import com.aryzko.scrabblegame.domain.model.Game;
import com.aryzko.scrabblegame.domain.model.Move;
import com.aryzko.scrabblegame.domain.model.Player;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.aryzko.scrabblegame.application.response.GameFailure.INCORRECT_MOVE_REQUEST;

@Component
public class HumanMovePerformer extends PlayerMovePerformer {

    public HumanMovePerformer(BoardProvider boardProvider, TileProvider tileProvider) {
        super(boardProvider, tileProvider);
    }
}
