package com.aryzko.scrabble.scrabbleboardmanager.domain.model.preview;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.BoardParameters;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.EmptyField;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BoardPreview {
    private List<EmptyField> fields;
    private BoardParameters boardParameters;
}
