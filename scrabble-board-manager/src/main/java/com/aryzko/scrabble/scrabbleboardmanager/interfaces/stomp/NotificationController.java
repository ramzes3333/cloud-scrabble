package com.aryzko.scrabble.scrabbleboardmanager.interfaces.stomp;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendSolutionWord(String userId, String wordJson) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/solution-words", wordJson);
    }
}
