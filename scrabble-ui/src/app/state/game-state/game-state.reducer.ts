import { createReducer, on } from '@ngrx/store';
import {GameState} from "./game-state";
import {
  createSuccess,
  initSuccess,
  move,
  previewSuccess,
  resolveSuccess,
  start,
  validateError
} from "./game-state.actions";
import {Field, fromBoard, fromBoardPreview, Rack} from "../../model/board";
import {Move} from "../../game-ui/model/move";

export const initialState : GameState = {
  started: false,
  incorrectFields: []
};

export const gameStateReducer = createReducer(initialState,
  on(createSuccess, (state, response) => ({...state, boardId: response.boardId})),
  on(resolveSuccess, (state, solution) => ({...state, solution: solution})),
  on(previewSuccess, (state, boardPreview) => ({...state, board: fromBoardPreview(boardPreview)})),
  on(initSuccess, (state, data) => (
    {...state, board: fromBoard(data.board), boardId: data.board.id, gameId: data.game.id})),
  on(start, (state, action) => ({...state, started: true})),
  on(move, (state, move) => {
    if (!state.started || !state.board) {
      return state;
    }

    return {
      ...state,
      board: {
        ...state.board,
        fields: updateFields(state.board.fields, move),
        racks: updateRack(state.board.racks, move)
      },
      incorrectFields: []
    };
  }),
  on(validateError, (state, validationResult) => {
    const incorrectCharacters = validationResult.incorrectWords.flatMap(sequence => sequence.characters);
    const combinedCharacters = [...incorrectCharacters, ...validationResult.orphans];

    return {
      ...state,
      incorrectFields: combinedCharacters
    };
  }));

function updateFields(fields: Field[], move: Move): Field[] {
  return fields.map(field => {
    let modifiedField = {...field};

    if (move.fromY !== null && field.x === move.fromX && field.y === move.fromY) {
      modifiedField.letter = undefined;
    }

    if (move.field.y !== null && field.x === move.field.x && field.y === move.field.y) {
      modifiedField.letter = {
        letter: move.field.letter.letter,
        blank: move.field.letter.blank,
        points: move.field.letter.points,
        movable: true,
        suggested: false,
        invalid: false
      };
    }

    return modifiedField;
  });
}

function updateRack(racks: Rack[], move: Move): Rack[] {
  if (racks.length === 0) {
    return racks;
  }

  const rack = racks[0];
  let updatedLetters = [...rack.letters];

  if (move.fromY === null && rack.letters?.at(move.fromX)?.letter === move.field.letter.letter) {
    updatedLetters.splice(move.fromX, 1);
  }

  if (move.field.y === null) {
    updatedLetters.splice(move.field.x, 0, {
      letter: move.field.letter.letter,
      blank: move.field.letter.blank,
      points: move.field.letter.points,
      movable: true,
      suggested: false,
      invalid: false
    });
  }

  return [{ ...rack, letters: updatedLetters }];
}
