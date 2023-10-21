export interface GameStartResponse {
  id: number;
  boardId: string;
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
