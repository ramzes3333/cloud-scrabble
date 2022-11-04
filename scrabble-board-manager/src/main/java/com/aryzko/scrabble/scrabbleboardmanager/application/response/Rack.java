package com.aryzko.scrabble.scrabbleboardmanager.application.response;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class Rack {
    @Valid
    private List<Letter> letters;
}
