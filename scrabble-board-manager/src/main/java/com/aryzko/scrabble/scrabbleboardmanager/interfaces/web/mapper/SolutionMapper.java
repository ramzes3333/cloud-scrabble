package com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.mapper;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Bonus;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolutionMapper {

    com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.Solution.Bonus convert(Bonus bonus);
    com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.Solution.Element convert(Word.Element element);
    com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.Solution.Word convert(Word word);
    com.aryzko.scrabble.scrabbleboardmanager.interfaces.web.response.Solution convert(Solution solution);
}
