
export interface Game {
  id: string;
  boardId: string;
  creationDate: string;
  players: Player[];
  actualPlayerId: string;
}

export interface Player {
  id: string;
  type: Type;
  order: number;
  points: number;
  moveHistory: Move[]
  parameters: Map<string, string>;
}

export interface Move {
  order: number;
  gameOrder: number;
  word: string;
  tiles: string;
  points: number;
}

export enum Type {
  HUMAN = "HUMAN",
  BOT = "BOT",
}
