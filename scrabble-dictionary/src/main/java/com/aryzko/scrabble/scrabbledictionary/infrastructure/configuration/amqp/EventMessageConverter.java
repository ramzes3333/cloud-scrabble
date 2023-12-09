package com.aryzko.scrabble.scrabbledictionary.infrastructure.configuration.amqp;

import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common.Metadata;
import com.aryzko.scrabble.scrabbledictionary.adapters.amqp.common.EventMetadata;
import com.aryzko.scrabble.scrabbledictionary.adapters.common.MetadataHolder;
import com.nimbusds.jose.shaded.gson.Gson;
import org.reflections.Reflections;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class EventMessageConverter extends AbstractMessageConverter {

    private static final String X_METADATA = "x-metadata-%s";

    private final Gson gson = new Gson();
    private final Reflections reflections = new Reflections("com.aryzko.scrabble.scrabbledictionary");

    @Override
    protected Message createMessage(Object objectToConvert, MessageProperties messageProperties) {
        EventMetadata metadata = objectToConvert.getClass().getAnnotation(EventMetadata.class);
        if (metadata != null) {
            messageProperties.setHeader("EventType", metadata.type());
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            setMetadataHeaders(messageProperties, objectToConvert);
        }
        byte[] bytes = gson.toJson(objectToConvert).getBytes();
        return new Message(bytes, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        String eventType = message.getMessageProperties().getHeader("EventType");
        String correlationId = message.getMessageProperties().getCorrelationId();
        Class<?> clazz = findClassForType(eventType);
        String json = new String(message.getBody());
        Object object = gson.fromJson(json, clazz);
        setMetadata(message.getMessageProperties(), object);
        return object;
    }

    private Class<?> findClassForType(String eventType) {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(EventMetadata.class).stream()
                .filter(clazz -> clazz.getAnnotation(EventMetadata.class).type().equals(eventType))
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .collect(Collectors.toSet());

        if (classes.size() == 1) {
            return classes.iterator().next();
        } else {
            throw new MessageConversionException("Class with type (%s) not found: ".formatted(eventType));
        }
    }

    private void setMetadataHeaders(MessageProperties messageProperties, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        Set<String> objectMetadataKeys = Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Metadata.class))
                .map(field -> field.getAnnotation(Metadata.class).name())
                .collect(Collectors.toSet());

        for (String key : objectMetadataKeys) {
            try {
                Field field = object.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object value = field.get(object);

                if (value != null) {
                    messageProperties.setHeader(String.format(X_METADATA, key), value.toString());
                } else {
                    String metadataValue = MetadataHolder.getMetadataValue(key);
                    if (metadataValue != null) {
                        messageProperties.setHeader(String.format(X_METADATA, key), metadataValue);
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Error processing field: " + key, e);
            }
        }

        for (String key : MetadataHolder.getAllMetadataKeys()) {
            if (!objectMetadataKeys.contains(key)) {
                String value = MetadataHolder.getMetadataValue(key);
                if (value != null) {
                    messageProperties.setHeader(String.format(X_METADATA, key), value);
                }
            }
        }
    }

    private void setMetadata(MessageProperties messageProperties, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        List<Field> annotatedFields = Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Metadata.class))
                .toList();

        messageProperties.getHeaders().forEach((key, value) -> {
            if (key.startsWith(String.format(X_METADATA, ""))) {
                String metadataName = key.substring(String.format(X_METADATA, "").length());

                Optional<Field> fieldOptional = annotatedFields.stream()
                        .filter(field -> field.getAnnotation(Metadata.class).name().equals(metadataName))
                        .findFirst();

                if (fieldOptional.isPresent()) {
                    Field field = fieldOptional.get();
                    field.setAccessible(true);

                    try {
                        field.set(object, value.toString());
                        MetadataHolder.setMetadataValue(metadataName, value.toString());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot set metadata value", e);
                    }
                } else {
                    MetadataHolder.setMetadataValue(metadataName, value.toString());
                }
            }
        });
    }
}
