package com.aryzko.scrabble.scrabbleboardmanager.interfaces.common;

public class CorrelationIdHolder {
    private static final ThreadLocal<String> correlationId = new ThreadLocal<>();

    public static String getCorrelationId() {
        return correlationId.get();
    }

    public static void setCorrelationId(String id) {
        correlationId.set(id);
    }

    public static void clear() {
        correlationId.remove();
    }
}
