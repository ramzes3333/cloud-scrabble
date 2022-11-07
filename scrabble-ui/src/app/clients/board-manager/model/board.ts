import {Field} from "./field";
import {Rack} from "./rack";
import {BoardParameters} from "./board-parameters";

export class Board {
  public id: string;
  public fields: Field[];
  public racks: Rack[];
  public boardParameters: BoardParameters;

  constructor(id: string, fields: Field[], racks: Rack[], boardParameters: BoardParameters) {
    this.id = id;
    this.fields = fields;
    this.racks = racks;
    this.boardParameters = boardParameters;
  }
}
