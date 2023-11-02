export interface Game {
  id: number;
  boardId: string;
  creationDate: string;
  players: Player[];
}

export interface Player {
  id: string;
  type: Type;
  order: number;
  parameters: Map<string, string>;
}

export enum Type {
  HUMAN,
  BOT,
}
