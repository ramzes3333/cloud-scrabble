export enum Level {
  NEWBIE,
  BEGINNER,
  ADVANCED,
  EXPERT,
  LEGEND
}

export class BotPlayer {
  private level: Level;

  constructor(level: Level) {
    this.level = level;
  }
}

export class HumanPlayer {
}

export class GameStartRequest {
  private botPlayers: BotPlayer[];
  private humanPlayers: HumanPlayer[];

  constructor(botPlayers: BotPlayer[], humanPlayers: HumanPlayer[]) {
    this.botPlayers = botPlayers;
    this.humanPlayers = humanPlayers;
  }
}
