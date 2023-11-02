import {Field} from "./field";
import {Rack} from "./rack";
import {BoardParameters} from "./board-parameters";
import {BoardPreview} from "./board-preview";

export class Board {
  public id: string;
  public creationDate: string;
  public fields: Field[];
  public racks: Rack[];
  public boardParameters: BoardParameters;

  constructor(id: string, creationDate: string, fields: Field[], racks: Rack[], boardParameters: BoardParameters) {
    this.id = id;
    this.creationDate = creationDate;
    this.fields = fields;
    this.racks = racks;
    this.boardParameters = boardParameters;
  }
}
