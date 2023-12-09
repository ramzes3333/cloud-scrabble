package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@JsonIgnore
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metadata {
    String name();
}
