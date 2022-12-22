import {Letter} from "./letter";

export class MovableField {
  public x: number;
  public y: number | null;
  public letter: Letter;

  public invalid: boolean = false;

  constructor(x: number, y: number | null, letter: Letter) {
    this.letter = letter;
    this.x = x;
    this.y = y;
  }
}
