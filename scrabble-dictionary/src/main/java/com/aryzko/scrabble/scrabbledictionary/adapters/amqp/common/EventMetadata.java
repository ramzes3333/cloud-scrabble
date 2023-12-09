package com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventMetadata {
    String UNASSIGNED_VALUE = "[unassigned]";

    String type();
    String routingKey() default UNASSIGNED_VALUE;
}