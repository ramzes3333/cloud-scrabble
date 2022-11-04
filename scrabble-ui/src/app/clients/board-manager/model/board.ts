import {Field} from "./field";
import {Rack} from "./rack";

export class Board {
  public id: string;
  public fields: Field[];
  public racks: Rack[];

  constructor(id: string, fields: Field[], racks: Rack[]) {
    this.id = id;
    this.fields = fields;
    this.racks = racks;
  }
}
