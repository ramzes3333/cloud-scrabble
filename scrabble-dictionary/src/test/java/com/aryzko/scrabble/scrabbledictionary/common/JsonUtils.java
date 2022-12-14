package com.aryzko.scrabble.scrabbledictionary.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {

    public static String loadJsonFromClasspath(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return Files.readString(Paths.get(classPathResource.getURI()));
    }

    public static <T> T loadObjectFromJson(String jsonPath, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(loadJsonFromClasspath(jsonPath), clazz);
    }
}
