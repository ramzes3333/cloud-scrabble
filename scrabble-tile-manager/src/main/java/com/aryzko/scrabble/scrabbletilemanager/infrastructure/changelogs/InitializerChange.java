package com.aryzko.scrabble.scrabbletilemanager.infrastructure.changelogs;

import com.aryzko.scrabble.scrabbletilemanager.domain.Tile;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileConfiguration;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileSet;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.aryzko.scrabble.scrabbletilemanager.domain.TileSet.TILE_SET_COLLECTION_NAME;

@ChangeUnit(id="initializer", order = "1", author = "mongock")
@AllArgsConstructor
public class InitializerChange {

    private final MongoTemplate mongoTemplate;

    @BeforeExecution
    public void beforeExecution(MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection(TILE_SET_COLLECTION_NAME);
    }

    @Execution
    public void execution() {
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
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ł').points(3).build()).number(2).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('U').points(3).build()).number(2).build());

        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ą').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ę').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('F').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ó').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ś').points(5).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ż').points(5).build()).number(1).build());

        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ć').points(6).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ń').points(7).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter('Ź').points(9).build()).number(1).build());
        tileConfigurations.add(TileConfiguration.builder().tile(Tile.builder().letter(' ').points(0).build()).number(2).build());

        TileSet defaultTileSet = TileSet.builder()
                .tileConfigurations(tileConfigurations)
                .defaultSet(true)
                .name("Polish")
                .version(1)
                .build();

        mongoTemplate.insert(defaultTileSet);
    }

    @RollbackExecution
    public void rollbackExecution() {
        mongoTemplate.remove(new Document(), TILE_SET_COLLECTION_NAME);
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution() {
        mongoTemplate.dropCollection(TILE_SET_COLLECTION_NAME);
    }
}
