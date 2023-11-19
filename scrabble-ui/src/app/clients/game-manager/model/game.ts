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
  parameters: Map<string, string>;
}

export enum Type {
  HUMAN = "HUMAN",
  BOT = "BOT",
}
