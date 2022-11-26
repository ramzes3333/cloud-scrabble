import {Element} from "./element";

export class Word {
  public elements: Element[];
  public points: number;

  constructor(points: number, elements: Element[]) {
    this.points = points;
    this.elements = elements;
  }
}
