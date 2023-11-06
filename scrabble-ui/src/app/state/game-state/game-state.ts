import {Solution} from "../../clients/board-manager/model/solution/solution";
import {CharacterWithPosition} from "../../clients/board-manager/model/board-validation-result";
import {Board} from "../../model/board";

export interface GameState {
  started: boolean;
  gameId?: number;
  boardId?: string;
  actualPlayerId?: string;
  board?: Board;
  solution?: Solution;

  incorrectFields: CharacterWithPosition[];
}
