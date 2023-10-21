package com.aryzko.scrabble.scrabbleboardmanager.domain.command;

import lombok.Value;

import java.util.List;

@Value
public class CreateBoardCommand {
    private List<String> playerIds;
}
