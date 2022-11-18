package com.aryzko.scrabble.scrabbledictionary.adapters.api.web.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Arrays.asList;

@Value
@Builder
public class RestErrorResponse {

    @Singular
    private Set<RestError> errors;

    @Value
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RestError {
        private String code;
        private String message;

        public static RestError of(String code, String message) {
            return new RestError(code, message);
        }
    }
}
