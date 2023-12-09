package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.resolver;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class ResolverConfiguration {

    public static final String RESULT_EXCHANGE = "result";
    public static final String RESOLVE_RESULT_ROUTING_KEY = "dictionary.result.resolve";
    public static final String RESOLVE_RESULT_QUEUE = "board-manager.result.resolve";

    @Bean
    public Exchange resultExchange() {
        return ExchangeBuilder.topicExchange(RESULT_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue resolveResultQueue() {
        return new Queue(RESOLVE_RESULT_QUEUE, true);
    }

    public Binding binding(Queue queue, Exchange exchange, String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
    }

    @Bean
    public Declarables resolveResultBinding(Queue queue, Exchange exchange) {
        return new Declarables(Collections.singletonList(binding(queue, exchange, RESOLVE_RESULT_ROUTING_KEY)));
    }
}
