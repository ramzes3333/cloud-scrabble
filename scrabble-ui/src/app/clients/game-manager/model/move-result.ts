import {Tile} from "./tile";

export interface MoveResult {
  actualPlayerId: string;
  playerMoves: PlayerMove[];
}

export interface PlayerMove {
  playerId: string;
  tiles: Tile[];
  word: string;
  movePoints: number;
  allPoints: number;
}
