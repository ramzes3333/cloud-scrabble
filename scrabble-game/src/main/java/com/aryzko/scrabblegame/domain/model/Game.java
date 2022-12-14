package com.aryzko.scrabblegame.domain.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.annotation.Version;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Game {
    private Long id;
    private OffsetDateTime creationDate;
    private State state;
    private UUID boardId;
    private List<Player> players = new ArrayList<>();
    private String currentId;
    private String winnerId;
}
