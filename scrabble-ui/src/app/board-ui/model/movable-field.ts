import {Letter} from "./letter";

export class MovableField {
  public x: number;
  public y: number | null;
  public letter: Letter;

  public locked: boolean = false;

  constructor(locked: boolean, x: number, y: number | null, letter: Letter) {
    this.locked = locked;
    this.letter = letter;
    this.x = x;
    this.y = y;
  }
}
