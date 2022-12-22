export class GamePlayer {
  player: boolean;
  playerType: string;
  playerBotLevel: string;

  constructor(player: boolean, playerType: string, playerBotLevel: string) {
    this.player = player;
    this.playerType = playerType;
    this.playerBotLevel = playerBotLevel;
  }
}
