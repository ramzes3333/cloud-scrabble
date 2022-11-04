import {MovableField} from "./movable-field";
import {MoveType} from "./move-type";

export class Move {
  public moveType: MoveType;
  public fromX: number;
  public fromY: number | null;
  public field: MovableField;

  constructor(moveType: MoveType, fromX: number, fromY: number | null, field: MovableField) {
    this.moveType = moveType;
    this.fromX = fromX;
    this.fromY = fromY;
    this.field = field;
  }
}
