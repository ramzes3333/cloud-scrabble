package com.aryzko.scrabblegame.application.service.moveperformer;

import com.aryzko.scrabblegame.application.provider.BoardProvider;
import com.aryzko.scrabblegame.application.provider.TileProvider;
import org.springframework.stereotype.Component;

@Component
public class HumanMovePerformer extends PlayerMovePerformer {

    public HumanMovePerformer(BoardProvider boardProvider, TileProvider tileProvider) {
        super(boardProvider, tileProvider);
    }
}
