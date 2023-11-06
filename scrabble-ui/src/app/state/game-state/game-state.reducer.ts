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
import {MovableFieldSource} from "../../game-ui/model/movable-field";

export const initialState : GameState = {
  started: false,
  incorrectFields: []
};

export const gameStateReducer = createReducer(initialState,
  on(createSuccess, (state, response) => ({...state, boardId: response.boardId})),
  on(resolveSuccess, (state, solution) => ({...state, solution: solution})),
  on(previewSuccess, (state, boardPreview) => ({...state, board: fromBoardPreview(boardPreview)})),
  on(initSuccess, (state, data) => (
    {...state, board: fromBoard(data.board), boardId: data.board.id, gameId: data.game.id, actualPlayerId: data.game.actualPlayerId})),
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

function determineRackNumber(move: Move): number | null {
  if (move.fromSource >= MovableFieldSource.RACK0 && move.fromSource <= MovableFieldSource.RACK3) {
    return move.fromSource - MovableFieldSource.RACK0;
  } else if (move.field.source >= MovableFieldSource.RACK0 && move.field.source <= MovableFieldSource.RACK3){
    return move.field.source - MovableFieldSource.RACK0;
  }
  return null;
}

function updateRack(racks: Rack[], move: Move): Rack[] {
  const rackNumber = determineRackNumber(move);
  if (racks.length === 0 || rackNumber === null) {
    return racks;
  }

  const updatedRack = {
    ...racks[rackNumber],
    letters: [...racks[rackNumber].letters]
  };

  if (move.fromY === null && updatedRack.letters[move.fromX]?.letter === move.field.letter.letter) {
    updatedRack.letters = [
      ...updatedRack.letters.slice(0, move.fromX),
      ...updatedRack.letters.slice(move.fromX + 1)
    ];
  }

  if (move.field.y === null) {
    updatedRack.letters = [
      ...updatedRack.letters.slice(0, move.field.x),
      {
        letter: move.field.letter.letter,
        blank: move.field.letter.blank,
        points: move.field.letter.points,
        movable: true,
        suggested: false,
        invalid: false
      },
      ...updatedRack.letters.slice(move.field.x)
    ];
  }

  return racks.map((r, index) => index === rackNumber ? updatedRack : r);
}
