import {Letter} from "./letter";
import {BoardElement} from "./board-element";

export class Move {
  public letter: Letter;

  public fromX: number;
  public fromY: number | null;
  public from: BoardElement;

  public toX: number;
  public toY: number | null;
  public to: BoardElement;


  constructor(letter: Letter, fromX: number, fromY: number | null, from: BoardElement, toX: number, toY: number | null, to: BoardElement) {
    this.letter = letter;
    this.fromX = fromX;
    this.fromY = fromY;
    this.from = from;
    this.toX = toX;
    this.toY = toY;
    this.to = to;
  }
}
