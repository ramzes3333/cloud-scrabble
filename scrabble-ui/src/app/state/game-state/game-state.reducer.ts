import { createReducer, on } from '@ngrx/store';
import {GameState} from "./game-state";
import {
  createSuccess, init,
  initSuccess, makeMoveSuccess,
  move,
  previewSuccess, refreshBoard,
  resolveSuccess, setCharset,
  start, updateBlankLetter,
  validateError
} from "./game-state.actions";
import {Field, fromBoard, fromBoardPreview, Rack} from "../../model/board";
import {Move} from "../../game-ui/model/move";
import {BoardElement} from "../../game-ui/model/board-element";

export const initialState : GameState = {
  started: false,
  charset: [],
  incorrectFields: []
};

export const gameStateReducer = createReducer(initialState,
  on(init, (state, response) => (initialState)),
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
  }),
  on(makeMoveSuccess, (state, moveResult) => ({...state, actualPlayerId: moveResult.actualPlayerId})),
  on(refreshBoard, (state, board) => ({...state, board: fromBoard(board)})),
  on(setCharset, (state, data) => ({...state, charset: data.charset})),
  on(updateBlankLetter, (state, data) => {
    if (!state.started || !state.board) {
      return state;
    }

    return {
      ...state,
      board: {
        ...state.board,
        fields: updateBlank(state.board.fields, data.x, data.y, data.letter),
        racks: state.board.racks
      }
    };
  }),
);

function updateFields(fields: Field[], move: Move): Field[] {
  return fields.map(field => {
    let modifiedField = {...field};

    if (move.fromY !== null && field.x === move.fromX && field.y === move.fromY) {
      modifiedField.letter = undefined;
    }

    if (move.toY !== null && field.x === move.toX && field.y === move.toY) {
      modifiedField.letter = {
        letter: move.letter.letter,
        blank: move.letter.blank,
        points: move.letter.points,
        movable: true,
        suggested: false,
        invalid: false
      };
    }

    return modifiedField;
  });
}

function determineRackNumber(move: Move): number | null {
  if (move.from >= BoardElement.RACK0 && move.from <= BoardElement.RACK3) {
    return move.from - BoardElement.RACK0;
  } else if (move.to >= BoardElement.RACK0 && move.to <= BoardElement.RACK3){
    return move.to - BoardElement.RACK0;
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

  if (move.fromY === null && updatedRack.letters[move.fromX]?.letter === move.letter.letter) {
    updatedRack.letters = [
      ...updatedRack.letters.slice(0, move.fromX),
      ...updatedRack.letters.slice(move.fromX + 1)
    ];
  }

  if (move.toY === null) {
    updatedRack.letters = [
      ...updatedRack.letters.slice(0, move.toX),
      {
        letter: move.letter.letter,
        blank: move.letter.blank,
        points: move.letter.points,
        movable: true,
        suggested: false,
        invalid: false
      },
      ...updatedRack.letters.slice(move.toX)
    ];
  }

  return racks.map((r, index) => index === rackNumber ? updatedRack : r);
}

function updateBlank(fields: Field[], x: number, y: number, letter: string) {
  return fields.map(field => {
    let modifiedField = {...field};

    if (field.x === x && field.y === y) {
      modifiedField.letter = {
        letter: letter,
        blank: true,
        points: 0,
        movable: true,
        suggested: false,
        invalid: false
      };
    }

    return modifiedField;
  });
}
