package com.aryzko.scrabblegame.domain.service;

import com.aryzko.scrabblegame.domain.model.BotPlayer;
import com.aryzko.scrabblegame.domain.model.HumanPlayer;
import com.aryzko.scrabblegame.domain.model.Level;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerFactory {

    public List<HumanPlayer> prepareHumanPlayers(List<HumanPlayerCommand> humanPlayers) {
        return humanPlayers.stream().map(player -> HumanPlayer.builder()
                .id(generatePlayerId())
                .points(0)
                .login(player.getLogin())
                .build())
                .collect(Collectors.toList());
    }

    public List<BotPlayer> prepareBotPlayers(List<BotPlayerCommand> botPlayers) {
        return botPlayers.stream().map(player -> BotPlayer.builder()
                .id(generatePlayerId())
                .points(0)
                .level(Level.valueOf(player.getLevel().name()))
                .build())
                .collect(Collectors.toList());
    }

    private static String generatePlayerId() {
        return UUID.randomUUID().toString();
    }

    @Value
    @Builder
    public static class BotPlayerCommand {
        Level level;

        public enum Level {
            NEWBIE,
            BEGINNER,
            ADVANCED,
            EXPERT,
            LEGEND;
        }
    }

    @Value
    @Builder
    public static class HumanPlayerCommand {
        String login;
    }
}
