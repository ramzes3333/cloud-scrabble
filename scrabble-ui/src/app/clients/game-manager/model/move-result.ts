import {Tile} from "./tile";

export interface MoveResult {
  actualPlayerId: string;
  playerMoves: PlayerMove[];
}

export interface PlayerMove {
  playerId: string;
  moveTiles: Tile[];
  movePoints: number;
  allPoints: number;
}
