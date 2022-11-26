import {Element} from "../model/element";

export class Word {
  public points: number;
  public elements: Element[];

  constructor(points: number, elements: Element[]) {
    this.points = points;
    this.elements = elements;
  }
}
