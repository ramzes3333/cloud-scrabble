package com.aryzko.scrabble.scrabbleboardmanager.infrastructure.configuration.amqp;

import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.Metadata;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.EventMetadata;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common.MetadataHolder;
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
    private final Reflections reflections = new Reflections("com.aryzko.scrabble.scrabbleboardmanager");

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

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Metadata.class)) {
                String metadataName = field.getAnnotation(Metadata.class).name();
                field.setAccessible(true);

                try {
                    Object value = field.get(object);
                    if (value != null) {
                        messageProperties.setHeader(String.format(X_METADATA, metadataName), value.toString());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field: " + field.getName(), e);
                }
            }
        }

        for (String key : MetadataHolder.getAllMetadataKeys()) {
            if (!messageProperties.getHeaders().containsKey(String.format(X_METADATA, key))) {
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
