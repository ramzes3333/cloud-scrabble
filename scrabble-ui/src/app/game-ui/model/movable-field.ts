import {Letter} from "./letter";

export class MovableField {
  public x: number;
  public y: number | null;
  public letter: Letter;

  public invalid: boolean = false;
  public source: MovableFieldSource;

  constructor(x: number, y: number | null, letter: Letter, source: MovableFieldSource) {
    this.letter = letter;
    this.x = x;
    this.y = y;
    this.source = source;
  }
}

export enum MovableFieldSource {
  BOARD,
  RACK0,
  RACK1,
  RACK2,
  RACK3
}
