import {Solution} from "../../clients/board-manager/model/solution/solution";
import {CharacterWithPosition} from "../../clients/board-manager/model/board-validation-result";
import {Board} from "../../model/board";

export interface GameState {
  started: boolean;
  gameId?: string;
  boardId?: string;
  actualPlayerId?: string;
  board?: Board;

  charset?: string[];
  solution?: Solution;
  incorrectFields: CharacterWithPosition[];
}
