package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.request;

import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.common.Field;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.common.Rack;
import com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.validator.UUID;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class BoardRequest {
    @UUID
    private String id;
    @Valid
    private List<Field> fields;
    @Valid
    private List<Rack> racks;
}
