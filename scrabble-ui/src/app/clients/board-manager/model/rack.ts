import {Letter} from "./letter";

export class Rack {
  public playerId: string;
  public letters?: Letter[];

  constructor(playerId: string, letters: Letter[]) {
    this.playerId = playerId;
    this.letters = letters;
  }
}
