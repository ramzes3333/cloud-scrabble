export enum Level {
  NEWBIE,
  BEGINNER,
  ADVANCED,
  EXPERT,
  LEGEND
}

export interface BotPlayer {
  level: Level;
}

export interface HumanPlayer {
}

export interface CreateGameRequest {
  botPlayers: BotPlayer[];
  humanPlayers: HumanPlayer[];
}
