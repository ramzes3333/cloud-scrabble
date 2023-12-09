package com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AmqpSender {

    public static final String RESULT_EXCHANGE = "result";

    private final RabbitTemplate rabbitTemplate;

    public void sendResult(Result result) {
        String routingKey = extractRoutingKey(result.getClass());
        rabbitTemplate.convertAndSend(RESULT_EXCHANGE, routingKey, result);
    }

    private String extractRoutingKey(Class<?> clazz) {
        if (clazz.isAnnotationPresent(EventMetadata.class)) {
            EventMetadata metadata = clazz.getAnnotation(EventMetadata.class);
            return metadata.routingKey();
        } else {
            throw new IllegalStateException("Routing key not found");
        }
    }
}
