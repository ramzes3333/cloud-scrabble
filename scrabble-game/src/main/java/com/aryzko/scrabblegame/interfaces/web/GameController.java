package com.aryzko.scrabblegame.interfaces.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/game")
@RequiredArgsConstructor
public class GameController {

    @GetMapping("test")
    public Boolean test() {
        return true;
    }
}
