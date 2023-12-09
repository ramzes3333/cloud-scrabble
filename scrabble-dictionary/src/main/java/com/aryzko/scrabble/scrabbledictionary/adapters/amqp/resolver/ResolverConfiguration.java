package com.aryzko.scrabble.scrabbledictionary.adapters.amqp.resolver;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class ResolverConfiguration {

    public static final String COMMAND_EXCHANGE = "command";
    public static final String RESOLVE_ROUTING_KEY = "dictionary.command.resolve";
    public static final String RESOLVE_QUEUE = "dictionary.command.resolve";

    @Bean
    public Exchange commandExchange() {
        return ExchangeBuilder.topicExchange(COMMAND_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue resolveQueue() {
        return new Queue(RESOLVE_QUEUE, true);
    }

    public Binding binding(Queue queue, Exchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

    @Bean
    public Declarables resolveRequestBinding(Queue queue, Exchange exchange) {
        return new Declarables(Collections.singletonList(binding(queue, exchange, RESOLVE_ROUTING_KEY)));
    }
}
