package com.aryzko.scrabble.scrabbleboardmanager.domain.common;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class DateTimeProvider {

    public static OffsetDateTime getActualOffsetDateTime() {
        return OffsetDateTime.now(ZoneId.systemDefault());
    }
}
