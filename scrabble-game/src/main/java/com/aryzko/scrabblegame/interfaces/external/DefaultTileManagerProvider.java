package com.aryzko.scrabblegame.interfaces.external;

import com.aryzko.scrabblegame.application.provider.TileProvider;
import com.aryzko.scrabblegame.application.provider.model.Tile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultTileManagerProvider implements TileProvider {

    private final TileManagerClient tileManagerClient;
    private final TileMapper tileMapper;

    @Override
    public List<Tile> getTiles(String uuid, Integer numberOfItems) {
        return tileMapper.convert(tileManagerClient.getTiles(uuid, numberOfItems));
    }
}
