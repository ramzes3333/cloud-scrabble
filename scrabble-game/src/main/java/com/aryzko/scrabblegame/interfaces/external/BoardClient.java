package com.aryzko.scrabblegame.interfaces.external;

import lombok.Getter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("board-manager-service")
public interface BoardClient {

    @PostMapping("/api/boards")
    BoardResponse createBoard();

    @Getter
    class BoardResponse {
        private String id;
    }
}