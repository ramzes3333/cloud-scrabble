package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.preview.BoardPreview;

interface BoardCreator {
    Board prepareEmptyBoard();
    BoardPreview prepareEmptyBoardPreview();
}
