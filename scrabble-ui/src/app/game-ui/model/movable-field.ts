import {Letter} from "./letter";
import {BoardElement} from "./board-element";

export class MovableField {
  public x: number;
  public y: number | null;
  public letter: Letter;

  public source: BoardElement;

  constructor(x: number, y: number | null, letter: Letter, source: BoardElement) {
    this.x = x;
    this.y = y;
    this.letter = letter;
    this.source = source;
  }
}
