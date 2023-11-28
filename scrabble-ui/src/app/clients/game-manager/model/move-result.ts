import {Tile} from "./tile";

export interface MoveResult {
  actualPlayerId: string;
  playerMoves: PlayerMove[];
  gameState: GameState;
}

export interface PlayerMove {
  playerId: string;
  tiles: Tile[];
  word: string;
  movePoints: number;
  allPoints: number;
}

export interface GameState {
  state: string;
  winnerId: string;
}
