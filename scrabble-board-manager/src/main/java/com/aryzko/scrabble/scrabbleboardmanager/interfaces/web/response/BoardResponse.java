package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response;

import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.common.BoardParameters;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.common.Field;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.common.Rack;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class BoardResponse {
    private String id;
    private OffsetDateTime creationDate;
    private List<Field> fields;
    private List<Rack> racks;
    private BoardParameters boardParameters;
}




