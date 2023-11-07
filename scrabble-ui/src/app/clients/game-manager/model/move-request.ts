import {Tile} from "./tile";

export interface GameMoveRequest {
  gameId: string;
  playerId: string;
  tiles: Tile[];
}
