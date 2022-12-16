package com.aryzko.scrabblegame.application.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameStartResponse {
    private Long id;
    private String boardId;
}
