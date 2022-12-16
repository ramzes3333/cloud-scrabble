package com.aryzko.scrabblegame.interfaces.web.error;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

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
