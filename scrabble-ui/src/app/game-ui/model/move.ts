import {MovableField, MovableFieldSource} from "./movable-field";
import {MoveType} from "./move-type";

export class Move {
  public moveType: MoveType;
  public fromX: number;
  public fromY: number | null;
  public fromSource: MovableFieldSource;
  public field: MovableField;

  constructor(moveType: MoveType, fromX: number, fromY: number | null, fromSource: MovableFieldSource, field: MovableField) {
    this.moveType = moveType;
    this.fromX = fromX;
    this.fromY = fromY;
    this.fromSource = fromSource;
    this.field = field;
  }
}
