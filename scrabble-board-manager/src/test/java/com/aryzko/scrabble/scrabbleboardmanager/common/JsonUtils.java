package com.aryzko.scrabble.scrabbleboardmanager.common;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {

    public static String loadJsonFromClasspath(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return Files.readString(Paths.get(classPathResource.getURI()));
    }
}
