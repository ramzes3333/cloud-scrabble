package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.TransposeType;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardService;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.RelatedWordsFillService;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.ScoringService;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.Metadata;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.EventMetadata;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.mapper.ResolverMapper;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.common.aspect.LoggedAsSystemUser;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.common.aspect.PerformanceLog;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.stomp.NotificationController;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver.ResolverConfiguration.RESOLVE_RESULT_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResolveResultListener {

    private final Gson gson = new Gson();

    private final RelatedWordsFillService relatedWordsFillService;
    private final ScoringService scoringService;
    private final BoardService  boardService;
    private final ResolverMapper wordMapper;

    private final NotificationController notificationController;

    @PerformanceLog
    @LoggedAsSystemUser
    @RabbitListener(queues = RESOLVE_RESULT_QUEUE)
    public void receiveMessage(Word word) {
        Board board = boardService.getBoard(UUID.fromString(word.boardId));

        if(word.getTransposed().equals("true")) {
            board = board.transpose();
        }

        com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word domainWord = wordMapper.convert(word);

        domainWord.setRelatedWords(relatedWordsFillService.getRelatedWords(board, domainWord));
        scoringService.score(board, domainWord);

        if(word.getTransposed().equals("true")) {
            domainWord = domainWord.transpose();
        }

        notificationController.sendSolutionWord(word.getLogin(), gson.toJson(wordMapper.convert(domainWord)));
    }

    @Data
    @EventMetadata(type = "Word")
    public static class Word {
        @Metadata(name = "board-id")
        private String boardId;
        @Metadata(name = "transposed")
        private String transposed;
        @Metadata(name = "login")
        private String login;

        private final List<Element> elements;
    }

    @Data
    public static final class Element {
        private final int x;
        private final int y;
        private final char letter;
        private final boolean blank;
        private final boolean unmodifiableLetter;
    }
}
