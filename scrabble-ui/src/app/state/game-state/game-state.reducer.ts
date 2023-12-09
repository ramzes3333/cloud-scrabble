import {createReducer, on} from '@ngrx/store';
import {GameState} from "./game-state";
import {
  addSolutionWord,
  clearSuggestedWord,
  createSuccess,
  init,
  initSuccess,
  makeMoveSuccess,
  move,
  moveValidateError,
  previewSuccess,
  putSuggestedWord,
  refreshBoard,
  resolveSuccess,
  setCharset,
  showSuggestedWord,
  start,
  updateBlankLetter
} from "./game-state.actions";
import {Field, fieldsFromBoard, fieldsFromBoardPreview, Rack, racksFromBoard} from "../../model/board";
import {Move} from "../../game-ui/model/move";
import {Element} from "../../game-ui/model/element";
import {Type} from "../../clients/game-manager/model/game";

export const initialState : GameState = {
  started: false,
  moveHistory: [],
  charset: [],
  incorrectFields: [],
  winner: undefined
};

export const gameStateReducer = createReducer(initialState,
  on(init, (state, response) => (initialState)),
  on(createSuccess, (state, response) => ({...state, boardId: response.boardId})),
  on(resolveSuccess, (state, solution) => ({...state, solution: solution})),
  on(addSolutionWord, (state, word) => {
    const updatedWords = [...state.solution?.words || [], word];
    updatedWords.sort((a, b) => b.points - a.points);

    return {
      ...state,
      solution: {
        ...state.solution,
        words: updatedWords
      }
    };
  }),
  on(previewSuccess, (state, boardPreview) => ({
    ...state,
    fields: fieldsFromBoardPreview(boardPreview),
    racks: [],
    boardParameters: boardPreview.boardParameters
  })),
  on(initSuccess, (state, data) => {
    const combinedMoveHistory = data.game.players
      .flatMap(player =>
        player.moveHistory.map(move => ({
          ...move,
          playerId: player.id
        }))
      )
      .sort((a, b) => a.gameOrder - b.gameOrder)
      .map(move => {
        const player = data.game.players.find(player => player.id === move.playerId);
        return {
          playerName: player?.type === Type.HUMAN
            ? `Player: ${player?.parameters.get('login') || 'Unknown'}`
            : `Bot: ${player?.parameters.get('level') || 'Unknown'}`,
          word: move.word,
          points: move.points
        };
      });

    return {
      ...state,
      gameId: data.game.id,
      players: data.game.players,
      moveHistory: combinedMoveHistory,
      actualPlayerId: data.game.actualPlayerId,
      actualPlayer: data.game.players.find(p => p.id === data.game.actualPlayerId),
      winner: data.game.players.find(p => p.id === data.game.winnerId),
      boardId: data.board.id,
      fields: fieldsFromBoard(data.board),
      racks: racksFromBoard(data.board),
      boardParameters: data.board.boardParameters,
    };
  }),
  on(start, (state, action) => ({...state, started: true})),
  on(showSuggestedWord, (state, data) => {
    if (!state.started || !state.fields) {
      return state;
    }

    return {
      ...state,
      fields: setSuggestedWord(state.fields, data.elements)
    };
  }),
  on(clearSuggestedWord, (state, action) => {
    if (!state.started || !state.fields) {
      return state;
    }

    return {
      ...state,
      fields: removeSuggestedWord(state.fields)
    };
  }),
  on(putSuggestedWord, (state, { elements }) => {
    if (!state.started || !state.fields || !state.racks || !state.actualPlayerId) {
      return state;
    }

    return {
      ...state,
      fields: putSuggestedWordIntoFields(state.fields, elements),
      racks: removeLettersFromSuggestedWordFromRack(state.actualPlayerId, state.racks, elements),
      solution: undefined
    };
  }),
  on(move, (state, move) => {
    if (!state.started || !state.fields || !state.racks || !state.actualPlayerId) {
      return state;
    }

    return {
      ...state,
      fields: updateFields(state.fields, move),
      racks: updateRack(state.actualPlayerId, state.racks, move),
      incorrectFields: [],
      solution: undefined
    };
  }),
  on(moveValidateError, (state, validationResult) => {
    const incorrectCharacters = validationResult.incorrectWords.flatMap(sequence => sequence.characters);
    const combinedCharacters = [...incorrectCharacters, ...validationResult.orphans];

    return {
      ...state,
      incorrectFields: combinedCharacters
    };
  }),
  on(makeMoveSuccess, (state, moveResult) => {
    const updatedPlayers = state.players?.map(player => {
      const playerMove = moveResult.playerMoves.reduce((max, pm) => {
        if (pm.playerId === player.id) {
          return max.allPoints > pm.allPoints ? max : pm;
        }
        return max;
      }, { allPoints: -Infinity });

      if (playerMove) {
        return {
          ...player,
          points: playerMove.allPoints
        };
      }
      return player;
    });

    let updatedMoveHistory = [
      ...state.moveHistory,
      ...moveResult.playerMoves.map(playerMove => {
        const player = state.players?.find(p => p.id === playerMove.playerId);
        const playerName = player
          ? (player.type === Type.HUMAN
            ? `Player: ${player?.parameters.get('login') || 'Unknown'}`
            : `Bot: ${player?.parameters.get('level') || 'Unknown'}`)
          : 'Unknown Player';

        return {
          playerName: playerName,
          word: playerMove.word,
          points: playerMove.movePoints
        };
      })
    ];

    return {
      ...state,
      players: updatedPlayers,
      actualPlayerId: moveResult.actualPlayerId,
      actualPlayer: updatedPlayers?.find(p => p.id === moveResult.actualPlayerId),
      winner: updatedPlayers?.find(p => p.id === moveResult.gameState?.winnerId),
      moveHistory: updatedMoveHistory
    };
  }),
  on(refreshBoard, (state, board) => ({
    ...state,
    fields: fieldsFromBoard(board),
    racks: racksFromBoard(board),
  })),
  on(setCharset, (state, data) => ({...state, charset: data.charset})),
  on(updateBlankLetter, (state, data) => {
    if (!state.started || !state.fields) {
      return state;
    }

    return {
      ...state,
      fields: updateBlank(state.fields, data.x, data.y, data.letter)
    };
  }),
);


function determineRackNumberForActualPlayer(racks: Rack[], actualPlayerId: string): number | null {
  const index = racks.findIndex(r => r.playerId === actualPlayerId);
  return index !== -1 ? index : null;
}

function putSuggestedWordIntoFields(fields: Field[], elements: Element[]) {
  return fields.map(field => {
    const element = elements.find(e => e.x === field.x && e.y === field.y && !e.onBoard);
    if (element) {
      return {
        ...field,
        letter: {
          letter: element.letter.toUpperCase(),
          blank: element.blank,
          points: element.points,
          movable: true,
          suggested: false,
          invalid: false
        }
      };
    }
    return field;
  });
}

function removeLettersFromSuggestedWordFromRack(actualPlayerId: string, racks: Rack[], elements: Element[]) {
  const rackNumber = determineRackNumberForActualPlayer(racks, actualPlayerId);
  if (racks.length === 0 || rackNumber === null) {
    return racks;
  }

  let updatedLetters = [...racks[rackNumber].letters];
  elements.forEach(element => {
    if(element.onBoard) {
      return
    }
    const index = updatedLetters.findIndex(letter =>
      letter.blank === element.blank &&
      (element.blank || letter.letter.toUpperCase() === element.letter.toUpperCase()));

    if (index !== -1) {
      updatedLetters.splice(index, 1);
    }
  });

  const updatedRack = {
    ...racks[rackNumber],
    letters: updatedLetters
  };

  return racks.map((r, index) => index === rackNumber ? updatedRack : r);
}

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

function updateRack(actualPlayerId: string, racks: Rack[], move: Move): Rack[] {
  const rackNumber = determineRackNumberForActualPlayer(racks, actualPlayerId);
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

function setSuggestedWord(fields: Field[], elements: Element[]) {
  return fields.map(field => {
    let modifiedField = {...field};

    const matchingElement = elements.find(element =>
      element.x === field.x &&
      element.y === field.y &&
      !element.onBoard
    );

    if (matchingElement) {
      modifiedField.letter = {
        letter: matchingElement.letter.toUpperCase(),
        points: matchingElement.points,
        suggested: true,
        movable: modifiedField.letter?.movable || false,
        blank: matchingElement.blank,
        invalid: modifiedField.letter?.invalid || false
      };
    } else if(modifiedField.letter?.suggested) {
      modifiedField.letter = undefined;
    }

    return modifiedField;
  });
}

function removeSuggestedWord(fields: Field[]) {
  return fields.map(field => {
    let modifiedField = {...field};

    if (modifiedField.letter?.suggested) {
      modifiedField.letter = undefined;
    }

    return modifiedField;
  });
}
