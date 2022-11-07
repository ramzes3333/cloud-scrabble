package com.aryzko.scrabble.scrabbleboardmanager.application.response;

import lombok.Data;

import java.util.List;

@Data
public class BoardResponse {
    private String id;
    private List<Field> fields;
    private List<Rack> racks;
    private BoardParameters boardParameters;
}




