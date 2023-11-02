import {BoardPreview} from "../clients/board-manager/model/board-preview";
import {Board as ClientBoard} from "../clients/board-manager/model/board";

export interface Board {
  id: string;
  creationDate: string;
  fields: Field[];
  racks: Rack[];
  boardParameters: BoardParameters;
}

export interface Field {
  x: number;
  y: number;
  bonus: string;
  letter?: Letter;
}

export interface Rack {
  playerId: string;
  letters: Letter[];
}

export interface Letter {
  letter: string;
  blank: boolean;
  points: number;

  movable: boolean;
  suggested: boolean;
  invalid: boolean;
}

export interface BoardParameters {
  horizontalSize: number;
  verticalSize: number;
}

export enum Bonus {
  DoubleWordScore,
  TripleWordScore,
  DoubleLetterScore,
  TripleLetterScore,
  None,
}

export function fromBoardPreview(boardPreview: BoardPreview): Board {
  const fields: Field[] = boardPreview.fields.map(field => ({
    x: field.x,
    y: field.y,
    bonus: field.bonus,
    letter: undefined
  }));

  return {
    id: '',
    creationDate: '',
    fields,
    racks: [],
    boardParameters: boardPreview.boardParameters
  };
}

export function fromBoard(board: ClientBoard): Board {
  return {
    id: board.id,
    creationDate: board.creationDate,
    fields: board.fields.map(field => ({
      x: field.x,
      y: field.y,
      bonus: field.bonus,
      letter: field.letter ? {
        letter: field.letter.letter,
        blank: field.letter.blank,
        points: field.letter.points,
        movable: false,
        suggested: false,
        invalid: false
      } : undefined
    })),
    racks: board.racks.map(rack => ({
      playerId: rack.playerId,
      letters: rack.letters.map(letter => ({
        letter: letter.letter,
        blank: letter.blank,
        points: letter.points,
        movable: false,
        suggested: false,
        invalid: false
      }))
    })),
    boardParameters: {
      horizontalSize: board.boardParameters.horizontalSize,
      verticalSize: board.boardParameters.verticalSize
    }
  };
}
