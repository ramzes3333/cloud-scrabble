import {Solution} from "../../clients/board-manager/model/solution/solution";
import {CharacterWithPosition} from "../../clients/board-manager/model/board-validation-result";
import {BoardParameters, Field, Rack} from "../../model/board";
import {Player} from "../../clients/game-manager/model/game";

export interface GameState {
  started: boolean;

  gameId?: string;
  boardId?: string;

  players?: Player[];
  actualPlayerId?: string;

  fields?: Field[];
  racks?: Rack[];
  boardParameters?: BoardParameters;

  charset?: string[];
  solution?: Solution;
  incorrectFields: CharacterWithPosition[];
}
