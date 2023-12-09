package com.aryzko.scrabble.scrabbleboardmanager.interfaces.amqp.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MetadataHolder {
    private static final ThreadLocal<Map<String, String>> metadata = new ThreadLocal<>();

    public static String getMetadataValue(String key) {
        if(metadata.get() == null) {
            return null;
        }
        return metadata.get().get(key);
    }

    public static void setMetadataValue(String key, String value) {
        if(metadata.get() == null) {
            metadata.set(new HashMap<>());
        }
        metadata.get().put(key, value);
    }

    public static void clear() {
        metadata.remove();
    }

    public static Set<String> getAllMetadataKeys() {
        Map<String, String> metadataMap = metadata.get();
        if (metadataMap == null) {
            return Collections.emptySet();
        }
        return metadataMap.keySet();
    }
}
