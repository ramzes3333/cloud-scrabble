package com.aryzko.scrabble.scrabbletilemanager.infrastructure.changelogs;

import com.aryzko.scrabble.scrabbletilemanager.domain.Tile;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileConfiguration;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileSet;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@ChangeUnit(id="initializer", order = "1", author = "mongock")
@AllArgsConstructor
public class InitializerChange {

    private final MongoTemplate mongoTemplate;

    @Execution
    public void changeSet() {
        List<TileConfiguration> tileConfigurations = new ArrayList<>();
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('A').points(1).build()).number(9).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('I').points(1).build()).number(8).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('E').points(1).build()).number(7).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('O').points(1).build()).number(6).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('N').points(1).build()).number(5).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Z').points(1).build()).number(5).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('R').points(1).build()).number(4).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('S').points(1).build()).number(4).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('W').points(1).build()).number(4).build());

        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Y').points(2).build()).number(4).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('C').points(2).build()).number(3).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('D').points(2).build()).number(3).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('K').points(2).build()).number(3).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('L').points(2).build()).number(3).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('M').points(2).build()).number(3).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('P').points(2).build()).number(3).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('T').points(2).build()).number(3).build());

        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('B').points(3).build()).number(2).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('G').points(3).build()).number(2).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('H').points(3).build()).number(2).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('J').points(3).build()).number(2).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(3).build()).number(2).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('U').points(3).build()).number(2).build());

        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('F').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(5).build()).number(1).build());

        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(6).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(7).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('??').points(9).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter(' ').points(0).build()).number(2).build());

        TileSet defaultTileSet = TileSet.builder()
                .tileConfigurations(tileConfigurations)
                .defaultSet(true)
                .name("Polish")
                .version(1)
                .build();

        mongoTemplate.save(defaultTileSet);
    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.dropCollection(TileSet.class);
    }
}
