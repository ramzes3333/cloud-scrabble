package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AmqpSender {

    public static final String COMMAND_EXCHANGE = "command";

    private final RabbitTemplate rabbitTemplate;

    public void sendCommand(Command command) {
        String routingKey = extractRoutingKey(command.getClass());
        rabbitTemplate.convertAndSend(COMMAND_EXCHANGE, routingKey, command);
    }

    private String extractRoutingKey(Class<?> commandClass) {
        if (commandClass.isAnnotationPresent(EventMetadata.class)) {
            EventMetadata metadata = commandClass.getAnnotation(EventMetadata.class);
            return metadata.routingKey();
        } else {
            throw new IllegalStateException("Routing key not found");
        }
    }
}
